package fr.ddspstl.ports;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.ConnectClient;

import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortConnectClient<T> extends AbstractInboundPort implements ConnectClient<T> {

	private static final long serialVersionUID = 1L;

	public InPortConnectClient(String uri, ComponentI owner,String pluginURI) throws Exception {
		super(uri, ConnectClient.class, owner,pluginURI,null);
	}



	@Override
	public DataReader<T> getReader(TopicDescription<T> topic) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<DataReader<T>>(getPluginURI()) {
			@Override
			public DataReader<T> call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getDataReader(topic);
			}
		});
	}


	@Override
	public DataWriter<T> getWriter(Topic<T> topic) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<DataWriter<T>>(getPluginURI()) {
			@Override
			public DataWriter<T> call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getDataWriter(topic);
			}
		});
	}
	
	

	

}
