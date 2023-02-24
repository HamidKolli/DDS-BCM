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

import fr.ddspstl.DDS.data.Datas;

public class DataReader<T> implements org.omg.dds.sub.DataReader<T> {

	private Datas<T> datas;
	private Subscriber subscriber;
	private DataReaderQos qos;
	private DataReaderListener<T> listener;
	private Collection<Class<? extends Status>> statuses;

	public DataReader(TopicDescription<T> topic, Subscriber subscriber, DataReaderQos qos,
			DataReaderListener<T> listener, Collection<Class<? extends Status>> statuses) {
		this(topic, subscriber, qos);

		this.listener = listener;
		this.statuses = statuses;
	}

	public DataReader(TopicDescription<T> topic, Subscriber subscriber) {
		this.datas = (Datas<T>) topic;
		this.subscriber = subscriber;
	}

	public DataReader(TopicDescription<T> topic, Subscriber subscriber, DataReaderQos qos) {
		this(topic, subscriber);
		this.qos = qos;

	}

	@Override
	public DataReaderListener<T> getListener() {
		return listener;
	}

	@Override
	public void setListener(DataReaderListener<T> listener) {
		this.listener = listener;

	}

	@Override
	public void setListener(DataReaderListener<T> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataReaderQos getQos() {
		// TODO Auto-generated method stub
		return qos;
	}

	@Override
	public void setQos(DataReaderQos qos) {
		this.qos = qos;

	}

	@Override
	public void setQos(String qosLibraryName, String qosProfileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Class<? extends Status>> getStatusChanges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceHandle getInstanceHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void retain() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServiceEnvironment getEnvironment() {
		return getParent().getEnvironment();
	}

	@Override
	public <OTHER> org.omg.dds.sub.DataReader<OTHER> cast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReadCondition<T> createReadCondition(DataState states) {
		return null;
	}

	@Override
	public QueryCondition<T> createQueryCondition(String queryExpression, List<String> queryParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryCondition<T> createQueryCondition(DataState states, String queryExpression,
			List<String> queryParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeContainedEntities() {
		// TODO Auto-generated method stub

	}

	@Override
	public TopicDescription<T> getTopicDescription() {
		return datas;
	}

	@Override
	public SampleRejectedStatus getSampleRejectedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LivelinessChangedStatus getLivelinessChangedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestedDeadlineMissedStatus getRequestedDeadlineMissedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestedIncompatibleQosStatus getRequestedIncompatibleQosStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubscriptionMatchedStatus getSubscriptionMatchedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleLostStatus getSampleLostStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void waitForHistoricalData(Duration maxWait) throws TimeoutException {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForHistoricalData(long maxWait, TimeUnit unit) throws TimeoutException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<InstanceHandle> getMatchedPublications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicationBuiltinTopicData getMatchedPublicationData(InstanceHandle publicationHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> read() {
		return datas.read();
	}

	@Override
	public Iterator<T> read(Selector<T> query) {
		return null;
	}

	@Override
	public Iterator<T> read(int maxSamples) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample<T>> read(List<Sample<T>> samples) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample<T>> read(List<Sample<T>> samples, Selector<T> selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> take() {
		return datas.read();
	}

	@Override
	public Iterator<T> take(int maxSamples) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> take(Selector<T> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample<T>> take(List<Sample<T>> samples) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample<T>> take(List<Sample<T>> samples, Selector<T> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean readNextSample(Sample<T> sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean takeNextSample(Sample<T> sample) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T getKeyValue(T keyHolder, InstanceHandle handle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModifiableInstanceHandle lookupInstance(ModifiableInstanceHandle handle, T keyHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceHandle lookupInstance(T keyHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCondition<org.omg.dds.sub.DataReader<T>> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscriber getParent() {
		return subscriber;
	}

	@Override
	public Selector<T> select() {
		// TODO Auto-generated method stub
		return null;
	}

}
