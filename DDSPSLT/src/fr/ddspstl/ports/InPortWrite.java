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
 * Classe InPortWrite
 */
public class InPortWrite extends AbstractInboundPort implements WriteCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @param pluginURI : l'uri du plugin
	 * @throws Exception
	 */
	public InPortWrite(ComponentI owner,String pluginURI) throws Exception {
		super(WriteCI.class, owner,pluginURI,null);
	}

	/**
	 * @see fr.ddspstl.interfaces.WriteCI#write(Topic, Object)
	 */
	@Override
	public <T> void write(Topic<T> topic, T data) throws Exception {
		 ((DDSPlugin) getOwnerPlugin(pluginURI)).write(topic,data);
	}


	

}
