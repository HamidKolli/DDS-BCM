package fr.ddspstl.addresses;

public interface INodeAddress {
	
	public String getNodeURI();
	public String getPropagationURI();
	public String getPropagationLockURI();
	public String getClientUri();
	public String getReadURI();
	public String getWriteURI();
	
}
