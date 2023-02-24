package fr.ddspstl.ports;

import fr.ddspstl.interfaces.ConnectInClient;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortConnectClient extends AbstractInboundPort implements ConnectInClient {

	private static final long serialVersionUID = 1L;

	public InPortConnectClient(String uri, ComponentI owner,String pluginURI) throws Exception {
		super(uri, ConnectInClient.class, owner,pluginURI,null);
	}



	@Override
	public String getReaderURI() throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@Override
			public String call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getReaderURI();
			}
		});
	}

	@Override
	public String getWriterURI() throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@Override
			public String call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getWriterURI();
			}
		});
	}

	@Override
	public void disconnectClient(String dataReader, String dataWriter) throws Exception{
		getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(getPluginURI()) {
			@Override
			public Void call() throws Exception {
				((ConnectionPlugin)getServiceProviderReference()).disconnectClient( dataReader,  dataWriter);
				return null;
			}
		});

	}

}
