package fr.ddspstl.components.ddsRootNodes;

import java.util.Map;
import java.util.Set;

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
	private static final int NB_THREAD_PROPAGATION = 2;
	private static final int NB_THREAD_CONNECTION = 0;
	private DDSPlugin<T> plugin;
	private String uriDDSRoot;
	private String uriConnectDDSNode;
	private OutConnectionDDS connectRoot;
	private OutPortPropagation<T> propagationRoot;
	private InConnectionDDS connectionDDS;
	private InPortPropagation<T> inPortPropagation;
	private INodeAddress nodeAddress;

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode, String uriConnectClient,
			String uriDDSNode, Set<Topic<T>> topics, Map<Topic<T>, String> topicID) {
		super(nbThreads, nbSchedulableThreads);
		uriDDSRoot = uriDDSNode;
		String executorServiceURI = AbstractPort.generatePortURI();
		createNewExecutorService(executorServiceURI, NB_THREAD_CLIENT, false);
		plugin = new DDSPlugin<T>(topics, topicID, uriConnectClient,executorServiceURI);


		this.uriConnectDDSNode = uriConnectDDSNode;
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			
			String executorServicePropagationURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServicePropagationURI, NB_THREAD_PROPAGATION, false);
			
			inPortPropagation = new InPortPropagation<T>(this,executorServicePropagationURI);
			inPortPropagation.publishPort();

			
			String executorServiceConnectionURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceConnectionURI, NB_THREAD_CONNECTION, false);
			
			connectionDDS = new InConnectionDDS(uriConnectDDSNode, this,executorServicePropagationURI);
			connectionDDS.publishPort();
			
			connectRoot = new OutConnectionDDS(this);
			connectRoot.publishPort();
			propagationRoot = new OutPortPropagation<>(this);
			propagationRoot.publishPort();

			nodeAddress = new NodeAddress(connectionDDS.getPortURI(), inPortPropagation.getPortURI());

			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}
	
	@Override
	public void execute() throws Exception {
		connect();
		System.out.println("connexion fini ");
		super.execute();
	}

	@Override
	public synchronized void finalise() throws Exception {
		connectRoot.doDisconnection();
		propagationRoot.doDisconnection();
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			connectionDDS.unpublishPort();
			inPortPropagation.unpublishPort();
			inPortPropagation.destroyPort();
			connectionDDS.destroyPort();

			connectRoot.unpublishPort();
			propagationRoot.unpublishPort();
			connectRoot.destroyPort();
			propagationRoot.destroyPort();

		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	@Override
	public void connect() throws Exception {
		System.out.println("connect");
		doPortConnection(connectRoot.getPortURI(), uriDDSRoot, ConnectorConnectionDDS.class.getCanonicalName());
		connectRoot.connect(nodeAddress);
	}

	@Override
	public void connectPropagation(INodeAddress address) throws Exception {
		System.out.println("connect propagation");
		doPortConnection(propagationRoot.getPortURI(), address.getPropagationURI(),
				ConnectorPropagation.class.getCanonicalName());
	}

	@Override
	public void connectBack(INodeAddress address) throws Exception {
	}

	@Override
	public void disconnectBack(INodeAddress address) throws Exception {
	}

	@Override
	public void disconnect(INodeAddress uri) throws Exception {
		connectRoot.disconnect(nodeAddress);
	}

	@Override
	public void propagerIn(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		System.out.println("propager IN");
		this.plugin.propager(newObject, topic, id, time);
	}

	@Override
	public void propagerOut(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		System.out.println("propager out");
		propagationRoot.propager(newObject, topic, id, time);
	}

	@Override
	public void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		propagerOut(newObject, topic, id, time);
	}

}
