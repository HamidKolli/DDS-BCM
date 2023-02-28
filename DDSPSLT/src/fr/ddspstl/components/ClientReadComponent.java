package fr.ddspstl.components;

import fr.ddspstl.DDS.samples.Sample.Iterator;
import fr.ddspstl.plugin.ClientPlugin;

public class ClientReadComponent extends ClientComponent<String>{

	protected ClientReadComponent(int nbThreads, int nbSchedulableThreads, String uriConnectPortDDS) throws Exception {
		super(nbThreads, nbSchedulableThreads, uriConnectPortDDS);
	}

	
	@Override
	public void execute() throws Exception {
		
		Thread.sleep(2000L);
		
		ClientPlugin<String> plugin = ((ClientPlugin<String>)getPlugin(pluginURI));
		
		// Connection
		plugin.connect(uriConnectPortDDS);
		
		//How to read a data from a topic

		 
		plugin.connectReader();
		String dataReader  = plugin.getDataReader("myTopic");
		
		Iterator<?> data =  (Iterator<?>) plugin.read(dataReader);
		System.out.println(data);
		
		plugin.disconnect(dataReader,null);
		super.execute();
	}
	

}
