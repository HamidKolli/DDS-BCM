package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Interface Implantée par un ConnectorClient
 */
public interface ConnectClient extends RequiredCI,OfferedCI {
	/**
	 * getter
	 * 
	 * @return String : le ReaderUri
	 * @throws Exception
	 */
	public String getReaderURI() throws Exception;
	/**
	 * getter
	 * 
	 * @return String : le WriterUri
	 * @throws Exception
	 */
	public String getWriterURI() throws Exception;
}
