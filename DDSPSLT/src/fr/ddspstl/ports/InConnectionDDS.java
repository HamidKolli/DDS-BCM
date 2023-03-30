package fr.ddspstl.ports;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InConnectionDDS extends AbstractInboundPort implements ConnectDDSNode {

	private static final long serialVersionUID = 1L;

	public InConnectionDDS(String uri, ComponentI owner, String pluginURI) throws Exception {
		super(uri, ConnectDDSNode.class, owner, pluginURI, null);
	}

	@Override
	public void disconnect(INodeAddress address) throws Exception {

		this.getOwner().runTask(new AbstractComponent.AbstractTask(getPluginURI()) {
			@Override
			public void run() {
				try {
					((DDSPlugin)getTaskProviderReference()).disconnectBack(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void connect(INodeAddress address) throws Exception {
		this.getOwner().runTask(new AbstractComponent.AbstractTask(getPluginURI()) {

			@Override
			public void run() {
				try {
					((DDSPlugin)getTaskProviderReference()).connectBack(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void connectPropagation(INodeAddress address) throws Exception {
		this.getOwner().runTask(new AbstractComponent.AbstractTask(getPluginURI()) {

			@Override
			public void run() {
				try {
					((DDSPlugin)getTaskProviderReference()).connectPropagation(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

}
