package fr.ddspstl.DDS.data;

import org.omg.dds.core.Time;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.ViewState;
import org.omg.dds.topic.Topic;

public class Datas<T>  {
	private Sample.Iterator<T> samplesData;
	private Sample.Iterator<T> newSamplesData;
	private Topic<T> topic;

	
	public Datas(Topic<T> topic) {
		super();
	
		this.topic = topic;
		this.samplesData = new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
		this.newSamplesData = new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
	}

	public Sample.Iterator<T> read() {
		return samplesData;
	}

	public Sample.Iterator<T> take() {
		Sample.Iterator<T> tmp = newSamplesData;
		newSamplesData =  new fr.ddspstl.DDS.samples.Sample.Iterator<T>();
		return tmp;
	}
	public void write(T data, Time time) {
		samplesData.add(new fr.ddspstl.DDS.samples.Sample<T>(topic.getEnvironment(), data, SampleState.READ, ViewState.NEW,
				InstanceState.ALIVE, time));
		newSamplesData.add(new fr.ddspstl.DDS.samples.Sample<T>(topic.getEnvironment(), data, SampleState.READ, ViewState.NEW,
				InstanceState.ALIVE, time));
	}

	
}
