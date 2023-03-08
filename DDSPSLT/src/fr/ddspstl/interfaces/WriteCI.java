package fr.ddspstl.interfaces;

import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface WriteCI<T> extends RequiredCI,OfferedCI{
	public void write(Topic<T> topic,T data) throws Exception ;
}
