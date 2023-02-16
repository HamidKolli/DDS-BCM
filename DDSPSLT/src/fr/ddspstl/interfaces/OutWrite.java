package fr.ddspstl.interfaces;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface OutWrite extends RequiredCI{
	public <T> DataWriter<T> getDataWriter(Topic<T> topic) throws Exception ;
	public <T> void write(DataWriter<T> reader,T data) throws Exception ;
}
