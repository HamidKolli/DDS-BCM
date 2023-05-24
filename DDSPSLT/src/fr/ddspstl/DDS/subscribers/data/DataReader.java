package fr.ddspstl.DDS.subscribers.data;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ModifiableInstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.LivelinessChangedStatus;
import org.omg.dds.core.status.RequestedDeadlineMissedStatus;
import org.omg.dds.core.status.RequestedIncompatibleQosStatus;
import org.omg.dds.core.status.SampleLostStatus;
import org.omg.dds.core.status.SampleRejectedStatus;
import org.omg.dds.core.status.Status;
import org.omg.dds.core.status.SubscriptionMatchedStatus;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.QueryCondition;
import org.omg.dds.sub.ReadCondition;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.sub.Subscriber.DataState;
import org.omg.dds.topic.PublicationBuiltinTopicData;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.ports.OutPortConnectClient;
import fr.ddspstl.ports.OutPortRead;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : type de la donnée
 *
 * Plugin représentant un DataReader
 */
public class DataReader<T> extends AbstractPlugin implements org.omg.dds.sub.DataReader<T> {

	private static final long serialVersionUID = 1L;
	private TopicDescription<T> topic;
	private Subscriber subscriber;
	private OutPortRead portRead;
	private String uriPortDDSnode;
	private OutPortConnectClient outPortConnectClient;

	
	/**
	 * Constructeur
	 * 
	 * @param topic : le topic
	 * @param subscriber : le subscriber
	 * @param uriPortDDSNode : l'uri du port du noeud dds
	 */
	public DataReader(TopicDescription<T> topic, Subscriber subscriber, String uriPortDDSNode) {
		this.topic = topic;
		this.subscriber = subscriber;
		this.uriPortDDSnode = uriPortDDSNode;
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#installOn(ComponentI)
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addRequiredInterface(ReadCI.class);
		if (getOwner().getRequiredInterface(ConnectClient.class) == null)
			this.addRequiredInterface(ConnectClient.class);

	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#initialise()
	 */
	@Override
	public void initialise() throws Exception {
		super.initialise();
		portRead = new OutPortRead(getOwner());
		portRead.publishPort();
		outPortConnectClient = new OutPortConnectClient(getOwner());
		outPortConnectClient.publishPort();

	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	public void finalise() throws Exception {
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
	 */
	public void uninstall() throws Exception {
		portRead.unpublishPort();
		portRead.destroyPort();
		outPortConnectClient.unpublishPort();
		outPortConnectClient.destroyPort();
		super.uninstall();
	}

	public DataReaderListener<T> getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setListener(DataReaderListener<T> listener) {
		// TODO Auto-generated method stub

	}

	public void setListener(DataReaderListener<T> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	public DataReaderQos getQos() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setQos(DataReaderQos qos) {
		// TODO Auto-generated method stub

	}

	public void setQos(String qosLibraryName, String qosProfileName) {
		// TODO Auto-generated method stub

	}

	public void enable() {
		// TODO Auto-generated method stub

	}

	public Set<Class<? extends Status>> getStatusChanges() {
		// TODO Auto-generated method stub
		return null;
	}

	public InstanceHandle getInstanceHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void retain() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.omg.dds.sub.DataReader#getEnvironment()
	 */
	public ServiceEnvironment getEnvironment() {
		return getParent().getEnvironment();
	}

	public <OTHER> org.omg.dds.sub.DataReader<OTHER> cast() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReadCondition<T> createReadCondition(DataState states) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryCondition<T> createQueryCondition(String queryExpression, List<String> queryParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryCondition<T> createQueryCondition(DataState states, String queryExpression,
			List<String> queryParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	public void closeContainedEntities() {
		// TODO Auto-generated method stub

	}

	public TopicDescription<T> getTopicDescription() {
		return topic;
	}

	public SampleRejectedStatus getSampleRejectedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public LivelinessChangedStatus getLivelinessChangedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public RequestedDeadlineMissedStatus getRequestedDeadlineMissedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public RequestedIncompatibleQosStatus getRequestedIncompatibleQosStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public SubscriptionMatchedStatus getSubscriptionMatchedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public SampleLostStatus getSampleLostStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public void waitForHistoricalData(Duration maxWait) throws TimeoutException {
		// TODO Auto-generated method stub

	}

	public void waitForHistoricalData(long maxWait, TimeUnit unit) throws TimeoutException {
		// TODO Auto-generated method stub

	}

	public Set<InstanceHandle> getMatchedPublications() {
		// TODO Auto-generated method stub
		return null;
	}

	public PublicationBuiltinTopicData getMatchedPublicationData(InstanceHandle publicationHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.omg.dds.sub.DataReader#read()
	 */
	public Iterator<T> read() {
		try {
			getOwner().doPortConnection(outPortConnectClient.getPortURI(), uriPortDDSnode,
					ConnectorClient.class.getCanonicalName());
			String uriReader = outPortConnectClient.getReaderURI();
			getOwner().doPortConnection(portRead.getPortURI(), uriReader, ConnectorRead.class.getCanonicalName());

			return this.portRead.read(topic);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				portRead.doDisconnection();
				outPortConnectClient.doDisconnection();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public Iterator<T> read(Selector<T> query) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<T> read(int maxSamples) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Sample<T>> read(List<Sample<T>> samples) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Sample<T>> read(List<Sample<T>> samples, Selector<T> selector) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.omg.dds.sub.DataReader#take()
	 */
	public Iterator<T> take() {
		try {
			getOwner().doPortConnection(outPortConnectClient.getPortURI(), uriPortDDSnode,
					ConnectorClient.class.getCanonicalName());
			String uriReader = outPortConnectClient.getReaderURI();
			getOwner().doPortConnection(portRead.getPortURI(), uriReader, ConnectorRead.class.getCanonicalName());

			return this.portRead.take(topic);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				portRead.doDisconnection();
				outPortConnectClient.doDisconnection();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public Iterator<T> take(int maxSamples) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<T> take(Selector<T> query) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Sample<T>> take(List<Sample<T>> samples) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Sample<T>> take(List<Sample<T>> samples, Selector<T> query) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean readNextSample(Sample<T> sample) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean takeNextSample(Sample<T> sample) {
		// TODO Auto-generated method stub
		return false;
	}

	public T getKeyValue(T keyHolder, InstanceHandle handle) {
		// TODO Auto-generated method stub
		return null;
	}

	public ModifiableInstanceHandle lookupInstance(ModifiableInstanceHandle handle, T keyHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	public InstanceHandle lookupInstance(T keyHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	public StatusCondition<org.omg.dds.sub.DataReader<T>> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	public Subscriber getParent() {
		return subscriber;
	}

	public Selector<T> select() {
		// TODO Auto-generated method stub
		return null;
	}

}
