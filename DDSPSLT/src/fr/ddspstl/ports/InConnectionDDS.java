package fr.ddspstl.ports;

import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InConnectionDDS extends AbstractInboundPort implements ConnectDDSNode{

	private static final long serialVersionUID = 1L;

	public InConnectionDDS(String uri, ComponentI owner)
			throws Exception {
		super(uri,ConnectDDSNode.class , owner);
	}



	@Override
	public void disconnect(String uri) throws Exception {
		this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(){

			@Override
			public Void call() throws Exception {
				((ConnectionPlugin)getServiceProviderReference()).disconnectBack(uri);
				return null;
			}
			
		});
	}

	@Override
	public void connect(String uri, int domainID) throws Exception {
		this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(){

			@Override
			public Void call() throws Exception {
				((ConnectionPlugin)getServiceProviderReference()).connectBack(uri,domainID);
				return null;
			}
			
		});
	}

}
