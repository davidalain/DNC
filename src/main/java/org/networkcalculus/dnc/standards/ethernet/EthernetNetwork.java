package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.ArrivalCurve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;
import org.networkcalculus.dnc.network.server_graph.Turn;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetNetwork {

	private Set<EthernetDevice> deviceList;
	private ServerGraph serverGraph;

	public EthernetNetwork() {
		this.serverGraph = new ServerGraph();
		this.deviceList = new HashSet<EthernetDevice>();
	}

	public ServerGraph getServerGraph() {
		return this.serverGraph;
	}

	public void addDevice(EthernetDevice device) {
		this.deviceList.add(device);
	}

	private Pair<Server,Server> getPairSourceSinkServers(EthernetDevice source, EthernetDevice sink){

		//		final List<Server> possibleServersLinkingToSinkDevice = new LinkedList<>();
		//		final List<EthernetDevice> listSinkNeigbor = sink.getAllNeibours();
		//		
		//		for(EthernetDevice sinkNeighbor : listSinkNeigbor) {
		//			
		//			
		//			
		//		}

		//		for(Map.Entry<Integer, List<Turn>> entry : sink.getAllTurnsEntrySet()) {
		//			
		//			for(Turn turn : entry.getValue()) {
		//				
		//				
		//				
		//			}
		//			
		//		}

		//		sink.getAllNeibours();

		return null;
	}

	public void addFlow(ArrivalCurve arrivalCurve, EthernetDevice source, EthernetDevice sink) throws Exception {

		//FIXME:

		final Pair<Server,Server> sourceSinkServers = this.getPairSourceSinkServers(source, sink);

		this.serverGraph.addFlow(arrivalCurve, sourceSinkServers.getFirst(), sourceSinkServers.getSecond());
	}

	public static void main(String[] args) throws Exception {

		final EthernetNetwork ethernetNetwork = new EthernetNetwork();

		final EthernetEndSystem es1 = new EthernetEndSystem(ethernetNetwork, "ES1", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetEndSystem es2 = new EthernetEndSystem(ethernetNetwork, "ES2", EthernetInterfaceSpeed.s_100Mbps);
		final EthernetSwitch sw1 = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceSpeed.s_100Mbps);

		System.out.println("es1.setNeighbor(0, sw1, 0):");
		es1.setNeighbor(0, sw1, 0);
		System.out.println("es2.setNeighbor(0, sw1, 1):");
		es2.setNeighbor(0, sw1, 1);

		printInfo(es1);
		printInfo(es2);
		printInfo(sw1);

		//		System.out.println("---------------------------------");
		//		
		//		final EthernetSwitch a = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceConfig.s_100Mbps);
		//		final EthernetSwitch b = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceConfig.s_100Mbps);
		//		
		//		System.out.println(a.hashCode());
		//		System.out.println(b.hashCode());

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
		for(Map.Entry<EthernetInterface, Turn> entry : ethernetDevice.getInputTurns().entrySet()) {
			System.out.println(entry);
		}
		System.out.println();
		
		System.out.println(ethernetDevice.getDeviceName() + " output turns:");
		for(Map.Entry<EthernetInterface, Turn> entry : ethernetDevice.getOutputTurns().entrySet()) {
			System.out.println(entry);
		}
		System.out.println();
		
		System.out.println("---------------------------------");
	}

}
