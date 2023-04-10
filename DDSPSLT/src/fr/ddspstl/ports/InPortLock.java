package fr.ddspstl.ports;

import org.omg.dds.core.Time;
import org.omg.dds.topic.TopicDescription;

import fr.ddspstl.interfaces.PropagationLock;
import fr.ddspstl.plugin.LockPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class InPortLock extends AbstractInboundPort implements PropagationLock {

	/**
	 * serialVersionUID : de type long gere
	 */
	private static final long serialVersionUID = 1L;

	public InPortLock(String uri, ComponentI owner, String pluginURI, String executorServiceURI) throws Exception {
		super(uri, PropagationLock.class, owner, pluginURI, executorServiceURI);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void lock(TopicDescription<T> topic, String idPropagation,Time timestamp) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((LockPlugin)getOwnerPlugin(pluginURI)).propagateLock(topic, idPropagation,timestamp);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void unlock(TopicDescription<T> topic) throws Exception {
		getOwner().runTask(getExecutorServiceIndex(), (e) -> {
			try {
				((LockPlugin)getOwnerPlugin(pluginURI)).propagateUnlock(topic);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

	}

}
