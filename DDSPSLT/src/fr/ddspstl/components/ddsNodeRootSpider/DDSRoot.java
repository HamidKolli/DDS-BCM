package fr.ddspstl.components.ddsNodeRootSpider;

import java.util.HashSet;
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
public class DDSRoot<T> extends AbstractComponent implements IDDSNode<T>{

	private static final int NB_THREAD_PROPAGATION = 20;
	private static final int NB_THREAD_CONNECTION = 4;
	private INodeAddress nodeAddress;
	private String uriConnectDDSNode;
	private InConnectionDDS connectionDDS;
	private InPortPropagation<T> inPortPropagation;
	private LockPlugin<T> lockPlugin;
	private ConcurrentMap<INodeAddress, OutConnectionDDS> connectionOutWithAddresses;
	private ConcurrentMap<INodeAddress, OutPortPropagation<T>> outPropagationPorts;
	private Set<Topic<T>> topics;
	
	protected DDSRoot(int nbThreads, int nbSchedulableThreads,String uriConnection, Set<Topic<T>> topics) {
		super(nbThreads, nbSchedulableThreads);
		uriConnectDDSNode = uriConnection;
		connectionOutWithAddresses = new ConcurrentHashMap<>();
		outPropagationPorts = new ConcurrentHashMap<>();
		this.topics = new HashSet<>(topics);
		
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

			nodeAddress = new NodeAddress(connectionDDS.getPortURI(), inPortPropagation.getPortURI(),AbstractPort.generatePortURI());

			String executorServicePropagationLockURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServicePropagationLockURI, NB_THREAD_CONNECTION, false);
			
			lockPlugin = new LockPlugin<>(nodeAddress, topics, executorServicePropagationLockURI);
			lockPlugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(lockPlugin);
			
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}
	
	@Override
	public synchronized void finalise() throws Exception {
		for (Map.Entry<INodeAddress, OutConnectionDDS> cp : connectionOutWithAddresses.entrySet()) {
			cp.getValue().doDisconnection();
		}
		for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
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
			for (Map.Entry<INodeAddress, OutConnectionDDS> cp : connectionOutWithAddresses.entrySet()) {
				cp.getValue().unpublishPort();
				cp.getValue().destroyPort();
			}
			for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
				cp.getValue().unpublishPort();
				cp.getValue().destroyPort();
			}
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}

	@Override
	public void connect() throws Exception {		
	}

	@Override
	public void connectPropagation(INodeAddress address) throws Exception {
		
	}

	@Override
	public void connectBack(INodeAddress address) throws Exception {
		System.out.println("connect back root");
		OutConnectionDDS cout = new OutConnectionDDS(this);
		cout.publishPort();
		
		this.doPortConnection(cout.getPortURI(), address.getNodeURI(), ConnectorConnectionDDS.class.getCanonicalName());
		connectionOutWithAddresses.put(address, cout);
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(this);
		outPropagation.publishPort();
		
		this.doPortConnection(outPropagation.getPortURI(), address.getPropagationURI(),
				ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(address, outPropagation);
		cout.connectPropagation(nodeAddress);
		
	}

	@Override
	public void disconnectBack(INodeAddress address) throws Exception {
		OutConnectionDDS out = connectionOutWithAddresses.remove(address);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation<T> pOut = outPropagationPorts.remove(address);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
		
	}

	@Override
	public void disconnect(INodeAddress uri) throws Exception {
	}

	@Override
	public synchronized void propagerIn(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception {
		
		propagerOut(newObject, topic, id,time);
	}

	@Override
	public synchronized void propagerOut(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception {
		for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
			cp.getValue().propager(newObject, topic, id,time);
		}	
	}

	@Override
	public void propager(T newObject, TopicDescription<T> topic, String id,Time time) throws Exception {
	}


	
	
	
	

}
