package fr.ddspstl.cvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.Topic;

import fr.ddspstl.DDS.data.Datas;
import fr.ddspstl.components.ClientReadComponent;
import fr.ddspstl.components.ClientWriteComponent;
import fr.ddspstl.components.DDSNode;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

	public CVM() throws Exception {
		super();
	}

	@Override
	public void deploy() throws Exception {

		String topicName = "Hello";
		ServiceEnvironment se = ServiceEnvironment.createInstance(null);
		DomainParticipant dp = new fr.ddspstl.DDS.Domain.DomainParticipant(1, se);
		Topic<String> topic = dp.createTopic(topicName, String.class);
		
		Map<Topic<String>,String> topicId = new HashMap<>();
		topicId.put(topic, topicName);
		
		Map<Topic<String>,Datas<String>> datas = new HashMap<>();
		datas.put(topic, new Datas<>(topic));
		List<String> uris = new ArrayList<>();
		uris.add(AbstractPort.generatePortURI());
		uris.add(AbstractPort.generatePortURI());
		uris.add(AbstractPort.generatePortURI());

		List<String> urisForClient = new ArrayList<>();
		uris.add(AbstractPort.generatePortURI());
		uris.add(AbstractPort.generatePortURI());
		uris.add(AbstractPort.generatePortURI());

		for (int i = 0; i < uris.size(); i++) {
			String tmp = uris.get(i);
			uris.remove(uris.get(i));
			List<String> urisTmp = new ArrayList<>(uris);
			AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
					new Object[] { 1, 0, tmp, urisForClient.get(i), urisTmp, dp,datas,topicId });
			uris.add(i, tmp);
		}

		AbstractComponent.createComponent(ClientReadComponent.class.getCanonicalName(),
				new Object[] { 1, 0, urisForClient.get(2) });
		
		AbstractComponent.createComponent(ClientWriteComponent.class.getCanonicalName(),
				new Object[] { 1, 0, urisForClient.get(0) });
		
		

		super.deploy();
	}

	public static void main(String[] args) {
		CVM cvm;
		try {
			cvm = new CVM();
			cvm.startStandardLifeCycle(20000L);
			Thread.sleep(1000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
