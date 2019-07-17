package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.networkcalculus.dnc.network.server_graph.ServerGraph;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetNetwork {

	private Set<EthernetGenericDevice> deviceList;
	private ServerGraph serverGraph;
	
	public EthernetNetwork() {
		this.serverGraph = new ServerGraph();
		this.deviceList = new HashSet<EthernetGenericDevice>();
	}
	
	public ServerGraph getServerGraph() {
		return this.serverGraph;
	}
	
	public void addDevice(EthernetGenericDevice device) {
		this.deviceList.add(device);
	}
	
//	public void addFlow()
	
	public static void main(String[] args) throws Exception {
		
		EthernetNetwork ethernetNetwork = new EthernetNetwork();
		
		EthernetEndSystem es1 = new EthernetEndSystem(ethernetNetwork, "ES1", EthernetConfig.EthernetInterfaceConfig.s_100Mbps);
		EthernetEndSystem es2 = new EthernetEndSystem(ethernetNetwork, "ES2", EthernetConfig.EthernetInterfaceConfig.s_100Mbps);
		
		es1.setNeighbor(0, es2, 0);
		
	}
	
}
