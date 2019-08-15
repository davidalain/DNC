package org.networkcalculus.dnc.standards.ethernet;

import java.security.InvalidParameterException;
import java.util.Objects;

import org.apache.commons.math3.util.Pair;
import org.networkcalculus.dnc.curves.MaxServiceCurve;
import org.networkcalculus.dnc.curves.ServiceCurve;
import org.networkcalculus.dnc.network.server_graph.Server;
import org.networkcalculus.dnc.standards.ethernet.EthernetInterfaceSpeedConfig.EthernetInterfaceSpeed;

/**
 * 
 * @author DavidAlain
 *
 */
public class EthernetInterface {

	private EthernetDevice ethernetDeviceOwner; //Device owned of this interface
	private int interfaceId;//Interface ID of this ethernetDevice
	private Server outputServer; //
	private Server inputServer; //
	private EthernetInterfaceSpeed interfaceSpeed;

	public EthernetInterface(EthernetDevice ethernetDeviceOwner, int interfaceId, EthernetInterfaceSpeed interfaceSpeed) {

		if(ethernetDeviceOwner == null)
			throw new InvalidParameterException("ethernetDeviceOwner can not be null");
		if(interfaceId < 0)
			throw new InvalidParameterException("interfaceId can not be negative");

		this.ethernetDeviceOwner = ethernetDeviceOwner;
		this.interfaceId = interfaceId;
		this.interfaceSpeed = interfaceSpeed;
		
		this.setInputServer(interfaceSpeed);
		this.setOutputServer(interfaceSpeed);
		
		//Adds the output server related to Ethernet device 
		this.ethernetDeviceOwner.getNetwork().putServerDevice(this.outputServer, this.ethernetDeviceOwner);
	}

	public EthernetDevice getEthernetDeviceOwner() {
		return ethernetDeviceOwner;
	}

	public void setEthernetDeviceOwner(EthernetDevice ethernetDeviceOwner) {
		this.ethernetDeviceOwner = ethernetDeviceOwner;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	public Server getInputServer() {
		return inputServer;
	}
	
	public Server getOutputServer() {
		return outputServer;
	}
	
	private void setOutputServer(Server server) {
		this.outputServer = server;
		this.outputServer.setAlias("s_out_" + this.toString());
	}
	
	private void setInputServer(Server server) {
		this.inputServer = server;
		this.inputServer.setAlias("s_in_" + this.toString());
	}

	public void setInputServer(ServiceCurve serviceCurve, MaxServiceCurve maxServiceCurve) {
		
		if(serviceCurve == null)
			throw new InvalidParameterException("serviceCurve can not be null");
		if(maxServiceCurve == null)
			throw new InvalidParameterException("maxServiceCurve can not be null");
		
		final Server server = this.ethernetDeviceOwner.getNetwork().getServerGraph().addServer(serviceCurve, maxServiceCurve);
		this.setInputServer(server);
	}

	public void setOutputServer(ServiceCurve serviceCurve, MaxServiceCurve maxServiceCurve) {
		
		if(serviceCurve == null)
			throw new InvalidParameterException("serviceCurve can not be null");
		if(maxServiceCurve == null)
			throw new InvalidParameterException("maxServiceCurve can not be null");
		
		final Server server = this.ethernetDeviceOwner.getNetwork().getServerGraph().addServer(serviceCurve, maxServiceCurve);
		this.setOutputServer(server);
	}
	
	public void setInputServer(Pair<ServiceCurve, MaxServiceCurve> pairCurves) {
		
		if(pairCurves == null)
			throw new InvalidParameterException("pairCurves can not be null");
		
		final Server server = this.ethernetDeviceOwner.getNetwork().getServerGraph().addServer(pairCurves.getFirst(), pairCurves.getSecond());
		this.setInputServer(server);
	}

	public void setOutputServer(Pair<ServiceCurve, MaxServiceCurve> pairCurves) {
		
		if(pairCurves == null)
			throw new InvalidParameterException("pairCurves can not be null");
		
		final Server server = this.ethernetDeviceOwner.getNetwork().getServerGraph().addServer(pairCurves.getFirst(), pairCurves.getSecond());
		this.setOutputServer(server);
	}

	public void setInputServer(EthernetInterfaceSpeed interfaceConfig) {

		final Pair<ServiceCurve,MaxServiceCurve> pairCurves = EthernetInterfaceSpeedConfig.getInstance().getCurves(interfaceConfig);
		final Server server = this.ethernetDeviceOwner.getNetwork().getServerGraph().addServer(pairCurves.getFirst(), pairCurves.getSecond());
		this.setInputServer(server);
	}
	
	public void setOutputServer(EthernetInterfaceSpeed interfaceConfig) {

		final Pair<ServiceCurve,MaxServiceCurve> pairCurves = EthernetInterfaceSpeedConfig.getInstance().getCurves(interfaceConfig);
		final Server server = this.ethernetDeviceOwner.getNetwork().getServerGraph().addServer(pairCurves.getFirst(), pairCurves.getSecond());
		this.setOutputServer(server);
	}
	
	public EthernetInterfaceSpeed getInterfaceSpeed() {
		return interfaceSpeed;
	}

	public void setInterfaceSpeed(EthernetInterfaceSpeed interfaceSpeed) {
		this.interfaceSpeed = interfaceSpeed;
	}

	@Override
	public String toString() {
		return this.ethernetDeviceOwner.getName() + ".eth" + Integer.toString(interfaceId);
	}

	@Override
	public int hashCode() {
		//Note: do not use this.ethernetDevice in hash calculation. This make a infinite recursive call of hashCode() method and produces StackOverflowError exception.
		return Objects.hash(this.toString(), this.interfaceId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EthernetInterface other = (EthernetInterface) obj;

		//Note: do not use this.ethernetDevice in equals comparison. This make a infinite recursive call of equals() method and produces StackOverflowError exception.
		return Objects.equals(this.toString(), other.toString()) && 
				Objects.equals(this.interfaceId, other.interfaceId);
	}


}
