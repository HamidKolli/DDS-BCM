package fr.ddspstl.connectors;

import fr.ddspstl.interfaces.ConnectClient;
import fr.sorbonne_u.components.connectors.AbstractConnector;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe Connecteur d'un client
 */
public class ConnectorClient extends AbstractConnector implements ConnectClient {

	

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ConnectClient#getReaderURI()
	 */
	@Override
	public String getReaderURI() throws Exception {
		return ((ConnectClient)this.offering).getReaderURI();
	}

	/**
	 * 
	 * @see fr.ddspstl.interfaces.ConnectClient#getWriterURI()
	 */
	@Override
	public String getWriterURI() throws Exception {
		return ((ConnectClient)this.offering).getWriterURI();
	}


	

}
