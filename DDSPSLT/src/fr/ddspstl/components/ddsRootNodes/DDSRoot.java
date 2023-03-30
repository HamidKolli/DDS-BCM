package fr.ddspstl.components.ddsRootNodes;

import java.util.Map;

import org.omg.dds.core.Time;
import org.omg.dds.topic.Topic;

import fr.ddspstl.addresses.INodeAddress;
import fr.ddspstl.addresses.NodeAddress;
import fr.ddspstl.components.interfaces.IDDSNode;
import fr.ddspstl.connectors.ConnectorConnectionDDS;
import fr.ddspstl.connectors.ConnectorPropagation;
import fr.ddspstl.ports.InConnectionDDS;
import fr.ddspstl.ports.InPortPropagation;
import fr.ddspstl.ports.OutConnectionDDS;
import fr.ddspstl.ports.OutPortPropagation;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

public class DDSRoot<T> extends AbstractComponent implements IDDSNode<T>{

	private INodeAddress nodeAddress;
	private String uriConnectDDSNode;
	private InConnectionDDS connectionDDS;
	private InPortPropagation<T> inPortPropagation;
	private Map<INodeAddress, OutConnectionDDS> connectionOutWithAddresses;
	private Map<INodeAddress, OutPortPropagation<T>> outPropagationPorts;
	protected DDSRoot(int nbThreads, int nbSchedulableThreads,String uriConnection) {
		super(nbThreads, nbSchedulableThreads);
		uriConnectDDSNode = uriConnection;
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		try {

			inPortPropagation = new InPortPropagation<T>(this);
			inPortPropagation.publishPort();

			connectionDDS = new InConnectionDDS(uriConnectDDSNode, this);
			connectionDDS.publishPort();

			nodeAddress = new NodeAddress(connectionDDS.getPortURI(), inPortPropagation.getPortURI());

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
		super.start();
	}
	
	@Override
	public synchronized void finalise() throws Exception {
		for (Map.Entry<INodeAddress, OutConnectionDDS> cp : connectionOutWithAddresses.entrySet()) {
			cp.getValue().doDisconnection();
		}
		for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
			cp.getValue().doDisconnection();
		}
		super.finalise();
	}
	
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			connectionDDS.unpublishPort();
			inPortPropagation.unpublishPort();
			inPortPropagation.destroyPort();
			connectionDDS.destroyPort();
			for (Map.Entry<INodeAddress, OutConnectionDDS> cp : connectionOutWithAddresses.entrySet()) {
				cp.getValue().unpublishPort();
				cp.getValue().destroyPort();
			}
			for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
				cp.getValue().unpublishPort();
				cp.getValue().destroyPort();
			}
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}

	@Override
	public void connect() throws Exception {		
	}

	@Override
	public void connectPropagation(INodeAddress address) throws Exception {
		
	}

	@Override
	public void connectBack(INodeAddress address) throws Exception {
		OutConnectionDDS cout = new OutConnectionDDS(this);
		this.doPortConnection(cout.getPortURI(), address.getNodeURI(), ConnectorConnectionDDS.class.getCanonicalName());
		connectionOutWithAddresses.put(address, cout);
		OutPortPropagation<T> outPropagation = new OutPortPropagation<T>(this);
		this.doPortConnection(outPropagation.getPortURI(), address.getPropagationURI(),
				ConnectorPropagation.class.getCanonicalName());
		outPropagationPorts.put(address, outPropagation);
		cout.connectPropagation(nodeAddress);
		
	}

	@Override
	public void disconnectBack(INodeAddress address) throws Exception {
		OutConnectionDDS out = connectionOutWithAddresses.remove(address);
		out.doDisconnection();
		out.unpublishPort();
		out.destroyPort();
		OutPortPropagation<T> pOut = outPropagationPorts.remove(address);
		pOut.doDisconnection();
		pOut.unpublishPort();
		pOut.destroyPort();
		
	}

	@Override
	public void disconnect(INodeAddress uri) throws Exception {
	}

	@Override
	public void propagerIn(T newObject, Topic<T> topic, String id,Time time) throws Exception {
		propagerOut(newObject, topic, id,time);
	}

	@Override
	public synchronized void propagerOut(T newObject, Topic<T> topic, String id,Time time) throws Exception {
		for (Map.Entry<INodeAddress, OutPortPropagation<T>> cp : outPropagationPorts.entrySet()) {
			cp.getValue().propager(newObject, topic, id,time);
		}	
	}

	@Override
	public void propager(T newObject, Topic<T> topic, String id,Time time) throws Exception {
	}
	
	
	
	

}
