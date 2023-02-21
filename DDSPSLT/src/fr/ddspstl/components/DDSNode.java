package fr.ddspstl.components;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.Topic;

import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class DDSNode extends AbstractComponent implements IDDSNode {

	// BCM
	private String uriConnectDDSNode;
	private String uriConnectInPort;

	private ConnectionPlugin plugin;
	// DDS
	private ServiceEnvironment serviceEnvironment;
	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Subscriber subscriber;
	private Map<String, DataReader<?>> dataReaders;
	private Map<String, DataWriter<?>> dataWriters;

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode, String uriConnectInPort,
			DomainParticipant domainParticipant, ServiceEnvironment serviceEnvironment) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.uriConnectInPort = uriConnectInPort;

		this.serviceEnvironment = serviceEnvironment;
		this.domainParticipant = domainParticipant;
		publisher = domainParticipant.createPublisher();
		subscriber = domainParticipant.createSubscriber();
		this.uriConnectDDSNode = uriConnectDDSNode;
		dataReaders = new HashMap<>();
		dataWriters = new HashMap<>();

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			plugin = new ConnectionPlugin(uriConnectDDSNode, uriConnectInPort);
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	@Override
	public int getDomainId() {
		return domainParticipant.getDomainId();
	}

	@Override
	public void disconnectClient() {
	}

	@Override
	public String getDataReader(String topic) throws DDSTopicNotFoundException {
		String id = AbstractPort.generatePortURI();
		DataReader<?> dataReader = null;
		try {
			dataReader = subscriber.createDataReader(domainParticipant.findTopic(topic, null));
		} catch (TimeoutException e) {
			throw new DDSTopicNotFoundException(e);
		}
		dataReaders.put(id, dataReader);
		return id;
	}

	@Override
	public  Iterator<?> read(String reader) throws DDSTopicNotFoundException {
		return dataReaders.get(reader).read();
	}

	@Override
	public String getDataWriter(String topic) throws DDSTopicNotFoundException {
		String id = AbstractPort.generatePortURI();
		DataWriter<?> dataWriter;
		try {
			dataWriter = publisher.createDataWriter(domainParticipant.findTopic(topic, null));
		} catch (TimeoutException e) {
			throw new DDSTopicNotFoundException(e);
		}
		dataWriters.put(id, dataWriter);
		return id;
	}

	@Override
	public <T> void write(String writer, T data) throws TimeoutException, DDSTopicNotFoundException {

	}

	@Override
	public <T> void propager(T newObject, Topic<T> topic, String id) {
		// TODO
	}

	@Override
	public synchronized void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {

		super.shutdown();
	}

}
