package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe InPortRead
 */
public class InPortRead extends AbstractInboundPort implements ReadCI {

	private static final long serialVersionUID = 1L;

	/**
	 * @param owner : l'owner du port
	 * @param pluginURI : l'uri du plugin
	 * @throws Exception
	 */
	public InPortRead(ComponentI owner, String pluginURI) throws Exception {
		super(ReadCI.class, owner, pluginURI,null);

	}

	/**
	 * @see fr.ddspstl.interfaces.ReadCI#read(TopicDescription)
	 */
	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {

		return ((DDSPlugin) getOwnerPlugin(pluginURI)).read(topic);

	}

	/**
	 * @see fr.ddspstl.interfaces.ReadCI#take(TopicDescription)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Iterator<T> take(TopicDescription<T> topic) throws Exception {
		return (Iterator<T>) ((DDSPlugin) getOwnerPlugin(pluginURI)).take(topic);
	}

}
