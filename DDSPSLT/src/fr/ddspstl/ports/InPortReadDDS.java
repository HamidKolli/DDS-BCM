package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortReadDDS extends AbstractInboundPort implements ReadDDSCI {

	private static final long serialVersionUID = 1L;

	public InPortReadDDS(String uri, ComponentI owner, String pluginURI, String executorServiceURI) throws Exception {
		super(uri, ReadDDSCI.class, owner, pluginURI, executorServiceURI);
	}

	@Override
	public void read(TopicDescription<?> topic,INodeAddress address , String requestID) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin)getOwnerPlugin(getPluginURI())).read(topic,address,requestID);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		});
	}

	@Override
	public void take(TopicDescription<?> topic,INodeAddress address , String requestID) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin)getOwnerPlugin(getPluginURI())).take(topic,address,requestID);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		});
	}

	@Override
	public void acceptResult(Iterator<?> result, String requestID) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin)getOwnerPlugin(getPluginURI())).acceptResult(result,requestID);
				
			} catch (DDSTopicNotFoundException e1) {
				e1.printStackTrace();
			} 
		});
	}

}
