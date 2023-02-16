package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectDDSNode extends OfferedCI,RequiredCI{
		public String connect(String uri,String uriPropagation,int domainID) throws Exception ;
		public void disconnect(String uri) throws Exception ;
		
}
