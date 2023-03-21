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

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TypeSupportString)) {
			return false;
		}
		TypeSupportString other = (TypeSupportString) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (serviceEnvironment == null) {
			if (other.serviceEnvironment != null) {
				return false;
			}
		} else if (!serviceEnvironment.equals(other.serviceEnvironment)) {
			return false;
		}
		return true;
	}
	
	

}
