package fr.ddspstl.DDS.Exemples;

import org.omg.dds.core.ServiceEnvironment;

/**
 * 
 * @author Hamid KOLLI
 * @author Yanis ALAYOUD
 * 
 *
 * Classe TypeSupportString
 */
public class TypeSupportString extends org.omg.dds.type.TypeSupport<String> {

	private ServiceEnvironment serviceEnvironment;
	private String data;

	
	/**
	 * Constructeur
	 * 
	 * @param serviceEnvironment : .
	 * @param data : la donnée
	 */
	public TypeSupportString(ServiceEnvironment serviceEnvironment, String data) {
		super();
		this.serviceEnvironment = serviceEnvironment;
		this.data = data;
	}

	/**
	 * @see org.omg.dds.type.TypeSupport#getEnvironment()
	 */
	@Override
	public ServiceEnvironment getEnvironment() {
		return serviceEnvironment;
	}

	/**
	 * @see org.omg.dds.type.TypeSupport#newData()
	 */
	@Override
	public String newData() {
		return new String(data);
	}

	/**
	 * @see org.omg.dds.type.TypeSupport#getType()
	 */
	@Override
	public Class<String> getType() {
		return String.class;
	}

	/**
	 * @see org.omg.dds.type.TypeSupport#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return getType().getName();
	}

	/**
	 * @see org.omg.dds.type.TypeSupport#equals(Object)
	 */
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