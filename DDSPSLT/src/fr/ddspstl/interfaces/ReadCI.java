package fr.ddspstl.interfaces;

import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.topic.TopicDescription;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ReadCI<TYPE> extends RequiredCI,OfferedCI{
	public  Iterator<TYPE> read(TopicDescription<TYPE> topic)throws Exception ;
}
