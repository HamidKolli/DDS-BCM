package fr.ddspstl.addresses;


/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * Classe s'occupant des différents URI liés à un noeud DDS
 */
public class NodeAddress implements INodeAddress {

	private String nodeURI;
	private String propagationUri;
	private String propagationLockURI;
	private String clientURI;
	private String readURI;
	private String writeURI;

	/**
	 * Constructeur
	 * 
	 * 
	 * @param nodeURI : L'uri du noeud
	 * @param propagationUri : L'uri de la propagation
	 * @param propagationLockURI : L'uri de la propagation du lock
	 * @param clientURI : L'uri du client
	 * @param readURI : L'uri du read
	 * @param writeURI : L'uri du write
	 */
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

	
	/**
	 * 
	 * @see fr.ddspstl.addresses.INodeAddress#getNodeURI()
	 *
	 */
	@Override
	public String getNodeURI() {
		return nodeURI;
	}

	/**
	 * 
	 * @see fr.ddspstl.addresses.INodeAddress#getPropagationURI()
	 *
	 */
	@Override
	public String getPropagationURI() {
		return propagationUri;
	}

	/**
	 * 
	 * @see fr.ddspstl.addresses.INodeAddress#getPropagationLockURI()
	 *
	 */
	@Override
	public String getPropagationLockURI() {
		return propagationLockURI;
	}

	/**
	 * 
	 * @see fr.ddspstl.addresses.INodeAddress#getClientUri()
	 *
	 */
	@Override
	public String getClientUri() {
		return clientURI;
	}

	/**
	 * 
	 * @see fr.ddspstl.addresses.INodeAddress#getReadURI()
	 *
	 */
	@Override
	public String getReadURI() {
		return readURI;
	}

	/**
	 * 
	 * @see fr.ddspstl.addresses.INodeAddress#getWriteURI()
	 *
	 */
	@Override
	public String getWriteURI() {
		return writeURI;
	}

}
