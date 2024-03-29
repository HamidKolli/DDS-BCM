package fr.ddspstl.time;

import java.util.concurrent.TimeUnit;

import org.omg.dds.core.ModifiableTime;
import org.omg.dds.core.ServiceEnvironment;

public class Time extends org.omg.dds.core.Time{

	/**
	 * serialVersionUID : de type long gere
	 */
	private static final long serialVersionUID = 1L;
	
	private long timestamp;

	
	

	public Time(long timestamp) {
		super();
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(org.omg.dds.core.Time o) {
		assert o == null;
		if(o.getTime(TimeUnit.MICROSECONDS) > getTime(TimeUnit.MICROSECONDS))
			return -1;
		if(o.getTime(TimeUnit.MICROSECONDS) < getTime(TimeUnit.MICROSECONDS))
			return 1;
		
		return 0;
	}

	@Override
	public ServiceEnvironment getEnvironment() {
		return null;
	}

	@Override
	public long getTime(TimeUnit inThisUnit) {
		return timestamp;
	}

	@Override
	public long getRemainder(TimeUnit primaryUnit, TimeUnit remainderUnit) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ModifiableTime modifiableCopy() {
		// TODO Auto-generated method stub
		return null;
	}

}
