package fr.ddspstl.components;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.DDS.data.Datas;
import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.plugin.DDSPlugin;
import fr.ddspstl.plugin.LockPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 *         Composant représentant un noeud DDS
 */

public class DDSNode extends AbstractComponent implements IDDSNode {

	public static final String DIR_LOGGER_NAME = "loggers/";
	public static final String FILE_LOGGER_NAME = "node_";
	public static final String LOGGER_TAG = "DDSNode | ";

	private static final int NB_THREAD_CLIENT = 4;
	private static final int NB_THREAD_LOCK = 4;
	private static final int NB_THREAD_PROPAGATION = 5;
	private static final int NB_THREAD_CONNECTION = 5;

	private DDSPlugin plugin;
	private LockPlugin pluginLock;

	private INodeAddress nodeAddress;
	private Map<TopicDescription<?>, INodeAddress> rootOfTopics;
	private Map<TopicDescription<?>, INodeAddress> nextRoot;
	private Map<TopicDescription<?>, Datas<?>> datas;

	/**
	 * Constructeur
	 * 
	 * @param nbThreads            : nb de threads
	 * @param nbSchedulableThreads : nb de chedulableThreads
	 * @param address              : les différentes adresses/Uris liées au noeud
	 * @param rootOfTopics         : Map qui associe a chaque topic, un noeud Root
	 * @param nextRoot             : le prochain noeud Root à qui propager
	 * @throws Exception
	 */
	protected DDSNode(int nbThreads, int nbSchedulableThreads, INodeAddress address,
			Map<TopicDescription<?>, INodeAddress> rootOfTopics, Map<TopicDescription<?>, INodeAddress> nextRoot)
			throws Exception {
		super(nbThreads, nbSchedulableThreads);

		this.rootOfTopics = new ConcurrentHashMap<>(rootOfTopics);
		this.nextRoot = new ConcurrentHashMap<>(nextRoot);
		this.nodeAddress = address;
		this.datas = new ConcurrentHashMap<>();
		for (TopicDescription<?> topic : nextRoot.keySet()) {
			datas.put(topic, new Datas<>(topic));
		}

	}

	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 *
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		try {

			String executorServiceLockURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceLockURI, NB_THREAD_LOCK, false);
			new HashSet<>(nextRoot.values());
			pluginLock = new LockPlugin(nodeAddress, nextRoot.keySet(), executorServiceLockURI);
			pluginLock.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(pluginLock);

			String executorServicePropagationURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServicePropagationURI, NB_THREAD_PROPAGATION, false);

			String executorServiceReadWriteURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceReadWriteURI, NB_THREAD_CONNECTION, false);

			String executorServiceURI = AbstractPort.generatePortURI();
			createNewExecutorService(executorServiceURI, NB_THREAD_CLIENT, false);

			plugin = new DDSPlugin(nextRoot.keySet(), nodeAddress, executorServiceURI, executorServicePropagationURI,
					executorServiceReadWriteURI);
			logMessage(LOGGER_TAG + "Installer le plugin dds");
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	/**
	 * @see fr.ddspstl.components.interfaces.IDDSNode#propager(Object,
	 *      TopicDescription, String, Time)
	 *
	 */
	public <T> void propager(T newObject, TopicDescription<T> topic, String id, Time time) throws Exception {
		logMessage(LOGGER_TAG + "Propager topic :" + topic.getName());
		plugin.propager(newObject, topic, id, time);
		logMessage(LOGGER_TAG + "Fin propager topic :" + topic.getName());
	}

	/**
	 * @throws Exception
	 * @see fr.ddspstl.components.interfaces.IDDSNode#read(TopicDescription)
	 *
	 */
	@SuppressWarnings("unchecked")
	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {
		logMessage(LOGGER_TAG + "Read topic :" + topic.getName());
		pluginLock.lock(topic);
		Iterator<T> result = (Iterator<T>) datas.get(topic).read();
		pluginLock.unlock(topic);
		logMessage(LOGGER_TAG + "Fin read topic :" + topic.getName());

		return result;

	}

	/**
	 * @see fr.ddspstl.components.interfaces.IDDSNode#consommer(TopicDescription,
	 *      String, boolean)
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {

		logMessage(LOGGER_TAG + "Consommer topic :" + topic.getName());
		String propagationLockURI = AbstractPort.generatePortURI();

		if (pluginLock.propagateLock(topic, propagationLockURI, new fr.ddspstl.time.Time((new Date()).getTime()))) {
			Iterator<T> result = (Iterator<T>) plugin.consommer(topic, id, isFirst);
			pluginLock.propagateUnlock(topic, propagationLockURI, AbstractPort.generatePortURI());

			return result;
		}
		logMessage(LOGGER_TAG + "Consommer topic :" + topic.getName() + " propagation false");

		pluginLock.propagateUnlock(topic, propagationLockURI, AbstractPort.generatePortURI());
		logMessage(LOGGER_TAG + "Fin consommer topic :" + topic.getName());
		return new fr.ddspstl.DDS.samples.Sample.Iterator<T>();

	}

	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 *
	 */
	@Override
	public void execute() throws Exception {
		logMessage(LOGGER_TAG + "Connexion");

		for (Map.Entry<TopicDescription<?>, INodeAddress> topic : nextRoot.entrySet()) {
			plugin.connectRoot(topic.getValue(), topic.getKey());
			pluginLock.connect(topic.getValue());
		}

		for (Map.Entry<TopicDescription<?>, INodeAddress> topic : rootOfTopics.entrySet()) {
			plugin.connect(topic.getValue(), topic.getKey());
			pluginLock.connect(topic.getValue());
		}

		logMessage(LOGGER_TAG + "Fin connexion");
	}

	/**
	 * @see fr.ddspstl.components.interfaces.IDDSNode#write(TopicDescription, Object, Time)
	 *
	 */
	@Override
	public <T> void write(TopicDescription<T> topic, T newObject, Time time) throws Exception {
		logMessage(LOGGER_TAG + "Write topic :" + topic.getName());

		pluginLock.lock(topic);

		datas.get(topic).write(newObject, time);

		pluginLock.unlock(topic);

		logMessage(LOGGER_TAG + "Fin Write topic :" + topic.getName());
	}

	/**
	 * @see fr.ddspstl.components.interfaces.IDDSNode#take(TopicDescription)
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Iterator<T> take(TopicDescription<T> topic) throws Exception {
		logMessage(LOGGER_TAG + "Take topic :" + topic.getName());
		Iterator<T> result = (Iterator<T>) datas.get(topic).take();
		logMessage(LOGGER_TAG + "Fin Take topic :" + topic.getName());
		return result;
	}

	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 *
	 */
	@Override
	public synchronized void finalise() throws Exception {
		logMessage(LOGGER_TAG + "Fin");
		printExecutionLogOnFile(DIR_LOGGER_NAME + FILE_LOGGER_NAME + nodeAddress.getNodeURI());
		super.finalise();
	}

}
