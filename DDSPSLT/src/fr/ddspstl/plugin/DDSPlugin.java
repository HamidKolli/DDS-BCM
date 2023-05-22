package fr.ddspstl.plugin;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.DDS.data.Datas;
import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorReadDDS;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.ports.InPortConnectClient;
import fr.ddspstl.ports.InPortPropagation;
import fr.ddspstl.ports.InPortRead;
import fr.ddspstl.ports.InPortReadDDS;
import fr.ddspstl.ports.InPortWrite;
import fr.ddspstl.ports.InPortWriteDDS;
import fr.ddspstl.ports.OutPortPropagation;
import fr.ddspstl.ports.OutPortReadDDS;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;

public class DDSPlugin<T> extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	private InPortRead<T> inPortRead;
	private InPortWrite<T> inPortWrite;
	private InPortConnectClient inPortConnectClient;
	private Map<TopicDescription<T>, Datas<T>> datas;
	private Map<TopicDescription<T>, String> topicIDWrite;
	private Map<TopicDescription<T>, String> topicIDTake;
	private Map<TopicDescription<T>, OutPortPropagation<T>> propagationPortToNextRoot;
	private Map<TopicDescription<T>, OutPortReadDDS<T>> readPortToRoot;
	private Map<TopicDescription<T>, OutPortWrite<T>> writePortToRoot;
	private Map<String, Semaphore> clientLock;
	private Map<String, Iterator<T>> result;
	private OutPortReadDDS<T> outPortReadDDS;
	private INodeAddress address;
	private InPortPropagation<T> inPortPropagation;
	private InPortReadDDS<T> inPortReadDDS;
	private InPortWriteDDS<T> inPortWriteDDS;

	private String executorServiceURI;
	private String executorServicePropagationURI;

	private String executorServiceReadWriteURI;

	public DDSPlugin(Set<TopicDescription<T>> set,
			Map<TopicDescription<T>, OutPortPropagation<T>> propagationPortToNextRoot,
			Map<TopicDescription<T>, OutPortReadDDS<T>> readPortToRoot,
			Map<TopicDescription<T>, OutPortWrite<T>> writePortToRoot, INodeAddress addresses,
			String executorServiceURI, String executorServicePropagationURI, String executorServiceReadWriteURI) {

		this.address = addresses;
		this.datas = new ConcurrentHashMap<>();
		for (TopicDescription<T> topic : set) {
			datas.put(topic, new Datas<T>(topic));
		}
		this.topicIDWrite = new ConcurrentHashMap<>();
		this.topicIDTake = new ConcurrentHashMap<>();
		this.clientLock = new ConcurrentHashMap<>();
		this.result = new ConcurrentHashMap<>();
		this.executorServiceURI = executorServiceURI;
		this.executorServicePropagationURI = executorServicePropagationURI;

		this.propagationPortToNextRoot = propagationPortToNextRoot;
		this.readPortToRoot = readPortToRoot;
		this.writePortToRoot = writePortToRoot;
		this.executorServiceReadWriteURI = executorServiceReadWriteURI;

	}

	public String getReaderURI() throws Exception {
		return inPortRead.getPortURI();
	}

	public String getWriterURI() throws Exception {
		return inPortWrite.getPortURI();
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> read(TopicDescription<T> topic) throws Exception {
		if (datas.containsKey(topic))
			return ((IDDSNode<T>) getOwner()).read(topic);

		String requestId = AbstractPort.generatePortURI();
		Semaphore s = new Semaphore(0);

		clientLock.put(requestId, s);

		readPortToRoot.get(topic).read(topic, address, requestId);
		s.acquire();

		return result.get(requestId);
	}

	public Iterator<T> readData(TopicDescription<T> topic) {
		return datas.get(topic).read();
	}

	@SuppressWarnings("unchecked")
	public void write(Topic<T> topic, T data) throws Exception {
		if (datas.containsKey(topic)) {
			((IDDSNode<T>) getOwner()).propager(data, topic, AbstractPort.generatePortURI(),
					new fr.ddspstl.time.Time((new Date()).getTime()));
			return;
		}

		writePortToRoot.get(topic).write(topic, data);
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> take(TopicDescription<T> topic) throws Exception {
		if (datas.containsKey(topic)) {
			return ((IDDSNode<T>) getOwner()).consommer(topic, AbstractPort.generatePortURI(), true);
		}

		String requestId = AbstractPort.generatePortURI();
		Semaphore s = new Semaphore(0);

		clientLock.put(requestId, s);

		readPortToRoot.get(topic).take(topic, address, requestId);
		s.acquire();

		return result.get(requestId);
	}

	@SuppressWarnings("unchecked")
	public void take(TopicDescription<T> topic, INodeAddress address, String requestID) throws Exception {
		if (!datas.containsKey(topic)) {
			throw new DDSTopicNotFoundException("Topic not found");
		}
		getOwner().doPortConnection(outPortReadDDS.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());

		outPortReadDDS.acceptResult(((IDDSNode<T>) getOwner()).consommer(topic, AbstractPort.generatePortURI(), true),
				requestID);

		outPortReadDDS.doDisconnection();

	}

	@SuppressWarnings("unchecked")
	public void read(TopicDescription<T> topic, INodeAddress address, String requestID) throws Exception {
		if (!datas.containsKey(topic)) {
			throw new DDSTopicNotFoundException("Topic not found");
		}
		getOwner().doPortConnection(outPortReadDDS.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());
		outPortReadDDS.acceptResult(((IDDSNode<T>) getOwner()).read(topic), requestID);
		outPortReadDDS.doDisconnection();

	}

	public void acceptResult(Iterator<T> result, String requestID) throws DDSTopicNotFoundException {
		if (!this.clientLock.containsKey(requestID)) {
			throw new DDSTopicNotFoundException("RequestID not found");
		}
		this.result.put(requestID, result);
		clientLock.get(requestID).release();
	}

	public void propager(T newObject, TopicDescription<T> topicName, String id, Time time) throws Exception {

		if (topicIDWrite.get(topicName) != null && topicIDWrite.get(topicName).equals(id))
			return;

		topicIDWrite.put(topicName, id);
		datas.get(topicName).write(newObject, time);

		propagationPortToNextRoot.get(topicName).propager(newObject, topicName, id, time);

	}

	public Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		if (topicIDTake.containsKey(topic) && topicIDTake.get(topic).equals(id))
			return datas.get(topic).take();

		topicIDTake.put(topic, id);
		if (!isFirst)
			datas.get(topic).take();
		return propagationPortToNextRoot.get(topic).consommer(topic, id, false);

	}

	@Override
	public void installOn(ComponentI owner) throws Exception {

		// Owner doit respecter le contrat IDDSNode
		assert owner instanceof IDDSNode;
		super.installOn(owner);
		this.addOfferedInterface(WriteCI.class);
		this.addOfferedInterface(ReadCI.class);
		this.addOfferedInterface(ConnectClient.class);
	}

	@Override
	public void initialise() throws Exception {

		super.initialise();
		inPortConnectClient = new InPortConnectClient(address.getClientUri(), getOwner(), getPluginURI(),
				executorServiceURI);
		inPortConnectClient.publishPort();

		inPortRead = new InPortRead<T>(getOwner(), getPluginURI());
		inPortRead.publishPort();

		inPortWrite = new InPortWrite<T>(getOwner(), getPluginURI());
		inPortWrite.publishPort();

		outPortReadDDS = new OutPortReadDDS<>(getOwner());
		outPortReadDDS.publishPort();

		inPortReadDDS = new InPortReadDDS<>(address.getReadURI(), getOwner(), getPluginURI(),
				executorServiceReadWriteURI);
		inPortReadDDS.publishPort();

		inPortWriteDDS = new InPortWriteDDS<>(address.getWriteURI(), getOwner(), getPluginURI(),
				executorServiceReadWriteURI);
		inPortWriteDDS.publishPort();

		inPortPropagation = new InPortPropagation<>(address.getPropagationURI(), getOwner(), getPluginURI(),
				executorServicePropagationURI);
		inPortPropagation.publishPort();
	}

	@Override
	public void finalise() throws Exception {
		super.finalise();

		if (outPortReadDDS.connected())
			outPortReadDDS.doDisconnection();

		for (OutPortPropagation<T> port : propagationPortToNextRoot.values()) {
			port.doDisconnection();
		}

		for (OutPortReadDDS<T> port : readPortToRoot.values()) {
			port.doDisconnection();
		}

		for (OutPortWrite<T> port : writePortToRoot.values()) {
			port.doDisconnection();
		}

	}

	@Override
	public void uninstall() throws Exception {

		inPortConnectClient.unpublishPort();

		inPortRead.unpublishPort();

		inPortWrite.unpublishPort();

		outPortReadDDS.unpublishPort();

		inPortReadDDS.unpublishPort();

		inPortWriteDDS.unpublishPort();

		inPortPropagation.unpublishPort();

		for (OutPortPropagation<T> port : propagationPortToNextRoot.values()) {
			port.unpublishPort();
		}

		for (OutPortReadDDS<T> port : readPortToRoot.values()) {
			port.unpublishPort();
		}

		for (OutPortWrite<T> port : writePortToRoot.values()) {
			port.unpublishPort();
		}

		super.uninstall();
	}

	// deconnexion de tout les port et depublication de tout les ports
	// regler la classe ddsnode
	// regler les connexion et les uris
	// cvm et configuration

}
