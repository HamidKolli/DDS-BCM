package fr.ddspstl.components;

import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
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

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode, String uriConnectInPort,
			DomainParticipant domainParticipant, ServiceEnvironment serviceEnvironment) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.uriConnectInPort = uriConnectInPort;

		this.serviceEnvironment = serviceEnvironment;
		this.domainParticipant = domainParticipant;
		publisher = domainParticipant.createPublisher();
		subscriber = domainParticipant.createSubscriber();
		this.uriConnectDDSNode = uriConnectDDSNode;

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			plugin = new ConnectionPlugin(uriConnectDDSNode, uriConnectInPort);
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	@Override
	public synchronized void finalise() throws Exception {
		plugin.finalise();
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			plugin.uninstall();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	public <T> Topic<T> connect(int domainID, String topicName) throws TimeoutException {
		return domainParticipant.findTopic(topicName, Duration.infiniteDuration(serviceEnvironment));
	}

	public int getDomainId() {
		return domainParticipant.getDomainId();
	}

	public void disconnectClient() {
	}

	public <T> DataReader<T> getDataReader(Topic<T> topic) throws DDSTopicNotFoundException {
		return subscriber.createDataReader(topic);
	}

	public <T> T read(DataReader<T> reader) {
		return reader.read().next().getData();
	}

	public <T> DataWriter<T> getDataWriter(Topic<T> topic) {
		return publisher.createDataWriter(topic);
	}

	public <T> void write(DataWriter<T> reader, T data) throws TimeoutException {
		reader.write(data);
	}

	public <T> void propager(T newObject, Topic<T> topic, String id) {
		// TODO
	}

}
