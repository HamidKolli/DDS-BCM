package fr.ddspstl.cvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.Topic;

import fr.ddspstl.components.ClientReadComponent;
import fr.ddspstl.components.ClientWriteComponent;
import fr.ddspstl.components.ddsNodeRootSpider.DDSNode;
import fr.ddspstl.components.ddsNodeRootSpider.DDSRoot;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVMRoot extends AbstractCVM {

	public CVMRoot() throws Exception {
		super();
	}

	@Override
	public void deploy() throws Exception {

		String topicName = "Hello";
		DomainParticipant dp = new fr.ddspstl.DDS.Domain.DomainParticipant(1, null);
		Topic<String> topic = dp.createTopic(topicName, String.class);

		Map<Topic<String>, String> topicId = new HashMap<>();
		topicId.put(topic, topicName);

		Set<Topic<String>> topics = new HashSet<>();
		topics.add(topic);
		List<String> uris = new ArrayList<>();
		uris.add(AbstractPort.generatePortURI());
		uris.add(AbstractPort.generatePortURI());
		uris.add(AbstractPort.generatePortURI());

		List<String> urisForClient = new ArrayList<>();
		urisForClient.add(AbstractPort.generatePortURI());
		urisForClient.add(AbstractPort.generatePortURI());
		urisForClient.add(AbstractPort.generatePortURI());

		String uriRoot = AbstractPort.generatePortURI();

		AbstractComponent.createComponent(DDSRoot.class.getCanonicalName(), new Object[] { 1, 0, uriRoot });

		for (int i = 0; i < uris.size(); i++) {

			AbstractComponent.createComponent(DDSNode.class.getCanonicalName(),
					new Object[] { 1, 0, uris.get(i), urisForClient.get(i), uriRoot, topics, topicId });

		}

		AbstractComponent.createComponent(ClientReadComponent.class.getCanonicalName(),
				new Object[] { 1, 0, urisForClient.get(0), dp, topic.getName() });

		AbstractComponent.createComponent(ClientWriteComponent.class.getCanonicalName(),
				new Object[] { 1, 0, urisForClient.get(2), dp, topic.getName() });

		super.deploy();
	}

	public static void main(String[] args) {
		CVMRoot cvm;
		try {
			cvm = new CVMRoot();
			cvm.startStandardLifeCycle(20000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
