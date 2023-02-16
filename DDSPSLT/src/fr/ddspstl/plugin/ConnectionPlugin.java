package fr.ddspstl.plugin;

import java.util.HashMap;
import java.util.Map;

import fr.ddspstl.components.DDSNode;
import fr.ddspstl.connectors.ConnectorConnectionDDS;
import fr.ddspstl.ports.InConnectionDDS;
import fr.ddspstl.ports.OutConnectionDDS;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;

public class ConnectionPlugin extends AbstractPlugin {

	private static final long serialVersionUID = 1L;

	private InConnectionDDS connectionDDS;
	private String uriConnection;
	private Map<String, OutConnectionDDS> connectionOut;
	
	public ConnectionPlugin(String uriConnection) {
		this.uriConnection = uriConnection;
		connectionOut = new HashMap<>();
	}

	@Override
	public void installOn(ComponentI owner) throws Exception {
		connectionDDS = new InConnectionDDS(uriConnection, owner);
		super.installOn(owner);
	}

	@Override
	public void initialise() throws Exception {
		connectionDDS.publishPort();
		super.initialise();
	}

	@Override
	public void finalise() throws Exception {
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			disconnect(cp.getKey(),cp.getValue());
		}
		super.finalise();
	}

	@Override
	public void uninstall() throws Exception {
		connectionDDS.unpublishPort();
		connectionDDS.destroyPort();
		for (Map.Entry<String, OutConnectionDDS> cp : connectionOut.entrySet()) {
			cp.getValue().unpublishPort();
			cp.getValue().destroyPort();
		}
		super.uninstall();
	}

	public void connect(String uri, int domainID) throws Exception {
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		this.getOwner().doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());
		cout.connect(connectionDDS.getPortURI(), domainID);
		connectionOut.put(uri, cout);
	}

	public void connectBack(String uri, int domainID) throws Exception{
		if(((DDSNode)getOwner()).getDomainId() != domainID) {
			throw new Exception("impossible de ce connecter, domaine different");
		}
		OutConnectionDDS cout = new OutConnectionDDS(this.getOwner());
		this.getOwner().doPortConnection(cout.getPortURI(), uri, ConnectorConnectionDDS.class.getCanonicalName());
		connectionOut.put(uri, cout);
	}

	public void disconnectBack(String uri)throws Exception {
		OutConnectionDDS out = connectionOut.get(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		connectionOut.remove(uri);
	}

	public void disconnect(String uri,OutConnectionDDS out) throws Exception{
		out.disconnect(uri);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		connectionOut.remove(uri);
	}

}
