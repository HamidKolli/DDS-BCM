package fr.ddspstl.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.DDS.data.Datas;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.exceptions.DDSTopicNotFoundException;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class DDSNode<T> extends AbstractComponent implements IDDSNode<T> {

	private DDSPlugin<T> plugin;
	private List<String> uriDDSNodes;
	// DDS
	private Map<Topic<T>, Datas<T>> datas;
	private Map<Topic<T>, String> topicID;

	protected DDSNode(int nbThreads, int nbSchedulableThreads, String uriConnectDDSNode,
			List<String> uriDDSNodes, Map<Topic<T>, Datas<T>> datas, Map<Topic<T>, String> topicID) throws Exception {
		super(nbThreads, nbSchedulableThreads);

		this.datas = new HashMap<>(datas);
		this.topicID = new HashMap<>(topicID);
		this.uriDDSNodes = new ArrayList<String>(uriDDSNodes);

		plugin = new DDSPlugin<T>(uriConnectDDSNode);

	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {

			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}

	@Override
	public void disconnectClient(String dataReader, String dataWriter) {

	}

	@Override
	public void execute() throws Exception {
		for (String uri : uriDDSNodes) {
			this.plugin.connect(uri);
		}
		System.out.println("connexion fini ");
		super.execute();
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> read(TopicDescription<T> topic) throws DDSTopicNotFoundException {
		return datas.get(topic).read();
	}

	public void write(Topic<T> topic, T data) throws Exception {
		Datas<T> dt = datas.get(topic);
		dt.write(data);
		propager(data, topic, AbstractPort.generatePortURI());
	}

	public void propager(T newObject, Topic<T> topicName, String id) throws Exception {
		if (topicID.get(topicName) == null)
			return;
		if (topicID.get(topicName).equals(id))
			return;

		topicID.put(topicName, id);
		datas.get(topicName).write(newObject);
		plugin.propagerOut(newObject, topicName, id);

	}

}
