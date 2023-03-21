package fr.ddspstl.components;

import java.util.concurrent.TimeoutException;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.ddspstl.DDS.publishers.interfaces.Publisher;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.PluginI;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class ClientWriteComponent extends ClientComponent{

	private String topicName;
	private DataWriter<String> dataWriter;
	protected ClientWriteComponent(int nbThreads, int nbSchedulableThreads,String uriDDSNode,DomainParticipant domainParticipant,String topicName) throws Exception {
		super(nbThreads, nbSchedulableThreads,uriDDSNode,domainParticipant);
		this.topicName = topicName;
	}
	
	@Override
	public synchronized void start() throws ComponentStartException {
		org.omg.dds.topic.Topic<?> topic;
		try {
			topic = domainParticipant.findTopic(topicName, null);
			System.out.println(topicName);
			
			if(topic == null)
				throw new Exception("topic not found");
			dataWriter = (DataWriter<String>) ((Publisher)publisher).createDataWriter(topic,uriDDSNode);
			assert dataWriter != null;
			PluginI plugin  = (PluginI)dataWriter;
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		
		super.start();
	}
	
	@Override
	public void execute() throws Exception {
		
		System.out.println("debut writer");
		
		
		Thread.sleep(1000L);

		System.out.println("j'ecrit");
		dataWriter.write("Hello");
		super.execute();
	}

}
