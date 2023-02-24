package fr.ddspstl.DDS.samples;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.omg.dds.core.ModifiableInstanceHandle;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.Time;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.ViewState;

public class Sample<T> implements org.omg.dds.sub.Sample<T> {

	private static final long serialVersionUID = 1L;

	private ServiceEnvironment serviceEnvironment;
	private T data;
	private SampleState sampleState;
	private ViewState viewState;
	private InstanceState instanceState;
	private Time time;

	public Sample(ServiceEnvironment serviceEnvironment, T data) {
		super();
		this.serviceEnvironment = serviceEnvironment;
		this.data = data;
	}

	public Sample(ServiceEnvironment serviceEnvironment, T data, SampleState sampleState, ViewState viewState,
			InstanceState instanceState, Time time2) {
		super();
		this.serviceEnvironment = serviceEnvironment;
		this.data = data;
		this.sampleState = sampleState;
		this.viewState = viewState;
		this.instanceState = instanceState;
		this.time = time2;
	}

	@Override
	public ServiceEnvironment getEnvironment() {
		return serviceEnvironment;
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public SampleState getSampleState() {
		return sampleState;
	}

	@Override
	public ViewState getViewState() {
		return viewState;
	}

	@Override
	public InstanceState getInstanceState() {
		return instanceState;
	}

	@Override
	public Time getSourceTimestamp() {
		return time;
	}

	@Override
	public ModifiableInstanceHandle getInstanceHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModifiableInstanceHandle getPublicationHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDisposedGenerationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNoWritersGenerationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSampleRank() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGenerationRank() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAbsoluteGenerationRank() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Sample<T> clone() {
		return new Sample<T>(serviceEnvironment, data,sampleState,viewState,instanceState,time);
	}

	public static class Iterator<T> implements org.omg.dds.sub.Sample.Iterator<T> {

		private LinkedList<org.omg.dds.sub.Sample<T>> list;
		private boolean close;
		

		public Iterator() {
			super();
			list = new LinkedList<>();
			close = false;
		}

		@Override
		public boolean hasNext() {
			return list.listIterator().hasNext() && !close;
		}

		@Override
		public boolean hasPrevious() {
			return list.listIterator().hasPrevious() && !close;
		}

		@Override
		public org.omg.dds.sub.Sample<T> next() {
			if(close)
				return null;
			return list.listIterator().next();
		}

		@Override
		public int nextIndex() {
			if(close)
				return -1;
			return list.listIterator().nextIndex();
		}

		@Override
		public org.omg.dds.sub.Sample<T> previous() {
			if(close)
				return null;
			return list.listIterator().previous();
		}

		@Override
		public int previousIndex() {
			if(close)
				return -1;
			return list.listIterator().previousIndex();
		}

		@Override
		public void close() throws IOException {
			close = true;
		}

		@Override
		public void remove() {
			list.clear();
		}

		@Override
		public void set(org.omg.dds.sub.Sample<T> o) {
			list.set(0, o);
		}

		@Override
		public void add(org.omg.dds.sub.Sample<T> o) {
			list.add(o);
		}

	}
}
