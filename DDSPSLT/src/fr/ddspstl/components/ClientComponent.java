package fr.ddspstl.components;

import java.util.Random;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;

import fr.ddspstl.DDS.publishers.interfaces.Publisher;
import fr.ddspstl.DDS.samples.Sample.Iterator;
import fr.ddspstl.DDS.subscribers.interfaces.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.PluginI;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * @param <T> : le type de la donnée
 * 
 * Composant représentant un Client
 */

public class ClientComponent<T> extends AbstractComponent {

	private static final String TAG_NAME = "CLIENT | ";

	protected DomainParticipant domainParticipant;
	protected String uriDDSNode;
	protected String uriReader;
	protected String uriWriter;

	private String topicName;
	private Subscriber subscriber;
	private DataReader<T> dataReader;

	private DataWriter<T> dataWriter;
	protected Publisher publisher;
	private boolean take;
	private static int cpt = 0;

	private T data;

	/**
	 * Constructeur
	 * 
	 * @param nbThreads            : nb de threads
	 * @param nbSchedulableThreads : nb de chedulableThreads
	 * @param uriDDSNode           : l'URI du noeud DDS auquel il est rattaché
	 * @param domainParticipant    : Le domainParticipant
	 * @throws Exception
	 */
	protected ClientComponent(int nbThreads, int nbSchedulableThreads, String uriDDSNode,
			DomainParticipant domainParticipant, boolean take, String topicName, T data) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.domainParticipant = domainParticipant;
		this.uriDDSNode = uriDDSNode;
		this.take = take;
		this.topicName = topicName;
		this.data = data;

		getTracer().setTitle(TAG_NAME + (++cpt));

	}

	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void start() throws ComponentStartException {
		org.omg.dds.topic.Topic<?> topic;
		try {
			topic = domainParticipant.findTopic(topicName, null);
			publisher = (Publisher) domainParticipant.createPublisher();
			if (topic == null)
				throw new Exception("topic not found");
			dataWriter = (DataWriter<T>) ((Publisher) publisher).createDataWriter(topic, uriDDSNode);
			assert dataWriter != null;
			PluginI plugin = (PluginI) dataWriter;
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
			subscriber = (Subscriber) domainParticipant.createSubscriber();
			dataReader = (DataReader<T>) ((Subscriber) subscriber).createDataReader(topic, uriDDSNode);
			plugin = (PluginI) dataReader;
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}

		super.start();
	}

	
	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 *
	 */
	@Override
	public void execute() throws Exception {

		traceMessage("Debut client\n");

		Thread.sleep(1000L + (new Random()).nextLong(100L));

		traceMessage("Debut write\n");

		dataWriter.write(data);

		traceMessage("Fin write\n");

		Thread.sleep(3000L);

		traceMessage("Debut" + (take ? "Take" : "Read")+ "\n");

		Iterator<?> data = null;
		if (take) {
			data = (Iterator<?>) dataReader.take();
		} else {
			data = (Iterator<?>) dataReader.read();
		}
		
		
		
		while (data.hasNext()) {
			traceMessage(data.next().getData().toString()+"\n");
		}

		traceMessage("Fin" + (take ? "Take" : "Read")+"\n");

		super.execute();
	}

}
