package lte1800ConfigGenerator;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

public class TestInputReader {
	private AllLteSites allLteSites;
	private List<LteSite> listOfAllSites;

	@Before
	public void setUp() {
		allLteSites = new AllLteSites();
		setInputTestFiles();
		listOfAllSites = allLteSites.listOfAllSites;
	}

	private void setInputTestFiles() {
		allLteSites.inputReader.setRadioInput("C:\\CG input test\\Radio test.xlsx");
		allLteSites.inputReader.setTransmissionInput("C:\\CG input test\\Transmission test.xlsx");
		allLteSites.inputReader.setConfigInput("C:\\CG input test\\Config input test.xlsx");
	}

	private String readTransmissionInputFile(String siteCode, int columnNumber) {
		String result = null;
		XSSFWorkbook workbook = null;
		try {
			File transmissionInput = allLteSites.inputReader.getTransmissionInput();
			workbook = createExcelWorkbook(transmissionInput);
			XSSFSheet sheet1 = workbook.getSheetAt(0);
			int numberOfRows = sheet1.getLastRowNum();
			for (int i = 2; i <= numberOfRows; i++) {
				Row fromRow = sheet1.getRow(i);
				Cell locationId = fromRow.getCell(0);
				if (locationId.getStringCellValue().equals(siteCode)) {
					DataFormatter dataFormatter = new DataFormatter();
					result = dataFormatter.formatCellValue(fromRow.getCell(columnNumber));
					break;
				}
			}
			return result;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private XSSFWorkbook createExcelWorkbook(File fromFile) {
		OPCPackage opcPackage;
		XSSFWorkbook workbook = null;
		try {
			opcPackage = OPCPackage.open(fromFile);
			workbook = new XSSFWorkbook(opcPackage);
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	@Test
	public void testReadTransmission() {
		allLteSites.inputReader.readTransmissionFile(listOfAllSites);

		String siteCode = listOfAllSites.get(0).generalInfo.get("LocationId");
		String expected = readTransmissionInputFile(siteCode, 0);
		assertEquals(expected, listOfAllSites.get(0).generalInfo.get("LocationId"));
		expected = readTransmissionInputFile(siteCode, 1);
		assertEquals(expected, listOfAllSites.get(0).generalInfo.get("eNodeBId"));
		expected = readTransmissionInputFile(siteCode, 2);
		assertEquals(expected, listOfAllSites.get(0).generalInfo.get("eNodeBName"));
		expected = readTransmissionInputFile(siteCode, 3);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("cuVlanId"));
		expected = readTransmissionInputFile(siteCode, 4);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("cuDestIp"));
		expected = readTransmissionInputFile(siteCode, 5);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("cuSubnet"));
		expected = readTransmissionInputFile(siteCode, 6);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("cuSubnetSize"));
		expected = readTransmissionInputFile(siteCode, 7);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("cuGwIp"));
		expected = readTransmissionInputFile(siteCode, 8);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("mIp"));
		expected = readTransmissionInputFile(siteCode, 9);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("sVlanId"));
		expected = readTransmissionInputFile(siteCode, 10);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("sIp"));
		expected = readTransmissionInputFile(siteCode, 11);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("sSubnet"));
		expected = readTransmissionInputFile(siteCode, 12);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("sSubnetSize"));
		expected = readTransmissionInputFile(siteCode, 13);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("sGwIp"));
		expected = readTransmissionInputFile(siteCode, 14);
		assertEquals(expected, listOfAllSites.get(0).transmission.get("topIp"));

		siteCode = listOfAllSites.get(1).generalInfo.get("LocationId");
		expected = readTransmissionInputFile(siteCode, 0);
		assertEquals(expected, listOfAllSites.get(1).generalInfo.get("LocationId"));
		expected = readTransmissionInputFile(siteCode, 1);
		assertEquals(expected, listOfAllSites.get(1).generalInfo.get("eNodeBId"));
		expected = readTransmissionInputFile(siteCode, 2);
		assertEquals(expected, listOfAllSites.get(1).generalInfo.get("eNodeBName"));
		expected = readTransmissionInputFile(siteCode, 3);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("cuVlanId"));
		expected = readTransmissionInputFile(siteCode, 4);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("cuDestIp"));
		expected = readTransmissionInputFile(siteCode, 5);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("cuSubnet"));
		expected = readTransmissionInputFile(siteCode, 6);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("cuSubnetSize"));
		expected = readTransmissionInputFile(siteCode, 7);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("cuGwIp"));
		expected = readTransmissionInputFile(siteCode, 8);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("mIp"));
		expected = readTransmissionInputFile(siteCode, 9);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("sVlanId"));
		expected = readTransmissionInputFile(siteCode, 10);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("sIp"));
		expected = readTransmissionInputFile(siteCode, 11);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("sSubnet"));
		expected = readTransmissionInputFile(siteCode, 12);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("sSubnetSize"));
		expected = readTransmissionInputFile(siteCode, 13);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("sGwIp"));
		expected = readTransmissionInputFile(siteCode, 14);
		assertEquals(expected, listOfAllSites.get(1).transmission.get("topIp"));

		siteCode = listOfAllSites.get(2).generalInfo.get("LocationId");
		expected = readTransmissionInputFile(siteCode, 0);
		assertEquals(expected, listOfAllSites.get(2).generalInfo.get("LocationId"));
		expected = readTransmissionInputFile(siteCode, 1);
		assertEquals(expected, listOfAllSites.get(2).generalInfo.get("eNodeBId"));
		expected = readTransmissionInputFile(siteCode, 2);
		assertEquals(expected, listOfAllSites.get(2).generalInfo.get("eNodeBName"));
		expected = readTransmissionInputFile(siteCode, 3);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("cuVlanId"));
		expected = readTransmissionInputFile(siteCode, 4);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("cuDestIp"));
		expected = readTransmissionInputFile(siteCode, 5);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("cuSubnet"));
		expected = readTransmissionInputFile(siteCode, 6);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("cuSubnetSize"));
		expected = readTransmissionInputFile(siteCode, 7);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("cuGwIp"));
		expected = readTransmissionInputFile(siteCode, 8);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("mIp"));
		expected = readTransmissionInputFile(siteCode, 9);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("sVlanId"));
		expected = readTransmissionInputFile(siteCode, 10);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("sIp"));
		expected = readTransmissionInputFile(siteCode, 11);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("sSubnet"));
		expected = readTransmissionInputFile(siteCode, 12);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("sSubnetSize"));
		expected = readTransmissionInputFile(siteCode, 13);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("sGwIp"));
		expected = readTransmissionInputFile(siteCode, 14);
		assertEquals(expected, listOfAllSites.get(2).transmission.get("topIp"));
	}

	@Test
	public void testReadRadioFileForCellInfo() {
		allLteSites.inputReader.readTransmissionFile(listOfAllSites);

		allLteSites.inputReader.readRadioFileForCellInfo(listOfAllSites);

		assertEquals(3, listOfAllSites.size());
		assertEquals(3, listOfAllSites.get(0).lteCells.size());

		String siteCode = listOfAllSites.get(0).generalInfo.get("LocationId");
		String expected = readRadioInputFile(siteCode, 3, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("lnCellId"));
		expected = readRadioInputFile(siteCode, 4, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("localCellId"));
		expected = readRadioInputFile(siteCode, 5, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("pci"));
		expected = readRadioInputFile(siteCode, 6, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("tac"));
		expected = readRadioInputFile(siteCode, 7, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("cellName"));
		expected = readRadioInputFile(siteCode, 8, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("rootSeqIndex"));
		expected = readRadioInputFile(siteCode, 9, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("maxPower"));
		expected = readRadioInputFile(siteCode, 10, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("channelBw"));
		expected = readRadioInputFile(siteCode, 11, "2");
		assertEquals(expected, listOfAllSites.get(0).lteCells.get("2").cellInfo.get("dlEarfcn"));

		siteCode = listOfAllSites.get(2).generalInfo.get("LocationId");
		expected = readRadioInputFile(siteCode, 3, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("lnCellId"));
		expected = readRadioInputFile(siteCode, 4, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("localCellId"));
		expected = readRadioInputFile(siteCode, 5, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("pci"));
		expected = readRadioInputFile(siteCode, 6, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("tac"));
		expected = readRadioInputFile(siteCode, 7, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("cellName"));
		expected = readRadioInputFile(siteCode, 8, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("rootSeqIndex"));
		expected = readRadioInputFile(siteCode, 9, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("maxPower"));
		expected = readRadioInputFile(siteCode, 10, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("channelBw"));
		expected = readRadioInputFile(siteCode, 11, "1");
		assertEquals(expected, listOfAllSites.get(2).lteCells.get("1").cellInfo.get("dlEarfcn"));
	}

	private String readRadioInputFile(String siteCode, int columnNumber, String localCell) {
		String result = null;
		XSSFWorkbook workbook = null;
		try {
			File radioInput = allLteSites.inputReader.getRadioInput();
			workbook = createExcelWorkbook(radioInput);
			XSSFSheet sheet1 = workbook.getSheetAt(0);
			int numberOfRows = sheet1.getLastRowNum();
			for (int i = 1; i <= numberOfRows; i++) {
				Row fromRow = sheet1.getRow(i);
				Cell locationId = fromRow.getCell(1);
				Cell localCellId = fromRow.getCell(4);
				if (locationId.getStringCellValue().equals(siteCode)
						&& localCellId.getStringCellValue().equals(localCell)) {
					DataFormatter dataFormatter = new DataFormatter();
					result = dataFormatter.formatCellValue(fromRow.getCell(columnNumber));
					break;
				}
			}
			return result;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void testReadRadioFileForNeighbours() {
		allLteSites.inputReader.readTransmissionFile(listOfAllSites);
		allLteSites.inputReader.readRadioFileForCellInfo(listOfAllSites);

		allLteSites.inputReader.readRadioFileForNeighbours(listOfAllSites);

		assertEquals(3, listOfAllSites.size());
		assertEquals(3, listOfAllSites.get(0).lteCells.size());
		assertEquals(24, listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.size());
		assertEquals("BG0012_04/3", listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.get("123").cellName);
		assertEquals("8", listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.get("2701").bcch);
		assertEquals("738", listOfAllSites.get(2).lteCells.get("3").gsmNeighbours.get("3652").bcch);
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
		assertEquals("DA", lteSite0.hardware.get("ftif"));
		assertEquals("EIF3", lteSite0.hardware.get("gsmPort"));
		assertEquals("", lteSite0.hardware.get("umtsPort"));
		assertEquals("2", lteSite1.hardware.get("numberOfRfModules"));
		assertEquals("0", lteSite1.hardware.get("numberOfSharedRfModules"));
		assertEquals("1-1", lteSite1.hardware.get("cell1Ports"));
		assertEquals("3-3", lteSite1.hardware.get("cell2Ports"));
		assertEquals("5-5", lteSite1.hardware.get("cell3Ports"));
		assertEquals("", lteSite1.hardware.get("cell4Ports"));
		assertEquals("NE", lteSite1.hardware.get("rf1IsShared"));
		assertEquals("NE", lteSite1.hardware.get("rf2IsShared"));
		assertEquals("", lteSite1.hardware.get("rf3IsShared"));
		assertEquals("", lteSite1.hardware.get("rf4IsShared"));
		assertEquals("NE", lteSite1.hardware.get("ftif"));
		assertEquals("", lteSite1.hardware.get("gsmPort"));
		assertEquals("", lteSite1.hardware.get("umtsPort"));
		assertEquals("2", lteSite2.hardware.get("numberOfRfModules"));
		assertEquals("0", lteSite2.hardware.get("numberOfSharedRfModules"));
		assertEquals("1-1", lteSite2.hardware.get("cell1Ports"));
		assertEquals("3-3", lteSite2.hardware.get("cell2Ports"));
		assertEquals("5-5", lteSite2.hardware.get("cell3Ports"));
		assertEquals("", lteSite2.hardware.get("cell4Ports"));
		assertEquals("NE", lteSite2.hardware.get("rf1IsShared"));
		assertEquals("NE", lteSite2.hardware.get("rf2IsShared"));
		assertEquals("", lteSite2.hardware.get("rf3IsShared"));
		assertEquals("", lteSite2.hardware.get("rf4IsShared"));
		assertEquals("DA", lteSite2.hardware.get("ftif"));
		assertEquals("EIF3", lteSite2.hardware.get("gsmPort"));
		assertEquals("", lteSite2.hardware.get("umtsPort"));
	}

}
