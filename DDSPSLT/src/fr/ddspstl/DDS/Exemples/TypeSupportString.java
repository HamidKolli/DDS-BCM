package fr.ddspstl.DDS.Exemples;

import org.omg.dds.core.ServiceEnvironment;

public class TypeSupportString extends org.omg.dds.type.TypeSupport<String> {

	private ServiceEnvironment serviceEnvironment;
	private String data;

	
	
	public TypeSupportString(ServiceEnvironment serviceEnvironment, String data) {
		super();
		this.serviceEnvironment = serviceEnvironment;
		this.data = data;
	}

	@Override
	public ServiceEnvironment getEnvironment() {
		return serviceEnvironment;
	}

	@Override
	public String newData() {
		return new String(data);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String getTypeName() {
		return getType().getName();
	}

}
