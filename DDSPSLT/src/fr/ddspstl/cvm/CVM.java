package fr.ddspstl.cvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		DomainParticipant dp = new fr.ddspstl.DDS.Domain.DomainParticipant(1, null);
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
		urisForClient.add(AbstractPort.generatePortURI());
		urisForClient.add(AbstractPort.generatePortURI());
		urisForClient.add(AbstractPort.generatePortURI());

		for (int i = 0; i < uris.size(); i++) {
			String tmp = uris.get(i);
			uris.remove(uris.get(i));
			List<String> urisTmp = new ArrayList<>(uris);
			AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
					new Object[] { 1, 0, tmp, urisForClient.get(i),urisTmp,datas,topicId });
			
			uris.add(i, tmp);
		}

		AbstractComponent.createComponent(ClientReadComponent.class.getCanonicalName(),
				new Object[] { 1, 0,urisForClient.get(0),dp,topic.getName() });
		
		AbstractComponent.createComponent(ClientWriteComponent.class.getCanonicalName(),
				new Object[] { 1, 0,urisForClient.get(2),dp,topic.getName() });
		
		

		super.deploy();
	}

	public static void main(String[] args) {
		CVM cvm;
		try {
			cvm = new CVM();
			cvm.startStandardLifeCycle(200000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
