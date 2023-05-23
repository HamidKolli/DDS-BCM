package fr.ddspstl.plugin;

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
import fr.sorbonne_u.components.ComponentI;

public class LockPlugin extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	public static final String LOGGER_TAG = "LockPlugin | ";

	private ConcurrentMap<TopicDescription<?>, Lock> topicsLock;
	private InPortLock inPortLock;
	private ConcurrentMap<INodeAddress, OutPortLock> ports;
	private ConcurrentMap<TopicDescription<?>, String> topicsID;
	private ConcurrentMap<TopicDescription<?>, String> topicsIDUnlock;
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
			topicsLock.put(topic, new ReentrantLock());
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
			return topicsLock.get(topic).tryLock();
		}
		getOwner().logMessage(LOGGER_TAG + "Fin try lock topic :" + topic.getName() + " Succes : false");
		return false;
	}

	public void lock(TopicDescription<?> topic) {

		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Lock topic :" + topic.getName());
			topicsLock.get(topic).lock();
			getOwner().logMessage(LOGGER_TAG + "Fin lock topic :" + topic.getName() + " Succes : true");
			return;
		}
		getOwner().logMessage(LOGGER_TAG + "Lock topic :" + topic.getName() + " not found");

	}

	public void unlock(TopicDescription<?> topic) {
		if (topicsLock.containsKey(topic)) {
			getOwner().logMessage(LOGGER_TAG + "Unlock topic :" + topic.getName());
			topicsLock.get(topic).unlock();
			getOwner().logMessage(LOGGER_TAG + "fin unLock topic :" + topic.getName());
		}
		getOwner().logMessage(LOGGER_TAG + "unlock topic :" + topic.getName() + " not found");
	}

	public boolean propagateLock(TopicDescription<?> topic, String idPropagation, Time timestamp) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propagate Lock topic :" + topic.getName() + " id = " + idPropagation);

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
			b &= port.lock(topic, idPropagation, timestamp);
		}
		getOwner().logMessage(
				LOGGER_TAG + "Propagate Lock topic :" + topic.getName() + " id = " + idPropagation + " succes " + b);

		return b;

	}

	public void propagateUnlock(TopicDescription<?> topic, String idPropagation, String idPropagationUnlock)
			throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propagate unlock topic :" + topic.getName() + " id propagation = "
				+ idPropagation + " id propagationUnlock = " + idPropagationUnlock);

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
