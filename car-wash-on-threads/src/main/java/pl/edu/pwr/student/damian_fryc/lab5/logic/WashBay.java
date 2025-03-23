package pl.edu.pwr.student.damian_fryc.lab5.logic;

import pl.edu.pwr.student.damian_fryc.lab5.view.CarUI;
import pl.edu.pwr.student.damian_fryc.lab5.view.WashBayUI;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class WashBay{
	public final Washer[] waterWashers;
	public final Washer[] soapWashers;
	public final int id;
	private Car car;
	public final WashBayUI washBayUI;

	public WashBay(int id, Washer[] waterWashers, Washer[] soapWashers, WashBayUI washBayUI) {
		this.waterWashers = waterWashers;
		this.soapWashers = soapWashers;
        this.id = id;
        this.washBayUI = washBayUI;
    }

	public synchronized boolean isEmpty() {
        return car == null;
    }

	public synchronized void setCar(Car car) {
		this.car = car;
	}

	public synchronized void removeCar(){
		this.car = null;
	}

	public Washer tryUseLeft(Washer.WasherType washerType, long waitTime) throws InterruptedException {
		if(washerType == Washer.WasherType.WATER){
			if(waterWashers[0] == null) return null;
			if(waterWashers[0].semaphore.tryAcquire(waitTime, TimeUnit.MILLISECONDS))
				return waterWashers[0];
			else return null;
		}
		if(washerType == Washer.WasherType.SOAP){
			if(soapWashers[0] == null) return null;
			if(soapWashers[0].semaphore.tryAcquire(waitTime, TimeUnit.MILLISECONDS))
				return soapWashers[0];
			else return null;
		}
		return null;
	}
	public Washer tryUseRight(Washer.WasherType washerType, long waitTime) throws InterruptedException {
		if(washerType == Washer.WasherType.WATER){
			if(waterWashers[1] == null) return null;
			if(waterWashers[1].semaphore.tryAcquire(waitTime, TimeUnit.MILLISECONDS))
				return waterWashers[1];
			else return null;
		}
		if(washerType == Washer.WasherType.SOAP){
			if(soapWashers[1] == null) return null;
			if(soapWashers[1].semaphore.tryAcquire(waitTime, TimeUnit.MILLISECONDS))
				return soapWashers[1];
			else return null;
		}
		return null;
	}

	@Override
	public String toString() {
		return "WashBay{" +
				"waterWashers=" + Arrays.toString(waterWashers) +
				", soapWashers=" + Arrays.toString(soapWashers) +
				'}';
	}

}
