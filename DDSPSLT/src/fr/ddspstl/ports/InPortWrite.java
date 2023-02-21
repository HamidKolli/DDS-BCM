package fr.ddspstl.ports;

import fr.ddspstl.interfaces.InWrite;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortWrite extends AbstractInboundPort implements InWrite {

	private static final long serialVersionUID = 1L;

	public InPortWrite(ComponentI owner,String pluginURI) throws Exception {
		super(InWrite.class, owner,pluginURI,null);
	}

	@Override
	public String getDataWriter(String topic) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@Override
			public String call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getDataWriter(topic);
			}
		});
	}

	@Override
	public <T> Void write(String writer,T data) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<Void>(getPluginURI()) {
			@Override
			public Void call() throws Exception {
				((ConnectionPlugin)getServiceProviderReference()).write(writer,data);
				 return null;
			}
		});
	}

}
