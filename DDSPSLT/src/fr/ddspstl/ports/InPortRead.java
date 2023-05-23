package fr.ddspstl.ports;

import org.omg.dds.core.Time;
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
 * @param <TYPE> : type de la donnée
 *
 * Classe InPortRead
 */
public class InPortRead<TYPE> extends AbstractInboundPort implements ReadCI<TYPE> {

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Iterator<TYPE> read(TopicDescription<TYPE> topic) throws Exception {

		return ((DDSPlugin) getOwnerPlugin(pluginURI)).read(topic);

	}

	/**
	 * @see fr.ddspstl.interfaces.ReadCI#take(TopicDescription)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator<TYPE> take(TopicDescription<TYPE> topic) throws Exception {
		
		return ((DDSPlugin) getOwnerPlugin(pluginURI)).take(topic);
	}

}
