package pl.edu.pwr.student.damian_fryc.lab7.retention_basin;

import interfaces.IRetensionBasin;
import interfaces.IRiverSection;
import interfaces.ITailor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RetentionBasin extends UnicastRemoteObject implements IRetensionBasin {
	private final int maxVolume;
    private final String name;
	private ITailor it;
	private int currentVolume = 0;
	private int waterDischarge;

	private IRiverSection outflowRiverSection;

    private final Map<String, Integer> inflows = new HashMap<>();

	public RetentionBasin(int maxVolume, int port, String name) throws RemoteException {
		super(port);
		this.maxVolume = maxVolume;
        this.name = name;
		waterDischarge = maxVolume / 10;
    }

	public void closeServer() throws RemoteException {
		UnicastRemoteObject.unexportObject(this, true);
		if (it != null) it.unregister(this);
	}

	public boolean registerToTailor(String tailorName, String host, int tailorPort) {
		try {
			Registry registry = LocateRegistry.getRegistry(host, tailorPort);
			it = (ITailor) registry.lookup(tailorName);
			return it.register(this, name);
		} catch (RemoteException | NotBoundException e) {
			return false;
		}
	}

	@Override
	public int getWaterDischarge() {
		return waterDischarge;
	}

	@Override
	public long getFillingPercentage() {
		return (long) (100.0 * currentVolume / maxVolume);
	}

	@Override
	public void setWaterDischarge(int waterDischarge) {
		this.waterDischarge = waterDischarge;
	}

	@Override
	public void setWaterInflow(int waterInflow, String name) throws RemoteException {
		inflows.put(name, waterInflow);
	}

	@Override
	public void assignRiverSection(IRiverSection irs, String name) throws RemoteException {
		outflowRiverSection = irs;
        System.out.println("Assigned river section" + name);
	}

	public void basinLogic() throws RemoteException {
		int totalInflow = 0;
		for (int inflow : inflows.values())
			totalInflow += inflow;
		inflows.clear();

		currentVolume += totalInflow;
		currentVolume -= waterDischarge;

		int realDischarge = waterDischarge;
		if(currentVolume < 0){
			realDischarge = waterDischarge + currentVolume;
			currentVolume = 0;
		}
		if(currentVolume > maxVolume) {
			realDischarge += currentVolume - maxVolume;
			currentVolume = maxVolume;
		}

		if(realDischarge == 0)
			return;
		if(outflowRiverSection == null)
			return;

		outflowRiverSection.setRealDischarge(realDischarge);
	}

	public int getCurrentVolume() {
		return currentVolume;
	}
	public int getMaxVolume() {
		return maxVolume;
	}
	public String getName() {
		return name;
	}
}