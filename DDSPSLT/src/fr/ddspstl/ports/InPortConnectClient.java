package fr.ddspstl.ports;

import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortConnectClient extends AbstractInboundPort implements ConnectClient {

	private static final long serialVersionUID = 1L;

	public InPortConnectClient(String uri, ComponentI owner,String pluginURI, String executorServiceURI) throws Exception {
		super(uri, ConnectClient.class, owner,pluginURI,executorServiceURI);
	}

	@Override
	public String getReaderURI() throws Exception {
		return getOwner().handleRequest(getExecutorServiceIndex(),new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@Override
			public String call() throws Exception {
				return ((DDSPlugin)getServiceProviderReference()).getReaderURI();
			}
		});
	}



	@Override
	public String getWriterURI() throws Exception {
		return getOwner().handleRequest(getExecutorServiceIndex(),new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@Override
			public String call() throws Exception {
				return ((DDSPlugin)getServiceProviderReference()).getWriterURI();
			}
		});
	}
	
	

	

}
