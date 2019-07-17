package org.networkcalculus.dnc.standards.ethernet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.ServerGraph;
import org.networkcalculus.dnc.network.server_graph.Turn;

/**
 * 
 * @author DavidAlain
 *
 */
public abstract class EthernetGenericDevice {

	protected String deviceName;
	protected ServerGraph serverGraph;

	protected Map<Integer, Server> outputServers;
	protected Map<Integer, EthernetGenericDevice> neighbors;

	/**
	 * Turn links two adjacent servers on flows' direction.
	 * 
	 * flowsTurns maps on output interface's Server (accordingly to its interfaceId) to Servers in linked Ethernet device neighbor  
	 */
	protected Map<Integer, List<Turn>> turns;


	public EthernetGenericDevice(ServerGraph serverGraph, String deviceName) {
		this.deviceName = deviceName;
		this.serverGraph = serverGraph;

		this.outputServers = new HashMap<Integer, Server>();
		this.neighbors = new HashMap<Integer, EthernetGenericDevice>();
		this.turns = new HashMap<Integer, List<Turn>>();
	}

	public Server getServer(Integer interfaceId) {
		return this.outputServers.get(interfaceId);
	}

	public void setServer(Integer interfaceId, Server server) {
		this.outputServers.put(interfaceId, server);
	}

	public EthernetGenericDevice getNeibour(Integer interfaceId) {
		return this.neighbors.get(interfaceId);
	}

	public void setNeighbor(Integer interfaceId, EthernetGenericDevice neighborDevice, Integer neighborInterfaceId) throws Exception {
		setNeighbor(this, interfaceId, neighborDevice, neighborInterfaceId); //Creates path from this to neighborDevice 
		setNeighbor(neighborDevice, neighborInterfaceId, this, interfaceId); //Creates path from neighborDevice to this
	}

	private void setNeighbor(EthernetGenericDevice sourceDevice, Integer sourceInterfaceId, EthernetGenericDevice sinkDevice, Integer sinkInterfaceId) throws Exception {

		final List<Turn> turnsFromSource = new LinkedList<Turn>();
		final Server outServerFromSource = sourceDevice.outputServers.get(sourceInterfaceId);

		for(Map.Entry<Integer,Server> entry : sinkDevice.outputServers.entrySet()) {

			//Does not create Turn with input interface Server, only with output interface Servers
			//Flow passes through Ethernet devices and does not echoing on them 
			if(!entry.getKey().equals(sinkInterfaceId)) {
				final Server neighbourServer = entry.getValue();
				final Turn t = sourceDevice.serverGraph.addTurn(outServerFromSource.getAlias() + "->" + neighbourServer.getAlias(), outServerFromSource, neighbourServer);
				turnsFromSource.add(t);
			}
		}

		sourceDevice.neighbors.put(sourceInterfaceId, sinkDevice);
		sourceDevice.turns.put(sourceInterfaceId, turnsFromSource);
	}

	protected void setServer(Integer interfaceId, ServiceCurve serviceCurve, MaxServiceCurve maxServiceCurve) {
		final Server server = this.serverGraph.addServer(serviceCurve, maxServiceCurve);
		server.setAlias(this.deviceName+"."+interfaceId);

		this.outputServers.put(interfaceId, server);
	}

	public int getInterfacesCount() {
		return this.outputServers.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceName, neighbors, outputServers, serverGraph, turns);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		EthernetGenericDevice other = (EthernetGenericDevice) obj;
		return 	Objects.equals(deviceName, other.deviceName) &&
				Objects.equals(neighbors, other.neighbors) &&
				Objects.equals(outputServers, other.outputServers) &&
				Objects.equals(serverGraph, other.serverGraph) &&
				Objects.equals(turns, other.turns);
	}



}
