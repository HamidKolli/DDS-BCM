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

public class Topic<T> implements org.omg.dds.topic.Topic<T> {
	private TypeSupport<T> typeSupport;
	private ServiceEnvironment serviceEnvironment;
	private String typeName;
	private DomainParticipant domainParticipant;

	public Topic(TypeSupport<T> type, ServiceEnvironment serviceEnvironment, String typeName,
			DomainParticipant domainParticipant) {
		super();
		this.typeSupport = type;
		this.serviceEnvironment = serviceEnvironment;
		this.typeName = typeName;
		this.domainParticipant = domainParticipant;
	}


	@Override
	public TypeSupport<T> getTypeSupport() {
		return typeSupport;
	}

	@Override
	public <OTHER> TopicDescription<OTHER> cast() {
		return null;
	}

	@Override
	public String getTypeName() {
		return typeSupport.getTypeName();
	}

	@Override
	public String getName() {
		return typeName;
	}

	@Override
	public void close() {
	}

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

	@Override
	public DomainParticipant getParent() {
		return domainParticipant;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		result = prime * result + ((typeSupport == null) ? 0 : typeSupport.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Topic)) {
			return false;
		}
		Topic other = (Topic) obj;
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
