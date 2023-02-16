package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectDDSNode extends OfferedCI,RequiredCI{
		public void connect(String uri,int domainID) throws Exception ;
		public void disconnect(String uri) throws Exception ;
		
}
