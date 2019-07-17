package org.networkcalculus.dnc.standards.ethernet;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetSwitch extends EthernetGenericDevice{

	public EthernetSwitch(ServerGraph serverGraph, String deviceName, int interfacesCount, ServiceCurve service_curve, MaxServiceCurve max_service_curve) {
		super(serverGraph, deviceName);

		for(int i = 0 ; i < interfacesCount ; i++) {
			setServer(i, service_curve, max_service_curve);
		}
	}

	public EthernetSwitch(ServerGraph serverGraph, String deviceName, int interfacesCount, Pair<ServiceCurve, MaxServiceCurve> pairCurves) {
		super(serverGraph, deviceName);

		for(int i = 0 ; i < interfacesCount ; i++) {
			setServer(i, pairCurves.getFirst(), pairCurves.getSecond());
		}
	}

	public EthernetSwitch(ServerGraph serverGraph, String deviceName, List<Pair<ServiceCurve, MaxServiceCurve>> listPairCurves) {
		super(serverGraph, deviceName);

		int i = 0;
		for(Pair<ServiceCurve, MaxServiceCurve> pairCurves : listPairCurves) {
			setServer(i, pairCurves.getFirst(), pairCurves.getSecond());
			i++;
		}
	}

	public EthernetSwitch(ServerGraph serverGraph, String deviceName, List<ServiceCurve> listServiceCurve, List<MaxServiceCurve> listMaxServiceCurve) {
		super(serverGraph, deviceName);

		if(listServiceCurve.size() != listMaxServiceCurve.size())
			throw new InvalidParameterException("The size of ServiceCurve's list must be equals to the size of MaxServiceCurve's list");

		for(int i = 0 ; i < listServiceCurve.size() ; i++) {
			setServer(i, listServiceCurve.get(i), listMaxServiceCurve.get(i));
		}
	}

}
