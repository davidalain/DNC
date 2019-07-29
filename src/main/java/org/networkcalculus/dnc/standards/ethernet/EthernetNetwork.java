package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashSet;
import java.util.Set;

import org.networkcalculus.dnc.curves.ArrivalCurve;
import org.networkcalculus.dnc.network.server_graph.Flow;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetNetwork {

	private Set<EthernetDevice> deviceList;
	private Set<Flow> flows;
	private ServerGraph serverGraph;

	public EthernetNetwork() {
		this.serverGraph = new ServerGraph();
		this.deviceList = new HashSet<EthernetDevice>();
		this.flows = new HashSet<Flow>();
	}

	public ServerGraph getServerGraph() {
		return this.serverGraph;
	}

	public void addDevice(EthernetDevice device) {
		this.deviceList.add(device);
	}
	
	public Set<Flow> getFlows(){
		return this.flows;
	}

	public void addFlow(ArrivalCurve arrivalCurve, EthernetDevice source, EthernetDevice sink) throws Exception {

		Server s_out = source.getInterface(0).getOutputServer();
		Server s_in = sink.getInterface(0).getInputServer();
		
//		System.out.println(s_out);
//		System.out.println(s_in);
		
		Flow f = this.serverGraph.addFlow(arrivalCurve, s_out, s_in);
		
//		System.out.println(f);
		
		this.flows.add(f);
	}

}
