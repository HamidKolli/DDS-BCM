package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectClient extends RequiredCI,OfferedCI {
	public String getReaderURI() throws Exception;
	public String getWriterURI() throws Exception;
}
