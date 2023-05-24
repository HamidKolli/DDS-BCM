package fr.ddspstl.DDS.subscribers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;

import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.SubscriberListener;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicQos;

import fr.ddspstl.DDS.subscribers.data.DataReader;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe représentant un Subscriber
 */
public class Subscriber implements fr.ddspstl.DDS.subscribers.interfaces.Subscriber {

	private DomainParticipant domainParticipant;
	private Map<String, org.omg.dds.sub.DataReader<Object>> dataReaders;
	

	/**
	 * Constructeur
	 * 
	 * @param domainParticipant : le Domaine Participant
	 */
	public Subscriber(DomainParticipant domainParticipant) {
		super();
		this.domainParticipant = domainParticipant;
		this.dataReaders = new HashMap<>();
	}

	@Override
	public SubscriberListener getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListener(SubscriberListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener(SubscriberListener listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	@Override
	public SubscriberQos getQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setQos(SubscriberQos qos) {
		// TODO Auto-generated method stub

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

	/**
	 * @see fr.ddspstl.DDS.subscribers.interfaces.Subscriber#getEnvironment()
	 */
	@Override
	public ServiceEnvironment getEnvironment() {
		return domainParticipant.getEnvironment();
	}

	/**
	 * @see fr.ddspstl.DDS.subscribers.interfaces.Subscriber#lookupDataReader(String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> org.omg.dds.sub.DataReader<TYPE> lookupDataReader(String topicName) {
		return (org.omg.dds.sub.DataReader<TYPE>) dataReaders.get(topicName);
	}

	/**
	 * @see fr.ddspstl.DDS.subscribers.interfaces.Subscriber#lookupDataReader(TopicDescription)
	 */
	@Override
	public <TYPE> org.omg.dds.sub.DataReader<TYPE> lookupDataReader(TopicDescription<TYPE> topicName) {
		return lookupDataReader(topicName.getName());
	}

	@Override
	public void closeContainedEntities() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<org.omg.dds.sub.DataReader<?>> getDataReaders(Collection<org.omg.dds.sub.DataReader<?>> readers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<org.omg.dds.sub.DataReader<?>> getDataReaders(Collection<org.omg.dds.sub.DataReader<?>> readers,
			DataState dataState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyDataReaders() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginAccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endAccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public DataReaderQos getDefaultDataReaderQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultDataReaderQos(DataReaderQos qos) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataReaderQos copyFromTopicQos(DataReaderQos drQos, TopicQos tQos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCondition<org.omg.dds.sub.Subscriber> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see fr.ddspstl.DDS.subscribers.interfaces.Subscriber#getParent()
	 */
	@Override
	public DomainParticipant getParent() {
		return domainParticipant;
	}

	@Override
	public DataState createDataState() {
		// TODO Auto-generated method stub
		return null;
	}

	public <TYPE> org.omg.dds.sub.DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic) {
		// TODO Auto-generated method stub
		return null;
	}

	public <TYPE> org.omg.dds.sub.DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic, DataReaderQos qos,
			DataReaderListener<TYPE> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	public <TYPE> org.omg.dds.sub.DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic, DataReaderQos qos) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see fr.ddspstl.DDS.subscribers.interfaces.Subscriber#createDataReader(TopicDescription, String)
	 */
	public <TYPE> org.omg.dds.sub.DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic, String uriPortDDSNode) {
		DataReader<TYPE> data = new DataReader<>(topic, this, uriPortDDSNode);
		dataReaders.put(topic.getName(), (org.omg.dds.sub.DataReader<Object>) data);
		return data;
	}

}