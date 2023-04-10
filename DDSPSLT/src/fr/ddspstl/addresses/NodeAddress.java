package fr.ddspstl.addresses;

public class NodeAddress implements INodeAddress{

	private String nodeURI;
	private String propagationUri;
	private String propagationLockURI;
	
	public NodeAddress(String nodeURI, String propagationUri,String propagationLockURI) {
		super();
		this.nodeURI = nodeURI;
		this.propagationUri = propagationUri;
		this.propagationLockURI =propagationLockURI;
	}

	@Override
	public String getNodeURI() {
		return nodeURI;
	}

	@Override
	public String getPropagationURI() {
		return propagationUri;
	}

	@Override
	public String getPropagationLockURI() {

		return propagationLockURI;
	}

}
