package fr.ddspstl.DDS.Domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ModifiableTime;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.Time;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipantListener;
import org.omg.dds.domain.DomainParticipantQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.pub.PublisherListener;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.sub.SubscriberListener;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.ContentFilteredTopic;
import org.omg.dds.topic.MultiTopic;
import org.omg.dds.topic.ParticipantBuiltinTopicData;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicBuiltinTopicData;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicListener;
import org.omg.dds.topic.TopicQos;
import org.omg.dds.type.TypeSupport;
import org.omg.dds.type.dynamic.DynamicType;

import fr.ddspstl.DDS.Exemples.TypeSupportString;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe représentant un DomainParticipant
 */
public class DomainParticipant implements org.omg.dds.domain.DomainParticipant {

	private int domainId;
	private Map<String, Topic<Object>> topics;
	private ServiceEnvironment serviceEnvironment;

	/**
	 * Constructeur
	 * 
	 * @param serviceEnvironment : .
	 */
	public DomainParticipant(ServiceEnvironment serviceEnvironment) {
		this((new Random()).nextInt(), serviceEnvironment);
	}

	/**
	 * Constructeur
	 * 
	 * @param domainID : Id du domaine
	 * @param serviceEnvironment : .
	 */
	public DomainParticipant(int domainID, ServiceEnvironment serviceEnvironment) {
		this.domainId = domainID;
		this.serviceEnvironment = serviceEnvironment;
		topics = new HashMap<>();
	}

	// Listener

	@Override
	public DomainParticipantListener getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListener(DomainParticipantListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener(DomainParticipantListener listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	// QOS

	@Override
	public DomainParticipantQos getQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setQos(DomainParticipantQos qos) {
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

	// Listener

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
	 * @see org.omg.dds.domain.DomainParticipant#getEnvironment()
	 */
	@Override
	public ServiceEnvironment getEnvironment() {
		return serviceEnvironment;
	}

	// Publisher

	@Override
	public Publisher createPublisher() {
		return new fr.ddspstl.DDS.publishers.Publisher(this);
	}

	@Override
	public Publisher createPublisher(PublisherQos qos) {
		return null;
	}

	@Override
	public Publisher createPublisher(PublisherQos qos, PublisherListener listener,
			Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	// Subscriber

	/**
	 * @see org.omg.dds.domain.DomainParticipant#createSubscriber()
	 */
	@Override
	public Subscriber createSubscriber() {
		return new fr.ddspstl.DDS.subscribers.Subscriber(this);
	}

	@Override
	public Subscriber createSubscriber(SubscriberQos qos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscriber createSubscriber(SubscriberQos qos, SubscriberListener listener,
			Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscriber getBuiltinSubscriber() {
		// TODO Auto-generated method stub
		return null;
	}

	// Topic
	
	/**
	 * @see org.omg.dds.domain.DomainParticipant#createTopic(String, Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> Topic<TYPE> createTopic(String topicName, Class<TYPE> type) {
		
		return (Topic<TYPE>) createTopic(topicName, new TypeSupportString(getEnvironment(), type.getName()));
	}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
	@Override
	public <TYPE> Topic<TYPE> createTopic(String topicName, Class<TYPE> type, TopicQos qos,
			TopicListener<TYPE> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.omg.dds.domain.DomainParticipant#createTopic(String, TypeSupport)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> Topic<TYPE> createTopic(String topicName, TypeSupport<TYPE> type) {

		Topic<TYPE> t = new fr.ddspstl.DDS.topic.Topic<>(type, serviceEnvironment, topicName, this);
		topics.put(topicName, (Topic<Object>) t);
		return t;
	}

	@Override
	public <TYPE> Topic<TYPE> createTopic(String topicName, TypeSupport<TYPE> type, TopicQos qos,
			TopicListener<TYPE> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic<DynamicType> createTopic(String topicName, DynamicType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic<DynamicType> createTopic(String topicName, DynamicType type, TopicQos qos,
			TopicListener<DynamicType> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic<DynamicType> createTopic(String topicName, DynamicType type, TypeSupport<DynamicType> typeSupport) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic<DynamicType> createTopic(String topicName, DynamicType type, TypeSupport<DynamicType> typeSupport,
			TopicQos qos, TopicListener<DynamicType> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.omg.dds.domain.DomainParticipant#findTopic(String, Duration)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> Topic<TYPE> findTopic(String topicName, Duration timeout) throws TimeoutException {
		return (Topic<TYPE>) topics.get(topicName);
	}

	@Override
	public <TYPE> Topic<TYPE> findTopic(String topicName, long timeout, TimeUnit unit) throws TimeoutException {
		return null;
	}

	/**
	 * @see org.omg.dds.domain.DomainParticipant#lookupTopicDescription(String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> TopicDescription<TYPE> lookupTopicDescription(String name) {
		return (TopicDescription<TYPE>) topics.get(name);
	}

	@Override
	public <TYPE> ContentFilteredTopic<TYPE> createContentFilteredTopic(String name, Topic<? extends TYPE> relatedTopic,
			String filterExpression, List<String> expressionParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> MultiTopic<TYPE> createMultiTopic(String name, Class<TYPE> type, String subscriptionExpression,
			List<String> expressionParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TYPE> MultiTopic<TYPE> createMultiTopic(String name, TypeSupport<TYPE> type, String subscriptionExpression,
			List<String> expressionParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeContainedEntities() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignoreParticipant(InstanceHandle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignoreTopic(InstanceHandle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorePublication(InstanceHandle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignoreSubscription(InstanceHandle handle) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.omg.dds.domain.DomainParticipant#getDomainId()
	 */
	@Override
	public int getDomainId() {
		return domainId;
	}

	@Override
	public void assertLiveliness() {
		// TODO Auto-generated method stub

	}

	@Override
	public PublisherQos getDefaultPublisherQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultPublisherQos(PublisherQos qos) {
		// TODO Auto-generated method stub

	}

	@Override
	public SubscriberQos getDefaultSubscriberQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultSubscriberQos(SubscriberQos qos) {
		// TODO Auto-generated method stub

	}

	@Override
	public TopicQos getDefaultTopicQos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultTopicQos(TopicQos qos) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<InstanceHandle> getDiscoveredParticipants() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticipantBuiltinTopicData getDiscoveredParticipantData(InstanceHandle participantHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<InstanceHandle> getDiscoveredTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicBuiltinTopicData getDiscoveredTopicData(InstanceHandle topicHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsEntity(InstanceHandle handle) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModifiableTime getCurrentTime(ModifiableTime currentTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getCurrentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCondition<org.omg.dds.domain.DomainParticipant> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
