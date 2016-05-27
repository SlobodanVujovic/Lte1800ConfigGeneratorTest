package lte1800ConfigGenerator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestAllLteSites {
	private AllLteSites allLteSites;

	@Before
	public void setUp() {
		allLteSites = new AllLteSites();
		allLteSites.inputReader.setRadioInput("C:\\CG input test\\Radio test.xlsx");
		allLteSites.inputReader.setTransmissionInput("C:\\CG input test\\Transmission test.xlsx");
		allLteSites.inputReader.setConfigInput("C:\\CG input test\\Config input test.xlsx");
		allLteSites.createListOfAllSites();
	}

	@Test
	public void testCreateListOfAllSites() {
		LteSite lteSite0 = allLteSites.listOfAllSites.get(0);
		LteSite lteSite1 = allLteSites.listOfAllSites.get(1);
		LteSite lteSite2 = allLteSites.listOfAllSites.get(2);

		assertEquals(3, allLteSites.listOfAllSites.size());
		assertEquals(3, allLteSites.listOfAllSites.get(0).lteCells.size());
		assertEquals(27, allLteSites.listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.size());
		assertEquals("BA1081_02/7",
				allLteSites.listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.get("10812").cellName);
		assertEquals("2", allLteSites.listOfAllSites.get(0).lteCells.get("1").gsmNeighbours.get("10661").bcch);
		assertEquals("857", allLteSites.listOfAllSites.get(2).lteCells.get("3").gsmNeighbours.get("11001").bcch);

		assertEquals(38, lteSite0.uniqueGsmNeighbours.size());
		assertEquals(34, lteSite1.uniqueGsmNeighbours.size());
		assertEquals(29, lteSite2.uniqueGsmNeighbours.size());
		assertEquals(31, lteSite0.uniqueBcchOfNeighbours.size());
		assertEquals(31, lteSite1.uniqueBcchOfNeighbours.size());
		assertEquals(28, lteSite2.uniqueBcchOfNeighbours.size());
	}

}
