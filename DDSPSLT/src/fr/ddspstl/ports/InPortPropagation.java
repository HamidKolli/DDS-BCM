package fr.ddspstl.ports;



import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortPropagation extends AbstractInboundPort implements Propagation{

	private static final long serialVersionUID = 1L;

	public InPortPropagation(ComponentI owner,String pluginURI)
			throws Exception {
		super(Propagation.class, owner,pluginURI,null);
	}

	@Override
	public <T> void propager(T newObject, Topic<T> topic, String id) throws Exception {
		getOwner().runTask(new AbstractComponent.AbstractTask(getPluginURI()) {
			
			@Override
			public void run() {
				try {
					((ConnectionPlugin)getTaskProviderReference()).propager(newObject,topic,id);
				} catch (Exception e) {
				}
				
			}
		});
	}
	
	

}
