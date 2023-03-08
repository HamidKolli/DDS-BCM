package fr.ddspstl.DDS.publishers.interfaces;


import org.omg.dds.topic.Topic;

import fr.ddspstl.DDS.publishers.data.DataWriter;

public interface Publisher extends org.omg.dds.pub.Publisher{
	public <TYPE> DataWriter<TYPE> createDataWriter(Topic<TYPE> topic, String uriPortSortant) ;
}
