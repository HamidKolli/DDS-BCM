package fr.ddspstl.DDS.data;

import org.omg.dds.core.Time;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.ViewState;
import org.omg.dds.topic.Topic;

public class Datas<T>  {
	private Sample.Iterator<T> samplesData;
	private Topic<T> topic;

	
	public Datas(Topic<T> topic) {
		super();
	
		this.topic = topic;
		this.samplesData = new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
	}

	public Sample.Iterator<T> read() {
	
		return samplesData;
	}

	public void write(T data) {
		samplesData.add(new fr.ddspstl.DDS.samples.Sample<T>(topic.getEnvironment(), data));
	}

	public void write(T data, Time time) {
		samplesData.add(new fr.ddspstl.DDS.samples.Sample<T>(topic.getEnvironment(), data, SampleState.READ, ViewState.NEW,
				InstanceState.ALIVE, time));
	}

	
}
