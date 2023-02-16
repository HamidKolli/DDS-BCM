package fr.ddspstl.components;

import java.util.HashSet;
import java.util.Set;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.ddspstl.connectors.ConnectorClient;
import fr.ddspstl.connectors.ConnectorRead;
import fr.ddspstl.connectors.ConnectorWrite;
import fr.ddspstl.ports.OutPortConnectClient;
import fr.ddspstl.ports.OutPortRead;
import fr.ddspstl.ports.OutPortWrite;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class ClientComponent<T> extends AbstractComponent {
	private Set<DataReader<T>> readers;
	private Set<DataWriter<T>> writers;
	private String uriConnectPortDDS;
	private Topic<T> topic;
	private OutPortConnectClient<T> outPortconnectClient;
	private OutPortRead outPortRead;
	private OutPortWrite outPortWrite;
	private boolean isReader;
	private boolean isWriter;

	protected ClientComponent(int nbThreads, int nbSchedulableThreads, String uriConnectPortDDS) throws Exception {
		super(nbThreads, nbSchedulableThreads);
		readers = new HashSet<>();
		writers = new HashSet<>();
		this.uriConnectPortDDS = uriConnectPortDDS;
		this.outPortconnectClient = new OutPortConnectClient<T>(this);
		outPortRead = new OutPortRead(this);
		outPortWrite = new OutPortWrite(this);
		isReader = false;
		isWriter = false;
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {
			this.outPortconnectClient.publishPort();
			this.outPortRead.publishPort();
			this.outPortWrite.publishPort();
			doPortConnection(this.outPortconnectClient.getPortURI(), uriConnectPortDDS,
					ConnectorClient.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}

		super.start();
	}

	public void connect(int domainID, String t) throws Exception {
		topic = this.outPortconnectClient.connect(domainID, t);
	}
	
	public void connectReader() throws Exception {
		String uri = this.outPortconnectClient.getReaderURI();
		doPortConnection(this.outPortRead.getPortURI(), uri, ConnectorRead.class.getCanonicalName());
		isReader = true;
	}
	
	public void connectWriter() throws Exception {
		String uri = this.outPortconnectClient.getWriterURI();
		doPortConnection(this.outPortWrite.getPortURI(), uri, ConnectorWrite.class.getCanonicalName());
	}
	


	public DataReader<T> getDataReader() throws Exception{
		return this.outPortRead.getDataReader(topic);
	}

	public DataWriter<T> getDataWriter() throws Exception{
		return this.outPortWrite.getDataWriter(topic);
	}
	
	
	public void read(DataReader<T> dataReader) throws Exception {
		this.outPortRead.read(dataReader);
	}

	public void write(DataWriter<T> dataWriter,T Data) throws Exception {
		this.outPortWrite.write(dataWriter, Data);
	}


	@Override
	public synchronized void finalise() throws Exception {
		this.outPortconnectClient.doDisconnection();
		if(isReader)
			this.outPortRead.doDisconnection();
		if(isWriter)
			this.outPortWrite.doDisconnection();
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.outPortconnectClient.unpublishPort();
			this.outPortRead.unpublishPort();
			this.outPortWrite.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}
}
