package fr.ddspstl.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactory;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.Topic;

import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.ports.InPortConnectClient;
import fr.ddspstl.ports.InPortRead;
import fr.ddspstl.ports.InPortWrite;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**]
 * TODO
 * listeners readers/ writers
 * * Reader -> quand on change un etat dans le DDSNode (Propagation ou DataWriter)
 * * Writer -> ACK(TCP)
 * 
 * composant qui genere les topics et qui les ajoute dans le DDSNode + propagation du topic
 * gestion de reader et writer
 * * Map URI DataReader 
 * * Map URI DataWriter
 * 
 * ajout de port in dans  client pour notification reader OU writer OU les deux 
 * gestion topic et participant 
 * * Map Topic Set<DataReader>
 * * Map Topic Set<DataWriter>
 * gestion propagation
 * * Map au lieu de Set de <Id,Topic>
 * * changer la methode propager a propager(id,newid,newTopic)
 * * test si map[id]  -> suprimer map[id] remplacer par map[newid] = newTopic et 
 * 			notifier les dataReaders du topic et
 * 			lancer propagation sur les autre noeud avec id newid et newTopic
 * * si map[id ] n'existe pas test map[bewId ] -> exist? true : throw exception
 * * 
 * 
 */


public class DDSNode extends AbstractComponent {

	// BCM
	private InPortConnectClient inPortConnectClient;
	private InPortRead inPortRead;
	private InPortWrite inPortWrite;

	// DDS
	private Set<Topic<Object>> topics;
	private ServiceEnvironment serviceEnvironment;
	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Subscriber subscriber;
	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectInPort) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.inPortConnectClient = new InPortConnectClient(uriConnectInPort, this);
		this.inPortRead = new InPortRead(this);
		this.inPortWrite = new InPortWrite(this);
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("nbThreads", nbThreads);
		map.put("nbSchedulableThreads", nbSchedulableThreads);
		map.put("uriConnectInPort", uriConnectInPort);
		serviceEnvironment = ServiceEnvironment.createInstance(DDSNode.class.getCanonicalName(), map,
				DDSNode.class.getClassLoader());
		
		domainParticipant = DomainParticipantFactory.getInstance(serviceEnvironment).createParticipant();
		
		publisher =  domainParticipant.createPublisher();
		subscriber = domainParticipant.createSubscriber();
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			inPortConnectClient.publishPort();
			inPortRead.publishPort();
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	public Set<Topic<Object>> connect() {
		return topics;
	}

	public String getReaderURI() throws Exception {
		return inPortRead.getPortURI();
	}

	public String getWriterURI() throws Exception {
		return inPortWrite.getPortURI();
	}

	public void disconnectClient() {

	}

	public <T> DataReader<T> getDataReader(Topic<T> topic) throws DDSTopicNotFoundException {
		if (topics.contains(topic)) {
			return subscriber.createDataReader(topic);
		}
		throw new DDSTopicNotFoundException("getDataReader : topic not found");
	}

	public <T> T read(DataReader<T> reader) {
		
		return null;
	}

	public <T> DataWriter<T> getDataWriter(Topic<T> topic) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T write(DataWriter<T> reader) {
		// TODO Auto-generated method stub
		return null;
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

}
