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

import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class DataWriter<T> extends AbstractPlugin implements org.omg.dds.pub.DataWriter<T> {

	private static final long serialVersionUID = 1L;
	private Topic<T> topic;
	private Publisher publisher;
	private DataWriterQos qos;
	private DataWriterListener<T> listener;
	private Collection<Class<? extends Status>> statuses;
	private OutPortWrite<T> outPortWrite;	
	private String inPortReadDDSNode;

	public DataWriter(Topic<T> datas, Publisher publisher,String inPortReadDDSNode) {
		super();
		this.topic =  datas;
		this.publisher = publisher;
		this.inPortReadDDSNode = inPortReadDDSNode;
	}

	
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addRequiredInterface(WriteCI.class);
	}
	
	
	@Override
	public void initialise() throws Exception {
		super.initialise();
		this.outPortWrite = new OutPortWrite<T>(getOwner());
		this.outPortWrite.publishPort();
		getOwner().doPortConnection(outPortWrite.getPortURI(), inPortReadDDSNode, ConnectorRead.class.getCanonicalName());
	}
	
	@Override
	public void finalise() throws Exception {
		outPortWrite.doDisconnection();
		super.finalise();
	}
	
	public void uninstall() throws Exception {
		outPortWrite.unpublishPort();
		outPortWrite.destroyPort();
		super.uninstall();
	}
	

	@Override
	public DataWriterListener<T> getListener() {
		return listener;
	}

	@Override
	public void setListener(DataWriterListener<T> listener) {
		this.listener = listener;

	}

	@Override
	public void setListener(DataWriterListener<T> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataWriterQos getQos() {
		return qos;
	}

	@Override
	public void setQos(DataWriterQos qos) {
		this.qos = qos;
	}

	@Override
	public void setQos(String qosLibraryName, String qosProfileName) {

	}

	@Override
	public void enable() {
		topic.enable();
	}

	@Override
	public Set<Class<? extends Status>> getStatusChanges() {
		return topic.getStatusChanges();
	}

	@Override
	public InstanceHandle getInstanceHandle() {
		return topic.getInstanceHandle();
	}

	@Override
	public void close() {

	}

	@Override
	public void retain() {
		topic.retain();
	}

	@Override
	public ServiceEnvironment getEnvironment() {
		return getParent().getEnvironment();
	}

	@Override
	public <OTHER> org.omg.dds.pub.DataWriter<OTHER> cast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic<T> getTopic() {
		return topic;
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
		try {
			this.outPortWrite.write(topic, instanceData);
		} catch (Exception e) {

		}
	}

	@Override
	public void write(T instanceData, Time sourceTimestamp) throws TimeoutException {
		

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
		return null;
	}

	@Override
	public Publisher getParent() {
		return publisher;
	}

}
