package fr.ddspstl.plugin;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.ports.OutPortConnectClient;
import fr.ddspstl.ports.OutPortRead;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class ClientPlugin<T> extends AbstractPlugin {

	private static final long serialVersionUID = 1L;
	private OutPortConnectClient<T> outPortconnectClient;
	private OutPortRead<T> outPortRead;
	private OutPortWrite<T> outPortWrite;
	private boolean isReader;
	private boolean isWriter;
	private String uriDDSNode;

	public ClientPlugin(String uriDDSNode) {
		isReader = false;
		isWriter = false;
		this.uriDDSNode = uriDDSNode;
	}

	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addRequiredInterface(ConnectorClient.class);
		this.addRequiredInterface(WriteCI.class);
		this.addRequiredInterface(ReadCI.class);
	}

	@Override
	public void initialise() throws Exception {

		super.initialise();

		this.outPortconnectClient = new OutPortConnectClient<>(getOwner());
		this.outPortconnectClient.publishPort();
		getOwner().doPortConnection(this.outPortconnectClient.getPortURI(), uriDDSNode,
				ConnectorClient.class.getCanonicalName());
	}

	public DataReader<T> connectReader(TopicDescription<T> topic) throws Exception {
		return this.outPortconnectClient.getReader(topic);
	}

	public DataWriter<T> connectWriter(Topic<T> topic) throws Exception {
		return this.outPortconnectClient.getWriter(topic);
	}


	public Iterator<T> read(TopicDescription<T> topic) throws Exception {
		return this.outPortRead.read(topic);
	}

	public void write(Topic<T> topic, T Data) throws Exception {
		this.outPortWrite.write(topic, Data);
	}

	
	@Override
	public void finalise() throws Exception {
		this.outPortconnectClient.doDisconnection();
		if (isReader)
			this.outPortRead.doDisconnection();
		if (isWriter)
			this.outPortWrite.doDisconnection();
		super.finalise();
	}

	@Override
	public void uninstall() throws Exception {
		this.outPortconnectClient.unpublishPort();
		this.outPortRead.unpublishPort();
		this.outPortWrite.unpublishPort();
		this.outPortconnectClient.destroyPort();
		this.outPortRead.destroyPort();
		this.outPortWrite.destroyPort();
		super.uninstall();
	}

}
