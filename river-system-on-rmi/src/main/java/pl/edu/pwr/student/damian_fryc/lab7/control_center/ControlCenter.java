package pl.edu.pwr.student.damian_fryc.lab7.control_center;

import interfaces.IControlCenter;
import interfaces.IRetensionBasin;
import interfaces.ITailor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class ControlCenter extends UnicastRemoteObject implements IControlCenter {
    private final String name;
	private ITailor it;
	private final ArrayList<RetentionBasinInfo> retentionBasins = new ArrayList<>();

	public ControlCenter(int port, String name) throws RemoteException {
		super(port);
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

	public void setWaterDischarge(int basinId, int waterDischarge){
//		if(waterDischarge == 0) return;

		RetentionBasinInfo retentionBasin = retentionBasins.get(basinId);
        try {
            retentionBasin.getIRetensionBasin().setWaterDischarge(waterDischarge);
			retentionBasin.waterDischarge = waterDischarge;
        } catch (RemoteException e) {
			System.err.println("Failed to send message to retention basin: " + e.getMessage());
        }
	}

	public ArrayList<RetentionBasinInfo> updatePercentages(){
		for (RetentionBasinInfo retentionBasin : retentionBasins) {
            try {
                retentionBasin.fillingPercentage = retentionBasin.getIRetensionBasin().getFillingPercentage();
            } catch (RemoteException e) {
				System.err.println("Failed to send message to retention basin: " + e.getMessage());
				return new ArrayList<>();
            }
		}
		return retentionBasins;
	}

	@Override
	public void assignRetensionBasin(IRetensionBasin irb, String name) throws RemoteException {
		RetentionBasinInfo newRetentionBasin = new RetentionBasinInfo(irb, name);

		for (RetentionBasinInfo retentionBasinT : retentionBasins){
			if (Objects.equals(retentionBasinT.getName(), newRetentionBasin.getName())) return;
		}
		newRetentionBasin.waterDischarge = irb.getWaterDischarge();
		newRetentionBasin.fillingPercentage = irb.getFillingPercentage();
		retentionBasins.add(newRetentionBasin);
		System.out.println("Assigned " + newRetentionBasin.getName());
	}
}
