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
		ClientPlugin<String> plugin = ((ClientPlugin<String>)getPlugin(pluginURI));
		
		DataWriter<String> dataWriter = plugin.connectWriter(topic);
		((PluginI)dataWriter).setPluginURI(AbstractPort.generatePortURI());
		this.installPlugin(((PluginI)dataWriter));
		plugin.write(topic, "Hello");
		super.execute();
	}

}
