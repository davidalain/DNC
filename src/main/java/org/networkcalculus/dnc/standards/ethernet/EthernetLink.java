package org.networkcalculus.dnc.standards.ethernet;

import java.security.InvalidParameterException;
import java.util.Objects;

import org.apache.commons.math3.util.Pair;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetLink extends Pair<EthernetInterface,EthernetInterface>{

	public EthernetLink(EthernetInterface interfaceA, EthernetInterface interfaceB) {
		super(interfaceA, interfaceB);

		if(Objects.equals(interfaceA, interfaceB))
			throw new InvalidParameterException("interfaces must be different");
		if(interfaceA == null)
			throw new InvalidParameterException("interfaceA must not be null");
		if(interfaceB == null)
			throw new InvalidParameterException("interfaceB must not be null");
		if(!Objects.equals(interfaceA.getInterfaceSpeed(), interfaceB.getInterfaceSpeed()))
			throw new InvalidParameterException("interfaces' speed must be equals");
		
	}
	
	/**
	 * Based on this link, this method returns neighbor device of 'deviceRef' 
	 * 
	 * @param deviceRef
	 * @return
	 */
	public EthernetDevice getNeighbor(EthernetDevice deviceRef) {
		
		if(this.getFirst().getEthernetDeviceOwner().equals(deviceRef)) {
			return this.getSecond().getEthernetDeviceOwner();
		}
		
		if(this.getSecond().getEthernetDeviceOwner().equals(deviceRef)) {
			return this.getFirst().getEthernetDeviceOwner();
		}
		
		return null;
	}

}
