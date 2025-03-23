package pl.edu.pwr.student.damian_fryc.lab7.river_section;

import interfaces.IRetensionBasin;
import interfaces.IRiverSection;
import interfaces.ITailor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RiverSection extends UnicastRemoteObject implements IRiverSection {
    public final int[] waterFragments;
	private final String name;
	private ITailor it;

	private IRetensionBasin outflowBasin;

    private int realDischarge = 0;
	private int rainfall = 0;

	public RiverSection(int riverSize, int port, String name) throws RemoteException {
		super(port);
        waterFragments = new int[riverSize];
		this.name = name;
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
	public void setRealDischarge(int realDischarge) {
		this.realDischarge = realDischarge;
	}

	@Override
	public void setRainfall(int rainfall) {
		this.rainfall = rainfall;
	}

	@Override
	public void assignRetensionBasin(IRetensionBasin irb, String name) throws RemoteException {
		outflowBasin = irb;
        System.out.println("Assigned retention basin" + name);
	}

	public void riverLogic() throws RemoteException {
		int waterToRemove = waterFragments[waterFragments.length - 1];
		for (int i = waterFragments.length - 1; i >= 1; i--) {
			waterFragments[i] = waterFragments[i-1];
		}
		waterFragments[0] = realDischarge + rainfall;
		realDischarge = 0;
		rainfall = 0;

		if(waterToRemove == 0)
			return;

		outflowBasin.setWaterInflow(waterToRemove, name);
	}
}
