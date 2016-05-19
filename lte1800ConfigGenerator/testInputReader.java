package lte1800ConfigGenerator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class testInputReader {
	private InputReader inputReader;

	@Before
	public void setUp() {
		inputReader = new InputReader();
		inputReader.setRadioInput("C:\\CG input test\\Radio.xlsx");
		inputReader.setTransmissionInput("C:\\CG input test\\Transmission.xlsx");
	}

	@Test
	public void testReadTransmission() {
		inputReader.readTransmissionFile();

		assertEquals("BA1022", inputReader.allSites.listOfSites.get(0).generalInfo.get("LocationId"));
		assertEquals("11022", inputReader.allSites.listOfSites.get(0).generalInfo.get("eNodeBId"));
		assertEquals("SD_Smederevo", inputReader.allSites.listOfSites.get(0).generalInfo.get("eNodeBName"));
		assertEquals("1178", inputReader.allSites.listOfSites.get(0).transmission.get("cuVlanId"));
		assertEquals("10.85.163.66", inputReader.allSites.listOfSites.get(0).transmission.get("cuDestIp"));
		assertEquals("10.85.163.64", inputReader.allSites.listOfSites.get(0).transmission.get("cuSubnet"));
		assertEquals("27", inputReader.allSites.listOfSites.get(0).transmission.get("cuSubnetSize"));
		assertEquals("10.85.163.65", inputReader.allSites.listOfSites.get(0).transmission.get("cuGwIp"));
		assertEquals("10.85.145.216", inputReader.allSites.listOfSites.get(0).transmission.get("mIp"));
		assertEquals("52", inputReader.allSites.listOfSites.get(0).transmission.get("sVlanId"));
		assertEquals("10.85.171.66", inputReader.allSites.listOfSites.get(0).transmission.get("sIp"));
		assertEquals("10.85.171.64", inputReader.allSites.listOfSites.get(0).transmission.get("sSubnet"));
		assertEquals("27", inputReader.allSites.listOfSites.get(0).transmission.get("sSubnetSize"));
		assertEquals("10.85.171.65", inputReader.allSites.listOfSites.get(0).transmission.get("sGwIp"));
		assertEquals("10.245.228.60", inputReader.allSites.listOfSites.get(0).transmission.get("topIp"));
		assertEquals("BA1023", inputReader.allSites.listOfSites.get(1).generalInfo.get("LocationId"));
		assertEquals("11023", inputReader.allSites.listOfSites.get(1).generalInfo.get("eNodeBId"));
		assertEquals("SD_Smederevo2", inputReader.allSites.listOfSites.get(1).generalInfo.get("eNodeBName"));
		assertEquals("1178", inputReader.allSites.listOfSites.get(1).transmission.get("cuVlanId"));
		assertEquals("10.85.163.67", inputReader.allSites.listOfSites.get(1).transmission.get("cuDestIp"));
		assertEquals("10.85.163.64", inputReader.allSites.listOfSites.get(1).transmission.get("cuSubnet"));
		assertEquals("27", inputReader.allSites.listOfSites.get(1).transmission.get("cuSubnetSize"));
		assertEquals("10.85.163.65", inputReader.allSites.listOfSites.get(1).transmission.get("cuGwIp"));
		assertEquals("10.85.145.217", inputReader.allSites.listOfSites.get(1).transmission.get("mIp"));
		assertEquals("52", inputReader.allSites.listOfSites.get(1).transmission.get("sVlanId"));
		assertEquals("10.85.171.67", inputReader.allSites.listOfSites.get(1).transmission.get("sIp"));
		assertEquals("10.85.171.64", inputReader.allSites.listOfSites.get(1).transmission.get("sSubnet"));
		assertEquals("27", inputReader.allSites.listOfSites.get(1).transmission.get("sSubnetSize"));
		assertEquals("10.85.171.65", inputReader.allSites.listOfSites.get(1).transmission.get("sGwIp"));
		assertEquals("10.245.228.60", inputReader.allSites.listOfSites.get(1).transmission.get("topIp"));
		assertEquals("BA1048", inputReader.allSites.listOfSites.get(2).generalInfo.get("LocationId"));
		assertEquals("11048", inputReader.allSites.listOfSites.get(2).generalInfo.get("eNodeBId"));
		assertEquals("SA_Sabac", inputReader.allSites.listOfSites.get(2).generalInfo.get("eNodeBName"));
		assertEquals("1164", inputReader.allSites.listOfSites.get(2).transmission.get("cuVlanId"));
		assertEquals("10.85.165.146", inputReader.allSites.listOfSites.get(2).transmission.get("cuDestIp"));
		assertEquals("10.85.165.144", inputReader.allSites.listOfSites.get(2).transmission.get("cuSubnet"));
		assertEquals("28", inputReader.allSites.listOfSites.get(2).transmission.get("cuSubnetSize"));
		assertEquals("10.85.165.145", inputReader.allSites.listOfSites.get(2).transmission.get("cuGwIp"));
		assertEquals("10.85.145.179", inputReader.allSites.listOfSites.get(2).transmission.get("mIp"));
		assertEquals("50", inputReader.allSites.listOfSites.get(2).transmission.get("sVlanId"));
		assertEquals("10.85.173.146", inputReader.allSites.listOfSites.get(2).transmission.get("sIp"));
		assertEquals("10.85.173.144", inputReader.allSites.listOfSites.get(2).transmission.get("sSubnet"));
		assertEquals("28", inputReader.allSites.listOfSites.get(2).transmission.get("sSubnetSize"));
		assertEquals("10.85.173.145", inputReader.allSites.listOfSites.get(2).transmission.get("sGwIp"));
		assertEquals("10.245.228.60", inputReader.allSites.listOfSites.get(2).transmission.get("topIp"));
	}
}
