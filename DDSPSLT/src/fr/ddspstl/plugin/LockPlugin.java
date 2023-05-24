package fr.ddspstl.plugin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.connectors.ConnectorLock;
import fr.ddspstl.interfaces.PropagationLock;
import fr.ddspstl.ports.InPortLock;
import fr.ddspstl.ports.OutPortLock;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Plugin pour les lock
 */
public class LockPlugin extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	public static final String LOGGER_TAG = "LockPlugin | ";

	private ConcurrentMap<TopicDescription<?>, Semaphore> topicsLock;
	private InPortLock inPortLock;
	private ConcurrentMap<INodeAddress, OutPortLock> ports;
	private ConcurrentMap<String, TopicDescription<?>> topicsID;
	private ConcurrentMap<String, TopicDescription<?>> topicsIDUnlock;
	private ConcurrentMap<TopicDescription<?>, Time> topicsTimestamp;
	private Set<TopicDescription<?>> topics;
	private INodeAddress address;
	private String executorServiceURI;

	/**
	 * Constructeur
	 * 
	 * @param address : l'adresse du noeud
	 * @param topics : l'ensemble des topics
	 * @param executorServiceURI : l'uri de l'executorService
	 * @throws Exception
	 */
	public LockPlugin(INodeAddress address, Set<TopicDescription<?>> topics, String executorServiceURI)
			throws Exception {
		this.topics = topics;
		this.address = address;
		this.executorServiceURI = executorServiceURI;
		topicsTimestamp = new ConcurrentHashMap<>();
		topicsID = new ConcurrentHashMap<>();
		ports = new ConcurrentHashMap<>();
		topicsLock = new ConcurrentHashMap<>();
		topicsIDUnlock = new ConcurrentHashMap<>();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#installOn(ComponentI)
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addOfferedInterface(PropagationLock.class);
		this.addRequiredInterface(PropagationLock.class);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#initialise()
	 */
	@Override
	public void initialise() throws Exception {
		super.initialise();

		for (TopicDescription<?> topic : topics) {
			topicsLock.put(topic, new Semaphore(1));
		}

		inPortLock = new InPortLock(address.getPropagationLockURI(), getOwner(), getPluginURI(), executorServiceURI);
		inPortLock.publishPort();

	}
	
	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	@Override
	public void finalise() throws Exception {
		super.finalise();
		for (OutPortLock port : ports.values()) {
			if (port.connected())
				port.doDisconnection();
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
	 */
	@Override
	public void uninstall() throws Exception {
		super.uninstall();
		inPortLock.unpublishPort();
		for (OutPortLock port : ports.values()) {
			port.unpublishPort();
			port.destroyPort();
		}

	}

	/**
	 * Methode tryLock : verifie si un topic est deja lock ou non
	 * 
	 * @param topic : le nom du topic
	 * @return booleen : le resultat du try
	 */
	public boolean trylock(TopicDescription<?> topic) {
		getOwner().logMessage(LOGGER_TAG + "Try lock topic :" + topic.getName());
		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Fin try lock topic :" + topic.getName() + " Succes : true");
			return topicsLock.get(topic).tryAcquire();
		}
		getOwner().logMessage(LOGGER_TAG + "Fin try lock topic :" + topic.getName() + " Succes : false");
		return false;
	}

	/**
	 * Methode Lock : lock un topic
	 * 
	 * @param topic : le topic a lock
	 */
	public void lock(TopicDescription<?> topic) throws Exception {

		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Lock topic :" + topic.getName());
			topicsLock.get(topic).acquire();
			getOwner().logMessage(LOGGER_TAG + "Fin lock topic :" + topic.getName() + " Succes : true");
			return;
		}
		getOwner().logMessage(LOGGER_TAG + "Lock topic :" + topic.getName() + " not found");

	}

	/**
	 * Methode unlock : unlock un topic
	 * 
	 * @param topic : le topic a unlock
	 */
	public void unlock(TopicDescription<?> topic) {
		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Unlock topic :" + topic.getName());
			topicsLock.get(topic).release();
			getOwner().logMessage(LOGGER_TAG + "fin unLock topic :" + topic.getName());
		}
		getOwner().logMessage(LOGGER_TAG + "unlock topic :" + topic.getName() + " not found");
	}

	/**
	 * Methode propagateLock : propage le lock
	 * 
	 * @param topic : le topic
	 * @param idPropagation : l'ID de la propagation
	 * @param timestamp : le timeStamp de la propagation
	 * @return boolean : booleen retournant si la propagation apu se faire
	 * @throws Exception
	 */
	public boolean propagateLock(TopicDescription<?> topic, String idPropagation, Time timestamp) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propagate Lock topic :" + topic.getName() + " id = " + idPropagation);

		if (topicsID.containsKey(idPropagation) && topicsID.get(idPropagation).equals(topic))
			return true;

		topicsID.put(idPropagation, topic);

		if (topicsLock.containsKey(topic)) {
			if (trylock(topic)) {
				topicsTimestamp.put(topic, timestamp);
				
			} else {
				if (topicsTimestamp.get(topic).getTime(TimeUnit.MILLISECONDS) > timestamp.getTime(TimeUnit.MILLISECONDS)) {
					lock(topic);
				} else {
					return false;
				}
			}
			boolean b = true;
			for (OutPortLock port : ports.values()) {
				b &= port.lock(topic, idPropagation, timestamp);
			}
			getOwner().logMessage(LOGGER_TAG + "Propagate Lock topic :" + topic.getName() + " id = " + idPropagation
					+ " succes " + b);

			return b;

		}

		return true;

	}

	/**
	 * Methode propagateUnlock : propage le Unlock
	 * 
	 * @param topic : le topic
	 * @param idPropagation : l'id de la propagation
	 * @param idPropagationUnlock : l'id de la propagation du unlock
	 * @throws Exception
	 */
	public void propagateUnlock(TopicDescription<?> topic, String idPropagation, String idPropagationUnlock)
			throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propagate unlock topic :" + topic.getName() + " id propagation = "
				+ idPropagation + " id propagationUnlock = " + idPropagationUnlock);

		if (topicsIDUnlock.containsKey(idPropagationUnlock) && topicsIDUnlock.get(idPropagationUnlock).equals(topic))
			return;

		topicsIDUnlock.put(idPropagationUnlock, topic);

		if (topicsLock.containsKey(topic)) {
			if (topicsID.containsKey(idPropagation) && topicsID.get(idPropagation).equals(topic)) {
				unlock(topic);
			} else {
				return;
			}

			for (OutPortLock port : ports.values()) {
				port.unlock(topic, idPropagation, idPropagationUnlock);
			}
		}
		getOwner().logMessage(LOGGER_TAG + "Fin propagate unlock topic :" + topic.getName() + " id propagation = "
				+ idPropagation + " id propagationUnlock = " + idPropagationUnlock);

	}

	/**
	 * Methode Connect
	 * 
	 * @param address : l'adresse du noeud auquel se connecter
	 * @throws Exception
	 */
	public void connect(INodeAddress address) throws Exception {
		if (ports.containsKey(address))
			return;
		OutPortLock tmp = new OutPortLock(getOwner());
		ports.put(address, tmp);
		tmp.publishPort();
		getOwner().doPortConnection(tmp.getPortURI(), address.getPropagationLockURI(),
				ConnectorLock.class.getCanonicalName());
	}

}
