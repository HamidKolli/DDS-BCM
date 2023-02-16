package fr.ddspstl.DDS.publishers.data;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ModifiableInstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.Time;
import org.omg.dds.core.status.LivelinessLostStatus;
import org.omg.dds.core.status.OfferedDeadlineMissedStatus;
import org.omg.dds.core.status.OfferedIncompatibleQosStatus;
import org.omg.dds.core.status.PublicationMatchedStatus;
import org.omg.dds.core.status.Status;
import org.omg.dds.pub.DataWriterListener;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.topic.SubscriptionBuiltinTopicData;
import org.omg.dds.topic.Topic;

public class DataWriter<T> implements org.omg.dds.pub.DataWriter<T>{

	@Override
	public DataWriterListener<T> getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListener(DataWriterListener<T> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener(DataWriterListener<T> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DataWriterQos getQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setQos(DataWriterQos qos) {
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
	public <OTHER> org.omg.dds.pub.DataWriter<OTHER> cast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic<T> getTopic() {
		// TODO Auto-generated method stub
		return null;
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
	public LivelinessLostStatus getLivelinessLostStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfferedDeadlineMissedStatus getOfferedDeadlineMissedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfferedIncompatibleQosStatus getOfferedIncompatibleQosStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicationMatchedStatus getPublicationMatchedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void assertLiveliness() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<InstanceHandle> getMatchedSubscriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubscriptionBuiltinTopicData getMatchedSubscriptionData(InstanceHandle subscriptionHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceHandle registerInstance(T instanceData) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceHandle registerInstance(T instanceData, Time sourceTimestamp) throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceHandle registerInstance(T instanceData, long sourceTimestamp, TimeUnit unit)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterInstance(InstanceHandle handle) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterInstance(InstanceHandle handle, T instanceData) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterInstance(InstanceHandle handle, T instanceData, Time sourceTimestamp)
			throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterInstance(InstanceHandle handle, T instanceData, long sourceTimestamp, TimeUnit unit)
			throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(T instanceData) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(T instanceData, Time sourceTimestamp) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(T instanceData, long sourceTimestamp, TimeUnit unit) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(T instanceData, InstanceHandle handle) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(T instanceData, InstanceHandle handle, Time sourceTimestamp) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(T instanceData, InstanceHandle handle, long sourceTimestamp, TimeUnit unit)
			throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(InstanceHandle instanceHandle) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(InstanceHandle instanceHandle, T instanceData) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(InstanceHandle instanceHandle, T instanceData, Time sourceTimestamp) throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(InstanceHandle instanceHandle, T instanceData, long sourceTimestamp, TimeUnit unit)
			throws TimeoutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getKeyValue(T keyHolder, InstanceHandle handle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T getKeyValue(InstanceHandle handle) {
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
	public StatusCondition<org.omg.dds.pub.DataWriter<T>> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Publisher getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
