package org.networkcalculus.dnc.standards.ethernet;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.standards.ethernet.EthernetConfig.EthernetInterfaceConfig;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetEndSystem extends EthernetDevice{

	public EthernetEndSystem(EthernetNetwork network, String deviceName, ServiceCurve service_curve, MaxServiceCurve max_service_curve) {
		super(network, deviceName);

		//Only one interface and only one (output) Server
		setServer(0, service_curve, max_service_curve);
	}

	public EthernetEndSystem(EthernetNetwork network, String deviceName, EthernetInterfaceConfig ethernetSpeed) {
		this(network, deviceName, EthernetConfig.getInstance().getCurves(ethernetSpeed));
	}

	public EthernetEndSystem(EthernetNetwork network, String deviceName, Pair<ServiceCurve, MaxServiceCurve> pairCurves) {
		this(network, deviceName, pairCurves.getFirst(), pairCurves.getSecond());
	}

	public String toString() {
		return ("ES_"+this.deviceName);
	}
	
}
