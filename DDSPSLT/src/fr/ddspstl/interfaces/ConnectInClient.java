package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface ConnectInClient extends OfferedCI {

	public String getReaderURI()throws Exception;

	public String getWriterURI()throws Exception;

	public void disconnectClient()throws Exception;
}
