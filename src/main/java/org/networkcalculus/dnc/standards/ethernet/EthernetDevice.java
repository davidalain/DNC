package org.networkcalculus.dnc.standards.ethernet;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.Turn;

/**
 * 
 * @author DavidAlain
 *
 */
public abstract class EthernetDevice {

	protected String deviceName;
	protected EthernetNetwork network;

	protected Map<Integer, EthernetInterface> interfaces; //<Interface ID, Ethernet interface>
	protected Set<EthernetLink> links;

	protected Map<EthernetInterface, Turn> inputTurns; //Input turns coming from neighbor. EthernetInterface = output interface.
	protected Map<EthernetInterface, Turn> outputTurns; //Output turns going to neighbor. EthernetInterface = output interface.

	public EthernetDevice(EthernetNetwork network, String deviceName) {
		this.deviceName = deviceName;
		this.network = network;

		this.interfaces = new HashMap<Integer, EthernetInterface>();

		this.links = new HashSet<EthernetLink>();
		this.inputTurns = new HashMap<>();
		this.outputTurns = new HashMap<>();
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public EthernetNetwork getNetwork() {
		return network;
	}

	public void setNetwork(EthernetNetwork network) {
		this.network = network;
	}

	public Set<Map.Entry<Integer, EthernetInterface>> getInterfacesEntrySet() {
		return interfaces.entrySet();
	}

	public Set<EthernetInterface> getInterfaces() {
		return new HashSet<EthernetInterface>(this.interfaces.values());
	}

	public EthernetInterface getInterface(int interfaceId) {
		return interfaces.get(interfaceId);
	}

	public void setInterface(Integer interfaceId, EthernetInterface ethernetInterface) {
		this.interfaces.put(interfaceId, ethernetInterface);
	}

	public Set<EthernetDevice> getNeighbors() {
		final Set<EthernetDevice> list = new HashSet<EthernetDevice>();

		for(EthernetLink link : this.links) {
			list.add(link.getNeighbor(this));
		}

		return list;
	}

	public Map<EthernetInterface, Turn> getInputTurns() {
		return inputTurns;
	}

	public void setInputTurns(Map<EthernetInterface, Turn> turns) {
		this.inputTurns = turns;
	}
	
	public Map<EthernetInterface, Turn> getOutputTurns() {
		return outputTurns;
	}

	public void setOutputTurns(Map<EthernetInterface, Turn> turns) {
		this.outputTurns = turns;
	}
	
	public void setNeighbor(int interfaceId, EthernetDevice neighborDevice, int neighborInterfaceId) throws Exception {

		final EthernetInterface sourceEthernetInterface = this.getInterface(interfaceId);
		final EthernetInterface sinkDeviceInterface = neighborDevice.getInterface(neighborInterfaceId);

		this.setNeighbor(sourceEthernetInterface, sinkDeviceInterface);
	}

	public void setNeighbor(EthernetInterface outputInterface, EthernetInterface neighborInputInterface) throws Exception {
		
		final EthernetLink link = new EthernetLink(outputInterface, neighborInputInterface);

		if(!Objects.equals(this, outputInterface.getEthernetDeviceOwner())) 
			throw new InvalidParameterException("this EthernetDevice instance must be the owner of outputInterface");
		
		if(this.links.contains(link))
			throw new InvalidParameterException("link is already added to this device");
		if(neighborInputInterface.getEthernetDeviceOwner().links.contains(link))
			throw new InvalidParameterException("link is already added to neighbor device");
		
		this.links.add(link);
		neighborInputInterface.getEthernetDeviceOwner().links.add(link);

		this.addTurn(outputInterface, neighborInputInterface);
	}
	
	protected void addTurn(EthernetInterface outputInterface, EthernetInterface neighborInputInterface) throws Exception {

		if(!Objects.equals(this, outputInterface.getEthernetDeviceOwner()))
			throw new InvalidParameterException("");
		
		//Add turns in this device
		addTurnOutput(outputInterface, neighborInputInterface);
		addTurnInput(outputInterface, neighborInputInterface);
		
		//Add turns in neighbor device
		addTurnOutput(neighborInputInterface, outputInterface);
		addTurnInput(neighborInputInterface, outputInterface);
		
	}
	
	private void addTurnOutput(EthernetInterface outputInterface, EthernetInterface neighborInputInterface) throws Exception {
		
		if(outputInterface == null)
			throw new InvalidParameterException("outputInterface must not be null");
		if(neighborInputInterface == null)
			throw new InvalidParameterException("neighborInputInterface must not be null");
		
		final EthernetDevice ethernetDevice = outputInterface.getEthernetDeviceOwner();

		final Server forwardSourceServer = outputInterface.getOutputServer();
		final Server forwardSinkServer = neighborInputInterface.getInputServer();
		final String turnAlias = forwardSourceServer.getAlias() + "->" + forwardSinkServer.getAlias();
		final Turn outputTurn = ethernetDevice.network.getServerGraph().addTurn(turnAlias, forwardSourceServer, forwardSinkServer);

		ethernetDevice.outputTurns.put(outputInterface, outputTurn);
	}

	private void addTurnInput(EthernetInterface outputInterface, EthernetInterface neighborInputInterface) throws Exception {
		
		if(outputInterface == null)
			throw new InvalidParameterException("outputInterface must not be null");
		if(neighborInputInterface == null)
			throw new InvalidParameterException("neighborInputInterface must not be null");
		
		final EthernetDevice ethernetDevice = outputInterface.getEthernetDeviceOwner();

		final Server backwardSourceServer = neighborInputInterface.getOutputServer();
		final Server backwardSinkServer = outputInterface.getInputServer();
		final String turnAlias = backwardSourceServer.getAlias() + "->" + backwardSinkServer.getAlias();
		final Turn inputTurn = ethernetDevice.network.getServerGraph().addTurn(turnAlias, backwardSourceServer, backwardSinkServer);

		ethernetDevice.inputTurns.put(outputInterface, inputTurn);
	}

	@Override
	public int hashCode() {
		//Note: do not use this.neighbors in hash calculation. This makes a recursive call of this method and generates StackOverflowException.
		return Objects.hash(this.deviceName, this.interfaces, this.network);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		//Note: do not use this.neighbors in equals comparison. This makes a recursive call of this method and generates StackOverflowException.
		EthernetDevice other = (EthernetDevice) obj;
		return 	Objects.equals(this.deviceName, other.deviceName) &&
				Objects.equals(this.interfaces, other.interfaces) &&
				Objects.equals(this.network, other.network);
	}

	@Override
	public abstract String toString();

}
