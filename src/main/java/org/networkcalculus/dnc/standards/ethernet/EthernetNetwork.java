package org.networkcalculus.dnc.standards.ethernet;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.networkcalculus.dnc.curves.ArrivalCurve;
import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.network.server_graph.Flow;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;
import org.networkcalculus.dnc.tandem.analyses.TotalFlowAnalysis;
import org.networkcalculus.dnc.utils.DataUnit;
import org.networkcalculus.num.Num;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetNetwork {

	private Set<EthernetDevice> devices;
	private Set<Flow> flows;
	private ServerGraph serverGraph;
	private Map<String, Map<EthernetDevice, Double>> mapAnalysisNameBackLogDevice; //Map<String = Analysis instance class name (to avoid hashCode() issue), Map<EthernetDevice = device instance, Double = backlog>>
	private Map<Server, EthernetDevice> mapServersEthernetDevices; //Mapping output interfaces' servers to their ethernet device

	public EthernetNetwork() {
		this.serverGraph = new ServerGraph();
		this.devices = new HashSet<EthernetDevice>();
		this.flows = new HashSet<Flow>();
		this.mapAnalysisNameBackLogDevice = new HashMap<String, Map<EthernetDevice, Double>>();
		this.mapServersEthernetDevices = new HashMap<Server, EthernetDevice>();
	}

	public ServerGraph getServerGraph() {
		return this.serverGraph;
	}

	public void addDevice(EthernetDevice device) {
		this.devices.add(device);
	}

	public Set<Flow> getFlows(){
		return this.flows;
	}

	public void putServerDevice(Server server, EthernetDevice device) {
		this.mapServersEthernetDevices.put(server, device);
	}

	public void saveBacklog(TotalFlowAnalysis tfa) {

		//Iterate over all network's servers
		for(Entry<Server, Set<Num>> entry : tfa.getServerBacklogBoundMap().entrySet()) {

			final Server server = entry.getKey();
			final Set<Num> setBacklogNum = entry.getValue();

			//TODO: Why backlog is a set of Nums? Why not one Num only?
			double backlog = 0.0;
			for(Num b : setBacklogNum) {
				backlog += b.doubleValue();
			}

			final EthernetDevice device = this.mapServersEthernetDevices.get(server);
			final String analysisClassName = tfa.getClass().getSimpleName();

			if(device == null)
				//throw new InvalidParameterException("this.mapServersEthernetDevices.get(server="+server+") have returned null");
				continue;

			final Map<EthernetDevice, Double> mapDeviceBacklog = Objects.requireNonNullElse(
					mapAnalysisNameBackLogDevice.get(analysisClassName), 
					new HashMap<EthernetDevice, Double>());

			final double oldBacklog = Objects.requireNonNullElse(
					mapDeviceBacklog.get(device), 
					0.0);

			mapDeviceBacklog.put(device, oldBacklog + backlog);
			mapAnalysisNameBackLogDevice.put(analysisClassName, mapDeviceBacklog);
		}
	}

	public void addFlow(ArrivalCurve arrivalCurve, EthernetDevice source, EthernetDevice sink) throws Exception {

		final Server s_out = source.getInterface(0).getOutputServer(); 	//Ethernet source's output
		final Server s_in = sink.getInterface(0).getInputServer(); 		//Ethernet sink's input
		final Flow f = this.serverGraph.addFlow(arrivalCurve, s_out, s_in);

		this.flows.add(f);
	}

	public void addFlowTokenBucket(double rate, DataUnit rateUnit, double burst, DataUnit burstUnit, EthernetDevice source, EthernetDevice sink) throws Exception {

		final ArrivalCurve arrivalCurve = Curve.getFactory().createTokenBucket(
				DataUnit.convert(rate, rateUnit, DataUnit.b), 
				DataUnit.convert(burst, burstUnit, DataUnit.b));

		final Server s_out = source.getInterface(0).getOutputServer(); 	//Ethernet source's output
		final Server s_in = sink.getInterface(0).getInputServer(); 		//Ethernet sink's input
		final Flow f = this.serverGraph.addFlow(arrivalCurve, s_out, s_in);

		this.flows.add(f);
	}

	public void printDevicesBacklog() {

		System.out.println("--- Ethernet Devices Backlog Report ---");
		for(Entry<String, Map<EthernetDevice, Double>> entry : this.mapAnalysisNameBackLogDevice.entrySet()) {

			System.out.println(" -- Analysis: " + entry.getKey() + " --");

			//Converting Collection to Set and then to List to avoid duplicates
			List<EthernetDevice> devices = new LinkedList<EthernetDevice>(new HashSet<EthernetDevice>(this.mapServersEthernetDevices.values()));

			//Order by device name
			Collections.sort(devices, new Comparator<EthernetDevice>() {
				@Override
				public int compare(EthernetDevice o1, EthernetDevice o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			for(EthernetDevice device : devices) {
				Double backlog = entry.getValue().get(device);
				if(backlog == null)
					backlog = 0.0;

				System.out.println("Device=" + device + ", Backlog=" + DataUnit.convert(backlog, DataUnit.b, DataUnit.kb) + " kb");
			}

			System.out.println();
		}

	}

}
