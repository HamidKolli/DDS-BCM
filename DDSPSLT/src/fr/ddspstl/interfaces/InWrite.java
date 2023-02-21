package fr.ddspstl.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface InWrite extends OfferedCI{
	public String getDataWriter(String topic)throws Exception;
	public <T> Void write(String writer,T data)throws Exception;
}
