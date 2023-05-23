package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortRead extends AbstractInboundPort implements ReadCI {

	private static final long serialVersionUID = 1L;

	public InPortRead(ComponentI owner, String pluginURI) throws Exception {
		super(ReadCI.class, owner, pluginURI,null);

	}

	public <T> Iterator<T> read(TopicDescription<T> topic) throws Exception {

		return ((DDSPlugin) getOwnerPlugin(pluginURI)).read(topic);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Iterator<T> take(TopicDescription<T> topic) throws Exception {
		return (Iterator<T>) ((DDSPlugin) getOwnerPlugin(pluginURI)).take(topic);
	}

}
