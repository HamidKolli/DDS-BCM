package fr.ddspstl.interfaces;

import fr.ddspstl.addresses.INodeAddress;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ConnectDDSNode extends OfferedCI, RequiredCI {
	
	public void connect(INodeAddress nodeAddress) throws Exception;

	public void connectPropagation(INodeAddress nodeAddress) throws Exception;
	
	public void disconnect(INodeAddress nodeAddress) throws Exception;

}
