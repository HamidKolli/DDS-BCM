package fr.ddspstl.components;

import java.util.HashSet;
import java.util.Set;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.AbstractComponent;

public class ClientComponent extends AbstractComponent {
	public static final int READ = 1;
	public static final int WRITE = 2;
	private Set<DataReader<Object>> readers;
	private Set<DataWriter<Object>> writers;
	private String uriConnectPortDDS;
	private Set<Topic<Object>> topics;
	
	protected ClientComponent(int nbThreads, int nbSchedulableThreads,String uriConnectPortDDS) {
		super(nbThreads, nbSchedulableThreads);
		readers = new HashSet<>();
		writers = new HashSet<>();
		this.uriConnectPortDDS =  uriConnectPortDDS;
	}
	
	public void connect() {
		//recup la liste des topic en appelant la methode de DDSNode
	}
	
	public void read(Topic<Object> topic) {
		// recup uri du portINRead de dds
		// doConnection avec le port portINRead
		// getDataReader en appelant la methode getDataReader de DDS par le port portINRead
		// read 
		
	}
	
	public void write(Topic<Object> topic) {
		// meme chose mais en write
	}
	
	
	
	
	
	

}
