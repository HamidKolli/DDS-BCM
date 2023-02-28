package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface Propagation extends OfferedCI, RequiredCI{
	public <T> void propager(T newObject, String topic,String id) throws Exception;
}
