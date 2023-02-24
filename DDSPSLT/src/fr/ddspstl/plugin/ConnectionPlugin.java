package fr.ddspstl.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;

import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorConnectionDDS;
import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.ddspstl.interfaces.ConnectInClient;
import fr.ddspstl.interfaces.InRead;
import fr.ddspstl.interfaces.InWrite;
import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.ports.InConnectionDDS;
import fr.ddspstl.ports.InPortConnectClient;
import fr.ddspstl.ports.InPortPropagation;
import fr.ddspstl.ports.InPortRead;
import fr.ddspstl.ports.InPortWrite;
import fr.ddspstl.ports.OutConnectionDDS;
import fr.ddspstl.ports.OutPortPropagation;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class ConnectionPlugin extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	private InPortConnectClient inPortConnectClient;
	private InPortRead inPortRead;
	private InPortWrite inPortWrite;
	private InPortPropagation inPortPropagation;
	private InConnectionDDS connectionDDS;
	private String uriConnectInPort;
	private String uriConnectDDSNode;
	private Map<String, OutConnectionDDS> connectionOut;
	private Map<String,OutPortPropagation> outPropagationPorts ;

	public ConnectionPlugin(String uriConnection, String uriConnectInPort) {
		this.uriConnectInPort = uriConnectInPort;
		this.uriConnectDDSNode = uriConnection;
		this.connectionOut = new HashMap<>();
		this.outPropagationPorts = new HashMap<>();
	}


	@Override
	public void installOn(ComponentI owner) throws Exception {
		
		// Owner doit respecter le contrat IDDSNode
		assert owner instanceof IDDSNode;
		super.installOn(owner);
		
		
		this.addOfferedInterface(InRead.class);
		this.addOfferedInterface(InWrite.class);
		this.addOfferedInterface(ConnectInClient.class);
		this.addOfferedInterface(Propagation.class);
		this.addOfferedInterface(ConnectDDSNode.class);
		
		this.addRequiredInterface(ConnectDDSNode.class);
		this.addRequiredInterface(Propagation.class);
		
				
		
		
	}

	@Override
	public void initialise() throws Exception {
		
		super.initialise();
		
		this.connectionDDS = new InConnectionDDS(uriConnectDDSNode, getOwner(),getPluginURI());
		this.inPortConnectClient = new InPortConnectClient(uriConnectInPort, getOwner(),getPluginURI());
		this.inPortRead = new InPortRead(getOwner(),getPluginURI());
		this.inPortWrite = new InPortWrite(getOwner(),getPluginURI());
		this.inPortPropagation = new InPortPropagation(getOwner(),getPluginURI());
		connectionDDS.publishPort();
		inPortConnectClient.publishPort();
		inPortRead.publishPort();
		inPortWrite.publishPort();
		inPortPropagation.publishPort();
		
	}

	@Override
	public void finalise() throws Exception {
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			disconnect(cp.getKey());
		}
		super.finalise();
	}

	@Override
	public void uninstall() throws Exception {
		connectionDDS.unpublishPort();
		inPortConnectClient.unpublishPort();
		inPortRead.unpublishPort();
		inPortWrite.unpublishPort();
		connectionDDS.destroyPort();
		inPortConnectClient.destroyPort();
		inPortRead.destroyPort();
		inPortWrite.destroyPort();
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			cp.getValue().unpublishPort();
			cp.getValue().destroyPort();
		}
		for (Map.Entry<String, OutPortPropagation> cp : outPropagationPorts.entrySet()) {
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

	public void connect(String uri, int domainID) throws Exception {
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		this.getOwner().doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());
		String uriPropagationVoisin = cout.connect(connectionDDS.getPortURI(),inPortPropagation.getPortURI(), domainID);
		connectionOut.put(uri, cout);
		OutPortPropagation outPropagation = new OutPortPropagation(getOwner());
		this.getOwner().doPortConnection(outPropagation.getPortURI(), uriPropagationVoisin, ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(uri, outPropagation);
	}

	public String connectBack(String uri,String uriPropagation, int domainID) throws Exception {
		if (((IDDSNode) getOwner()).getDomainId() != domainID) {
			throw new Exception("impossible de ce connecter, domaine different");
		}
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		this.getOwner().doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());
		connectionOut.put(uri, cout);
		OutPortPropagation outPropagation = new OutPortPropagation(getOwner());
		this.getOwner().doPortConnection(outPropagation.getPortURI(), uriPropagation, ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(uri, outPropagation);
		
		return inPortPropagation.getPortURI();
	}

	public void disconnectBack(String uri) throws Exception {
		OutConnectionDDS out = connectionOut.get(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		connectionOut.remove(uri);
	}

	public void disconnect(String uri) throws Exception {
		OutConnectionDDS out  =  connectionOut.get(uri);
		out.disconnect(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation pOut = outPropagationPorts.get(uri);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
		connectionOut.remove(uri);
	}



	public int getDomainId() {
		return ((IDDSNode) getOwner()).getDomainId();
	}

	public void disconnectClient(String dataReader, String dataWriter) {
		((IDDSNode) getOwner()).disconnectClient(dataReader, dataWriter);
	}

	public String getDataReader(String topic) throws DDSTopicNotFoundException {
		return ((IDDSNode) getOwner()).getDataReader(topic);
	}

	public  Iterator<?> read(String reader) throws DDSTopicNotFoundException {
		return ((IDDSNode) getOwner()).read(reader);
	}

	public String getDataWriter(String topic) throws DDSTopicNotFoundException {
		return ((IDDSNode) getOwner()).getDataWriter(topic);
	}

	public <T> void write(String reader, T data) throws TimeoutException, DDSTopicNotFoundException {
		((IDDSNode) getOwner()).write(reader, data);
	}

	public <T> void propager(T newObject, Topic<T> topic, String id) throws Exception {
		((IDDSNode) getOwner()).propager(newObject, topic, id);
		for (OutPortPropagation cp : outPropagationPorts.values()) {
			cp.propager(newObject, topic, id);
		}
	}

}
