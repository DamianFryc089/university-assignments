package pl.edu.pwr.student.damian_fryc.lab7.control_center;

import interfaces.IRetensionBasin;

public class RetentionBasinInfo{
	public int waterDischarge = 0;
	public long fillingPercentage = 0;
	private final IRetensionBasin iRetensionBasin;
	private final String name;

	RetentionBasinInfo(IRetensionBasin iRetensionBasin, String name){
        this.iRetensionBasin = iRetensionBasin;
        this.name = name;
    }

	public IRetensionBasin getIRetensionBasin() {
		return iRetensionBasin;
	}

	public String getName() {
		return name;
	}
}