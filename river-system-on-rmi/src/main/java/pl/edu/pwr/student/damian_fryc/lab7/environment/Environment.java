package pl.edu.pwr.student.damian_fryc.lab7.environment;

import interfaces.IEnvironment;
import interfaces.IRiverSection;
import interfaces.ITailor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class Environment extends UnicastRemoteObject implements IEnvironment {
    private final String name;
	private ITailor it;
	private final ArrayList<RiverSectionInfo> riverSections = new ArrayList<>();

	public Environment(int port, String name) throws RemoteException {
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

	public void setRainfall(int riverId, int rainAmount){
		riverSections.get(riverId).rainAmount = rainAmount;
	}
	public void deliverNewRain(){
		for (RiverSectionInfo riverSection : riverSections) {
//			if(riverSection.rainAmount == 0) continue;
            try {
                riverSection.getIRiverSection().setRainfall(riverSection.rainAmount);
            } catch (RemoteException e) {
				System.err.println("Failed to send message to river section: " + e.getMessage());
            }
		}
	}
	public ArrayList<RiverSectionInfo> getRiverSections(){
		return riverSections;
	}

	@Override
	public void assignRiverSection(IRiverSection irs, String name) throws RemoteException {
		RiverSectionInfo riverSection = new RiverSectionInfo(irs, name);
		for (RiverSectionInfo riverSectionT : riverSections){
			if (Objects.equals(riverSection.getName(), riverSectionT.getName())) return;
		}
		riverSections.add(riverSection);
		System.out.println("Assigned river section " + riverSection);
	}
}
