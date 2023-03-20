package fr.ddspstl.components;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.ddspstl.plugin.ClientPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.PluginI;

public class ClientWriteComponent extends ClientComponent<String>{

	protected ClientWriteComponent(int nbThreads, int nbSchedulableThreads, String uriConnectPortDDS, Topic<String> topic) throws Exception {
		super(nbThreads, nbSchedulableThreads, uriConnectPortDDS,topic);
	}
	
	@Override
	public void execute() throws Exception {
		
		System.out.println("debut writer");
		
		ClientPlugin<String> plugin = ((ClientPlugin<String>)getPlugin(pluginURI));
		plugin.connect();
		Thread.sleep(1000L);
		DataWriter<String> dataWriter = plugin.connectWriter(topic);
		System.out.println("data writer recup");
		String tmp = AbstractPort.generatePortURI();
		((PluginI)dataWriter).setPluginURI(tmp);
		this.installPlugin(((PluginI)dataWriter));
		System.out.println("data writer installer");
		System.out.println("j'ecrit");
		dataWriter.write("Hello");
		super.execute();
	}

}
