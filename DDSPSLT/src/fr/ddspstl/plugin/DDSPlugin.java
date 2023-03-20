package fr.ddspstl.plugin;

import java.util.HashMap;
import java.util.Map;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

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

	private InPortConnectClient<T> inPortConnectClient;
	private InPortRead<T> inPortRead;
	private InPortWrite<T> inPortWrite;
	private InPortPropagation<T> inPortPropagation;
	private InConnectionDDS connectionDDS;
	private String uriConnectInPort;
	private String uriConnectDDSNode;
	private Map<String, OutConnectionDDS> connectionOut;
	private Map<String,OutPortPropagation<T>> outPropagationPorts ;
	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Subscriber subscriber;

	public DDSPlugin(String uriConnection, String uriConnectInPort, DomainParticipant domainParticipant, Publisher publisher, Subscriber subscriber) {
		this.uriConnectInPort = uriConnectInPort;
		this.uriConnectDDSNode = uriConnection;
		this.connectionOut = new HashMap<>();
		this.outPropagationPorts = new HashMap<>();
		this.domainParticipant = domainParticipant;
		this.publisher = publisher;
		this.subscriber = subscriber;
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
		
		this.connectionDDS = new InConnectionDDS(uriConnectDDSNode, getOwner(),getPluginURI());
		this.inPortConnectClient = new InPortConnectClient<T>(uriConnectInPort, getOwner(),getPluginURI());
		this.inPortRead = new InPortRead<T>(getOwner(),getPluginURI());
		this.inPortWrite = new InPortWrite<T>(getOwner(),getPluginURI());
		this.inPortPropagation = new InPortPropagation<T>(getOwner(),getPluginURI());
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
		inPortPropagation.unpublishPort();
		
		inPortPropagation.destroyPort();
		connectionDDS.destroyPort();
		inPortConnectClient.destroyPort();
		inPortRead.destroyPort();
		inPortWrite.destroyPort();
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			cp.getValue().unpublishPort();
			cp.getValue().destroyPort();
		}
		for (Map.Entry<String, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
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
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(getOwner());
		this.getOwner().doPortConnection(outPropagation.getPortURI(), uriPropagationVoisin, ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(uri, outPropagation);
	}

	@SuppressWarnings("unchecked")
	public String connectBack(String uri,String uriPropagation, int domainID) throws Exception {
		if (((IDDSNode<T>) getOwner()).getDomainId() != domainID) {
			throw new Exception("impossible de ce connecter, domaine different");
		}
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		this.getOwner().doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());
		connectionOut.put(uri, cout);
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(getOwner());
		this.getOwner().doPortConnection(outPropagation.getPortURI(), uriPropagation, ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(uri, outPropagation);
		
		return inPortPropagation.getPortURI();
	}

	public void disconnectBack(String uri) throws Exception {
		OutConnectionDDS out = connectionOut.remove(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		@SuppressWarnings("unchecked")
		OutPortPropagation<T> pOut = outPropagationPorts.remove(uri);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
		
	}

	public void disconnect(String uri) throws Exception {
		OutConnectionDDS out  =  connectionOut.remove(uri);
		out.disconnect(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		@SuppressWarnings("unchecked")
		OutPortPropagation<T> pOut = outPropagationPorts.remove(uri);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
	}



	@SuppressWarnings("unchecked")
	public int getDomainId() {
		return ((IDDSNode<T>) getOwner()).getDomainId();
	}

	@SuppressWarnings("unchecked")
	public void disconnectClient(String dataReader, String dataWriter) {
		((IDDSNode<T>) getOwner()).disconnectClient(dataReader, dataWriter);
	}

	@SuppressWarnings("unchecked")
	public DataReader<T> getDataReader(TopicDescription<T> topic) throws Exception {
		if(domainParticipant.findTopic(topic.getName(), null) == null) {
			throw new DDSTopicNotFoundException();
		}
		return ((fr.ddspstl.DDS.subscribers.interfaces.Subscriber)subscriber).createDataReader(topic, inPortRead.getPortURI());
	}

	@SuppressWarnings("unchecked")
	public  Iterator<T> read(TopicDescription<T> reader) throws DDSTopicNotFoundException {
		return ((IDDSNode<T>) getOwner()).read(reader);
	}

	@SuppressWarnings("unchecked")
	public DataWriter<T> getDataWriter(Topic<T> topic) throws Exception {
		if(domainParticipant.findTopic(topic.getName(), null) == null) {
			throw new DDSTopicNotFoundException();
		}
		return ((fr.ddspstl.DDS.publishers.interfaces.Publisher)publisher).createDataWriter(topic,inPortWrite.getPortURI());
	}

	@SuppressWarnings("unchecked")
	public void  write(Topic<T> topic, T data) throws Exception {
		((IDDSNode<T>) getOwner()).write(topic, data);
	}

	@SuppressWarnings("unchecked")
	public void propagerOut(T newObject, Topic<T> topic, String id) throws Exception {
		for (OutPortPropagation<T> cp : outPropagationPorts.values()) {
			cp.propager(newObject, topic, id);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void propagerIn(T newObject, Topic<T> topic, String id) throws Exception {
		((IDDSNode<T>) getOwner()).propager(newObject, topic, uriConnectDDSNode);
	}
	

}
