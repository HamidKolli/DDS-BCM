package fr.ddspstl.ports;

import org.omg.dds.sub.Sample.Iterator;

import fr.ddspstl.interfaces.InRead;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortRead extends AbstractInboundPort implements InRead{

	private static final long serialVersionUID = 1L;

	public InPortRead(ComponentI owner,String pluginURI) throws Exception {
		super(InRead.class, owner,pluginURI,null);
		
	}

	@Override
	public String getDataReader(String topic) throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<String>(getPluginURI()) {
			@Override
			public String call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getDataReader(topic);
			}
		});
	}

	@Override
	public Iterator<?> read(String reader) throws Exception{
		return getOwner().handleRequest(new AbstractComponent.AbstractService<Iterator<?>>(getPluginURI()) {
			@Override
			public Iterator<?> call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).read(reader);
			}
		});
	}

}
