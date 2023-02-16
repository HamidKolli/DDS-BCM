package fr.ddspstl.plugin;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.connectors.ConnectorWrite;
import fr.ddspstl.ports.OutPortConnectClient;
import fr.ddspstl.ports.OutPortRead;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class ClientPlugin<T> extends AbstractPlugin{

	private static final long serialVersionUID = 1L;
	private String uriConnectPortDDS;
	private OutPortConnectClient<T> outPortconnectClient;
	private OutPortRead outPortRead;
	private OutPortWrite outPortWrite;
	private boolean isReader;
	private boolean isWriter;
	
	public ClientPlugin(String uriConnectPortDDS) {
		this.uriConnectPortDDS = uriConnectPortDDS;
		isReader = false;
		isWriter = false;
	}
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		this.outPortconnectClient = new OutPortConnectClient<T>(owner);
		this.outPortRead = new OutPortRead(owner);
		this.outPortWrite = new OutPortWrite(owner);
		owner.doPortConnection(this.outPortconnectClient.getPortURI(), uriConnectPortDDS,
				ConnectorClient.class.getCanonicalName());
		
		super.installOn(owner);
	}
	
	@Override
	public void initialise() throws Exception {
		this.outPortconnectClient.publishPort();
		this.outPortRead.publishPort();
		this.outPortWrite.publishPort();
		super.initialise();
	}
	
	public Topic<T> connect(int domainID, String t) throws Exception {
		 return this.outPortconnectClient.connect(domainID, t);
	}
	
	public void connectReader() throws Exception {
		String uri = this.outPortconnectClient.getReaderURI();
		getOwner().doPortConnection(this.outPortRead.getPortURI(), uri, ConnectorRead.class.getCanonicalName());
		isReader = true;
	}
	
	public void connectWriter() throws Exception {
		String uri = this.outPortconnectClient.getWriterURI();
		getOwner().doPortConnection(this.outPortWrite.getPortURI(), uri, ConnectorWrite.class.getCanonicalName());
		isWriter = true;
	}
	


	public DataReader<T> getDataReader(Topic<T>  topic) throws Exception{
		return this.outPortRead.getDataReader(topic);
	}

	public DataWriter<T> getDataWriter(Topic<T>  topic) throws Exception{
		return this.outPortWrite.getDataWriter(topic);
	}
	
	
	public T read(DataReader<T> dataReader) throws Exception {
		return this.outPortRead.read(dataReader);
	}

	public void write(DataWriter<T> dataWriter,T Data) throws Exception {
		this.outPortWrite.write(dataWriter, Data);
	}

	
	@Override
	public void finalise() throws Exception {
		this.outPortconnectClient.doDisconnection();
		if(isReader)
			this.outPortRead.doDisconnection();
		if(isWriter)
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
