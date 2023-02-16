package fr.ddspstl.DDS.publishers;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.DataWriterListener;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.PublisherListener;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicQos;

public class Publisher implements org.omg.dds.pub.Publisher{

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

	@Override
	public ServiceEnvironment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataWriter<TYPE> createDataWriter(Topic<TYPE> topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataWriter<TYPE> createDataWriter(Topic<TYPE> topic, DataWriterQos qos,
			DataWriterListener<TYPE> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataWriter<TYPE> createDataWriter(Topic<TYPE> topic, DataWriterQos qos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataWriter<TYPE> lookupDataWriter(String topicName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataWriter<TYPE> lookupDataWriter(Topic<TYPE> topic) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public DomainParticipant getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
