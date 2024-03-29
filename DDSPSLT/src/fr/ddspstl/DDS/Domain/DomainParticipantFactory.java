package fr.ddspstl.DDS.Domain;

import java.util.Collection;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactoryQos;
import org.omg.dds.domain.DomainParticipantListener;
import org.omg.dds.domain.DomainParticipantQos;

public class DomainParticipantFactory extends org.omg.dds.domain.DomainParticipantFactory{

	private ServiceEnvironment serviceEnvironment;
	
	
	
	public DomainParticipantFactory(ServiceEnvironment serviceEnvironment) {
		super();
		this.serviceEnvironment = serviceEnvironment;
	}

	@Override
	public ServiceEnvironment getEnvironment() {
		return serviceEnvironment;
	}

	@Override
	public DomainParticipant createParticipant() {
		return new fr.ddspstl.DDS.Domain.DomainParticipant(getEnvironment());
	}

	@Override
	public DomainParticipant createParticipant(int domainId) {
		return new fr.ddspstl.DDS.Domain.DomainParticipant(domainId,getEnvironment());
	}

	@Override
	public DomainParticipant createParticipant(int domainId, DomainParticipantQos qos,
			DomainParticipantListener listener, Collection<Class<? extends Status>> statuses) {
		return null;
	}

	@Override
	public DomainParticipant lookupParticipant(int domainId) {
		return null;
	}

	@Override
	public DomainParticipantFactoryQos getQos() {
		return null;
	}

	@Override
	public void setQos(DomainParticipantFactoryQos qos) {		
	}

	@Override
	public DomainParticipantQos getDefaultParticipantQos() {
		return null;
	}

	@Override
	public void setDefaultParticipantQos(DomainParticipantQos qos) {		
	}

}
