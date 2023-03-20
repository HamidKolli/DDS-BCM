package fr.ddspstl.ports;


import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortWrite<T> extends AbstractInboundPort implements WriteCI<T> {

	private static final long serialVersionUID = 1L;

	public InPortWrite(ComponentI owner,String pluginURI) throws Exception {
		super(WriteCI.class, owner,pluginURI,null);
	}

	@Override
	public void write(Topic<T> topic, T data) throws Exception {
		 getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(getPluginURI()) {
			@Override
			public Void call() throws Exception {
				((DDSPlugin)getServiceProviderReference()).write(topic,data);
				 return null;
			}
		});
		
	}


	

}
