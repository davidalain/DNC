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
public class EthernetConfig {

	/**
	 * Private static instance (singleton pattern)
	 */
	private static EthernetConfig instance = null;

	/**
	 * Possible Ethernet interfaces' configurations
	 */
	public enum EthernetInterfaceConfig{
		s_10Mbps,
		s_100Mbps,
		s_1000Mbps,
	}

	/**
	 * Map to link EthernetInterfaceConfig with its curves pair
	 */
	private final Map<EthernetInterfaceConfig, Pair<ServiceCurve, MaxServiceCurve>> mapCurves;

	/**
	 * Private constructor (singleton pattern)
	 */
	private EthernetConfig() {

		final Curve factory = Curve.getFactory();

		this.mapCurves = new HashMap<EthernetConfig.EthernetInterfaceConfig, Pair<ServiceCurve,MaxServiceCurve>>();

		//TODO: fix curves values
		final Pair<ServiceCurve, MaxServiceCurve> curve10Mbps = new Pair<ServiceCurve, MaxServiceCurve>(
				factory.createRateLatency(10.0e6, 0.01), 
				factory.createRateLatencyMSC(100.0e6, 0.001));
		this.mapCurves.put(EthernetInterfaceConfig.s_10Mbps, curve10Mbps);

		//TODO: fix curves values
		final Pair<ServiceCurve, MaxServiceCurve> curve100Mbps = new Pair<ServiceCurve, MaxServiceCurve>(
				factory.createRateLatency(10.0e6, 0.01), 
				factory.createRateLatencyMSC(100.0e6, 0.001));
		this.mapCurves.put(EthernetInterfaceConfig.s_100Mbps, curve100Mbps);

		//TODO: fix curves values
		final Pair<ServiceCurve, MaxServiceCurve> curve1000Mbps = new Pair<ServiceCurve, MaxServiceCurve>(
				factory.createRateLatency(10.0e6, 0.01), 
				factory.createRateLatencyMSC(100.0e6, 0.001));
		this.mapCurves.put(EthernetInterfaceConfig.s_1000Mbps, curve1000Mbps);
	}

	/**
	 * getInstance() method (singleton pattern)
	 * 
	 * @return EthernetConfig instance
	 */
	public static EthernetConfig getInstance() {
		
		if(instance == null) {
			instance = new EthernetConfig();
		}
		return instance;
	}

	/**
	 * Given a EthernetInterfaceConfig instance, this method returns a pair with ServiceCurve and MaxServiceCurve that they are used to create a Server.
	 * 
	 * @param ethernetSpeed
	 * @return
	 */
	public Pair<ServiceCurve,MaxServiceCurve> getCurves(EthernetConfig.EthernetInterfaceConfig ethernetSpeed){
		return this.mapCurves.get(ethernetSpeed);
	}

}
