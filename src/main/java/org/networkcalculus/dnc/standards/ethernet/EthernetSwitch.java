package org.networkcalculus.dnc.standards.ethernet;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.networkcalculus.dnc.curves.Curve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.network.server_graph.Turn;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetSwitch extends EthernetDevice{

	protected Server switchingFabricServer;
	protected Map<EthernetInterface, Turn> internalInputTurns;  //Input turns coming from input interface's server and going to switching fabric's server.
	protected Map<EthernetInterface, Turn> internalOutputTurns; //Output turns coming from switching fabric's server and going to output interface's server.

	public EthernetSwitch(EthernetNetwork network, String deviceName, int interfacesCount, EthernetInterfaceSpeed interfaceSpeed) throws Exception {
		this(network, deviceName, Collections.nCopies(interfacesCount, interfaceSpeed));
	}

	public EthernetSwitch(EthernetNetwork network, String deviceName, EthernetInterfaceSpeed... interfacesSpeed) throws Exception {
		this(network, deviceName, Arrays.asList(interfacesSpeed));
	}

	public EthernetSwitch(EthernetNetwork network, String deviceName, List<EthernetInterfaceSpeed> interfacesSpeed) throws Exception {
		super(network, deviceName);

		//Using unlimited Service Curve with no delay
		this.switchingFabricServer = this.network.getServerGraph().addServer("s_swfab_" + deviceName,
				Curve.getFactory().createZeroDelayInfiniteBurst(), 
				Curve.getFactory().createZeroDelayInfiniteBurstMSC());

		for(int i = 0 ; i < interfacesSpeed.size() ; i++) {
			this.interfaces.put(i, new EthernetInterface(this, i, interfacesSpeed.get(i)));
		}

		this.internalInputTurns = new HashMap<>();
		this.internalOutputTurns = new HashMap<>();

		for(int i = 0 ; i < this.interfaces.size() ; i++) {
			for(int j = 0 ; j < this.interfaces.size() ; j++) {
				if(i != j) {
					addInternalTandemTurn(this.getInterface(i), this.getInterface(j));
				}
			}
		}
	}

	private void addInternalTandemTurn(EthernetInterface interfaceIn, EthernetInterface interfaceOut) throws Exception {

		if(interfaceIn == null)
			throw new InvalidParameterException("interfaceIn must not be null");
		if(interfaceOut == null)
			throw new InvalidParameterException("interfaceOut must not be null");
		if(!Objects.equals(this, interfaceIn.getEthernetDeviceOwner()))
			throw new InvalidParameterException("interfaceIn must be in this device");
		if(!Objects.equals(this, interfaceOut.getEthernetDeviceOwner()))
			throw new InvalidParameterException("interfaceOut must be in this device");

		final Server forwardSourceServer = interfaceIn.getInputServer();
		final Server forwardSinkServer = interfaceOut.getOutputServer();

		final String turnAliasToSwictingFabric = forwardSourceServer.getAlias() + "->" + switchingFabricServer.getAlias();
		final String turnAliasFromSwictingFabric = switchingFabricServer.getAlias() + "->" + forwardSinkServer.getAlias();

		final Turn inputTurn = this.network.getServerGraph().addTurn(turnAliasToSwictingFabric, forwardSourceServer, switchingFabricServer);
		final Turn outputTurn = this.network.getServerGraph().addTurn(turnAliasFromSwictingFabric, switchingFabricServer, forwardSinkServer);

		this.internalInputTurns.put(interfaceIn, inputTurn);
		this.internalOutputTurns.put(interfaceOut, outputTurn);
	}

	@Override
	public String toString() {
		return (this.name);
	}

}
