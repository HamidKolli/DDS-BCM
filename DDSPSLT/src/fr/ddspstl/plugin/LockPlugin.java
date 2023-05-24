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

	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addOfferedInterface(PropagationLock.class);
		this.addRequiredInterface(PropagationLock.class);
	}

	@Override
	public void initialise() throws Exception {
		super.initialise();

		for (TopicDescription<?> topic : topics) {
			topicsLock.put(topic, new Semaphore(1));
		}

		inPortLock = new InPortLock(address.getPropagationLockURI(), getOwner(), getPluginURI(), executorServiceURI);
		inPortLock.publishPort();

	}

	@Override
	public void finalise() throws Exception {
		super.finalise();
		for (OutPortLock port : ports.values()) {
			if (port.connected())
				port.doDisconnection();
		}
	}

	@Override
	public void uninstall() throws Exception {
		super.uninstall();
		inPortLock.unpublishPort();
		for (OutPortLock port : ports.values()) {
			port.unpublishPort();
			port.destroyPort();
		}

	}

	public boolean trylock(TopicDescription<?> topic) {
		getOwner().logMessage(LOGGER_TAG + "Try lock topic :" + topic.getName());
		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Fin try lock topic :" + topic.getName() + " Succes : true");
			return topicsLock.get(topic).tryAcquire();
		}
		getOwner().logMessage(LOGGER_TAG + "Fin try lock topic :" + topic.getName() + " Succes : false");
		return false;
	}

	public void lock(TopicDescription<?> topic) throws Exception {

		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Lock topic :" + topic.getName());
			topicsLock.get(topic).acquire();
			getOwner().logMessage(LOGGER_TAG + "Fin lock topic :" + topic.getName() + " Succes : true");
			return;
		}
		getOwner().logMessage(LOGGER_TAG + "Lock topic :" + topic.getName() + " not found");

	}

	public void unlock(TopicDescription<?> topic) {
		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Unlock topic :" + topic.getName());
			topicsLock.get(topic).release();
			getOwner().logMessage(LOGGER_TAG + "fin unLock topic :" + topic.getName());
		}
		getOwner().logMessage(LOGGER_TAG + "unlock topic :" + topic.getName() + " not found");
	}

	public boolean propagateLock(TopicDescription<?> topic, String idPropagation, Time timestamp) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propagate Lock topic :" + topic.getName() + " id = " + idPropagation);

		if (topicsID.containsKey(idPropagation) && topicsID.get(idPropagation).equals(topic))
			return true;

		topicsID.put(idPropagation, topic);

		if (topicsLock.containsKey(topic)) {
			System.out.println("trylock");
			if (trylock(topic)) {
				System.out.println("trylock true " + idPropagation );
				topicsTimestamp.put(topic, timestamp);
				
			} else {
				System.out.println("trylock false");
				if (topicsTimestamp.get(topic).getTime(TimeUnit.MILLISECONDS) > timestamp.getTime(TimeUnit.MILLISECONDS)) {
					System.out.println("lock "+ idPropagation );
					lock(topic);
					System.out.println("lock in");
				} else {
					System.out.println("return");
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
				System.out.println("unlock "+ idPropagation );
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
