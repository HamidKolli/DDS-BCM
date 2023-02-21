package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface OutWrite extends RequiredCI{
	public String getDataWriter(String topic) throws Exception ;
	public <T> void write(String reader,T data) throws Exception ;
}
