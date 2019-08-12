package org.networkcalculus.dnc.standards.ethernet.demos;

import java.security.InvalidAlgorithmParameterException;
import java.util.Map;

import org.networkcalculus.dnc.AnalysisConfig;
import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.network.server_graph.Flow;
import org.networkcalculus.dnc.network.server_graph.Turn;
import org.networkcalculus.dnc.standards.ethernet.EthernetDevice;
import org.networkcalculus.dnc.standards.ethernet.EthernetEndSystem;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterface;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;
import org.networkcalculus.dnc.standards.ethernet.EthernetNetwork;
import org.networkcalculus.dnc.standards.ethernet.EthernetSwitch;
import org.networkcalculus.dnc.tandem.analyses.PmooAnalysis;
import org.networkcalculus.dnc.tandem.analyses.SeparateFlowAnalysis;
import org.networkcalculus.dnc.tandem.analyses.TandemMatchingAnalysis;
import org.networkcalculus.dnc.tandem.analyses.TotalFlowAnalysis;
import org.networkcalculus.dnc.utils.DataUnit;

public class Ethernet_Demo2 {

	/**
	 * Topology found in https://link.springer.com/article/10.1007/s11241-015-9243-y
	 * 
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {

		final EthernetNetwork ethernetNetwork = new EthernetNetwork();

		final EthernetEndSystem ecuCtrl1 = new EthernetEndSystem(ethernetNetwork, "ECU_Ctrl1", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem ecuCtrl2 = new EthernetEndSystem(ethernetNetwork, "ECU_Ctrl2", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem ecuCtrl3 = new EthernetEndSystem(ethernetNetwork, "ECU_Ctrl3", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem ecuInfo = new EthernetEndSystem(ethernetNetwork, "ECU_Info", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem ecuCam = new EthernetEndSystem(ethernetNetwork, "ECU_Cam", EthernetInterfaceSpeed.s_100Mbps);

		final EthernetEndSystem cam1 = new EthernetEndSystem(ethernetNetwork, "CAM1", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem cam2 = new EthernetEndSystem(ethernetNetwork, "CAM2", EthernetInterfaceSpeed.s_100Mbps);

		final EthernetEndSystem gw1 = new EthernetEndSystem(ethernetNetwork, "GW1", EthernetInterfaceSpeed.s_10Mbps);
		final EthernetEndSystem gw2 = new EthernetEndSystem(ethernetNetwork, "GW2", EthernetInterfaceSpeed.s_10Mbps);
		final EthernetEndSystem gw3 = new EthernetEndSystem(ethernetNetwork, "GW3", EthernetInterfaceSpeed.s_10Mbps);
		final EthernetEndSystem gw4 = new EthernetEndSystem(ethernetNetwork, "GW4", EthernetInterfaceSpeed.s_10Mbps);

		final EthernetSwitch s1 = new EthernetSwitch(ethernetNetwork, "S1", 
				EthernetInterfaceSpeed.s_10Mbps, 
				EthernetInterfaceSpeed.s_100Mbps, 
				EthernetInterfaceSpeed.s_100Mbps, 
				EthernetInterfaceSpeed.s_100Mbps);
		final EthernetSwitch s2 = new EthernetSwitch(ethernetNetwork, "S2", 
				EthernetInterfaceSpeed.s_10Mbps,
				EthernetInterfaceSpeed.s_100Mbps,
				EthernetInterfaceSpeed.s_100Mbps);
		final EthernetSwitch s3 = new EthernetSwitch(ethernetNetwork, "S3", 
				EthernetInterfaceSpeed.s_10Mbps,
				EthernetInterfaceSpeed.s_100Mbps,
				EthernetInterfaceSpeed.s_100Mbps);
		final EthernetSwitch s4 = new EthernetSwitch(ethernetNetwork, "S4", 
				EthernetInterfaceSpeed.s_10Mbps,
				EthernetInterfaceSpeed.s_100Mbps,
				EthernetInterfaceSpeed.s_100Mbps);

		final EthernetSwitch sCenter = new EthernetSwitch(ethernetNetwork, "S_center", 6, EthernetInterfaceSpeed.s_100Mbps);

		s1.setNeighbor(0, gw1, 0);
		s1.setNeighbor(1, ecuCtrl1, 0);
		s1.setNeighbor(2, cam1, 0);

		s2.setNeighbor(0, gw2, 0);
		s2.setNeighbor(1, cam2, 0);

		s3.setNeighbor(0, gw3, 0);
		s3.setNeighbor(1, ecuCtrl2, 0);

		s4.setNeighbor(0, gw4, 0);
		s4.setNeighbor(1, ecuCtrl3, 0);

		sCenter.setNeighbor(0, s1, 3);
		sCenter.setNeighbor(1, s2, 2);
		sCenter.setNeighbor(2, s3, 2);
		sCenter.setNeighbor(3, s4, 2);
		sCenter.setNeighbor(4, ecuInfo, 0);
		sCenter.setNeighbor(5, ecuCam, 0);

		//--- For checking only ---
		checkNeighborCount(s1, 4);
		checkNeighborCount(s2, 3);
		checkNeighborCount(s3, 3);
		checkNeighborCount(s4, 3);
		checkNeighborCount(sCenter, 6);
		
		checkNeighborCount(gw1, 1);
		checkNeighborCount(gw2, 1);
		checkNeighborCount(gw3, 1);
		checkNeighborCount(gw4, 1);
		
		checkNeighborCount(ecuCtrl1, 1);
		checkNeighborCount(ecuCtrl2, 1);
		checkNeighborCount(ecuCtrl3, 1);
		
		checkNeighborCount(ecuInfo, 1);
		checkNeighborCount(ecuCam, 1);
		
		checkNeighborCount(cam1, 1);
		checkNeighborCount(cam2, 1);
		//-------------------------

		//		printInfo(es1);
		//		printInfo(es2);
		//		printInfo(sw1);

		//Test
		ethernetNetwork.addFlow(Curve.getFactory().createTokenBucket(
				DataUnit.convert(2, DataUnit.Mb, DataUnit.b), 
				DataUnit.convert(1500, DataUnit.B, DataUnit.b)),
				gw1, gw4);

		ethernetNetwork.addFlowTokenBucket(2, DataUnit.Mb, 1500, DataUnit.B, gw3, cam2);
		
		ethernetNetwork.addFlow(Curve.getFactory().createTokenBucket(1e6, 1500), cam1, ecuCam);
		ethernetNetwork.addFlow(Curve.getFactory().createTokenBucket(10e6, 1500), ecuInfo, ecuCtrl2);

		performAnalysis(ethernetNetwork);

		ethernetNetwork.printDevicesBacklog();
	}

	private static void checkNeighborCount(EthernetDevice device, int neighborCount) throws InvalidAlgorithmParameterException {

		if(device.getNeighbors().size() != neighborCount) 
			throw new InvalidAlgorithmParameterException(device + " does not have " + neighborCount + " neighbors. Neighbors={"+device.getNeighbors()+"}");

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

				ethernetNetwork.saveBacklog(tfa);

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

		System.out.println(ethernetDevice.getName() + "'s neigborhood:");
		for(EthernetDevice device : ethernetDevice.getNeighbors()) {
			System.out.println(device);
		}
		System.out.println();

		System.out.println(ethernetDevice.getName() + "'s servers:");
		for(EthernetInterface ethernetInterface : ethernetDevice.getInterfaces()) {
			System.out.println(ethernetInterface.getInputServer());
			System.out.println(ethernetInterface.getOutputServer());
		}
		System.out.println();

		System.out.println(ethernetDevice.getName() + "'s input turns:");
		for(Map.Entry<EthernetInterface, Turn> entry : ethernetDevice.getNeighborInputTurns().entrySet()) {
			System.out.println(entry);
		}
		System.out.println();

		System.out.println(ethernetDevice.getName() + "'s output turns:");
		for(Map.Entry<EthernetInterface, Turn> entry : ethernetDevice.getNeighborOutputTurns().entrySet()) {
			System.out.println(entry);
		}
		System.out.println();

		System.out.println("---------------------------------");
	}

}
