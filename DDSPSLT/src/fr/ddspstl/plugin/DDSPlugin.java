package fr.ddspstl.plugin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.DDS.data.Datas;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.interfaces.ConnectClient;
import fr.ddspstl.interfaces.ReadCI;
import fr.ddspstl.interfaces.WriteCI;
import fr.ddspstl.ports.InPortConnectClient;
import fr.ddspstl.ports.InPortRead;
import fr.ddspstl.ports.InPortWrite;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;

public class DDSPlugin<T> extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	private InPortRead<T> inPortRead;
	private InPortWrite<T> inPortWrite;
	private InPortConnectClient inPortConnectClient;
	private Map<Topic<T>, Datas<T>> datas;
	private Map<Topic<T>, String> topicID;
	private String uriConnectClient;

	public DDSPlugin(Set<Topic<T>> topics, Map<Topic<T>, String> topicID, String uriConnectClient) {

		this.uriConnectClient = uriConnectClient;
		this.datas = new HashMap<>();
		for (Topic<T> topic : topics) {
			datas.put(topic, new Datas<T>(topic));
		}
		this.topicID = new HashMap<>(topicID);
	}

	public String getReaderURI() throws Exception {
		return inPortRead.getPortURI();
	}

	public String getWriterURI() throws Exception {
		return inPortWrite.getPortURI();
	}

	public Iterator<T> read(TopicDescription<T> topic) throws DDSTopicNotFoundException {
		return datas.get(topic).read();
	}

	@SuppressWarnings("unchecked")
	public void write(Topic<T> topic, T data) throws Exception {
		((IDDSNode<T>)getOwner()).propager(data, topic, AbstractPort.generatePortURI(),Time.newTime((new Date()).getTime(), TimeUnit.MICROSECONDS,
				ServiceEnvironment.createInstance(getOwner().getComponentLoader())));
	}

	
	public void propager(T newObject, Topic<T> topicName, String id,Time time) throws Exception {
		if (topicID.get(topicName) == null)
			return;
		if (topicID.get(topicName).equals(id))
			return;

		topicID.put(topicName, id);
		datas.get(topicName).write(newObject, time);
		

	}

	public Iterator<T> take(TopicDescription<T> topic) {
		return datas.get(topic).take();
	}



	@Override
	public void installOn(ComponentI owner) throws Exception {

		// Owner doit respecter le contrat IDDSNode
		assert owner instanceof IDDSNode;
		super.installOn(owner);
		this.addOfferedInterface(WriteCI.class);
		this.addOfferedInterface(ReadCI.class);
		this.addOfferedInterface(ConnectClient.class);
	}

	@Override
	public void initialise() throws Exception {

		super.initialise();
		inPortConnectClient = new InPortConnectClient(uriConnectClient, getOwner(), getPluginURI());
		inPortConnectClient.publishPort();

		inPortRead = new InPortRead<T>(getOwner(), getPluginURI());
		inPortRead.publishPort();

		inPortWrite = new InPortWrite<T>(getOwner(), getPluginURI());
		inPortWrite.publishPort();

	}

	@Override
	public void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public void uninstall() throws Exception {
		inPortConnectClient.unpublishPort();

		inPortRead.unpublishPort();
		inPortWrite.unpublishPort();

		inPortConnectClient.destroyPort();

		inPortRead.destroyPort();
		inPortWrite.destroyPort();

		super.uninstall();
	}

}
