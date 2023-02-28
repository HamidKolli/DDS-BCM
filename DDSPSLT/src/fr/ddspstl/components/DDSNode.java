package fr.ddspstl.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.Subscriber;

import fr.ddspstl.DDS.data.Datas;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class DDSNode extends AbstractComponent implements IDDSNode {

	private ConnectionPlugin plugin;
	private List<String> uriDDSNodes;
	// DDS
	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Subscriber subscriber;
	private Map<String, DataReader<Object>> dataReaders;
	private Map<String, DataWriter<Object>> dataWriters;
	private Map<String,String> topicID;

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode, String uriConnectInPort,
			List<String> uriDDSNodes, DomainParticipant domainParticipant,Map<String,String> topicID)
			throws Exception {
		super(nbThreads, nbSchedulableThreads);

		this.domainParticipant = domainParticipant;
		publisher = domainParticipant.createPublisher();
		subscriber = domainParticipant.createSubscriber();
		dataReaders = new HashMap<>();
		dataWriters = new HashMap<>();
		this.topicID = new HashMap<>(topicID);
		this.uriDDSNodes = new ArrayList<String>(uriDDSNodes);

		plugin = new ConnectionPlugin(uriConnectDDSNode, uriConnectInPort);

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {

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
	public void disconnectClient(String dataReader, String dataWriter) {
		if (dataReader != null)
			dataReaders.remove(dataReader);
		if (dataWriter != null)
			dataWriters.remove(dataWriter);
	}

	@Override
	public void execute() throws Exception {
		for (String uri : uriDDSNodes) {
			this.plugin.connect(uri, this.domainParticipant.getDomainId());
		}
		System.out.println("connexion fini ");
		super.execute();
	}

	@Override
	public String getDataReader(String topic) throws DDSTopicNotFoundException {
		String id = AbstractPort.generatePortURI();
		DataReader<Object> dataReader = null;
		try {
			dataReader = subscriber.createDataReader(domainParticipant.findTopic(topic, null));
		} catch (TimeoutException e) {
			throw new DDSTopicNotFoundException(e);
		}
		dataReaders.put(id, dataReader);
		return id;
	}

	@Override
	public Iterator<?> read(String reader) throws DDSTopicNotFoundException {
		return dataReaders.get(reader).read();
	}

	@Override
	public String getDataWriter(String topic) throws DDSTopicNotFoundException {
		String id = AbstractPort.generatePortURI();
		DataWriter<Object> dataWriter;
		try {
			dataWriter = publisher.createDataWriter(domainParticipant.findTopic(topic, null));
		} catch (TimeoutException e) {
			throw new DDSTopicNotFoundException(e);
		}
		dataWriters.put(id, dataWriter);
		return id;
	}

	@Override
	public <T> void write(String writer, T data) throws Exception {
		dataWriters.get(writer).write(data);
		propager(data, dataWriters.get(writer).getTopic().getName(), AbstractPort.generatePortURI());
	}



	@Override
	public synchronized void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		super.shutdown();
	}

	@Override
	public <T> void propager(T newObject, String topicName, String id) throws Exception {
		if (topicID.get(topicName) == id) {
			return;
		}
		topicID.put(topicName, id);
		Datas<T> data = (Datas<T>) domainParticipant.findTopic(topicName, null);
		data.write(newObject);
		this.plugin.propager(newObject, topicName, id);
	}

}
