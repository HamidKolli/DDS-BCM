package fr.ddspstl.components;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.sub.DataReader;

import fr.ddspstl.DDS.samples.Sample.Iterator;
import fr.ddspstl.DDS.subscribers.interfaces.Subscriber;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.PluginI;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class ClientReadComponent extends ClientComponent{

	private String topicName;
	private Subscriber subscriber;
	private DataReader<?> dataReader;
	protected ClientReadComponent(int nbThreads, int nbSchedulableThreads,String uriDDSNode,DomainParticipant domainParticipant,String topicName) throws Exception {
		super(nbThreads, nbSchedulableThreads,uriDDSNode,domainParticipant);
		this.topicName = topicName;
		this.subscriber = (Subscriber) domainParticipant.createSubscriber();
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			org.omg.dds.topic.Topic<?> topic  =  domainParticipant.findTopic(topicName, null);
			if(topic == null)
				throw new Exception("topic not found");
			dataReader = ((Subscriber)subscriber).createDataReader(topic,uriDDSNode);
			PluginI plugin  = (PluginI)dataReader;
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}
	@Override
	public void execute() throws Exception {
		
		System.out.println("debut reader");
		
		Thread.sleep(1000L);
		
		
		//How to read a data from a topic

		Thread.sleep(5000L);
		@SuppressWarnings("unchecked")
		Iterator<?> data =  (Iterator<?>) dataReader.read();
		
		
		while(data.hasNext()) {
			System.out.println("haha");
			System.out.println("la donnee " + data.next().getData());
		}
		
		
		
		super.execute();
	}
	

}
