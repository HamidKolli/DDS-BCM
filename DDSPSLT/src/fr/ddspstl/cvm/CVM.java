package fr.ddspstl.cvm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.addresses.NodeAddress;
import fr.ddspstl.components.ClientComponent;
import fr.ddspstl.components.DDSNode;
import fr.ddspstl.topic.exemple.Note;
import fr.ddspstl.topic.exemple.Notification;
import fr.ddspstl.topic.exemple.Temperature;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 *         Classe CVM principale (le main en quelque sorte)
 */
public class CVM extends AbstractCVM {

	public static final String[] NOM_TOPIC = new String[] { "Temperatures", "Notification", "Notes" };
	public static final int NB_THREAD = 10;
	public static final int NB_THREAD_SCHED = 10;
	public static final int NB_THREAD_CLIENT = 10;
	public static final int NB_THREAD_SCHED_CLIENT = 10;

	public CVM() throws Exception {
		super();
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {

		DomainParticipant dp = new fr.ddspstl.DDS.Domain.DomainParticipant(1, null);
		Topic<Temperature> topic = dp.createTopic(NOM_TOPIC[0], Temperature.class);
		Topic<Notification> topic2 = dp.createTopic(NOM_TOPIC[1], Notification.class);
		Topic<Note> topic3 = dp.createTopic(NOM_TOPIC[2], Note.class);

		INodeAddress address1 = new NodeAddress(1 + "", AbstractPort.generatePortURI(), AbstractPort.generatePortURI(),
				AbstractPort.generatePortURI(), AbstractPort.generatePortURI(), AbstractPort.generatePortURI());
		INodeAddress address2 = new NodeAddress(2 + "", AbstractPort.generatePortURI(), AbstractPort.generatePortURI(),
				AbstractPort.generatePortURI(), AbstractPort.generatePortURI(), AbstractPort.generatePortURI());
		INodeAddress address3 = new NodeAddress(3 + "", AbstractPort.generatePortURI(), AbstractPort.generatePortURI(),
				AbstractPort.generatePortURI(), AbstractPort.generatePortURI(), AbstractPort.generatePortURI());
		INodeAddress address4 = new NodeAddress(4 + "", AbstractPort.generatePortURI(), AbstractPort.generatePortURI(),
				AbstractPort.generatePortURI(), AbstractPort.generatePortURI(), AbstractPort.generatePortURI());
		INodeAddress address5 = new NodeAddress(5 + "", AbstractPort.generatePortURI(), AbstractPort.generatePortURI(),
				AbstractPort.generatePortURI(), AbstractPort.generatePortURI(), AbstractPort.generatePortURI());

		Map<TopicDescription<?>, INodeAddress> root1 = new ConcurrentHashMap<>();
		root1.put(topic, address5);
		root1.put(topic2, address2);
		Map<TopicDescription<?>, INodeAddress> root2 = new ConcurrentHashMap<>();
		root2.put(topic2, address3);
		Map<TopicDescription<?>, INodeAddress> root3 = new ConcurrentHashMap<>();
		root3.put(topic2, address2);
		root3.put(topic3, address4);
		Map<TopicDescription<?>, INodeAddress> root4 = new ConcurrentHashMap<>();
		root4.put(topic3, address5);
		Map<TopicDescription<?>, INodeAddress> root5 = new ConcurrentHashMap<>();
		root5.put(topic3, address3);
		root5.put(topic, address1);

		Map<TopicDescription<?>, INodeAddress> toRoot1 = new ConcurrentHashMap<>();
		toRoot1.put(topic3, address5);
		Map<TopicDescription<?>, INodeAddress> toRoot2 = new ConcurrentHashMap<>();
		toRoot2.put(topic3, address5);
		toRoot2.put(topic, address1);
		Map<TopicDescription<?>, INodeAddress> toRoot3 = new ConcurrentHashMap<>();
		toRoot3.put(topic, address1);
		Map<TopicDescription<?>, INodeAddress> toRoot4 = new ConcurrentHashMap<>();
		toRoot4.put(topic, address1);
		toRoot4.put(topic2, address3);
		Map<TopicDescription<?>, INodeAddress> toRoot5 = new ConcurrentHashMap<>();
		toRoot5.put(topic2, address3);

		String uri = AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
				new Object[] { NB_THREAD, NB_THREAD_SCHED, address1, toRoot1, root1 });
		this.toggleLogging(uri);
		uri = AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
				new Object[] { NB_THREAD, NB_THREAD_SCHED, address2, toRoot2, root2 });
		this.toggleLogging(uri);
		uri = AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
				new Object[] { NB_THREAD, NB_THREAD_SCHED, address3, toRoot3, root3 });
		this.toggleLogging(uri);
		uri = AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
				new Object[] { NB_THREAD, NB_THREAD_SCHED, address4, toRoot4, root4 });
		this.toggleLogging(uri);
		uri = AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
				new Object[] { NB_THREAD, NB_THREAD_SCHED, address5, toRoot5, root5 });
		this.toggleLogging(uri);

		// Client
		uri = AbstractComponent.createComponent(ClientComponent.class.getCanonicalName(),
				new Object[] { NB_THREAD_CLIENT, NB_THREAD_SCHED_CLIENT, address1.getClientUri(), dp, false,
						topic.getName(), new Temperature("Salle de bain", 27) });
		this.toggleTracing(uri);

		uri = AbstractComponent.createComponent(ClientComponent.class.getCanonicalName(),
				new Object[] { NB_THREAD_CLIENT, NB_THREAD_SCHED_CLIENT, address5.getClientUri(), dp, false,
						topic.getName(), new Temperature("Salon", 20) });
		this.toggleTracing(uri);

		uri = AbstractComponent.createComponent(ClientComponent.class.getCanonicalName(),
				new Object[] { NB_THREAD_CLIENT, NB_THREAD_SCHED_CLIENT, address3.getClientUri(), dp, false,
						topic3.getName(), new Note(14, "28717594", "PSTL") });
		this.toggleTracing(uri);

		uri = AbstractComponent.createComponent(ClientComponent.class.getCanonicalName(),
				new Object[] { NB_THREAD_CLIENT, NB_THREAD_SCHED_CLIENT, address4.getClientUri(), dp, false,
						topic3.getName(), new Note(13, "21617590", "PSTL") });
		this.toggleTracing(uri);

		uri = AbstractComponent.createComponent(ClientComponent.class.getCanonicalName(),
				new Object[] { NB_THREAD_CLIENT, NB_THREAD_SCHED_CLIENT, address5.getClientUri(), dp, true,
						topic2.getName(), new Notification("Stom@", "salut") });
		this.toggleTracing(uri);

		uri = AbstractComponent.createComponent(ClientComponent.class.getCanonicalName(),
				new Object[] { NB_THREAD_CLIENT, NB_THREAD_SCHED_CLIENT, address5.getClientUri(), dp, true,
						topic2.getName(), new Notification("yaniword", "hello") });
		this.toggleTracing(uri);

		super.deploy();
	}

	public static void main(String[] args) {
		CVM cvm;
		try {
			cvm = new CVM();
			cvm.startStandardLifeCycle(20000L);
			Thread.sleep(200000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
