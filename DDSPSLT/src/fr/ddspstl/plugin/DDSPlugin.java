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

public class DDSPlugin extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	public static final String LOGGER_TAG = "DDSPlugin | ";

	private InPortRead inPortRead;
	private InPortWrite inPortWrite;
	private InPortConnectClient inPortConnectClient;
	private Map<TopicDescription<?>, Datas<?>> datas;
	private Map<TopicDescription<?>, String> topicIDWrite;
	private Map<TopicDescription<?>, String> topicIDTake;
	private Map<TopicDescription<?>, OutPortPropagation> propagationPortToNextRoot;
	private Map<TopicDescription<?>, OutPortReadDDS> readPortToRoot;
	private Map<TopicDescription<?>, OutPortWrite> writePortToRoot;
	private Map<String, Semaphore> clientLock;
	private Map<String, Iterator<?>> result;
	private OutPortReadDDS outPortReadDDS;
	private INodeAddress address;
	private InPortPropagation inPortPropagation;
	private InPortReadDDS inPortReadDDS;
	private InPortWriteDDS inPortWriteDDS;

	private String executorServiceURI;
	private String executorServicePropagationURI;

	private String executorServiceReadWriteURI;

	public DDSPlugin(Set<TopicDescription<?>> set,
			Map<TopicDescription<?>, OutPortPropagation> propagationPortToNextRoot,
			Map<TopicDescription<?>, OutPortReadDDS> readPortToRoot,
			Map<TopicDescription<?>, OutPortWrite> writePortToRoot, INodeAddress addresses, String executorServiceURI,
			String executorServicePropagationURI, String executorServiceReadWriteURI) {

		this.address = addresses;
		this.datas = new ConcurrentHashMap<>();
		for (TopicDescription<?> topic : set) {
			datas.put(topic, new Datas<>(topic));
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
		getOwner().logMessage(LOGGER_TAG + " getReaderURI");
		return inPortRead.getPortURI();
	}

	public String getWriterURI() throws Exception {
		getOwner().logMessage(LOGGER_TAG + " getWriterURI");
		return inPortWrite.getPortURI();
	}

	@SuppressWarnings("unchecked")
	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {
		getOwner().logMessage(LOGGER_TAG + " read topic : " + topic.getName());

		if (datas.containsKey(topic))
			return ((IDDSNode) getOwner()).read(topic);

		getOwner().logMessage(LOGGER_TAG + " read topic : " + topic.getName() + " get from topic");
		String requestId = AbstractPort.generatePortURI();
		Semaphore s = new Semaphore(0);

		clientLock.put(requestId, s);

		readPortToRoot.get(topic).read(topic, address, requestId);
		s.acquire();
		getOwner().logMessage(LOGGER_TAG + "Fin read topic : " + topic.getName());

		return (Iterator<T>) result.get(requestId);
	}

	public Iterator<?> readData(TopicDescription<?> topic) {
		getOwner().logMessage(LOGGER_TAG + "Fin readData topic : " + topic.getName());
		return datas.get(topic).read();
	}

	public <T> void write(Topic<T> topic, T data) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Write topic : " + topic.getName());

		if (datas.containsKey(topic)) {
			((IDDSNode) getOwner()).propager(data, topic, AbstractPort.generatePortURI(),
					new fr.ddspstl.time.Time((new Date()).getTime()));
			getOwner().logMessage(LOGGER_TAG + "Fin write topic : " + topic.getName());

			return;
		}
		getOwner().logMessage(LOGGER_TAG + "Fin write topic : " + topic.getName());

		writePortToRoot.get(topic).write(topic, data);
	}

	public Iterator<?> take(TopicDescription<?> topic) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Take topic : " + topic.getName());

		if (datas.containsKey(topic))
			return ((IDDSNode) getOwner()).consommer(topic, AbstractPort.generatePortURI(), true);

		getOwner().logMessage(LOGGER_TAG + "Take topic : " + topic.getName() + " get from topic");

		String requestId = AbstractPort.generatePortURI();
		Semaphore s = new Semaphore(0);

		clientLock.put(requestId, s);

		readPortToRoot.get(topic).take(topic, address, requestId);
		s.acquire();
		getOwner().logMessage(LOGGER_TAG + "Fin take topic : " + topic.getName());

		return result.get(requestId);
	}

	public void take(TopicDescription<?> topic, INodeAddress address, String requestID) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Take root topic : " + topic.getName());
		if (!datas.containsKey(topic)) {
			throw new DDSTopicNotFoundException("Topic not found");
		}
		getOwner().doPortConnection(outPortReadDDS.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());

		outPortReadDDS.acceptResult(((IDDSNode) getOwner()).consommer(topic, AbstractPort.generatePortURI(), true),
				requestID);

		outPortReadDDS.doDisconnection();
		getOwner().logMessage(LOGGER_TAG + "Fin take root topic : " + topic.getName());

	}

	public void read(TopicDescription<?> topic, INodeAddress address, String requestID) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Read root topic : " + topic.getName());

		if (!datas.containsKey(topic)) {
			throw new DDSTopicNotFoundException("Topic not found");
		}
		getOwner().doPortConnection(outPortReadDDS.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());
		outPortReadDDS.acceptResult(((IDDSNode) getOwner()).read(topic), requestID);
		outPortReadDDS.doDisconnection();
		getOwner().logMessage(LOGGER_TAG + "Fin read root topic : " + topic.getName());


	}

	public void acceptResult(Iterator<?> result, String requestID) throws DDSTopicNotFoundException {
		getOwner().logMessage(LOGGER_TAG + "AcceptResult" );

		if (!this.clientLock.containsKey(requestID)) {
			throw new DDSTopicNotFoundException("RequestID not found");
		}
		this.result.put(requestID, result);
		clientLock.get(requestID).release();
		getOwner().logMessage(LOGGER_TAG + "Fin acceptResult" );
	}

	public <T> void propager(T newObject, TopicDescription<T> topicName, String id, Time time) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propager topic : " + topicName.getName() );
		if (topicIDWrite.get(topicName) != null && topicIDWrite.get(topicName).equals(id))
			return;

		topicIDWrite.put(topicName, id);
		datas.get(topicName).write(newObject, time);

		propagationPortToNextRoot.get(topicName).propager(newObject, topicName, id, time);
		getOwner().logMessage(LOGGER_TAG + "Fin propager topic : " + topicName.getName() );
	}

	public Iterator<?> consommer(TopicDescription<?> topic, String id, boolean isFirst) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Consommer topic : " + topic.getName() );
		if (topicIDTake.containsKey(topic) && topicIDTake.get(topic).equals(id))
			return datas.get(topic).take();

		topicIDTake.put(topic, id);
		if (!isFirst)
			datas.get(topic).take();
		getOwner().logMessage(LOGGER_TAG + "Fin Consommer topic : " + topic.getName());
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

		inPortRead = new InPortRead(getOwner(), getPluginURI());
		inPortRead.publishPort();

		inPortWrite = new InPortWrite(getOwner(), getPluginURI());
		inPortWrite.publishPort();

		outPortReadDDS = new OutPortReadDDS(getOwner());
		outPortReadDDS.publishPort();

		inPortReadDDS = new InPortReadDDS(address.getReadURI(), getOwner(), getPluginURI(),
				executorServiceReadWriteURI);
		inPortReadDDS.publishPort();

		inPortWriteDDS = new InPortWriteDDS(address.getWriteURI(), getOwner(), getPluginURI(),
				executorServiceReadWriteURI);
		inPortWriteDDS.publishPort();

		inPortPropagation = new InPortPropagation(address.getPropagationURI(), getOwner(), getPluginURI(),
				executorServicePropagationURI);
		inPortPropagation.publishPort();
	}

	@Override
	public void finalise() throws Exception {
		super.finalise();

		if (outPortReadDDS.connected())
			outPortReadDDS.doDisconnection();

		for (OutPortPropagation port : propagationPortToNextRoot.values()) {
			port.doDisconnection();
		}

		for (OutPortReadDDS port : readPortToRoot.values()) {
			port.doDisconnection();
		}

		for (OutPortWrite port : writePortToRoot.values()) {
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

		for (OutPortPropagation port : propagationPortToNextRoot.values()) {
			port.unpublishPort();
		}

		for (OutPortReadDDS port : readPortToRoot.values()) {
			port.unpublishPort();
		}

		for (OutPortWrite port : writePortToRoot.values()) {
			port.unpublishPort();
		}

		super.uninstall();
	}

}
