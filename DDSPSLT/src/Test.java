import java.util.Collection;
import java.util.Set;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.InconsistentTopicStatus;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicListener;
import org.omg.dds.topic.TopicQos;
import org.omg.dds.type.TypeSupport;

import fr.sorbonne_u.components.AbstractComponent;

public class Test extends AbstractComponent implements Topic<Integer>{

	protected Test(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TypeSupport<Integer> getTypeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <OTHER> TopicDescription<OTHER> cast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceEnvironment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicListener<Integer> getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListener(TopicListener<Integer> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener(TopicListener<Integer> listener, Collection<Class<? extends Status>> statuses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TopicQos getQos() {
		// TODO Auto-generated method stub
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
	public void retain() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InconsistentTopicStatus getInconsistentTopicStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCondition<Topic<Integer>> getStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainParticipant getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
