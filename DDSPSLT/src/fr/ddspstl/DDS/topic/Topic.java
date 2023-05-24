package fr.ddspstl.DDS.topic;

import java.util.Collection;
import java.util.Set;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.InconsistentTopicStatus;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicListener;
import org.omg.dds.topic.TopicQos;
import org.omg.dds.type.TypeSupport;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 * @param <T> : type de la donnée
 *
 * Classe représentant un Topic
 */
public class Topic<T> implements org.omg.dds.topic.Topic<T> {
	private TypeSupport<T> typeSupport;
	private ServiceEnvironment serviceEnvironment;
	private String typeName;
	private DomainParticipant domainParticipant;

	/**
	 * constructeur
	 * 
	 * @param type : le TypeSupport
	 * @param serviceEnvironment : le Service Environment
	 * @param typeName : le nom du type
	 * @param domainParticipant : le DomainParticipant
	 */
	public Topic(TypeSupport<T> type, ServiceEnvironment serviceEnvironment, String typeName,
			DomainParticipant domainParticipant) {
		super();
		this.typeSupport = type;
		this.serviceEnvironment = serviceEnvironment;
		this.typeName = typeName;
		this.domainParticipant = domainParticipant;
	}


	/**
	 * @see org.omg.dds.topic.Topic#getTypeSupport()
	 */
	@Override
	public TypeSupport<T> getTypeSupport() {
		return typeSupport;
	}


	@Override
	public <OTHER> TopicDescription<OTHER> cast() {
		return null;
	}

	/**
	 * @see org.omg.dds.topic.Topic#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return typeSupport.getTypeName();
	}

	/**
	 * @see org.omg.dds.topic.Topic#getName()
	 */
	@Override
	public String getName() {
		return typeName;
	}

	@Override
	public void close() {
	}

	/**
	 * @see org.omg.dds.topic.Topic#getEnvironment()
	 */
	@Override
	public ServiceEnvironment getEnvironment() {
		return serviceEnvironment;
	}

	@Override
	public TopicListener<T> getListener() {
		return null;
	}

	@Override
	public void setListener(TopicListener<T> listener) {

	}

	@Override
	public void setListener(TopicListener<T> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub

	}

	@Override
	public TopicQos getQos() {
		return null;
	}

	@Override
	public void setQos(TopicQos qos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQos(String qosLibraryName, String qosProfileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable() {
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
	public void retain() {
		// TODO Auto-generated method stub

	}

	@Override
	public InconsistentTopicStatus getInconsistentTopicStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCondition<org.omg.dds.topic.Topic<T>> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.omg.dds.topic.Topic#getParent()
	 */
	@Override
	public DomainParticipant getParent() {
		return domainParticipant;
	}


	/**
	 * 
	* @see java.lang.Object#hashCode()
	*
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		result = prime * result + ((typeSupport == null) ? 0 : typeSupport.hashCode());
		return result;
	}


	/**
	 * 
	* @see java.lang.Object#equals(java.lang.Object)
	*
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Topic)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Topic<T> other = (Topic<T>) obj;
		if (typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!typeName.equals(other.typeName)) {
			return false;
		}
		if (typeSupport == null) {
			if (other.typeSupport != null) {
				return false;
			}
		} else if (!typeSupport.equals(other.typeSupport)) {
			return false;
		}
		return true;
	}
	
	
	
	

}