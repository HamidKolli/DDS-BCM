package fr.ddspstl.interfaces;

import org.omg.dds.sub.DataReader;
import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface OutRead extends RequiredCI{
	public <T> DataReader<T> getDataReader(Topic<T> topic)throws Exception ;
	public <T> T read(DataReader<T> reader)throws Exception ;
}
