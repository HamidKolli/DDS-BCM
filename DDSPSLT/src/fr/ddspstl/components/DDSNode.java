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

import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.ddspstl.ports.InPortConnectClient;
import fr.ddspstl.ports.InPortRead;
import fr.ddspstl.ports.InPortWrite;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * TODO listeners readers/ writers * Reader -> quand on change un etat dans le
 * DDSNode (Propagation ou DataWriter) * Writer -> ACK(TCP)
 * 
 * composant qui genere les topics et qui les ajoute dans le DDSNode +
 * propagation du topic gestion de reader et writer * Map URI DataReader * Map
 * URI DataWriter
 * 
 * ajout de port in dans client pour notification reader OU writer OU les deux
 * gestion topic et participant * Map Topic Set<DataReader> * Map Topic
 * Set<DataWriter> gestion propagation * Map au lieu de Set de <Id,Topic> *
 * changer la methode propager a propager(id,newid,newTopic) * test si map[id]
 * -> suprimer map[id] remplacer par map[newid] = newTopic et notifier les
 * dataReaders du topic et lancer propagation sur les autre noeud avec id newid
 * et newTopic * si map[id ] n'existe pas test map[bewId ] -> exist? true :
 * throw exception *
 */

/**
 * 
 * set de topic -> set de domainParticipant
 *
 */
public class DDSNode extends AbstractComponent {

	// BCM
	private InPortConnectClient<Object> inPortConnectClient;
	private InPortRead inPortRead;
	private InPortWrite inPortWrite;
	private String uriConnectDDSNode;
	private String uriPluginConnection;
	// DDS
	private ServiceEnvironment serviceEnvironment;

	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Subscriber subscriber;
	

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode,String uriConnectInPort,DomainParticipant domainParticipant,ServiceEnvironment serviceEnvironment) throws Exception {
		super(nbThreads, nbSchedulableThreads);

		this.inPortConnectClient = new InPortConnectClient<Object>(uriConnectInPort, this);
		this.inPortRead = new InPortRead(this);
		this.inPortWrite = new InPortWrite(this);
		this.serviceEnvironment = serviceEnvironment;
		this.domainParticipant = domainParticipant;
		publisher = domainParticipant.createPublisher();
		subscriber = domainParticipant.createSubscriber();
		this.uriConnectDDSNode = uriConnectDDSNode;
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			inPortConnectClient.publishPort();
			inPortRead.publishPort();
			uriPluginConnection = AbstractPort.generatePortURI();
			ConnectionPlugin plugin = new ConnectionPlugin(uriConnectDDSNode);
			plugin.setPluginURI(uriPluginConnection);
			this.installPlugin(plugin);
			
			
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	public <T> Topic<T> connect(int domainID, String topicName) throws TimeoutException {
		
		domainParticipant.findTopic(topicName, Duration.infiniteDuration(serviceEnvironment));
		return null;
	}

	public String getReaderURI() throws Exception {
		return inPortRead.getPortURI();
	}

	public String getWriterURI() throws Exception {
		return inPortWrite.getPortURI();
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

	public <T> void write(DataWriter<T> reader,T data) throws TimeoutException {
		reader.write(data);
	}

	@Override
	public synchronized void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			inPortConnectClient.unpublishPort();
			inPortRead.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	public <T> void propager(T newObject, Topic<T> topic, String id) {
		
		
	}

}
