package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe InPortWriteDDS
 */
public class InPortWriteDDS extends AbstractInboundPort implements WriteCI {

	/**
	 * serialVersionUID : de type long gere
	 */
	private static final long serialVersionUID = 1524813463125346347L;

	/**
	 * Constructeur
	 * 
	 * @param uri : l'uri du port
	 * @param owner : l'owner du port
	 * @param pluginURI : l'uri du plugin
	 * @param executorServiceURI : l'uri de l'executor service
	 * @throws Exception
	 */
	public InPortWriteDDS(String uri, ComponentI owner,
			String pluginURI, String executorServiceURI) throws Exception {
		super(uri, WriteCI.class, owner, pluginURI, executorServiceURI);
	}

	/**
	 * @see fr.ddspstl.interfaces.WriteCI#write(Topic, Object)
	 */
	@Override
	public <T> void write(Topic<T> topic, T data) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin)getOwnerPlugin(getPluginURI())).write(topic, data);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		});
		
	}
	
	

}
