package fr.ddspstl.plugin;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.ports.OutPortConnectClient;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class ClientPlugin<T> extends AbstractPlugin {

	private static final long serialVersionUID = 1L;
	private OutPortConnectClient<T> outPortconnectClient;

	private boolean isConnected;
	private String uriDDSNode;

	public ClientPlugin(String uriDDSNode) {
		isConnected = false;
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
		
	}
	
	public void connect() throws Exception {
		isConnected = true;
		getOwner().doPortConnection(this.outPortconnectClient.getPortURI(), uriDDSNode,
				ConnectorClient.class.getCanonicalName());
	}

	public DataReader<T> connectReader(TopicDescription<T> topic) throws Exception {
		
		return this.outPortconnectClient.getReader(topic);
	}

	public DataWriter<T> connectWriter(Topic<T> topic) throws Exception {
		return this.outPortconnectClient.getWriter(topic);
	}


	

	
	@Override
	public void finalise() throws Exception {
		
		if (isConnected)
			this.outPortconnectClient.doDisconnection();
		super.finalise();
	}

	@Override
	public void uninstall() throws Exception {
		this.outPortconnectClient.unpublishPort();
		this.outPortconnectClient.destroyPort();

		super.uninstall();
	}

}
