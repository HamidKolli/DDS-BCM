package fr.ddspstl.components.ddsNodeRoot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.omg.dds.core.Time;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.addresses.NodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorConnectionDDS;
import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.plugin.DDSPlugin;
import fr.ddspstl.plugin.LockPlugin;
import fr.ddspstl.ports.InConnectionDDS;
import fr.ddspstl.ports.InPortPropagation;
import fr.ddspstl.ports.OutConnectionDDS;
import fr.ddspstl.ports.OutPortPropagation;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

@RequiredInterfaces(required = { Propagation.class, ConnectDDSNode.class })
@OfferedInterfaces(offered = { Propagation.class, ConnectDDSNode.class })
public class DDSNode<T> extends AbstractComponent implements IDDSNode<T> {

	private static final int NB_THREAD_CLIENT = 4;
	private static final int NB_THREAD_PROPAGATION = 5;
	private static final int NB_THREAD_CONNECTION = 5;
	private static final int NB_THREAD_PROPAGATION_LOCK = 2;
	private DDSPlugin<T> plugin;
	private LockPlugin<T> lockPlugin;
	private List<String> uriDDSNodes;

	private InPortPropagation<T> inPortPropagation;
	private String uriConnectDDSNode;
	private InConnectionDDS connectionDDS;
	private ConcurrentMap<String, OutConnectionDDS> connectionOut;
	private ConcurrentMap<TopicDescription<T>, List<String>> topicsClients;
	private ConcurrentMap<String, OutPortPropagation<T>> outPropagationPorts;
	private ConcurrentMap<String, INodeAddress> nodeAddresses;
	private INodeAddress nodeAddress;
	private Set<Topic<T>> topics;

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode, String uriConnectClient,
			List<String> uriDDSNodes, Set<Topic<T>> topics, Map<Topic<T>, String> topicID,
			Map<TopicDescription<T>, List<String>> topicsClients) throws Exception {
		super(nbThreads, nbSchedulableThreads);

		this.uriDDSNodes = new ArrayList<String>(uriDDSNodes);

		this.topics = new HashSet<>(topics);

		String executorServiceURI = AbstractPort.generatePortURI();
		createNewExecutorService(executorServiceURI, NB_THREAD_CLIENT, false);
		plugin = new DDSPlugin<T>(topics, topicID, uriConnectClient, executorServiceURI);

		this.uriConnectDDSNode = uriConnectDDSNode;
		this.connectionOut = new ConcurrentHashMap<>();
		this.outPropagationPorts = new ConcurrentHashMap<>();
		this.nodeAddresses = new ConcurrentHashMap<>();
		this.topicsClients = new ConcurrentHashMap<>(topicsClients);

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {

			String executorServicePropagationURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServicePropagationURI, NB_THREAD_PROPAGATION, false);

			inPortPropagation = new InPortPropagation<T>(this, executorServicePropagationURI);
			inPortPropagation.publishPort();

			String executorServiceConnectionURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceConnectionURI, NB_THREAD_CONNECTION, false);

			connectionDDS = new InConnectionDDS(uriConnectDDSNode, this, executorServicePropagationURI);
			connectionDDS.publishPort();
			inPortPropagation.publishPort();

			nodeAddress = new NodeAddress(connectionDDS.getPortURI(), inPortPropagation.getPortURI(),
					AbstractPort.generatePortURI());

			String executorServiceLockURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceConnectionURI, NB_THREAD_PROPAGATION_LOCK, false);
			lockPlugin = new LockPlugin<>(nodeAddress, topics, executorServiceLockURI);

			lockPlugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(lockPlugin);

			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	public void connect() throws Exception {
		for (String uri : uriDDSNodes) {

			if (connectionOut.containsKey(uri)) {
				return;
			}
			System.out.println("connect");
			OutConnectionDDS cout = new OutConnectionDDS(this);
			cout.publishPort();

			this.doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());

			connectionOut.put(uri, cout);
			cout.connect(nodeAddress);

		}
	}

	public void connectPropagation(INodeAddress address) throws Exception {
		System.out.println("connect propagation");
		if (outPropagationPorts.containsKey(address.getPropagationURI())) {
			return;
		}
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(this);
		outPropagation.publishPort();
		this.doPortConnection(outPropagation.getPortURI(), address.getPropagationURI(),
				ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(address.getPropagationURI(), outPropagation);
		nodeAddresses.put(address.getNodeURI(), address);
		lockPlugin.connect(address);
	}

	public void connectBack(INodeAddress address) throws Exception {
		OutConnectionDDS cout = connectionOut.get(address.getNodeURI());
		if (cout == null) {
			System.out.println("connect back");
			cout = new OutConnectionDDS(this);
			cout.publishPort();
			this.doPortConnection(cout.getPortURI(), address.getNodeURI(),
					ConnectorConnectionDDS.class.getCanonicalName());
			connectionOut.put(address.getNodeURI(), cout);
			nodeAddresses.put(address.getNodeURI(), address);

		}

		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(this);
		outPropagation.publishPort();
		this.doPortConnection(outPropagation.getPortURI(), address.getPropagationURI(),
				ConnectorPropagation.class.getCanonicalName());

		outPropagationPorts.put(address.getPropagationURI(), outPropagation);
		
		lockPlugin.connect(address);
		
		cout.connectPropagation(nodeAddress);

	}

	public void disconnectBack(INodeAddress address) throws Exception {
		lockPlugin.disconnect(address);
		OutConnectionDDS out = connectionOut.remove(address.getNodeURI());
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation<T> pOut = outPropagationPorts.remove(address.getPropagationURI());
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();

	}

	public void disconnect(INodeAddress address) throws Exception {
		lockPlugin.disconnect(address);
		OutConnectionDDS out = connectionOut.remove(address.getNodeURI());
		out.disconnect(this.nodeAddress);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation<T> pOut = outPropagationPorts.remove(address.getPropagationURI());
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
	}

	public void propagerIn(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		
		this.plugin.propager(newObject, topic, uriConnectDDSNode, time);
		
		if (topicsClients.get(topic).size() > 1) {
			propagerOut(newObject, topic, id, time);
		}
	}

	public void propagerOut(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		for (String uriCout : topicsClients.get(topic)) {
			outPropagationPorts.get(nodeAddresses.get(uriCout).getPropagationURI()).propager(newObject, topic, id,
					time);
		}

	}

	public void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		
		lockPlugin.lock(topic);
		if (topicsClients.get(topic).size() > 1)
			propagerIn(newObject, topic, id, time);
		else
			propagerOut(newObject, topic, id, time);
		
		lockPlugin.unlock(topic);
	}

	@Override
	public void execute() throws Exception {
		connect();
		System.out.println("connexion fini ");
		super.execute();
	}

	@Override
	public synchronized void finalise() throws Exception {
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			cp.getValue().doDisconnection();
		}

		for (Map.Entry<String, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
			cp.getValue().doDisconnection();
		}
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			connectionDDS.unpublishPort();
			inPortPropagation.unpublishPort();
			inPortPropagation.destroyPort();
			connectionDDS.destroyPort();
			for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
				cp.getValue().unpublishPort();
				cp.getValue().destroyPort();
			}
			for (Map.Entry<String, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
				cp.getValue().unpublishPort();
				cp.getValue().destroyPort();
			}

		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}


}
