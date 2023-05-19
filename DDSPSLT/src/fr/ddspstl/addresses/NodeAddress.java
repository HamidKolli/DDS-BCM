package fr.ddspstl.addresses;

public class NodeAddress implements INodeAddress {

	private String nodeURI;
	private String propagationUri;
	private String propagationLockURI;
	private String clientURI;
	private String readURI;
	private String writeURI;

	public NodeAddress(String nodeURI, String propagationUri, String propagationLockURI, String clientURI,
			String readURI, String writeURI) {
		super();
		this.nodeURI = nodeURI;
		this.propagationUri = propagationUri;
		this.propagationLockURI = propagationLockURI;
		this.writeURI = writeURI;
		this.readURI = readURI;
		this.clientURI = clientURI;
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

	@Override
	public String getClientUri() {
		return clientURI;
	}
	
	@Override
	public String getReadURI() {
		return readURI;
	}

	@Override
	public String getWriteURI() {
		return writeURI;
	}

}
