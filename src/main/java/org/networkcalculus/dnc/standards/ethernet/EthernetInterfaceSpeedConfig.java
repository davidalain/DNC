package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;

/**
 * 
 * @author DavidAlain
 * 
 */
public class EthernetInterfaceSpeedConfig {

	/**
	 * Private static instance (singleton pattern)
	 */
	private static EthernetInterfaceSpeedConfig instance = null;

	/**
	 * Possible Ethernet's bandwidth configurations
	 */
	public enum EthernetInterfaceSpeed{
		s_10Mbps,
		s_100Mbps,
		s_1000Mbps,

//		s_switchingFabric_unlimited,
	}

	/**
	 * Map to link EthernetInterfaceConfig with its curves pair
	 */
	private final Map<EthernetInterfaceSpeed, Pair<ServiceCurve, MaxServiceCurve>> mapCurves;

	/**
	 * Private constructor (singleton pattern)
	 */
	private EthernetInterfaceSpeedConfig() {

		final Curve factory = Curve.getFactory();

		this.mapCurves = new HashMap<EthernetInterfaceSpeed, Pair<ServiceCurve,MaxServiceCurve>>();

		//TODO: fix curves values
		this.mapCurves.put(EthernetInterfaceSpeed.s_10Mbps,  new Pair<ServiceCurve, MaxServiceCurve>(
				factory.createRateLatency(10.0e6, 0.01), 
				factory.createRateLatencyMSC(100.0e6, 0.001)));

		//TODO: fix curves values
		this.mapCurves.put(EthernetInterfaceSpeed.s_100Mbps, new Pair<ServiceCurve, MaxServiceCurve>(
				factory.createRateLatency(10.0e6, 0.01), 
				factory.createRateLatencyMSC(100.0e6, 0.001)));

		//TODO: fix curves values
		this.mapCurves.put(EthernetInterfaceSpeed.s_1000Mbps, new Pair<ServiceCurve, MaxServiceCurve>(
				factory.createRateLatency(10.0e6, 0.01), 
				factory.createRateLatencyMSC(100.0e6, 0.001)));

//		this.mapCurves.put(EthernetInterfaceSpeed.s_switchingFabric_unlimited, new Pair<ServiceCurve, MaxServiceCurve>(
//				factory.createZeroDelayInfiniteBurst(), 
//				factory.createZeroDelayInfiniteBurstMSC()));
	}

	/**
	 * getInstance() method (singleton pattern)
	 * 
	 * @return EthernetConfig instance
	 */
	public static EthernetInterfaceSpeedConfig getInstance() {

		if(instance == null) {
			instance = new EthernetInterfaceSpeedConfig();
		}
		return instance;
	}

	/**
	 * Given a EthernetInterfaceConfig instance, this method returns a pair with ServiceCurve and MaxServiceCurve that they may be used to create a Server.
	 * 
	 * @param ethernetSpeed
	 * @return
	 */
	public Pair<ServiceCurve,MaxServiceCurve> getCurves(EthernetInterfaceSpeed ethernetSpeed){
		return this.mapCurves.get(ethernetSpeed);
	}

}
