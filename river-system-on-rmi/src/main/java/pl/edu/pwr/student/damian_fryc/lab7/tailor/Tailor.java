package pl.edu.pwr.student.damian_fryc.lab7.tailor;

import interfaces.*;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Tailor implements ITailor {

    private final String name;
    private final UpdateListener updateListener;

    private final Map<IControlCenter, String> ccmap = new HashMap<>();
    private final Map<IEnvironment, String> emap = new HashMap<>();
    private final Map<IRetensionBasin, String> rbmap = new HashMap<>();
    private final Map<IRiverSection, String> rsmap = new HashMap<>();

    public Tailor(int port, String name, UpdateListener mainApp) throws RemoteException {

        this.name = name;
        this.updateListener = mainApp;

        UnicastRemoteObject.exportObject(this, port);
        Registry r = LocateRegistry.createRegistry(port);
        r.rebind(name, this);
    }
    public void closeServer() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this, true);
    }
    @Override
    public boolean register(Remote r, String name) throws RemoteException {
        System.out.println(r);
        switch (r){
            case IControlCenter cc -> {
                if(!ccmap.containsKey(cc)) {
                    ccmap.put(cc, name);
                    updateListener.elementAdded(cc, name);
                    System.out.println("Registration of control center named: " + name);
                    return true;
                }
            }
            case IEnvironment e -> {
                if(!emap.containsKey(e)) {
                    updateListener.elementAdded(e, name);
                    emap.put(e, name);
                    System.out.println("Registration of environment named: " + name);
                    return true;
                }
            }
            case IRetensionBasin rb -> {
                if(!rbmap.containsKey(rb)) {
                    rbmap.put(rb, name);
                    updateListener.elementAdded(rb, name);
                    System.out.println("Registration of retention basin named: " + name);
                    return true;
                }
            }
            case IRiverSection rs -> {
                if(!rsmap.containsKey(rs)) {
                    rsmap.put(rs, name);
                    updateListener.elementAdded(rs, name);
                    System.out.println("Registration of river section named: " + name);
                    return true;
                }
            }
            default -> {
                System.out.println("Unexpected value: " + r);
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean unregister(Remote r) throws RemoteException {
        if(r instanceof IControlCenter) {
            if(ccmap.containsKey(r)) {
                ccmap.remove(r);
                updateListener.elementRemoved(r, ccmap.get(r));
                System.out.println("Unregistration of control center named: " + name);
                return true;
            }
        }
        if(r instanceof IEnvironment) {
            if(emap.containsKey(r)) {
                emap.remove(r);
                updateListener.elementRemoved(r, emap.get(r));
                System.out.println("Unregistration of environment named: " + name);
                return true;
            }
        }
        if(r instanceof IRetensionBasin) {
            if(rbmap.containsKey(r)) {
                rbmap.remove(r);
                updateListener.elementRemoved(r, rbmap.get(r));
                System.out.println("Unregistration of retention basin named: " + name);
                return true;
            }
        }
        if(r instanceof IRiverSection) {
            if(rsmap.containsKey(r)) {
                rsmap.remove(r);
                updateListener.elementRemoved(r, rsmap.get(r));
                System.out.println("Unregistration of river section named: " + name);
                return true;
            }
        }
        return false;
    }

    public void assignBasinToCenter(IRetensionBasin rb, IControlCenter cc) throws RemoteException {
        cc.assignRetensionBasin(rb, rbmap.get(rb));
    }
    public void assignRiverToEnvironment(IRiverSection rs, IEnvironment e) throws RemoteException {
        e.assignRiverSection(rs, rsmap.get(rs));
    }
    public void assignBasinToRiver(IRetensionBasin rb, IRiverSection rs) throws RemoteException {
        rs.assignRetensionBasin(rb, rbmap.get(rb));
    }
    public void assignRiverToBasin(IRiverSection rs, IRetensionBasin rb) throws RemoteException {
        rb.assignRiverSection(rs, rsmap.get(rs));
    }

}
