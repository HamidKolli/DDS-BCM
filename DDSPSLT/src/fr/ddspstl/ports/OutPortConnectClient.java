package fr.ddspstl.ports;


import fr.ddspstl.interfaces.ConnectClient;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe OutPortConnectClient
 */
public class OutPortConnectClient extends AbstractOutboundPort implements ConnectClient{


	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param owner : l'owner du port
	 * @throws Exception
	 */
	public OutPortConnectClient( ComponentI owner) throws Exception {
		super(ConnectClient.class, owner);	
	}
	

	/**
	 * @see fr.ddspstl.interfaces.ConnectClient#getReaderURI()
	 */
	@Override
	public String getReaderURI() throws Exception {
		return ((ConnectClient)getConnector()).getReaderURI();
	}

	/**
	 * @see fr.ddspstl.interfaces.ConnectClient#getWriterURI()
	 */
	@Override
	public String getWriterURI() throws Exception {
		return ((ConnectClient)getConnector()).getWriterURI();
	}


}