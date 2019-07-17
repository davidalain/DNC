package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.networkcalculus.dnc.curves.ArrivalCurve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;
import org.networkcalculus.dnc.network.server_graph.Turn;
import org.networkcalculus.dnc.standards.ethernet.EthernetConfig.EthernetInterfaceConfig;

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

	public void addFlow(ArrivalCurve arrivalCurve, EthernetDevice source, EthernetDevice sink) {

	}

	public static void main(String[] args) throws Exception {

		final EthernetNetwork ethernetNetwork = new EthernetNetwork();

		final EthernetEndSystem es1 = new EthernetEndSystem(ethernetNetwork, "ES1", EthernetInterfaceConfig.s_100Mbps);
		final EthernetEndSystem es2 = new EthernetEndSystem(ethernetNetwork, "ES2", EthernetInterfaceConfig.s_100Mbps);
		final EthernetSwitch sw1 = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceConfig.s_100Mbps);

		es1.setNeighbor(0, sw1, 0);
		es2.setNeighbor(0, sw1, 1);

		System.out.println("ES1 neigborhood:");
		for(EthernetDevice device : es1.getAllNeibours()) {
			System.out.println(device);
		}

		System.out.println("ES1 servers:");
		for(Server server : es1.getAllServers()) {
			System.out.println(server);	
		}

		System.out.println("ES1 turns:");
		for(List<Turn> listTurns : es1.getAllTurns()) {
			for(Turn turn : listTurns) {
				System.out.println(turn);	
			}
		}

		System.out.println("---------------------------------");

		System.out.println("ES2 neigborhood:");
		for(EthernetDevice device : es2.getAllNeibours()) {
			System.out.println(device);
		}

		System.out.println("ES2 servers:");
		for(Server server : es2.getAllServers()) {
			System.out.println(server);	
		}

		System.out.println("ES2 turns:");
		for(List<Turn> listTurns : es2.getAllTurns()) {
			for(Turn turn : listTurns) {
				System.out.println(turn);	
			}
		}

		System.out.println("---------------------------------");

		System.out.println("SW1 neigborhood:");
		for(EthernetDevice device : sw1.getAllNeibours()) {
			System.out.println(device);
		}
		
		System.out.println("SW1 servers:");
		for(Server server : sw1.getAllServers()) {
			System.out.println(server);	
		}

		System.out.println("SW1 turns:");
		for(List<Turn> listTurns : sw1.getAllTurns()) {
			for(Turn turn : listTurns) {
				System.out.println(turn);	
			}
		}
		
//		System.out.println("---------------------------------");
//		
//		final EthernetSwitch a = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceConfig.s_100Mbps);
//		final EthernetSwitch b = new EthernetSwitch(ethernetNetwork, "SW1", 2, EthernetInterfaceConfig.s_100Mbps);
//		
//		System.out.println(a.hashCode());
//		System.out.println(b.hashCode());

	}

}
