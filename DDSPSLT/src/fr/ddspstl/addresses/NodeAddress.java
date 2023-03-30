package fr.ddspstl.addresses;

public class NodeAddress implements INodeAddress{

	private String nodeURI;
	private String propagationUri;
	
	public NodeAddress(String nodeURI, String propagationUri) {
		super();
		this.nodeURI = nodeURI;
		this.propagationUri = propagationUri;
	}

	@Override
	public String getNodeURI() {
		return nodeURI;
	}

	@Override
	public String getPropagationURI() {
		return propagationUri;
	}

}
