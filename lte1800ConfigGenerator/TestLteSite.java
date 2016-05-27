package lte1800ConfigGenerator;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestLteSite {
	private AllLteSites allLteSites;
	private List<LteSite> listOfAllSites;

	@Before
	public void setUp() {
		allLteSites = new AllLteSites();
		allLteSites.inputReader.setRadioInput("C:\\CG input test\\Radio test.xlsx");
		allLteSites.inputReader.setTransmissionInput("C:\\CG input test\\Transmission test.xlsx");
		allLteSites.inputReader.setConfigInput("C:\\CG input test\\Config input test.xlsx");
		listOfAllSites = allLteSites.listOfAllSites;

		allLteSites.createListOfAllSites();
	}

	@Test
	public void testCreateUniqueGsmNeighbours() {
		LteSite lteSite0 = listOfAllSites.get(0);
		LteSite lteSite1 = listOfAllSites.get(1);
		LteSite lteSite2 = listOfAllSites.get(2);

		assertEquals(38, lteSite0.uniqueGsmNeighbours.size());
		assertEquals(34, lteSite1.uniqueGsmNeighbours.size());
		assertEquals(29, lteSite2.uniqueGsmNeighbours.size());
	}

	@Test
	public void testCreateUniqueBcchOfNeighbours() {
		LteSite lteSite0 = listOfAllSites.get(0);
		LteSite lteSite1 = listOfAllSites.get(1);
		LteSite lteSite2 = listOfAllSites.get(2);

		assertEquals(31, lteSite0.uniqueBcchOfNeighbours.size());
		assertEquals(31, lteSite1.uniqueBcchOfNeighbours.size());
		assertEquals(28, lteSite2.uniqueBcchOfNeighbours.size());
	}
}
