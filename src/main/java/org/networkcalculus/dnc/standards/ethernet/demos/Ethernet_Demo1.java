package org.networkcalculus.dnc.standards.ethernet.demos;

import java.util.Map;

import org.networkcalculus.dnc.AnalysisConfig;
import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.network.server_graph.Flow;
import org.networkcalculus.dnc.network.server_graph.Turn;
import org.networkcalculus.dnc.standards.ethernet.EthernetDevice;
import org.networkcalculus.dnc.standards.ethernet.EthernetEndSystem;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterface;
import org.networkcalculus.dnc.standards.ethernet.EthernetNetwork;
import org.networkcalculus.dnc.standards.ethernet.EthernetSwitch;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;
import org.networkcalculus.dnc.tandem.analyses.PmooAnalysis;
import org.networkcalculus.dnc.tandem.analyses.SeparateFlowAnalysis;
import org.networkcalculus.dnc.tandem.analyses.TandemMatchingAnalysis;
import org.networkcalculus.dnc.tandem.analyses.TotalFlowAnalysis;

public class Ethernet_Demo1 {

	public static void main(String[] args) throws Exception {

		final EthernetNetwork ethernetNetwork = new EthernetNetwork();

		final EthernetEndSystem es1 = new EthernetEndSystem(ethernetNetwork, "ES1", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem es2 = new EthernetEndSystem(ethernetNetwork, "ES2", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetSwitch sw1 = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceSpeed.s_100Mbps);

		es1.setNeighbor(0, sw1, 0);
		es2.setNeighbor(0, sw1, 1);

		printInfo(es1);
		printInfo(es2);
		printInfo(sw1);

		ethernetNetwork.addFlow(Curve.getFactory().createTokenBucket(2e6, 0), es1, es2);
		ethernetNetwork.addFlow(Curve.getFactory().createTokenBucket(1e6, 1500), es1, es2);
		ethernetNetwork.addFlow(Curve.getFactory().createTokenBucket(10e6, 1500), es1, es2);

		performAnalysis(ethernetNetwork);
	}
	
	private static void performAnalysis(EthernetNetwork ethernetNetwork) {
		
		AnalysisConfig configuration = new AnalysisConfig();

		for(Flow flow_of_interest : ethernetNetwork.getFlows()) {

			System.out.println("Flow of interest : " + flow_of_interest.toString());
			System.out.println();

			// Analyze the network
			// TFA
			System.out.println("--- Total Flow Analysis ---");
			TotalFlowAnalysis tfa = new TotalFlowAnalysis(ethernetNetwork.getServerGraph(), configuration);

			try {
				tfa.performAnalysis(flow_of_interest);
				System.out.println("delay bound     : " + tfa.getDelayBound());
				System.out.println("     per server : " + tfa.getServerDelayBoundMapString());
				System.out.println("backlog bound   : " + tfa.getBacklogBound());
				System.out.println("     per server : " + tfa.getServerBacklogBoundMapString());
				System.out.println("alpha per server: " + tfa.getServerAlphasMapString());
			} catch (Exception e) {
				System.out.println("TFA analysis failed");
				e.printStackTrace();
			}

			System.out.println();

			// SFA
			System.out.println("--- Separated Flow Analysis ---");
			SeparateFlowAnalysis sfa = new SeparateFlowAnalysis(ethernetNetwork.getServerGraph(), configuration);

			try {
				sfa.performAnalysis(flow_of_interest);
				System.out.println("e2e SFA SCs     : " + sfa.getLeftOverServiceCurves());
				System.out.println("     per server : " + sfa.getServerLeftOverBetasMapString());
				System.out.println("xtx per server  : " + sfa.getServerAlphasMapString());
				System.out.println("delay bound     : " + sfa.getDelayBound());
				System.out.println("backlog bound   : " + sfa.getBacklogBound());
			} catch (Exception e) {
				System.out.println("SFA analysis failed");
				e.printStackTrace();
			}

			System.out.println();

			// PMOO
			System.out.println("--- PMOO Analysis ---");
			PmooAnalysis pmoo = new PmooAnalysis(ethernetNetwork.getServerGraph(), configuration);

			try {
				pmoo.performAnalysis(flow_of_interest);
				System.out.println("e2e PMOO SCs    : " + pmoo.getLeftOverServiceCurves());
				System.out.println("xtx per server  : " + pmoo.getServerAlphasMapString());
				System.out.println("delay bound     : " + pmoo.getDelayBound());
				System.out.println("backlog bound   : " + pmoo.getBacklogBound());
			} catch (Exception e) {
				System.out.println("PMOO analysis failed");
				e.printStackTrace();
			}

			System.out.println();

			// TMA
			System.out.println("--- Tandem Matching Analysis ---");
			TandemMatchingAnalysis tma = new TandemMatchingAnalysis(ethernetNetwork.getServerGraph(), configuration);

			try {
				tma.performAnalysis(flow_of_interest);
				System.out.println("e2e TMA SCs     : " + tma.getLeftOverServiceCurves());
				System.out.println("xtx per server  : " + tma.getServerAlphasMapString());
				System.out.println("delay bound     : " + tma.getDelayBound());
				System.out.println("backlog bound   : " + tma.getBacklogBound());
			} catch (Exception e) {
				System.out.println("TMA analysis failed");
				e.printStackTrace();
			}
			
			System.out.println();
			System.out.println();

		}
		
	}

	private static void printInfo(EthernetDevice ethernetDevice) {

		System.out.println();

		System.out.println(ethernetDevice.getDeviceName() + " neigborhood:");
		for(EthernetDevice device : ethernetDevice.getNeighbors()) {
			System.out.println(device);
		}
		System.out.println();

		//		System.out.println(ethernetDevice.getDeviceName() + " neigborhood (interface vs neighbor):");
		//		for(Map.Entry<EthernetInterface, EthernetDevice> entry : ethernetDevice.getNeighborhood().entrySet()) {
		//			System.out.println("{interface=" + entry.getKey() + ", neighbour=" + entry.getValue() + "}");
		//		}
		//		System.out.println();

		System.out.println(ethernetDevice.getDeviceName() + " servers:");
		for(EthernetInterface ethernetInterface : ethernetDevice.getInterfaces()) {
			System.out.println(ethernetInterface.getInputServer());
			System.out.println(ethernetInterface.getOutputServer());
		}
		System.out.println();

		System.out.println(ethernetDevice.getDeviceName() + " input turns:");
		for(Map.Entry<EthernetInterface, Turn> entry : ethernetDevice.getNeighborInputTurns().entrySet()) {
			System.out.println(entry);
		}
		System.out.println();

		System.out.println(ethernetDevice.getDeviceName() + " output turns:");
		for(Map.Entry<EthernetInterface, Turn> entry : ethernetDevice.getNeighborOutputTurns().entrySet()) {
			System.out.println(entry);
		}
		System.out.println();

		System.out.println("---------------------------------");
	}
	
}
