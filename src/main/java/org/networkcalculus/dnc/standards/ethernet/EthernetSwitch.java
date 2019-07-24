package org.networkcalculus.dnc.standards.ethernet;

import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetSwitch extends EthernetDevice{

	protected Server switchingFabricServer;

	public EthernetSwitch(EthernetNetwork network, String deviceName, int interfacesCount, EthernetInterfaceSpeed interfaceSpeed) {
		super(network, deviceName);

		this.switchingFabricServer = this.network.getServerGraph().addServer(
				Curve.getFactory().createZeroDelayInfiniteBurst(), 
				Curve.getFactory().createZeroDelayInfiniteBurstMSC());

		for(int i = 0 ; i < interfacesCount ; i++) {
			this.interfaces.put(i, new EthernetInterface(this, i, interfaceSpeed));
		}
	}
	
	public String toString() {
		return (this.deviceName);
	}

}
