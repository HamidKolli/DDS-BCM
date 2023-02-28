package fr.ddspstl.components;

import fr.ddspstl.plugin.ClientPlugin;

public class ClientWriteComponent extends ClientComponent<String>{

	protected ClientWriteComponent(int nbThreads, int nbSchedulableThreads, String uriConnectPortDDS) throws Exception {
		super(nbThreads, nbSchedulableThreads, uriConnectPortDDS);
	}
	
	@Override
	public void execute() throws Exception {
		ClientPlugin<String> plugin = ((ClientPlugin<String>)getPlugin(pluginURI));
		plugin.connectWriter();
		String dataWriter = plugin.getDataWriter("myTopic");
		plugin.write(dataWriter, "Hello");
		plugin.disconnect(null,dataWriter);
		super.execute();
	}

}
