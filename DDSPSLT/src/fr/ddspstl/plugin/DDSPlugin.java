package fr.ddspstl.plugin;

import java.util.HashMap;
import java.util.Map;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.addresses.NodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorConnectionDDS;
import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.ports.InConnectionDDS;
import fr.ddspstl.ports.InPortConnectClient;
import fr.ddspstl.ports.InPortPropagation;
import fr.ddspstl.ports.InPortRead;
import fr.ddspstl.ports.InPortWrite;
import fr.ddspstl.ports.OutConnectionDDS;
import fr.ddspstl.ports.OutPortPropagation;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class DDSPlugin<T> extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	private InPortRead<T> inPortRead;
	private InPortWrite<T> inPortWrite;
	private InPortPropagation<T> inPortPropagation;
	private InConnectionDDS connectionDDS;
	private InPortConnectClient inPortConnectClient;
	private String uriConnectDDSNode;
	private String uriConnectClient;
	private Map<String, OutConnectionDDS> connectionOut;
	private Map<INodeAddress, OutConnectionDDS> connectionOutWithAddresses;
	private Map<INodeAddress,OutPortPropagation<T>> outPropagationPorts ;
	private INodeAddress nodeAddress;
	

	public DDSPlugin(String uriConnection, String uriConnectClient) {
		this.uriConnectDDSNode = uriConnection;
		this.uriConnectClient = uriConnectClient;
		this.connectionOut = new HashMap<>();
		this.outPropagationPorts = new HashMap<>();
		this.connectionOutWithAddresses = new HashMap<>();

	}


	@Override
	public void installOn(ComponentI owner) throws Exception {
		
		// Owner doit respecter le contrat IDDSNode
		assert owner instanceof IDDSNode;
		super.installOn(owner);
		
		
		this.addOfferedInterface(WriteCI.class);
		this.addOfferedInterface(ReadCI.class);
		this.addOfferedInterface(ConnectClient.class);
		this.addOfferedInterface(Propagation.class);
		this.addOfferedInterface(ConnectDDSNode.class);
		
		this.addRequiredInterface(ConnectDDSNode.class);
		this.addRequiredInterface(Propagation.class);
		
	}

	@Override
	public void initialise() throws Exception {
		
		super.initialise();
		inPortConnectClient = new InPortConnectClient(uriConnectClient, getOwner(), getPluginURI());
		inPortConnectClient.publishPort();
		
		connectionDDS = new InConnectionDDS(uriConnectDDSNode, getOwner(),getPluginURI());
		connectionDDS.publishPort();
		
		inPortRead = new InPortRead<T>(getOwner(),getPluginURI());
		inPortRead.publishPort();
		
		inPortWrite = new InPortWrite<T>(getOwner(),getPluginURI());
		inPortWrite.publishPort();
		
		inPortPropagation = new InPortPropagation<T>(getOwner(),getPluginURI());
		inPortPropagation.publishPort();
		
		nodeAddress = new NodeAddress(connectionDDS.getPortURI(), inPortPropagation.getPortURI());
		
	}

	@Override
	public void finalise() throws Exception {
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			cp.getValue().doDisconnection();
		}
		super.finalise();
	}

	@Override
	public void uninstall() throws Exception {
		inPortConnectClient.unpublishPort();
		connectionDDS.unpublishPort();
		inPortRead.unpublishPort();
		inPortWrite.unpublishPort();
		inPortPropagation.unpublishPort();
		
		inPortConnectClient.destroyPort();
		inPortPropagation.destroyPort();
		connectionDDS.destroyPort();
		inPortRead.destroyPort();
		inPortWrite.destroyPort();
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			cp.getValue().unpublishPort();
			cp.getValue().destroyPort();
		}
		for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
			cp.getValue().unpublishPort();
			cp.getValue().destroyPort();
		}
		super.uninstall();
	}

	public String getReaderURI() throws Exception {
		return inPortRead.getPortURI();
	}

	public String getWriterURI() throws Exception {
		return inPortWrite.getPortURI();
	}

	public void connect(String uri) throws Exception {
		if(connectionOut.containsKey(uri)) {
			return;
		}
		System.out.println("connect");
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		System.out.println("mon uri " + uriConnectDDSNode + "uri du voisin " + nodeAddress.getNodeURI());
		getOwner().doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());
		System.out.println("port connected");
		cout.connect(nodeAddress);
		connectionOut.put(uri, cout);

	}
	
	public void connectPropagation(INodeAddress address) throws Exception {
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(getOwner());
		this.getOwner().doPortConnection(outPropagation.getPortURI(), address.getPropagationURI(), ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(address, outPropagation);
		OutConnectionDDS cout =  connectionOut.get(address.getNodeURI());
		connectionOutWithAddresses.put(address, cout);
	}


	public void connectBack(INodeAddress address) throws Exception {
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		this.getOwner().doPortConnection(cout.getPortURI(), address.getNodeURI(), ConnectorConnectionDDS.class.getCanonicalName());
		connectionOut.put(address.getNodeURI(), cout);
		connectionOutWithAddresses.put(address, cout);
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(getOwner());
		this.getOwner().doPortConnection(outPropagation.getPortURI(), address.getPropagationURI(), ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(address, outPropagation);
		cout.connectPropagation(nodeAddress);
		
	}

	public void disconnectBack(INodeAddress address) throws Exception {
		OutConnectionDDS out = connectionOut.remove(address.getNodeURI());
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation<T> pOut = outPropagationPorts.remove(address);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
		
	}

	public void disconnect(INodeAddress uri) throws Exception {
		OutConnectionDDS out  =  connectionOut.remove(uri.getNodeURI());
		out.disconnect(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation<T> pOut = outPropagationPorts.remove(uri);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
	}



	@SuppressWarnings("unchecked")
	public  Iterator<T> read(TopicDescription<T> reader) throws DDSTopicNotFoundException {
		return ((IDDSNode<T>) getOwner()).read(reader);
	}


	@SuppressWarnings("unchecked")
	public void  write(Topic<T> topic, T data) throws Exception {
		((IDDSNode<T>) getOwner()).write(topic, data);
	}

	public void propagerOut(T newObject, Topic<T> topic, String id) throws Exception {
		for (OutPortPropagation<T> cp : outPropagationPorts.values()) {
			System.out.println("yaw");
			cp.propager(newObject, topic, id);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void propagerIn(T newObject, Topic<T> topic, String id) throws Exception {
		((IDDSNode<T>) getOwner()).propager(newObject, topic, uriConnectDDSNode);
	}
	

}
