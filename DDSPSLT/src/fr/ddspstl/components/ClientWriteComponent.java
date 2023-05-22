package fr.ddspstl.components;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;

import fr.ddspstl.DDS.publishers.interfaces.Publisher;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.PluginI;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * Sous-Composant de Client, représentant un ClientWriter
 */
public class ClientWriteComponent extends ClientComponent{

	private String topicName;
	private DataWriter<String> dataWriter;
	protected Publisher publisher;
	
	
	/**
	 * Constructeur
	 * 
	 * @param nbThreads : nb de threads
	 * @param nbSchedulableThreads : nb de chedulableThreads
	 * @param uriDDSNode : l'URI du noeud DDS auquel il est rattaché
	 * @param domainParticipant : Le domainParticipant
	 * @param topicName : Le nom du topic
	 * @throws Exception
	 */
	protected ClientWriteComponent(int nbThreads, int nbSchedulableThreads,String uriDDSNode,DomainParticipant domainParticipant,String topicName) throws Exception {
		super(nbThreads, nbSchedulableThreads,uriDDSNode,domainParticipant);
		this.topicName = topicName;
		this.publisher = (Publisher) domainParticipant.createPublisher();
	}
	
	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void start() throws ComponentStartException {
		org.omg.dds.topic.Topic<?> topic;
		try {
			topic = domainParticipant.findTopic(topicName, null);
			
			if(topic == null)
				throw new Exception("topic not found");
			dataWriter = (DataWriter<String>) ((Publisher)publisher).createDataWriter(topic,uriDDSNode);
			assert dataWriter != null;
			PluginI plugin  = (PluginI)dataWriter;
			plugin.setPluginURI(AbstractPort.generatePortURI());
			this.installPlugin(plugin);
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		
		super.start();
	}
	
	/**
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 *
	 */
	@Override
	public void execute() throws Exception {
		
		System.out.println("debut writer");
		
		
		Thread.sleep(2000L);

		System.out.println("j'ecrit");
		dataWriter.write("Hello");
		System.out.println("fin d'ecriture");
		super.execute();
	}

}
