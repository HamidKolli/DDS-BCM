package fr.ddspstl.ports;

import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe InPortConnectClient : port entrant de la connection client
 */
public class InPortConnectClient extends AbstractInboundPort implements ConnectClient {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param uri : l'URI du port
	 * @param owner : l'owner du port
	 * @param pluginURI : l'uri du plugin
	 * @param executorServiceURI : l'uri de l'executor Service
	 * @throws Exception
	 */
	public InPortConnectClient(String uri, ComponentI owner,String pluginURI, String executorServiceURI) throws Exception {
		super(uri, ConnectClient.class, owner,pluginURI,executorServiceURI);
	}

	/**
	 * @see fr.ddspstl.interfaces.ConnectClient#getReaderURI()
	 */
	@Override
	public String getReaderURI() throws Exception {
		return getOwner().handleRequest(getExecutorServiceIndex(),new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public String call() throws Exception {
				return ((DDSPlugin)getServiceProviderReference()).getReaderURI();
			}
		});
	}



	/**
	 * @see fr.ddspstl.interfaces.ConnectClient#getWriterURI()
	 */
	@Override
	public String getWriterURI() throws Exception {
		return getOwner().handleRequest(getExecutorServiceIndex(),new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@SuppressWarnings({  "rawtypes" })
			@Override
			public String call() throws Exception {
				return ((DDSPlugin)getServiceProviderReference()).getWriterURI();
			}
		});
	}
	
	

	

}