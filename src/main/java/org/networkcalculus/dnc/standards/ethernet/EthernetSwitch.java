package org.networkcalculus.dnc.standards.ethernet;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.standards.ethernet.EthernetConfig.EthernetInterfaceConfig;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetSwitch extends EthernetDevice{
	
	public EthernetSwitch(EthernetNetwork network, String deviceName, int interfacesCount, EthernetInterfaceConfig config) {
		super(network, deviceName);
		
		for(int i = 0 ; i < interfacesCount ; i++) {
			setServer(i, config);
		}
	}

	public EthernetSwitch(EthernetNetwork network, String deviceName, int interfacesCount, ServiceCurve service_curve, MaxServiceCurve max_service_curve) {
		super(network, deviceName);

		for(int i = 0 ; i < interfacesCount ; i++) {
			setServer(i, service_curve, max_service_curve);
		}
	}

	public EthernetSwitch(EthernetNetwork network, String deviceName, int interfacesCount, Pair<ServiceCurve, MaxServiceCurve> pairCurves) {
		super(network, deviceName);

		for(int i = 0 ; i < interfacesCount ; i++) {
			setServer(i, pairCurves.getFirst(), pairCurves.getSecond());
		}
	}

	public EthernetSwitch(EthernetNetwork network, String deviceName, List<Pair<ServiceCurve, MaxServiceCurve>> listPairCurves) {
		super(network, deviceName);

		int i = 0;
		for(Pair<ServiceCurve, MaxServiceCurve> pairCurves : listPairCurves) {
			setServer(i, pairCurves.getFirst(), pairCurves.getSecond());
			i++;
		}
	}

	public EthernetSwitch(EthernetNetwork network, String deviceName, List<ServiceCurve> listServiceCurve, List<MaxServiceCurve> listMaxServiceCurve) {
		super(network, deviceName);

		if(listServiceCurve.size() != listMaxServiceCurve.size())
			throw new InvalidParameterException("The size of ServiceCurve's list must be equals to the size of MaxServiceCurve's list");

		for(int i = 0 ; i < listServiceCurve.size() ; i++) {
			setServer(i, listServiceCurve.get(i), listMaxServiceCurve.get(i));
		}
	}

	public String toString() {
		return ("SW_"+this.deviceName);
	}

}
