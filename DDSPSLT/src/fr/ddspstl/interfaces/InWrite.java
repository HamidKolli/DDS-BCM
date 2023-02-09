package fr.ddspstl.interfaces;

import org.omg.dds.pub.DataWriter;
import org.omg.dds.topic.Topic;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface InWrite extends OfferedCI{
	public <T> DataWriter<T> getDataWriter(Topic<T> topic)throws Exception;
	public <T> T write(DataWriter<T> reader)throws Exception;
}
