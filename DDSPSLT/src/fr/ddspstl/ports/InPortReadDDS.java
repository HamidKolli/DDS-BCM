package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ReadDDSCI;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortReadDDS<T> extends AbstractInboundPort implements ReadDDSCI<T> {

	private static final long serialVersionUID = 1L;

	public InPortReadDDS(String uri, ComponentI owner, String pluginURI, String executorServiceURI) throws Exception {
		super(uri, ReadDDSCI.class, owner, pluginURI, executorServiceURI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin<T>)getOwnerPlugin(getPluginURI())).read(topic,address,requestID);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void take(TopicDescription<T> topic,INodeAddress address , String requestID) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin<T>)getOwnerPlugin(getPluginURI())).take(topic,address,requestID);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void acceptResult(Iterator<T> result, String requestID) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((DDSPlugin<T>)getOwnerPlugin(getPluginURI())).acceptResult(result,requestID);
				
			} catch (DDSTopicNotFoundException e1) {
				e1.printStackTrace();
			} 
		});
	}

}
