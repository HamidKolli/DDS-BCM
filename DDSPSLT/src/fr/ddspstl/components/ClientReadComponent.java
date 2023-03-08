package fr.ddspstl.components;

import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.DDS.samples.Sample.Iterator;
import fr.ddspstl.DDS.topic.Topic;
import fr.ddspstl.plugin.ClientPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.PluginI;

public class ClientReadComponent extends ClientComponent<String>{

	protected ClientReadComponent(int nbThreads, int nbSchedulableThreads, String uriConnectPortDDS, Topic<String> topic) throws Exception {
		super(nbThreads, nbSchedulableThreads, uriConnectPortDDS,topic);
	}

	
	@Override
	public void execute() throws Exception {
		
		Thread.sleep(2000L);
		
		ClientPlugin<String> plugin = ((ClientPlugin<String>)getPlugin(pluginURI));
		
		
		
		//How to read a data from a topic
		
		@SuppressWarnings("unchecked")
		DataReader<String> dataReader  = plugin.connectReader(topic);
		
		((PluginI)dataReader).setPluginURI(AbstractPort.generatePortURI());
		this.installPlugin((PluginI)dataReader);
		
		
		@SuppressWarnings("unchecked")
		Iterator<String> data =  (Iterator<String>) plugin.read(topic);
		System.out.println(data);
		
		super.execute();
	}
	

}
