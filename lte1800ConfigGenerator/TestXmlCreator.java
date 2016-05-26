package lte1800ConfigGenerator;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class TestXmlCreator {
	private XmlCreator xmlCreator;

	@Before
	public void setUp() {
		xmlCreator = new XmlCreator();
	}

	@Test
	public void testCreateOutputFilePath() {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
		String expectedPath = "C:\\CG output\\Commissioning_BG0001_" + LocalDate.now().format(dateFormat) + ".xml";

		xmlCreator.createOutputFilePath("BG0001");
		String filePath = xmlCreator.outputFilePath;

		assertEquals(expectedPath, filePath);
	}

	@Test
	public void testCopyTemplateXmlFile() {
		xmlCreator.copyTemplateXmlFile("BG0001");
		File outputFile = xmlCreator.outputFile;

		if (!outputFile.exists()) {
			fail();
		}
		if (!xmlCreator.outputFile.delete()) {
			fail();
		}
	}
}
