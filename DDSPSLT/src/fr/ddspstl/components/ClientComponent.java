package fr.ddspstl.components;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.ddspstl.plugin.ClientPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class ClientComponent<T> extends AbstractComponent {

	private String uriConnectPortDDS;
	private String pluginURI;
	

	protected ClientComponent(int nbThreads, int nbSchedulableThreads, String uriConnectPortDDS) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		this.uriConnectPortDDS = uriConnectPortDDS;
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
		ClientPlugin<T> plugin  = new ClientPlugin<>(uriConnectPortDDS);
		pluginURI = AbstractPort.generatePortURI();
		plugin.setPluginURI(pluginURI);
		plugin.installOn(this);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}

		super.start();
	}
	
	@Override
	public void execute() throws Exception {
		
		// passer par getPlugin(URIPlugin) pour executer read/write
		/*
		 * __________ TUTO ______________
		 */
		@SuppressWarnings("unchecked")
		ClientPlugin<T> plugin = ((ClientPlugin<T>)getPlugin(pluginURI));
		
		// Connection
		Topic<T> topic = plugin.connect(3, "TempTopic");
		
		//How to read a data from a topic
		/*
		 * Connect reader port
		 * get dataReader from a topic 
		 * read the data 
		 */
		plugin.connectReader();
		DataReader<T> dataReader  = plugin.getDataReader(topic);
		T data = plugin.read(dataReader);
		System.out.println(data);
		
		
		//How to write a data from a topic
		/*
		 * Connect writer port
		 * get dataWriter from a topic 
		 * write a data 
		 */
		plugin.connectWriter();
		DataWriter<T> dataWriter = plugin.getDataWriter(topic);
		plugin.write(dataWriter, data);
		
		
		super.execute();
	}


	@Override
	public synchronized void finalise() throws Exception {
		getPlugin(pluginURI).finalise();
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			getPlugin(pluginURI).uninstall();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}
}
