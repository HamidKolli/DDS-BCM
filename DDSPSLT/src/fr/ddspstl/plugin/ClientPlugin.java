package fr.ddspstl.plugin;

import org.omg.dds.sub.Sample.Iterator;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.connectors.ConnectorWrite;
import fr.ddspstl.interfaces.ConnectOutClient;
import fr.ddspstl.interfaces.OutRead;
import fr.ddspstl.interfaces.OutWrite;
import fr.ddspstl.ports.OutPortConnectClient;
import fr.ddspstl.ports.OutPortRead;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class ClientPlugin<T> extends AbstractPlugin{

	private static final long serialVersionUID = 1L;
	private OutPortConnectClient outPortconnectClient;
	private OutPortRead outPortRead;
	private OutPortWrite outPortWrite;
	private boolean isReader;
	private boolean isWriter;
	
	public ClientPlugin() {
		isReader = false;
		isWriter = false;
	}
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner);
		this.addRequiredInterface(ConnectOutClient.class);
		this.addRequiredInterface(OutWrite.class);
		this.addRequiredInterface(OutRead.class);
	}
	
	@Override
	public void initialise() throws Exception {
		
		super.initialise();
		
		this.outPortconnectClient = new OutPortConnectClient(getOwner());
		this.outPortRead = new OutPortRead(getOwner());
		this.outPortWrite = new OutPortWrite(getOwner());

		this.outPortconnectClient.publishPort();
		this.outPortRead.publishPort();
		this.outPortWrite.publishPort();
		
	}
	
	public void connect(String ddsNodeURI) throws Exception {
		getOwner().doPortConnection(this.outPortconnectClient.getPortURI(), ddsNodeURI,
				ConnectorClient.class.getCanonicalName());
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
	


	public String getDataReader(String topic) throws Exception{
		return this.outPortRead.getDataReader(topic);
	}

	public String getDataWriter(String  topic) throws Exception{
		return this.outPortWrite.getDataWriter(topic);
	}
	
	
	public Iterator<?> read(String dataReader) throws Exception {
		return this.outPortRead.read(dataReader);
	}

	public void write(String dataWriter,T Data) throws Exception {
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
