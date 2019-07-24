package org.networkcalculus.dnc.standards.ethernet;

import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetEndSystem extends EthernetDevice{
	
	public EthernetEndSystem(EthernetNetwork network, String deviceName, EthernetInterfaceSpeed ethernetInterfaceSpeed) {
		super(network, deviceName);
		
		this.interfaces.put(0, new EthernetInterface(this, 0, ethernetInterfaceSpeed));
	}

	public String toString() {
		return (this.deviceName);
	}
	
}
