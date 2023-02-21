package fr.ddspstl.interfaces;

import org.omg.dds.sub.Sample.Iterator;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface OutRead extends RequiredCI{
	public String getDataReader(String topic)throws Exception ;
	public Iterator<?> read(String reader)throws Exception ;
}
