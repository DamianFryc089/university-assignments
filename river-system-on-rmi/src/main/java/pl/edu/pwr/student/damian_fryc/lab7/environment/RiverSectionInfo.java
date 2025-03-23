package pl.edu.pwr.student.damian_fryc.lab7.environment;

import interfaces.IRiverSection;

public class RiverSectionInfo {
	public int rainAmount = 0;
	private final IRiverSection iRiverSection;
	private final String name;

	RiverSectionInfo(IRiverSection iRiverSection, String name) {
		this.iRiverSection = iRiverSection;
        this.name = name;
    }

	public IRiverSection getIRiverSection() {
		return iRiverSection;
	}

	public String getName() {
		return name;
	}
}