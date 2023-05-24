package fr.ddspstl.plugin;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.connectors.ConnectorReadDDS;
import fr.ddspstl.connectors.ConnectorWrite;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.interfaces.ReadDDSCI;
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

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Plugin pour les noeuds DDS
 */
public class DDSPlugin extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	public static final String LOGGER_TAG = "DDSPlugin | ";

	private InPortRead inPortRead;
	private InPortWrite inPortWrite;
	private InPortConnectClient inPortConnectClient;

	private Map<String, TopicDescription<?>> topicIDWrite;
	private Map<String, TopicDescription<?>> topicIDTake;
	private Map<TopicDescription<?>, OutPortPropagation> propagationPortToNextRoot;
	private Map<TopicDescription<?>, OutPortReadDDS> readPortFromRoot;
	private Map<TopicDescription<?>, OutPortWrite> writePortFromRoot;
	private Map<String, Semaphore> clientLock;
	private Map<String, Iterator<?>> result;
	private OutPortReadDDS outPortReadDDS;
	private INodeAddress address;
	private InPortPropagation inPortPropagation;
	private InPortReadDDS inPortReadDDS;
	private InPortWriteDDS inPortWriteDDS;
	private Lock lockOutPort;

	private String executorServiceURI;
	private String executorServicePropagationURI;

	private String executorServiceReadWriteURI;
	private Set<TopicDescription<?>> topics;

	/**
	 * Constructeur
	 * 
	 * @param set : l'ensemble des TopicDescription
	 * @param addresses : adresse du noeud
	 * @param executorServiceURI : L'uri De l'executorService
	 * @param executorServicePropagationURI : L'uri De l'executorService pour la propagation
	 * @param executorServiceReadWriteURI : L'uri De l'executorService pour le read/write
	 */
	public DDSPlugin(Set<TopicDescription<?>> set, INodeAddress addresses, String executorServiceURI,
			String executorServicePropagationURI, String executorServiceReadWriteURI) {

		this.address = addresses;
		this.lockOutPort = new ReentrantLock();

		this.topicIDWrite = new ConcurrentHashMap<>();
		this.topicIDTake = new ConcurrentHashMap<>();
		this.clientLock = new ConcurrentHashMap<>();
		this.result = new ConcurrentHashMap<>();
		this.executorServiceURI = executorServiceURI;
		this.executorServicePropagationURI = executorServicePropagationURI;

		this.propagationPortToNextRoot = new ConcurrentHashMap<>();
		this.readPortFromRoot = new ConcurrentHashMap<>();
		this.writePortFromRoot = new ConcurrentHashMap<>();
		this.executorServiceReadWriteURI = executorServiceReadWriteURI;
		topics = set;

	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#installOn(ComponentI)
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception {

		// Owner doit respecter le contrat IDDSNode
		assert owner instanceof IDDSNode;
		super.installOn(owner);
		this.addOfferedInterface(WriteCI.class);
		this.addOfferedInterface(ReadCI.class);
		this.addOfferedInterface(ConnectClient.class);
		this.addOfferedInterface(Propagation.class);
		this.addOfferedInterface(ReadDDSCI.class);

		this.addRequiredInterface(Propagation.class);
		this.addRequiredInterface(ReadDDSCI.class);
		this.addRequiredInterface(WriteCI.class);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#initialise()
	 */
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


	/**
	 * methode connect : connection
	 * 
	 * @param address : adresse du noeud auquel se connecter
	 * @param topic : le nom du topic
	 * @throws Exception
	 */
	public void connect(INodeAddress address, TopicDescription<?> topic) throws Exception {
		OutPortWrite port = new OutPortWrite(getOwner());
		port.publishPort();
		getOwner().doPortConnection(port.getPortURI(), address.getWriteURI(), ConnectorWrite.class.getCanonicalName());
		writePortFromRoot.put(topic, port);

		OutPortReadDDS port2 = new OutPortReadDDS(getOwner());
		port2.publishPort();
		getOwner().doPortConnection(port2.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());
		readPortFromRoot.put(topic, port2);
	}

	/**
	 * methode connectRoot : connection à un noeud root
	 * 
	 * @param address : adresse du noeud auquel se connecter
	 * @param topic : le nom du topic
	 * @throws Exception
	 */
	public void connectRoot(INodeAddress address, TopicDescription<?> topic) throws Exception {
		OutPortPropagation port = new OutPortPropagation(getOwner());
		port.publishPort();
		getOwner().doPortConnection(port.getPortURI(), address.getPropagationURI(),
				ConnectorPropagation.class.getCanonicalName());
		propagationPortToNextRoot.put(topic, port);
	}

	/**
	 * getter
	 * 
	 * @return String : l'URi du port Reader
	 * @throws Exception
	 */
	public String getReaderURI() throws Exception {
		getOwner().logMessage(LOGGER_TAG + " getReaderURI");
		return inPortRead.getPortURI();
	}

	/**
	 * getter 
	 * 
	 * @return String : l'Uri du port wrtiter
	 * @throws Exception
	 */
	public String getWriterURI() throws Exception {
		getOwner().logMessage(LOGGER_TAG + " getWriterURI");
		return inPortWrite.getPortURI();
	}

	/**
	 * Methode read : effectue un read
	 * 
	 * @param topic : le topic dans lequel lire
	 * @return Iterator(T): la donnée lue
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {
		getOwner().logMessage(LOGGER_TAG + " read topic : " + topic.getName());

		if (topics.contains(topic))
			return ((IDDSNode) getOwner()).read(topic);
		if (!readPortFromRoot.containsKey(topic))
			throw new DDSTopicNotFoundException("Topic not found, please check your cvm config");
		getOwner().logMessage(LOGGER_TAG + " read topic : " + topic.getName() + " get from topic");
		String requestId = AbstractPort.generatePortURI();
		Semaphore s = new Semaphore(0);

		clientLock.put(requestId, s);

		readPortFromRoot.get(topic).read(topic, address, requestId);
		s.acquire();
		getOwner().logMessage(LOGGER_TAG + "Fin read topic : " + topic.getName());

		return (Iterator<T>) result.get(requestId);
	}

	/**
	 * Methode take : effectue un take
	 * 
	 * @param topic : le topic dans lequel take
	 * @return Iterator(T) : la donnée prise
	 * @throws Exception
	 */
	public Iterator<?> take(TopicDescription<?> topic) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Take topic : " + topic.getName());

		if (topics.contains(topic))
			return ((IDDSNode) getOwner()).consommer(topic, AbstractPort.generatePortURI(), true);

		if (!readPortFromRoot.containsKey(topic))
			throw new DDSTopicNotFoundException("Topic not found, please check your cvm config");

		getOwner().logMessage(LOGGER_TAG + "Take topic : " + topic.getName() + " get from topic");

		String requestId = AbstractPort.generatePortURI();
		Semaphore s = new Semaphore(0);

		clientLock.put(requestId, s);

		readPortFromRoot.get(topic).take(topic, address, requestId);
		s.acquire();
		getOwner().logMessage(LOGGER_TAG + "Fin take topic : " + topic.getName());

		return result.get(requestId);
	}

	/**
	 * Methode take : effectue un take
	 * 
	 * @param topic : le topic
	 * @param address : l'adresse du noeud
	 * @param requestID : l'ID de la requete
	 * @throws Exception
	 */
	public void take(TopicDescription<?> topic, INodeAddress address, String requestID) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Take root topic : " + topic.getName());

		if (!topics.contains(topic)) {
			throw new DDSTopicNotFoundException("Topic not found");
		}

		Iterator<?> res = ((IDDSNode) getOwner()).consommer(topic, AbstractPort.generatePortURI(), true);

		lockOutPort.lock();

		getOwner().doPortConnection(outPortReadDDS.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());

		outPortReadDDS.acceptResult(res, requestID);

		outPortReadDDS.doDisconnection();
		lockOutPort.unlock();

		getOwner().logMessage(LOGGER_TAG + "Fin take root topic : " + topic.getName());

	}

	/**
	 * methode read : effectue un read
	 * 
	 * @param topic : le topic
	 * @param address : l'adresse du noeud
	 * @param requestID : l'ID de la requete
	 * @throws Exception
	 */
	public void read(TopicDescription<?> topic, INodeAddress address, String requestID) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Read root topic : " + topic.getName());

		if (!topics.contains(topic)) {
			throw new DDSTopicNotFoundException("Topic not found");
		}
		
		Iterator<?> res = ((IDDSNode) getOwner()).read(topic);
		lockOutPort.lock();
		getOwner().doPortConnection(outPortReadDDS.getPortURI(), address.getReadURI(),
				ConnectorReadDDS.class.getCanonicalName());
		outPortReadDDS.acceptResult(res, requestID);
		outPortReadDDS.doDisconnection();
		lockOutPort.unlock();
		
		getOwner().logMessage(LOGGER_TAG + "Fin read root topic : " + topic.getName());

	}

	/**
	 * Methode accept result : accepte le resultat du read/take
	 * 
	 * @param result : la donnée acceptée
	 * @param requestID : l'ID de la requete
	 * @throws DDSTopicNotFoundException
	 */
	public void acceptResult(Iterator<?> result, String requestID) throws DDSTopicNotFoundException {
		getOwner().logMessage(LOGGER_TAG + "AcceptResult");

		if (!this.clientLock.containsKey(requestID)) {
			throw new DDSTopicNotFoundException("RequestID not found");
		}
		this.result.put(requestID, result);
		clientLock.get(requestID).release();
		getOwner().logMessage(LOGGER_TAG + "Fin acceptResult");
	}

	/**
	 * methode propagerIn : entrée de la propagation
	 * 
	 * @param newObject : la donnée à propager
	 * @param topicName : le nom du topic
	 * @param id : l'ID de la requete
	 * @param time : le TimeStamp
	 * @throws Exception
	 */
	public <T> void propagerIn(T newObject, TopicDescription<T> topicName, String id, Time time) throws Exception {
		if (topicIDWrite.get(id) != null && topicIDWrite.get(id).equals(topicName))
			return;
		propager(newObject, topicName, id, time);
	}

	/**
	 * Methode write : effectue un write
	 * 
	 * @param topic : le topic dans lequel ecrire
	 * @param data : la donnée à ecrire
	 * @throws Exception
	 */
	public <T> void write(Topic<T> topic, T data) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Write topic : " + topic.getName());
		if (topics.contains(topic)) {
			propager(data, topic, AbstractPort.generatePortURI(), new fr.ddspstl.time.Time((new Date()).getTime()));
			getOwner().logMessage(LOGGER_TAG + "Fin write topic : " + topic.getName());
			return;
		}
		if (!writePortFromRoot.containsKey(topic))
			throw new DDSTopicNotFoundException("Topic not found, please check your cvm config");
		writePortFromRoot.get(topic).write(topic, data);
		getOwner().logMessage(LOGGER_TAG + "Fin write topic : " + topic.getName());

	}

	/**
	 * methode de propagation
	 * 
	 * @param newObject : la donnée à propager
	 * @param topicName : le nom du topic
	 * @param id : l'ID de la requete
	 * @param time : le TimeStamp
	 * @throws Exception
	 */
	public <T> void propager(T newObject, TopicDescription<T> topicName, String id, Time time) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Propager topic : " + topicName.getName());
		if (topicIDWrite.get(id) != null && topicIDWrite.get(id).equals(topicName))
			return;

		topicIDWrite.put(id, topicName);
		((IDDSNode) getOwner()).write(topicName, newObject, time);

		propagationPortToNextRoot.get(topicName).propager(newObject, topicName, id, time);
		getOwner().logMessage(LOGGER_TAG + "Fin propager topic : " + topicName.getName());
	}

	/**
	 * methode qui consomme la donnée
	 * 
	 * @param topic : le topic concerné
	 * @param id : l'ID de la requete
	 * @param isFirst : booleen regardant si cette requete est la premiere
	 * @return Iterator(T) : la donnée consommée
	 * @throws Exception
	 */
	public Iterator<?> consommer(TopicDescription<?> topic, String id, boolean isFirst) throws Exception {
		getOwner().logMessage(LOGGER_TAG + "Consommer topic : " + topic.getName());
		if (topicIDTake.containsKey(id) && topicIDTake.get(id).equals(topic))
			return ((IDDSNode) getOwner()).take(topic);

		topicIDTake.put(id, topic);
		if (!isFirst)
			((IDDSNode) getOwner()).take(topic);
		getOwner().logMessage(LOGGER_TAG + "Fin Consommer topic : " + topic.getName());
		return propagationPortToNextRoot.get(topic).consommer(topic, id, false);

	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	@Override
	public void finalise() throws Exception {
		super.finalise();

		if (outPortReadDDS.connected())
			outPortReadDDS.doDisconnection();

		for (OutPortPropagation port : propagationPortToNextRoot.values()) {
			port.doDisconnection();
		}

		for (OutPortReadDDS port : readPortFromRoot.values()) {
			port.doDisconnection();
		}

		for (OutPortWrite port : writePortFromRoot.values()) {
			port.doDisconnection();
		}

	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
	 */
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

		for (OutPortReadDDS port : readPortFromRoot.values()) {
			port.unpublishPort();
		}

		for (OutPortWrite port : writePortFromRoot.values()) {
			port.unpublishPort();
		}

		super.uninstall();
	}

}
