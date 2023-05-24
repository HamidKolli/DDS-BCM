package fr.ddspstl.DDS.publishers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriterListener;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.PublisherListener;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicQos;

import fr.ddspstl.DDS.publishers.data.DataWriter;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe représentant un publisher
 */
public class Publisher implements fr.ddspstl.DDS.publishers.interfaces.Publisher {

	private DomainParticipant domainParticipant;
	private Map<String, org.omg.dds.pub.DataWriter<Object>> dataWriters;

	/**
	 * Constructeur
	 * 
	 * @param domainParticipant : le DomainParticipant
	 */
	public Publisher(DomainParticipant domainParticipant) {
		super();
		this.domainParticipant = domainParticipant;
		this.dataWriters = new HashMap<>();
	}

	@Override
	public PublisherListener getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListener(PublisherListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener(PublisherListener listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	@Override
	public PublisherQos getQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setQos(PublisherQos qos) {
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
	 * @see fr.ddspstl.DDS.publishers.interfaces.Publisher#getEnvironment()
	 */
	@Override
	public ServiceEnvironment getEnvironment() {
		return domainParticipant.getEnvironment();
	}



	/**
	 * @see fr.ddspstl.DDS.publishers.interfaces.Publisher#lookupDataWriter(String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> DataWriter<TYPE> lookupDataWriter(String topicName) {
		return (DataWriter<TYPE>) dataWriters.get(topicName);
	}

	/**
	 * @see fr.ddspstl.DDS.publishers.interfaces.Publisher#lookupDataWriter(Topic)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> DataWriter<TYPE> lookupDataWriter(Topic<TYPE> topic) {
		return (DataWriter<TYPE>) dataWriters.get(topic.getName());
	}

	@Override
	public void closeContainedEntities() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspendPublications() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumePublications() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginCoherentChanges() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endCoherentChanges() {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForAcknowledgments(Duration maxWait) throws TimeoutException {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForAcknowledgments(long maxWait, TimeUnit unit) throws TimeoutException {
		// TODO Auto-generated method stub

	}

	@Override
	public DataWriterQos getDefaultDataWriterQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultDataWriterQos(DataWriterQos qos) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataWriterQos copyFromTopicQos(DataWriterQos dwQos, TopicQos tQos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCondition<org.omg.dds.pub.Publisher> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see fr.ddspstl.DDS.publishers.interfaces.Publisher#getParent()
	 */
	@Override
	public DomainParticipant getParent() {
		return domainParticipant;
	}

	/**
	 * @see fr.ddspstl.DDS.publishers.interfaces.Publisher#createDataWriter(Topic, String)
	 */
	@SuppressWarnings("unchecked")
	public <TYPE> DataWriter<TYPE>  createDataWriter(Topic<TYPE>  topic, String uriPortSortant) {
		DataWriter<TYPE> d = new DataWriter<>(topic, this,uriPortSortant);
		dataWriters.put(topic.getName(), (org.omg.dds.pub.DataWriter<Object>) d);
		return d;
	}

	public <TYPE> org.omg.dds.pub.DataWriter<TYPE> createDataWriter(Topic<TYPE> topic) {
		// TODO Auto-generated method stub
		return null;
	}

	public <TYPE> org.omg.dds.pub.DataWriter<TYPE> createDataWriter(Topic<TYPE> topic, DataWriterQos qos,
			DataWriterListener<TYPE> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	public <TYPE> org.omg.dds.pub.DataWriter<TYPE> createDataWriter(Topic<TYPE> topic, DataWriterQos qos) {
		// TODO Auto-generated method stub
		return null;
	}



}