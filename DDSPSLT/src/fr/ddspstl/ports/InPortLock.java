package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.PropagationLock;
import fr.ddspstl.plugin.LockPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe InPortLock
 */
public class InPortLock extends AbstractInboundPort implements PropagationLock {

	/**
	 * serialVersionUID : de type long gere
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur
	 * 
	 * @param uri : l'uri du port
	 * @param owner : l'owner du port
	 * @param pluginURI : l'uri du plugin
	 * @param executorServiceURI : l'uri de l'executor service
	 * @throws Exception
	 */
	public InPortLock(String uri, ComponentI owner, String pluginURI, String executorServiceURI) throws Exception {
		super(uri, PropagationLock.class, owner, pluginURI, executorServiceURI);
	}

	/**
	 * @see fr.ddspstl.interfaces.PropagationLock#lock(TopicDescription, String, Time)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> boolean lock(TopicDescription<T> topic, String idPropagation,Time timestamp) throws Exception {
		return getOwner().handleRequest(getExecutorServiceIndex(), (e) -> {
			try {
				return ((LockPlugin)getOwnerPlugin(pluginURI)).propagateLock(topic, idPropagation,timestamp);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		});

	}

	/**
	 * @see fr.ddspstl.interfaces.PropagationLock#unlock(TopicDescription, String, String)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void unlock(TopicDescription<T> topic, String idPropagation,String idPropagationUnlock) throws Exception {
		getOwner().handleRequest(getExecutorServiceIndex(), (e) -> {
			try {
				((LockPlugin)getOwnerPlugin(pluginURI)).propagateUnlock(topic,idPropagation,idPropagationUnlock);
				return null;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return e;
			
		});

	}

}