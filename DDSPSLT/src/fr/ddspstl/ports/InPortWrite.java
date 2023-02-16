package fr.ddspstl.ports;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.ddspstl.interfaces.InWrite;
import fr.ddspstl.plugin.ConnectionPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortWrite extends AbstractInboundPort implements InWrite {

	private static final long serialVersionUID = 1L;

	public InPortWrite(ComponentI owner) throws Exception {
		super(InWrite.class, owner);
	}

	@Override
	public <T> DataWriter<T> getDataWriter(Topic<T> topic) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<DataWriter<T>>() {
			@Override
			public DataWriter<T> call() throws Exception {
				return ((ConnectionPlugin)getServiceProviderReference()).getDataWriter(topic);
			}
		});
	}

	@Override
	public <T> Void write(DataWriter<T> reader,T data) throws Exception {
		return getOwner().handleRequest(new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((ConnectionPlugin)getServiceProviderReference()).write(reader,data);
				 return null;
			}
		});
	}

}
