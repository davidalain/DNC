package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.Turn;
import org.networkcalculus.dnc.standards.ethernet.EthernetConfig.EthernetInterfaceConfig;

/**
 * 
 * @author DavidAlain
 *
 */
public abstract class EthernetDevice {

	protected String deviceName;
	protected EthernetNetwork network;

	protected Map<Integer, Server> outputServers;
	protected Map<Integer, EthernetDevice> neighbors;

	/**
	 * Turn links two adjacent servers on flows' direction.
	 * This maps the output interface's Server (accordingly to its interfaceId) to (output interface) Servers in the linked Ethernet device neighbor  
	 */
	protected Map<Integer, List<Turn>> turns;


	public EthernetDevice(EthernetNetwork network, String deviceName) {
		this.deviceName = deviceName;
		this.network = network;

		this.outputServers = new HashMap<Integer, Server>();
		this.neighbors = new HashMap<Integer, EthernetDevice>();
		this.turns = new HashMap<Integer, List<Turn>>();
	}

	public Server getServer(Integer interfaceId) {
		return this.outputServers.get(interfaceId);
	}

	public void setServer(Integer interfaceId, Server server) {
		this.outputServers.put(interfaceId, server);
	}
	
	public List<Server> getAllServers() {
		return new LinkedList<>(this.outputServers.values());
	}

	public EthernetDevice getNeibour(Integer interfaceId) {
		return this.neighbors.get(interfaceId);
	}
	
	public List<EthernetDevice> getAllNeibours() {
		return new LinkedList<EthernetDevice>(this.neighbors.values());
	}
	
	public Set<Map.Entry<Integer, EthernetDevice>> getAllNeiboursEntrySet() {
		return this.neighbors.entrySet();
	}

	public void setNeighbor(Integer interfaceId, EthernetDevice neighborDevice, Integer neighborInterfaceId) throws Exception {
		setNeighbor(this, interfaceId, neighborDevice, neighborInterfaceId); //Creates path from this to neighborDevice 
		setNeighbor(neighborDevice, neighborInterfaceId, this, interfaceId); //Creates path from neighborDevice to this
	}

	private void setNeighbor(EthernetDevice sourceDevice, Integer sourceInterfaceId, EthernetDevice sinkDevice, Integer sinkInterfaceId) throws Exception {

		final List<Turn> turnsFromSource = new LinkedList<Turn>();
		final Server outServerFromSource = sourceDevice.outputServers.get(sourceInterfaceId);

		for(Map.Entry<Integer,Server> entry : sinkDevice.outputServers.entrySet()) {

			//Does not create Turn with input interfaces' Server, only with output interfaces' Servers
			//Flow passes through Ethernet devices and does not echoing on them 
			if(!entry.getKey().equals(sinkInterfaceId)) {
				final Server neighbourServer = entry.getValue();
				final Turn t = sourceDevice.network.getServerGraph().addTurn(outServerFromSource.getAlias() + "->" + neighbourServer.getAlias(), outServerFromSource, neighbourServer);
				turnsFromSource.add(t);
			}
		}

		sourceDevice.neighbors.put(sourceInterfaceId, sinkDevice);
		sourceDevice.turns.put(sourceInterfaceId, turnsFromSource);
	}

	protected void setServer(Integer interfaceId, ServiceCurve serviceCurve, MaxServiceCurve maxServiceCurve) {
		
		final Server server = this.network.getServerGraph().addServer(serviceCurve, maxServiceCurve);
		server.setAlias(this.deviceName+"."+interfaceId);

		this.outputServers.put(interfaceId, server);
	}
	
	protected void setServer(Integer interfaceId, EthernetInterfaceConfig config) {
		
		final Pair<ServiceCurve,MaxServiceCurve> pair = EthernetConfig.getInstance().getCurves(config);
		setServer(interfaceId, pair.getFirst(), pair.getSecond());
	}
	
	public List<Turn> getTurns(Integer interfaceId){
		return this.turns.get(interfaceId);
	}
	
	public List<List<Turn>> getAllTurns(){
		return new LinkedList<List<Turn>>(this.turns.values());
	}
	
	public Set<Map.Entry<Integer, List<Turn>>> getAllTurnsEntrySet(){
		return this.turns.entrySet();
	}

	public int getInterfacesCount() {
		return this.outputServers.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceName, outputServers, network, turns);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		EthernetDevice other = (EthernetDevice) obj;
		return 	Objects.equals(deviceName, other.deviceName) &&
				Objects.equals(neighbors, other.neighbors) &&
				Objects.equals(outputServers, other.outputServers) &&
				Objects.equals(network, other.network) &&
				Objects.equals(turns, other.turns);
	}
	
	@Override
	public abstract String toString();

}
