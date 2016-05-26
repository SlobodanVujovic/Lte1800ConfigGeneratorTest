package lte1800ConfigGenerator;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestInputReader {
	private AllLteSites allLteSites;
	private List<LteSite> listOfAllSites;

	@Before
	public void setUp() {
		allLteSites = new AllLteSites();
		allLteSites.inputReader.setRadioInput("C:\\CG input test\\Radio.xlsx");
		allLteSites.inputReader.setTransmissionInput("C:\\CG input test\\Transmission.xlsx");
		allLteSites.inputReader.setConfigInput("C:\\CG input test\\Config input.xlsx");
		listOfAllSites = allLteSites.listOfAllSites;
	}

	@Test
	public void testReadTransmission() {
		allLteSites.inputReader.readTransmissionFile(listOfAllSites);

		assertEquals("BA1022", listOfAllSites.get(0).generalInfo.get("LocationId"));
		assertEquals("11022", listOfAllSites.get(0).generalInfo.get("eNodeBId"));
		assertEquals("SD_Smederevo", listOfAllSites.get(0).generalInfo.get("eNodeBName"));
		assertEquals("1178", listOfAllSites.get(0).transmission.get("cuVlanId"));
		assertEquals("10.85.163.66", listOfAllSites.get(0).transmission.get("cuDestIp"));
		assertEquals("10.85.163.64", listOfAllSites.get(0).transmission.get("cuSubnet"));
		assertEquals("27", listOfAllSites.get(0).transmission.get("cuSubnetSize"));
		assertEquals("10.85.163.65", listOfAllSites.get(0).transmission.get("cuGwIp"));
		assertEquals("10.85.145.216", listOfAllSites.get(0).transmission.get("mIp"));
		assertEquals("52", listOfAllSites.get(0).transmission.get("sVlanId"));
		assertEquals("10.85.171.66", listOfAllSites.get(0).transmission.get("sIp"));
		assertEquals("10.85.171.64", listOfAllSites.get(0).transmission.get("sSubnet"));
		assertEquals("27", listOfAllSites.get(0).transmission.get("sSubnetSize"));
		assertEquals("10.85.171.65", listOfAllSites.get(0).transmission.get("sGwIp"));
		assertEquals("10.245.228.60", listOfAllSites.get(0).transmission.get("topIp"));

		assertEquals("BA1023", listOfAllSites.get(1).generalInfo.get("LocationId"));
		assertEquals("11023", listOfAllSites.get(1).generalInfo.get("eNodeBId"));
		assertEquals("SD_Smederevo2", listOfAllSites.get(1).generalInfo.get("eNodeBName"));
		assertEquals("1178", listOfAllSites.get(1).transmission.get("cuVlanId"));
		assertEquals("10.85.163.67", listOfAllSites.get(1).transmission.get("cuDestIp"));
		assertEquals("10.85.163.64", listOfAllSites.get(1).transmission.get("cuSubnet"));
		assertEquals("27", listOfAllSites.get(1).transmission.get("cuSubnetSize"));
		assertEquals("10.85.163.65", listOfAllSites.get(1).transmission.get("cuGwIp"));
		assertEquals("10.85.145.217", listOfAllSites.get(1).transmission.get("mIp"));
		assertEquals("52", listOfAllSites.get(1).transmission.get("sVlanId"));
		assertEquals("10.85.171.67", listOfAllSites.get(1).transmission.get("sIp"));
		assertEquals("10.85.171.64", listOfAllSites.get(1).transmission.get("sSubnet"));
		assertEquals("27", listOfAllSites.get(1).transmission.get("sSubnetSize"));
		assertEquals("10.85.171.65", listOfAllSites.get(1).transmission.get("sGwIp"));
		assertEquals("10.245.228.60", listOfAllSites.get(1).transmission.get("topIp"));

		assertEquals("BA1048", listOfAllSites.get(2).generalInfo.get("LocationId"));
		assertEquals("11048", listOfAllSites.get(2).generalInfo.get("eNodeBId"));
		assertEquals("SA_Sabac", listOfAllSites.get(2).generalInfo.get("eNodeBName"));
		assertEquals("1164", listOfAllSites.get(2).transmission.get("cuVlanId"));
		assertEquals("10.85.165.146", listOfAllSites.get(2).transmission.get("cuDestIp"));
		assertEquals("10.85.165.144", listOfAllSites.get(2).transmission.get("cuSubnet"));
		assertEquals("28", listOfAllSites.get(2).transmission.get("cuSubnetSize"));
		assertEquals("10.85.165.145", listOfAllSites.get(2).transmission.get("cuGwIp"));
		assertEquals("10.85.145.179", listOfAllSites.get(2).transmission.get("mIp"));
		assertEquals("50", listOfAllSites.get(2).transmission.get("sVlanId"));
		assertEquals("10.85.173.146", listOfAllSites.get(2).transmission.get("sIp"));
		assertEquals("10.85.173.144", listOfAllSites.get(2).transmission.get("sSubnet"));
		assertEquals("28", listOfAllSites.get(2).transmission.get("sSubnetSize"));
		assertEquals("10.85.173.145", listOfAllSites.get(2).transmission.get("sGwIp"));
		assertEquals("10.245.228.60", listOfAllSites.get(2).transmission.get("topIp"));
	}

	@Test
	public void testReadRadioFileForCellInfo() {
		allLteSites.inputReader.readTransmissionFile(listOfAllSites);

		allLteSites.inputReader.readRadioFileForCellInfo(listOfAllSites);

		assertEquals(3, listOfAllSites.size());
		assertEquals(3, listOfAllSites.get(0).lteCells.size());

		assertEquals("10221", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("lnCellId"));
		assertEquals("1", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("localCellId"));
		assertEquals("88", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("pci"));
		assertEquals("96", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("tac"));
		assertEquals("BA1022_1", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("cellName"));
		assertEquals("50", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("rootSeqIndex"));
		assertEquals("460", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("maxPower"));
		assertEquals("20 MHz", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("channelBw"));
		assertEquals("1741", listOfAllSites.get(0).lteCells.get("1").cellInfo.get("dlEarfcn"));

		assertEquals("10482", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("lnCellId"));
		assertEquals("2", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("localCellId"));
		assertEquals("156", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("pci"));
		assertEquals("128", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("tac"));
		assertEquals("BA1048_2", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("cellName"));
		assertEquals("630", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("rootSeqIndex"));
		assertEquals("460", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("maxPower"));
		assertEquals("20 MHz", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("channelBw"));
		assertEquals("1741", listOfAllSites.get(2).lteCells.get("2").cellInfo.get("dlEarfcn"));
	}

	@Test
	public void testReadRadioFileForNeighbours() {
		allLteSites.inputReader.readTransmissionFile(listOfAllSites);
		allLteSites.inputReader.readRadioFileForCellInfo(listOfAllSites);

		allLteSites.inputReader.readRadioFileForNeighbours(listOfAllSites);

		assertEquals(3, listOfAllSites.size());
		assertEquals(3, listOfAllSites.get(0).lteCells.size());
		assertEquals(27, listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.size());
		assertEquals("BA1081_02/7", listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.get("10812").cellName);
		assertEquals("2", listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.get("10661").bcch);
		assertEquals("857", listOfAllSites.get(2).lteCells.get("3").gsmNeighbours.get("11001").bcch);
	}

	@Test
	public void testReadConfigFile() {
		allLteSites.createListOfAllSites();

		LteSite lteSite0 = listOfAllSites.get(0);
		LteSite lteSite1 = listOfAllSites.get(1);
		LteSite lteSite2 = listOfAllSites.get(2);

		assertEquals("2", lteSite0.hardware.get("numberOfRfModules"));
		assertEquals("2", lteSite0.hardware.get("numberOfSharedRfModules"));
		assertEquals("1-1", lteSite0.hardware.get("cell1Ports"));
		assertEquals("3-3", lteSite0.hardware.get("cell2Ports"));
		assertEquals("5-5", lteSite0.hardware.get("cell3Ports"));
		assertEquals("", lteSite0.hardware.get("cell4Ports"));
		assertEquals("DA", lteSite0.hardware.get("rf1IsShared"));
		assertEquals("DA", lteSite0.hardware.get("rf2IsShared"));
		assertEquals("", lteSite0.hardware.get("rf3IsShared"));
		assertEquals("", lteSite0.hardware.get("rf4IsShared"));
		assertEquals("3", lteSite1.hardware.get("numberOfRfModules"));
		assertEquals("3", lteSite1.hardware.get("numberOfSharedRfModules"));
		assertEquals("1-3", lteSite1.hardware.get("cell1Ports"));
		assertEquals("1-3", lteSite1.hardware.get("cell2Ports"));
		assertEquals("1-3", lteSite1.hardware.get("cell3Ports"));
		assertEquals("", lteSite1.hardware.get("cell4Ports"));
		assertEquals("DA", lteSite1.hardware.get("rf1IsShared"));
		assertEquals("DA", lteSite1.hardware.get("rf2IsShared"));
		assertEquals("DA", lteSite1.hardware.get("rf3IsShared"));
		assertEquals("", lteSite1.hardware.get("rf4IsShared"));
		assertEquals("4", lteSite2.hardware.get("numberOfRfModules"));
		assertEquals("0", lteSite2.hardware.get("numberOfSharedRfModules"));
		assertEquals("1-3", lteSite2.hardware.get("cell1Ports"));
		assertEquals("1-3", lteSite2.hardware.get("cell2Ports"));
		assertEquals("1-3", lteSite2.hardware.get("cell3Ports"));
		assertEquals("1-3", lteSite2.hardware.get("cell4Ports"));
		assertEquals("NE", lteSite2.hardware.get("rf1IsShared"));
		assertEquals("NE", lteSite2.hardware.get("rf2IsShared"));
		assertEquals("NE", lteSite2.hardware.get("rf3IsShared"));
		assertEquals("NE", lteSite2.hardware.get("rf4IsShared"));
	}

}
