package fr.ddspstl.plugin;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.connectors.ConnectorLock;
import fr.ddspstl.interfaces.PropagationLock;
import fr.ddspstl.ports.InPortLock;
import fr.ddspstl.ports.OutPortLock;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : type de la donnée
 *
 * Plugin pour les lock
 */
public class LockPlugin<T> extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	private ConcurrentMap<TopicDescription<T>, Lock> topicsLock;
	private InPortLock inPortLock;
	private ConcurrentMap<INodeAddress, OutPortLock> ports;
	private ConcurrentMap<TopicDescription<T>, String> topicsID;
	private ConcurrentMap<TopicDescription<T>, String> topicsIDUnlock;
	private ConcurrentMap<TopicDescription<T>, Time> topicsTimestamp;
	private Set<TopicDescription<T>> topics;
	private INodeAddress address;
	private  String executorServiceURI;

	/**
	 * Constructeur
	 * 
	 * @param address : l'adresse du noeud
	 * @param topics : l'ensemble des topics
	 * @param executorServiceURI : l'uri de l'executorService
	 * @throws Exception
	 */
	public LockPlugin(INodeAddress address, Set<TopicDescription<T>> topics, String executorServiceURI) throws Exception {
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

		for (TopicDescription<T> topic : topics) {
			topicsLock.put(topic, new ReentrantLock());
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
	public boolean trylock(TopicDescription<T> topic) {
		if (topicsLock.containsKey(topic)) {
			return topicsLock.get(topic).tryLock();
		}
		return false;
	}

	/**
	 * Methode Lock : lock un topic
	 * 
	 * @param topic : le topic a lock
	 */
	public void lock(TopicDescription<T> topic) {
		if (topicsLock.containsKey(topic)) {
			topicsLock.get(topic).lock();
		}

	}

	/**
	 * Methode unlock : unlock un topic
	 * 
	 * @param topic : le topic a unlock
	 */
	public void unlock(TopicDescription<T> topic) {
		if (topicsLock.containsKey(topic)) {
			topicsLock.get(topic).unlock();
		}
	}

	
	/**
	 * Methode propagateLockIn : propage le lock
	 * 
	 * @param topic : le topic
	 * @throws Exception
	 */
	public void propagateLockIn(TopicDescription<T> topic) throws Exception {
		propagateLock(topic, AbstractPort.generatePortURI(), new fr.ddspstl.time.Time(new Date().getTime()));
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
	public boolean propagateLock(TopicDescription<T> topic, String idPropagation, Time timestamp) throws Exception {

		if (topicsID.containsKey(topic) && topicsID.get(topic).equals(idPropagation))
			return true;

		topicsID.put(topic, idPropagation);

		if (topicsLock.containsKey(topic)) {

			if (trylock(topic)) {
				topicsTimestamp.put(topic, timestamp);
			} else {
				if (topicsTimestamp.get(topic).compareTo(timestamp) > 0) {
					lock(topic);
				} else {
					return false;
				}
			}
		}
		boolean b = true;
		for (OutPortLock port : ports.values()) {
			b &=port.lock(topic, idPropagation, timestamp);
		}
		return b;
		

	}

	/**
	 * Methode propagateUnlockIn : propage le unlock
	 * 
	 * @param topic : le topic
	 * @param idPropagation : l'ID de la propagation
	 * @throws Exception
	 */
	public void propagateUnlockIn(TopicDescription<T> topic, String idPropagation) throws Exception {
		propagateUnlock(topic, idPropagation, AbstractPort.generatePortURI());
	}

	/**
	 * Methode propagateUnlock : propage le Unlock
	 * 
	 * @param topic : le topic
	 * @param idPropagation : l'id de la propagation
	 * @param idPropagationUnlock : l'id de la propagation du unlock
	 * @throws Exception
	 */
	public void propagateUnlock(TopicDescription<T> topic, String idPropagation, String idPropagationUnlock)
			throws Exception {
		if (topicsIDUnlock.containsKey(topic) && topicsIDUnlock.get(topic).equals(idPropagationUnlock))
			return;

		topicsIDUnlock.put(topic, idPropagationUnlock);

		if (topicsLock.containsKey(topic)) {
			if (topicsID.get(topic).equals(idPropagation)) {
				unlock(topic);
			} else {
				return;
			}
		}
		for (OutPortLock port : ports.values()) {
			port.unlock(topic, idPropagation, idPropagationUnlock);
		}
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
