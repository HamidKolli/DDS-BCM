package fr.ddspstl.components;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.connectors.ConnectorReadDDS;
import fr.ddspstl.connectors.ConnectorWrite;
import fr.ddspstl.plugin.DDSPlugin;
import fr.ddspstl.plugin.LockPlugin;
import fr.ddspstl.ports.OutPortPropagation;
import fr.ddspstl.ports.OutPortReadDDS;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class DDSNode<T> extends AbstractComponent implements IDDSNode<T> {

	private static final int NB_THREAD_CLIENT = 4;
	private static final int NB_THREAD_LOCK = 4;
	private static final int NB_THREAD_PROPAGATION = 5;
	private static final int NB_THREAD_CONNECTION = 5;

	private DDSPlugin<T> plugin;
	private LockPlugin<T> pluginLock;

	private INodeAddress nodeAddress;
	private Map<TopicDescription<T>, INodeAddress> rootOfTopics;
	private Map<TopicDescription<T>, INodeAddress> nextRoot;

	protected DDSNode(int nbThreads, int nbSchedulableThreads, INodeAddress address,
			Map<TopicDescription<T>, INodeAddress> rootOfTopics, Map<TopicDescription<T>, INodeAddress> nextRoot)
			throws Exception {
		super(nbThreads, nbSchedulableThreads);

		this.rootOfTopics = new ConcurrentHashMap<>(rootOfTopics);
		this.nextRoot = new ConcurrentHashMap<>(nextRoot);
		this.nodeAddress = address;

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {

			String executorServiceLockURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceLockURI, NB_THREAD_LOCK, false);
			new HashSet<>(nextRoot.values());
			pluginLock = new LockPlugin<T>(nodeAddress, nextRoot.keySet(), executorServiceLockURI);
			pluginLock.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(pluginLock);

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	public void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		pluginLock.lock(topic);
		plugin.propager(newObject, topic, id, time);
		pluginLock.unlock(topic);

	}

	public Iterator<T> read(TopicDescription<T> topic) {
		pluginLock.lock(topic);
		Iterator<T> result = plugin.readData(topic);
		pluginLock.unlock(topic);

		return result;

	}

	@Override
	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		String propagationLockURI = AbstractPort.generatePortURI();
		if (pluginLock.propagateLock(topic, propagationLockURI, new fr.ddspstl.time.Time((new Date()).getTime()))) {
			Iterator<T> result = plugin.consommer(topic, id, isFirst);
			pluginLock.propagateUnlock(topic, propagationLockURI, AbstractPort.generatePortURI());
			return result;
		}
		pluginLock.propagateUnlock(topic, propagationLockURI, AbstractPort.generatePortURI());
		return new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
	}

	@Override
	public void execute() throws Exception {

		String executorServicePropagationURI = AbstractPort.generatePortURI();
		createNewExecutorService(executorServicePropagationURI, NB_THREAD_PROPAGATION, false);

		String executorServiceReadWriteURI = AbstractPort.generatePortURI();
		createNewExecutorService(executorServiceReadWriteURI, NB_THREAD_CONNECTION, false);

		String executorServiceURI = AbstractPort.generatePortURI();
		createNewExecutorService(executorServiceURI, NB_THREAD_CLIENT, false);

		Map<TopicDescription<T>, OutPortPropagation<T>> propagationPortToNextRoot = new ConcurrentHashMap<>();

		for (Map.Entry<TopicDescription<T>, INodeAddress> topic : nextRoot.entrySet()) {
			OutPortPropagation<T> port = new OutPortPropagation<>(this);
			port.publishPort();
			doPortConnection(port.getPortURI(), topic.getValue().getPropagationURI(),
					ConnectorPropagation.class.getCanonicalName());
			propagationPortToNextRoot.put(topic.getKey(), port);
			
			pluginLock.connect(topic.getValue());
		}

		Map<TopicDescription<T>, OutPortReadDDS<T>> readPortToRoot = new ConcurrentHashMap<>();

		for (Map.Entry<TopicDescription<T>, INodeAddress> topic : rootOfTopics.entrySet()) {
			OutPortReadDDS<T> port = new OutPortReadDDS<>(this);
			port.publishPort();
			doPortConnection(port.getPortURI(), topic.getValue().getReadURI(),
					ConnectorReadDDS.class.getCanonicalName());
			readPortToRoot.put(topic.getKey(), port);
			
			pluginLock.connect(topic.getValue());
		}

		Map<TopicDescription<T>, OutPortWrite<T>> writePortToRoot = new ConcurrentHashMap<>();
		for (Map.Entry<TopicDescription<T>, INodeAddress> topic : rootOfTopics.entrySet()) {
			OutPortWrite<T> port = new OutPortWrite<>(this);
			port.publishPort();
			doPortConnection(port.getPortURI(), topic.getValue().getWriteURI(),
					ConnectorWrite.class.getCanonicalName());
			writePortToRoot.put(topic.getKey(), port);
		}
		
		plugin = new DDSPlugin<T>(nextRoot.keySet(), propagationPortToNextRoot, readPortToRoot, writePortToRoot,
				nodeAddress, executorServiceURI, executorServicePropagationURI, executorServiceReadWriteURI);

		plugin.setPluginURI(AbstractPort.generatePortURI());
		this.installPlugin(plugin);
		super.execute();
	}
	




}
