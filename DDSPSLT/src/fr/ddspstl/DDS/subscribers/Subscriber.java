package fr.ddspstl.DDS.subscribers;

import java.util.Collection;
import java.util.Set;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.SubscriberListener;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicQos;

public class Subscriber implements org.omg.dds.sub.Subscriber{

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

	@Override
	public ServiceEnvironment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic, DataReaderQos qos,
			DataReaderListener<TYPE> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic, DataReaderQos qos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataReader<TYPE> lookupDataReader(String topicName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> DataReader<TYPE> lookupDataReader(TopicDescription<TYPE> topicName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeContainedEntities() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<DataReader<?>> getDataReaders(Collection<DataReader<?>> readers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataReader<?>> getDataReaders(Collection<DataReader<?>> readers, DataState dataState) {
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

	@Override
	public DomainParticipant getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataState createDataState() {
		// TODO Auto-generated method stub
		return null;
	}

}
