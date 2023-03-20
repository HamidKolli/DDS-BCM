package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortRead<TYPE> extends AbstractInboundPort implements ReadCI<TYPE>{

	private static final long serialVersionUID = 1L;

	public InPortRead(ComponentI owner,String pluginURI) throws Exception {
		super(ReadCI.class, owner,pluginURI,null);
		
	}
	

	public  Iterator<TYPE> read(TopicDescription<TYPE> topic) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<Iterator<TYPE>>(getPluginURI()) {
			@Override
			public Iterator<TYPE> call() throws Exception {
				return ((DDSPlugin)getServiceProviderReference()).read(topic);
			}
		});
	}



}
