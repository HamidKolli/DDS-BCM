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
		
		System.out.println("debut reader");
		
		ClientPlugin<String> plugin = ((ClientPlugin<String>)getPlugin(pluginURI));
		plugin.connect();
		Thread.sleep(1000L);
		
		
		//How to read a data from a topic
		
		@SuppressWarnings("unchecked")
		DataReader<String> dataReader  = plugin.connectReader(topic);
		System.out.println("data reader recup");
		((PluginI)dataReader).setPluginURI(AbstractPort.generatePortURI());
		this.installPlugin((PluginI)dataReader);
		
		Thread.sleep(3000L);
		@SuppressWarnings("unchecked")
		Iterator<String> data =  (Iterator<String>) dataReader.read();
		System.out.println("la donnee" + data);
		
		
		super.execute();
	}
	

}
