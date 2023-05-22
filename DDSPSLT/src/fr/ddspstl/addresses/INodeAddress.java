package fr.ddspstl.addresses;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 *
 * Interface implantée par NodeAdress
 */

public interface INodeAddress {
	
	/**
	 * Getter
	 * 
	 * @return String : l'URI du noeud
	 */
	public String getNodeURI();
	/**
	 * Getter
	 * 
	 * @return String : le PropagationUri
	 */
	public String getPropagationURI();
	/**
	 * Getter
	 * 
	 * @return String : le PropagationLockUri
	 */
	public String getPropagationLockURI();
	/**
	 * Getter
	 * 
	 * @return String : l'URI du client
	 */
	public String getClientUri();
	/**
	 * Getter
	 * 
	 * @return String : le ReadURI
	 */
	public String getReadURI();
	/**
	 * Getter
	 * 
	 * @return String : le WriteURI
	 */
	public String getWriteURI();
	
}
