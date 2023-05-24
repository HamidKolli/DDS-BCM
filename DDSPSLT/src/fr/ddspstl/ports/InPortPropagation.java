package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.Propagation;
import fr.ddspstl.plugin.DDSPlugin;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe InPortPropagation
 */
public class InPortPropagation extends AbstractInboundPort implements Propagation {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param uri : l'uri du port
	 * @param owner : l'owner du port
	 * @param pluginURI : l'uri du plugin
	 * @param executorServicePropagationURI : l'uri de l'executor service
	 * @throws Exception
	 */
	public InPortPropagation(String uri,ComponentI owner,String pluginURI ,String executorServicePropagationURI) throws Exception {
		super(uri,Propagation.class, owner, pluginURI, executorServicePropagationURI);
	}

	/**
	 * @see fr.ddspstl.interfaces.Propagation#propager(Object, TopicDescription, String, Time)
	 */
	@Override
	public <T> void propager(T newObject, TopicDescription<T> topicName, String id, Time time) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					((DDSPlugin) getOwnerPlugin(getPluginURI())).propagerIn(newObject, topicName, id, time);
				} catch (Exception e) {
				}

			}
		});
	}

	/**
	 * @see fr.ddspstl.interfaces.Propagation#consommer(TopicDescription, String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Iterator<T> consommer(TopicDescription<T> topic, String id, boolean isFirst) throws Exception {
		return (Iterator<T>) getOwner().handleRequest(getExecutorServiceIndex(), (e) -> {
			return ((DDSPlugin) getOwnerPlugin(getPluginURI())).consommer(topic, id, isFirst);
		});

	}

}
