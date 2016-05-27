package lte1800ConfigGenerator;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestAllConfigFiles {
	private AllConfigFiles allConfigFiles;

	@Before
	public void setUp() {
		allConfigFiles = new AllConfigFiles();
		allConfigFiles.allLteSites.inputReader.setRadioInput("C:\\CG input test\\Radio test.xlsx");
		allConfigFiles.allLteSites.inputReader.setTransmissionInput("C:\\CG input test\\Transmission test.xlsx");
		allConfigFiles.allLteSites.inputReader.setConfigInput("C:\\CG input test\\Config input test.xlsx");
		allConfigFiles.createConfigFile();
	}

	// @Ignore
	@Test
	public void testEditXmlDateAndTime() {
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList logList = xmlDocument.getElementsByTagName("log");
			Node logNode = logList.item(0);
			NamedNodeMap logAttributes = logNode.getAttributes();
			Node dateTimeAttribute = logAttributes.getNamedItem("dateTime");
			String dateTime = dateTimeAttribute.getTextContent();
			String dateTimeAtMinuteAccuracy = dateTime.substring(0, 16) + ":00";

			assertEquals(
					LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
					dateTimeAtMinuteAccuracy);
		}
	}

	// @Ignore
	@Test
	public void testEditMrbts_eNodeBId() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				NamedNodeMap managedObjectAttributes = managedObjectNode.getAttributes();
				Node distNameAttribute = managedObjectAttributes.getNamedItem("distName");
				String distNameValue = distNameAttribute.getNodeValue();

				assertThat(distNameValue.indexOf("MRBTS-" + lteSite.generalInfo.get("eNodeBId")), is(not(-1)));
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLnbts_eNodeBId() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				NamedNodeMap managedObjectAttributes = managedObjectNode.getAttributes();
				Node distNameAttribute = managedObjectAttributes.getNamedItem("distName");
				String distNameValue = distNameAttribute.getNodeValue();
				if (distNameValue.contains("LNBTS")) {

					assertThat(distNameValue.indexOf("LNBTS-" + lteSite.generalInfo.get("eNodeBId")), is(not(-1)));
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLncellId() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			int cellId = 1;
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				NamedNodeMap managedObjectAttributes = managedObjectNode.getAttributes();
				Node distNameAttribute = managedObjectAttributes.getNamedItem("distName");
				String distNameValue = distNameAttribute.getNodeValue();
				if (distNameValue.contains("LNCEL")) {
					LteCell lteCell = lteSite.lteCells.get(String.valueOf(cellId));
					String lncellId = lteCell.cellInfo.get("lnCellId");

					assertThat(distNameValue.indexOf("LNCEL-" + lncellId), is(not(-1)));

					Node nextManagedObjectNode = managedObjectList.item(i + 1);
					NamedNodeMap nextManagedObjectAttributes = nextManagedObjectNode.getAttributes();
					Node classNameAttribute = nextManagedObjectAttributes.getNamedItem("class");
					String classNameValue = classNameAttribute.getNodeValue();
					if (classNameValue.equals("LNCEL")) {
						++cellId;
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditBtsscl_BtsId_BtsName() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				NamedNodeMap managedObjectAttributes = managedObjectNode.getAttributes();
				Node classNameAttribute = managedObjectAttributes.getNamedItem("class");
				String distNameValue = classNameAttribute.getNodeValue();
				if (distNameValue.contains("BTSSCL")) {
					NodeList childNodeList = managedObjectNode.getChildNodes();
					for (int j = 0; j < childNodeList.getLength(); j++) {
						Node childNode = childNodeList.item(j);
						if (childNode.getNodeName().equals("p")) {
							NamedNodeMap childNodeAttributes = childNode.getAttributes();
							Node nameNodeAttribute = childNodeAttributes.getNamedItem("name");
							String nameNodeValue = nameNodeAttribute.getNodeValue();
							if (nameNodeValue.equals("btsId")) {

								assertEquals(lteSite.generalInfo.get("eNodeBId"), childNode.getTextContent());

							} else if (nameNodeValue.equals("btsName")) {

								assertEquals(lteSite.generalInfo.get("LocationId"), childNode.getTextContent());

							}
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLnbts_EnbName() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				NamedNodeMap managedObjectAttributes = managedObjectNode.getAttributes();
				Node classNameAttribute = managedObjectAttributes.getNamedItem("class");
				String distNameValue = classNameAttribute.getNodeValue();
				if (distNameValue.contains("LNBTS")) {
					NodeList childNodeList = managedObjectNode.getChildNodes();
					for (int j = 0; j < childNodeList.getLength(); j++) {
						Node childNode = childNodeList.item(j);
						if (childNode.getNodeName().equals("p")) {
							NamedNodeMap childNodeAttributes = childNode.getAttributes();
							Node nameNodeAttribute = childNodeAttributes.getNamedItem("name");
							String nameNodeValue = nameNodeAttribute.getNodeValue();
							if (nameNodeValue.equals("enbName")) {

								assertEquals(lteSite.generalInfo.get("LocationId"), childNode.getTextContent());

							}
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLnadjg_cellParameters() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"LNADJG\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList lnadjgNodeList = (NodeList) result;

			assertEquals(lnadjgNodeList.getLength(), lteSite.uniqueGsmNeighbours.size());
		}
	}

	// @Ignore
	@Test
	public void testEditLncel_cellParameters() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			for (Map.Entry<String, LteCell> entry : lteSite.lteCells.entrySet()) {
				LteCell lteCell = entry.getValue();
				try {
					String eNodeBId = lteSite.generalInfo.get("eNodeBId");
					expression = xPath.compile("//cmData/managedObject[@class=\"LNCEL\" and @distName=\"MRBTS-"
							+ eNodeBId + "/LNBTS-" + eNodeBId + "/LNCEL-" + lteCell.cellInfo.get("lnCellId") + "\"]");
					result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				NodeList lncelNodeList = (NodeList) result;
				for (int i = 0; i < lncelNodeList.getLength(); i++) {
					Node managedObjectNode = lncelNodeList.item(i);
					NodeList childNodesList = managedObjectNode.getChildNodes();
					for (int j = 0; j < childNodesList.getLength(); j++) {
						Node childNode = childNodesList.item(j);
						if (childNode.getNodeName().equals("p")) {
							NamedNodeMap childNodeAttributes = childNode.getAttributes();
							Node nameNodeAttribute = childNodeAttributes.getNamedItem("name");
							String nameNodeValue = nameNodeAttribute.getNodeValue();
							if (nameNodeValue.equals("dlChBw")) {
								assertEquals(lteCell.cellInfo.get("channelBw"), childNode.getTextContent());
							} else if (nameNodeValue.equals("earfcnDL")) {
								assertEquals(lteCell.cellInfo.get("dlEarfcn"), childNode.getTextContent());
							} else if (nameNodeValue.equals("earfcnUL")) {
								String dlEarfcn = lteCell.cellInfo.get("dlEarfcn");
								int ulEarfcn = Integer.valueOf(dlEarfcn) + 18000;
								assertEquals(String.valueOf(ulEarfcn), childNode.getTextContent());
							} else if (nameNodeValue.equals("pMax")) {
								assertEquals(lteCell.cellInfo.get("maxPower"), childNode.getTextContent());
							} else if (nameNodeValue.equals("phyCellId")) {
								assertEquals(lteCell.cellInfo.get("pci"), childNode.getTextContent());
							} else if (nameNodeValue.equals("rootSeqIndex")) {
								assertEquals(lteCell.cellInfo.get("rootSeqIndex"), childNode.getTextContent());
							} else if (nameNodeValue.equals("tac")) {
								assertEquals(lteCell.cellInfo.get("tac"), childNode.getTextContent());
							} else if (nameNodeValue.equals("ulChBw")) {
								assertEquals(lteCell.cellInfo.get("channelBw"), childNode.getTextContent());
							} else if (nameNodeValue.equals("cellName")) {
								assertEquals(lteCell.cellInfo.get("cellName"), childNode.getTextContent());
							}
						}
					}
				}
			}

		}
	}

	// @Ignore
	@Test
	public void testEditGnfl_BcchUnique() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			for (Map.Entry<String, LteCell> entry : lteSite.lteCells.entrySet()) {
				LteCell lteCell = entry.getValue();
				String lnCellId = lteCell.cellInfo.get("lnCellId");
				String eNodeBId = lteSite.generalInfo.get("eNodeBId");
				try {
					expression = xPath.compile("//cmData/managedObject[@class=\"GNFL\" and @distName=\"MRBTS-"
							+ eNodeBId + "/LNBTS-" + eNodeBId + "/LNCEL-" + lnCellId + "/GFIM-1/GNFL-1\"]");
					result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				NodeList lncelNodeList = (NodeList) result;
				Node managedObjectNode = lncelNodeList.item(0);
				NodeList childNodesList = managedObjectNode.getChildNodes();
				for (int j = 0; j < childNodesList.getLength(); j++) {
					Node childNode = childNodesList.item(j);
					if (childNode.getNodeName().equals("list")) {
						NamedNodeMap childNodeAttributes = childNode.getAttributes();
						Node nameNodeAttribute = childNodeAttributes.getNamedItem("name");
						String nameNodeValue = nameNodeAttribute.getNodeValue();
						if (nameNodeValue.equals("gerArfcnVal")) {

							assertEquals(lteSite.uniqueBcchOfNeighbours.size(),
									getChildElementCount((Element) childNode));
						}
					}
				}
			}
		}
	}

	/*
	 * To get the number of child elements within a particular element, you need to take account of the fact that not all nodes are elements. So we
	 * can use:
	 */
	private int getChildElementCount(Element element) {
		int count = 0;
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				count++;
			}
		}
		return count;
	}

	// @Ignore
	@Test
	public void testEditLnhog_BcchUnique() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			for (Map.Entry<String, LteCell> entry : lteSite.lteCells.entrySet()) {
				LteCell lteCell = entry.getValue();
				String lnCellId = lteCell.cellInfo.get("lnCellId");
				String eNodeBId = lteSite.generalInfo.get("eNodeBId");
				try {
					expression = xPath.compile("//cmData/managedObject[@class=\"LNHOG\" and @distName=\"MRBTS-"
							+ eNodeBId + "/LNBTS-" + eNodeBId + "/LNCEL-" + lnCellId + "/LNHOG-0\"]");
					result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				NodeList lncelNodeList = (NodeList) result;
				Node managedObjectNode = lncelNodeList.item(0);
				NodeList childNodesList = managedObjectNode.getChildNodes();
				for (int j = 0; j < childNodesList.getLength(); j++) {
					Node childNode = childNodesList.item(j);
					if (childNode.getNodeName().equals("list")) {
						NamedNodeMap childNodeAttributes = childNode.getAttributes();
						Node nameNodeAttribute = childNodeAttributes.getNamedItem("name");
						String nameNodeValue = nameNodeAttribute.getNodeValue();
						if (nameNodeValue.equals("arfcnValueListGERAN")) {

							assertEquals(lteSite.uniqueBcchOfNeighbours.size(),
									getChildElementCount((Element) childNode));
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLnrelg_CellIdUniquePerCell() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			NodeList lnrelgNodeList = null;
			int sumOfNeighbours = 0;
			for (Map.Entry<String, LteCell> entry : lteSite.lteCells.entrySet()) {
				LteCell lteCell = entry.getValue();
				try {
					expression = xPath.compile("//cmData/managedObject[@class=\"LNRELG\"]");
					result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				lnrelgNodeList = (NodeList) result;
				sumOfNeighbours += lteCell.gsmNeighbours.size();
			}
			assertEquals(sumOfNeighbours, lnrelgNodeList.getLength());
		}
	}

	// @Ignore
	@Test
	public void testEditRedrt_BcchUnique() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			for (Map.Entry<String, LteCell> entry : lteSite.lteCells.entrySet()) {
				LteCell lteCell = entry.getValue();
				String lnCellId = lteCell.cellInfo.get("lnCellId");
				String eNodeBId = lteSite.generalInfo.get("eNodeBId");
				try {
					expression = xPath.compile("//cmData/managedObject[@class=\"REDRT\" and @distName=\"MRBTS-"
							+ eNodeBId + "/LNBTS-" + eNodeBId + "/LNCEL-" + lnCellId + "/REDRT-1\"]");
					result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				NodeList lncelNodeList = (NodeList) result;
				Node managedObjectNode = lncelNodeList.item(0);
				NodeList childNodesList = managedObjectNode.getChildNodes();
				for (int j = 0; j < childNodesList.getLength(); j++) {
					Node childNode = childNodesList.item(j);
					if (childNode.getNodeName().equals("list")) {
						NamedNodeMap childNodeAttributes = childNode.getAttributes();
						Node nameNodeAttribute = childNodeAttributes.getNamedItem("name");
						String nameNodeValue = nameNodeAttribute.getNodeValue();
						if (nameNodeValue.equals("redirGeranArfcnValueL")) {

							assertEquals(lteSite.uniqueBcchOfNeighbours.size(),
									getChildElementCount((Element) childNode));
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditRmod_SiteName() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"RMOD\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList rmodNodeList = (NodeList) result;
			String numberOfRfModulesStr = lteSite.hardware.get("numberOfRfModules");
			int numberOfRfModules = Integer.valueOf(numberOfRfModulesStr);

			assertEquals(numberOfRfModules, rmodNodeList.getLength());

			for (int i = 0; i < rmodNodeList.getLength(); i++) {
				Node rmodNode = rmodNodeList.item(i);
				NodeList rmodChildNodeList = rmodNode.getChildNodes();
				for (int j = 0; j < rmodChildNodeList.getLength(); j++) {
					Node rmodChildNode = rmodChildNodeList.item(j);
					if (rmodChildNode.getNodeName().equals("list")) {
						NamedNodeMap rmodchildNodeAttributes = rmodChildNode.getAttributes();
						Node nameNodeAttribute = rmodchildNodeAttributes.getNamedItem("name");
						String nameNodeValue = nameNodeAttribute.getNodeValue();
						if (nameNodeValue.equals("connectionList")) {
							NodeList listChildNodeList = rmodChildNode.getChildNodes();
							int numberOfItemsInRmodObject = 0;
							for (int k = 0; k < listChildNodeList.getLength(); k++) {
								Node itemNode = listChildNodeList.item(k);
								if (itemNode.getNodeName().equals("item")) {
									numberOfItemsInRmodObject++;
								}
							}
							String rfModuleNumber = "rf" + (i + 1) + "IsShared";
							String expectedStr = lteSite.hardware.get(rfModuleNumber);
							int expected = 1;
							if (expectedStr.equals("DA")) {
								expected = 2;
							}

							assertEquals(expected, numberOfItemsInRmodObject);
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditSmod_SiteName() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			try {
				expression = xPath.compile(
						"//cmData/managedObject[@class=\"SMOD\" and @distName=\"MRBTS-" + eNodeBId + "/SMOD-1\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList smodLteNodeList = (NodeList) result;

			assertEquals(1, smodLteNodeList.getLength());

			try {
				expression = xPath.compile(
						"//cmData/managedObject[@class=\"SMOD\" and @distName=\"MRBTS-" + eNodeBId + "/SMOD-2\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList smodGsmNodeList = (NodeList) result;
			int expected = 0;
			String numberOfSharedRfModules = lteSite.hardware.get("numberOfSharedRfModules");
			if (!numberOfSharedRfModules.equals("0") && !numberOfSharedRfModules.equals("")) {
				expected = 1;
			}

			assertEquals(expected, smodGsmNodeList.getLength());
		}
	}

	// @Ignore
	@Test
	public void testEditFtm_SiteCode() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"FTM\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList ftmNodeList = (NodeList) result;
			Node ftmNode = ftmNodeList.item(0);
			NodeList ftmChildNodeList = ftmNode.getChildNodes();
			for (int i = 0; i < ftmChildNodeList.getLength(); i++) {
				Node ftmChildNode = ftmChildNodeList.item(i);
				if (ftmChildNode.getNodeName().equals("p")) {
					NamedNodeMap ftmChildNodeAttributes = ftmChildNode.getAttributes();
					Node nameNodeAttribute = ftmChildNodeAttributes.getNamedItem("name");
					String nameNodeValue = nameNodeAttribute.getNodeValue();
					if (nameNodeValue.equals("systemTitle")) {

						assertEquals(lteSite.generalInfo.get("LocationId"), ftmChildNode.getTextContent());
					} else if (nameNodeValue.equals("systemTitle")) {

						assertEquals(lteSite.generalInfo.get("eNodeBName"), ftmChildNode.getTextContent());
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditIpno() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"IPNO\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList ipnoNodeList = (NodeList) result;
			Node ipnoNode = ipnoNodeList.item(0);
			NodeList ipnoChildNodeList = ipnoNode.getChildNodes();
			for (int i = 0; i < ipnoChildNodeList.getLength(); i++) {
				Node ipnoChildNode = ipnoChildNodeList.item(i);
				if (ipnoChildNode.getNodeName().equals("p")) {
					NamedNodeMap ipnoChildNodeAttributes = ipnoChildNode.getAttributes();
					Node nameNodeAttribute = ipnoChildNodeAttributes.getNamedItem("name");
					String nameNodeValue = nameNodeAttribute.getNodeValue();
					if (nameNodeValue.equals("mPlaneIpAddress")) {

						assertEquals(lteSite.transmission.get("mIp"), ipnoChildNode.getTextContent());

					} else if (nameNodeValue.equals("uPlaneIpAddress") | nameNodeValue.equals("cPlaneIpAddress")) {

						assertEquals(lteSite.transmission.get("cuDestIp"), ipnoChildNode.getTextContent());

					} else if (nameNodeValue.equals("sPlaneIpAddress")) {

						assertEquals(lteSite.transmission.get("sIp"), ipnoChildNode.getTextContent());

					} else if (nameNodeValue.equals("btsId")) {

						assertEquals(eNodeBId, ipnoChildNode.getTextContent());

					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditTwamp() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"TWAMP\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList twampNodeList = (NodeList) result;
			Node twampNode = twampNodeList.item(0);
			NodeList twampChildNodeList = twampNode.getChildNodes();
			for (int i = 0; i < twampChildNodeList.getLength(); i++) {
				Node ipnoChildNode = twampChildNodeList.item(i);
				if (ipnoChildNode.getNodeName().equals("p")) {
					NamedNodeMap ipnoChildNodeAttributes = ipnoChildNode.getAttributes();
					Node nameNodeAttribute = ipnoChildNodeAttributes.getNamedItem("name");
					String nameNodeValue = nameNodeAttribute.getNodeValue();
					if (nameNodeValue.equals("sourceIpAddress")) {

						assertEquals(lteSite.transmission.get("cuDestIp"), ipnoChildNode.getTextContent());

					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditIprt() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"IPRT\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList iprtNodeList = (NodeList) result;
			Node iprtNode = iprtNodeList.item(0);
			NodeList iprtChildNodeList = iprtNode.getChildNodes();
			for (int i = 0; i < iprtChildNodeList.getLength(); i++) {
				Node iprtChildNode = iprtChildNodeList.item(i);
				if (iprtChildNode.getNodeName().equals("list")) {
					NodeList itemNodeList = iprtChildNode.getChildNodes();
					for (int j = 0; j < itemNodeList.getLength(); j++) {
						Node itemNode = itemNodeList.item(j);
						if (itemNode.getNodeName().equals("item")) {
							NodeList pNodeList = itemNode.getChildNodes();
							for (int k = 0; k < pNodeList.getLength(); k++) {
								Node pNode = pNodeList.item(k);
								if (pNode.getNodeName().equals("p")) {
									NamedNodeMap pNodeAttributes = pNode.getAttributes();
									Node nameNodeAttribute = pNodeAttributes.getNamedItem("name");
									String nameNodeValue = nameNodeAttribute.getNodeValue();
									if (nameNodeValue.equals("destIpAddr")) {
										if (!pNode.getTextContent().equals("0.0.0.0")) {
											assertEquals(lteSite.transmission.get("topIp"), pNode.getTextContent());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditIvif1() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"IVIF\" and @distName=\"MRBTS-" + eNodeBId
						+ "/LNBTS-" + eNodeBId + "/FTM-1/IPNO-1/IEIF-1/IVIF-1\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList ivifNodeList = (NodeList) result;
			Node ivifNode = ivifNodeList.item(0);
			NodeList ivifChildNodeList = ivifNode.getChildNodes();
			for (int i = 0; i < ivifChildNodeList.getLength(); i++) {
				Node ivifChildNode = ivifChildNodeList.item(i);
				if (ivifChildNode.getNodeName().equals("p")) {
					NamedNodeMap ivifChildNodeAttributes = ivifChildNode.getAttributes();
					Node nameNodeAttribute = ivifChildNodeAttributes.getNamedItem("name");
					String nameNodeValue = nameNodeAttribute.getNodeValue();
					if (nameNodeValue.equals("vlanId")) {

						assertEquals(lteSite.transmission.get("sVlanId"), ivifChildNode.getTextContent());

					} else if (nameNodeValue.equals("localIpAddr")) {

						assertEquals(lteSite.transmission.get("sIp"), ivifChildNode.getTextContent());

					} else if (nameNodeValue.equals("netmask")) {

						assertEquals(lteSite.transmission.get("sSubnet"), ivifChildNode.getTextContent());

					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditIvif2() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"IVIF\" and @distName=\"MRBTS-" + eNodeBId
						+ "/LNBTS-" + eNodeBId + "/FTM-1/IPNO-1/IEIF-1/IVIF-2\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList ivifNodeList = (NodeList) result;
			Node ivifNode = ivifNodeList.item(0);
			NodeList ivifChildNodeList = ivifNode.getChildNodes();
			for (int i = 0; i < ivifChildNodeList.getLength(); i++) {
				Node ivifChildNode = ivifChildNodeList.item(i);
				if (ivifChildNode.getNodeName().equals("p")) {
					NamedNodeMap ivifChildNodeAttributes = ivifChildNode.getAttributes();
					Node nameNodeAttribute = ivifChildNodeAttributes.getNamedItem("name");
					String nameNodeValue = nameNodeAttribute.getNodeValue();
					if (nameNodeValue.equals("vlanId")) {

						assertEquals(lteSite.transmission.get("cuVlanId"), ivifChildNode.getTextContent());

					} else if (nameNodeValue.equals("localIpAddr")) {

						assertEquals(lteSite.transmission.get("cuDestIp"), ivifChildNode.getTextContent());

					} else if (nameNodeValue.equals("netmask")) {

						assertEquals(lteSite.transmission.get("cuSubnet"), ivifChildNode.getTextContent());

					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditTopf() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"TOPF\" and @distName=\"MRBTS-" + eNodeBId
						+ "/LNBTS-" + eNodeBId + "/FTM-1/TOPB-1/TOPF-1\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList topfNodeList = (NodeList) result;
			Node topfNode = topfNodeList.item(0);
			NodeList topfChildNodeList = topfNode.getChildNodes();
			for (int i = 0; i < topfChildNodeList.getLength(); i++) {
				Node topfChildNode = topfChildNodeList.item(i);
				if (topfChildNode.getNodeName().equals("list")) {
					NamedNodeMap topfChildNodeAttributes = topfChildNode.getAttributes();
					Node nameNodeAttribute = topfChildNodeAttributes.getNamedItem("name");
					String nameNodeValue = nameNodeAttribute.getNodeValue();
					if (nameNodeValue.equals("topMasters")) {
						NodeList itemNodeList = topfChildNode.getChildNodes();
						for (int j = 0; j < itemNodeList.getLength(); j++) {
							Node itemNode = itemNodeList.item(j);
							if (itemNode.getNodeName().equals("item")) {
								NodeList pNodeList = itemNode.getChildNodes();
								for (int k = 0; k < pNodeList.getLength(); k++) {
									Node pNode = pNodeList.item(k);
									if (pNode.getNodeName().equals("p")) {
										NamedNodeMap pNodeAttributes = pNode.getAttributes();
										Node pNameNodeAttribute = pNodeAttributes.getNamedItem("name");
										String pNameNodeValue = pNameNodeAttribute.getNodeValue();
										if (pNameNodeValue.equals("topMasters")) {

											assertEquals(lteSite.transmission.get("topIp"), pNode.getTextContent());

										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLcell_AnttenaPorts() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"LCELL\"]/list/item");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			String[] antPort = new String[] { "1", "3", "7", "9", "13", "15" };
			if (lteSite.hardware.get("cell1Ports").equals("1-1")) {
				antPort[0] = "1";
				antPort[1] = "7";
				antPort[2] = "3";
				antPort[3] = "9";
				antPort[4] = "5";
				antPort[5] = "11";
			}
			NodeList itemNodeList = (NodeList) result;
			for (int i = 0; i < itemNodeList.getLength(); i++) {
				Node itemNode = itemNodeList.item(i);
				NodeList childNodeList = itemNode.getChildNodes();
				for (int j = 0; j < childNodeList.getLength(); j++) {
					Node childNode = childNodeList.item(j);
					if (childNode.getNodeName().equals("p")) {
						NamedNodeMap pNodeAttributes = childNode.getAttributes();
						Node pNameNodeAttribute = pNodeAttributes.getNamedItem("name");
						String pNameNodeValue = pNameNodeAttribute.getNodeValue();
						if (pNameNodeValue.equals("antlId")) {

							assertEquals(antPort[i], childNode.getTextContent());

						}
					}
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testIsFtifUsed() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expressionOfPpttNode, expressionOfEthlkNode;
			Object resultOfPpttNode = null, resultOfEthlkNode = null;
			boolean ftifIsUsed = false;
			String ftifIsUsedStr = lteSite.hardware.get("ftif");
			if (ftifIsUsedStr.equals("DA")) {
				ftifIsUsed = true;
			}
			if (ftifIsUsed) {
				try {
					expressionOfPpttNode = xPath.compile("//cmData/managedObject[@class=\"PPTT\"]");
					expressionOfEthlkNode = xPath
							.compile("//cmData/managedObject[@class=\"ETHLK\" and contains(@distName,'ETHLK-1-')]");
					resultOfPpttNode = expressionOfPpttNode.evaluate(xmlDocument, XPathConstants.NODESET);
					resultOfEthlkNode = expressionOfEthlkNode.evaluate(xmlDocument, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				NodeList ppttNodeList = (NodeList) resultOfPpttNode;
				NodeList ethlkNodeList = (NodeList) resultOfEthlkNode;

				assertEquals(8, ppttNodeList.getLength());
				assertEquals(4, ethlkNodeList.getLength());
			}
		}
	}

	// @Ignore
	@Test
	public void testEditNumberOfAntenna() {
		LteSite lteSite;
		int numberOfSite = 0;
		Document xmlDocument = null;
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringComments(true);
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				xmlDocument = builder.parse(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression expression;
			Object result = null;
			try {
				expression = xPath.compile("//cmData/managedObject[@class=\"ANTL\"]");
				result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			NodeList antlNodeList = (NodeList) result;
			String numberOfRfModules = lteSite.hardware.get("numberOfRfModules");
			int numberOfAntennas = 6 * Integer.valueOf(numberOfRfModules);

			assertEquals(numberOfAntennas, antlNodeList.getLength());
		}
	}

	@After
	public void clean() {
		File outputDir = new File("C:\\CG output");
		for (File file : outputDir.listFiles()) {
			if (!file.getPath().equals("C:\\CG output\\Commissioning_KKLLL_YYYYMMDD.xml")
					& !file.getPath().equals("C:\\CG output\\FTIF_Config.xml")) {
				file.delete();
			}
		}
	}

}
