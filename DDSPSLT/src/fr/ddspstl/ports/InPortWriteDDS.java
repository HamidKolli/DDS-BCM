package fr.ddspstl.ports;

import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortWriteDDS<T> extends AbstractInboundPort implements WriteCI<T> {

	/**
	 * serialVersionUID : de type long gere
	 */
	private static final long serialVersionUID = 1524813463125346347L;

	public InPortWriteDDS(String uri, ComponentI owner,
			String pluginURI, String executorServiceURI) throws Exception {
		super(uri, WriteCI.class, owner, pluginURI, executorServiceURI);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(Topic<T> topic, T data) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin<T>)getOwnerPlugin(getPluginURI())).write(topic, data);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		});
		
	}
	
	

}
