package fr.ddspstl.ports;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.interfaces.ConnectDDSNode;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InConnectionDDS extends AbstractInboundPort implements ConnectDDSNode {

	private static final long serialVersionUID = 1L;

	public InConnectionDDS(String uri, ComponentI owner, String executorServicePropagationURI) throws Exception {
		super(uri, ConnectDDSNode.class, owner,null,executorServicePropagationURI);
	}

	@Override
	public void disconnect(INodeAddress address) throws Exception {

		this.getOwner().runTask(getExecutorServiceIndex(),new AbstractComponent.AbstractTask() {
			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				try {
					((IDDSNode)getOwner()).disconnectBack(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void connect(INodeAddress address) throws Exception {
		this.getOwner().runTask(getExecutorServiceIndex(),new AbstractComponent.AbstractTask() {

			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				try {
					((IDDSNode)getOwner()).connectBack(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void connectPropagation(INodeAddress address) throws Exception {
		this.getOwner().runTask(getExecutorServiceIndex(),new AbstractComponent.AbstractTask() {

			@SuppressWarnings({ "rawtypes" })
			@Override
			public void run() {
				try {
					((IDDSNode)getOwner()).connectPropagation(address);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

}
