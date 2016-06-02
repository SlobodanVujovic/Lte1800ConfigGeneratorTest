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
	private Document xmlDocument;
	private LteSite lteSite;
	int numberOfSite;

	@Before
	public void setUp() {
		allConfigFiles = new AllConfigFiles();
		allConfigFiles.allLteSites.inputReader.setRadioInput("C:\\CG input test\\Radio test.xlsx");
		allConfigFiles.allLteSites.inputReader.setTransmissionInput("C:\\CG input test\\Transmission test.xlsx");
		allConfigFiles.allLteSites.inputReader.setConfigInput("C:\\CG input test\\Config input test.xlsx");
		allConfigFiles.createConfigFile();
		xmlDocument = null;
		numberOfSite = 0;
	}

	// @Ignore
	@Test
	public void testEditXmlDateAndTime() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			xmlDocument = createXmlDocument(configFile);
			NodeList logList = xmlDocument.getElementsByTagName("log");
			Node logNode = logList.item(0);
			String dateTime = getAttributeValueFromNode(logNode, "dateTime");
			String dateTimeAtMinuteAccuracy = dateTime.substring(0, 16) + ":00";

			assertEquals(
					LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
					dateTimeAtMinuteAccuracy);
		}
	}

	private Document createXmlDocument(File configFile) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setIgnoringComments(true);
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			xmlDocument = builder.parse(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlDocument;
	}

	private String getAttributeValueFromNode(Node node, String attributeName) {
		NamedNodeMap nodeAttributes = node.getAttributes();
		Node specificAttribute = nodeAttributes.getNamedItem(attributeName);
		String valueOfAttribute = specificAttribute.getTextContent();
		return valueOfAttribute;
	}

	// @Ignore
	@Test
	public void testEditMrbts_eNodeBId() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				String distNameValue = getAttributeValueFromNode(managedObjectNode, "distName");

				assertThat(distNameValue.indexOf("MRBTS-" + lteSite.generalInfo.get("eNodeBId")), is(not(-1)));
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLnbts_eNodeBId() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				String distNameValue = getAttributeValueFromNode(managedObjectNode, "distName");
				if (distNameValue.contains("LNBTS")) {

					assertThat(distNameValue.indexOf("LNBTS-" + lteSite.generalInfo.get("eNodeBId")), is(not(-1)));
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testEditLncellId() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			int cellId = 1;
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				String distNameValue = getAttributeValueFromNode(managedObjectNode, "distName");
				if (distNameValue.contains("LNCEL")) {
					LteCell lteCell = lteSite.lteCells.get(String.valueOf(cellId));
					String lncellId = lteCell.cellInfo.get("lnCellId");

					assertThat(distNameValue.indexOf("LNCEL-" + lncellId), is(not(-1)));

					Node nextManagedObjectNode = managedObjectList.item(i + 1);
					String classNameValue = getAttributeValueFromNode(nextManagedObjectNode, "class");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				String distNameValue = getAttributeValueFromNode(managedObjectNode, "class");
				if (distNameValue.contains("BTSSCL")) {
					NodeList childNodeList = managedObjectNode.getChildNodes();
					for (int j = 0; j < childNodeList.getLength(); j++) {
						Node childNode = childNodeList.item(j);
						if (childNode.getNodeName().equals("p")) {
							String nameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList managedObjectList = xmlDocument.getElementsByTagName("managedObject");
			for (int i = 0; i < managedObjectList.getLength(); i++) {
				Node managedObjectNode = managedObjectList.item(i);
				String distNameValue = getAttributeValueFromNode(managedObjectNode, "class");
				if (distNameValue.contains("LNBTS")) {
					NodeList childNodeList = managedObjectNode.getChildNodes();
					for (int j = 0; j < childNodeList.getLength(); j++) {
						Node childNode = childNodeList.item(j);
						if (childNode.getNodeName().equals("p")) {
							String nameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList lnadjgNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"LNADJG\"]");

			assertEquals(lnadjgNodeList.getLength(), lteSite.uniqueGsmNeighbours.size());
		}
	}

	private Object getObjectFromXmlDocument(String stringExpression) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expression;
		Object result = null;
		try {
			expression = xPath.compile(stringExpression);
			result = expression.evaluate(xmlDocument, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return result;
	}

	// @Ignore
	@Test
	public void testEditLncel_cellParameters() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
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
							String nameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
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
						String nameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
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
						String nameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
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
						String nameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList rmodNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"RMOD\"]");
			String numberOfRfModulesStr = lteSite.hardware.get("numberOfRfModules");
			int numberOfRfModules = Integer.valueOf(numberOfRfModulesStr);

			assertEquals(numberOfRfModules, rmodNodeList.getLength());

			for (int i = 0; i < rmodNodeList.getLength(); i++) {
				Node rmodNode = rmodNodeList.item(i);
				NodeList rmodChildNodeList = rmodNode.getChildNodes();
				for (int j = 0; j < rmodChildNodeList.getLength(); j++) {
					Node rmodChildNode = rmodChildNodeList.item(j);
					if (rmodChildNode.getNodeName().equals("list")) {
						String nameNodeValue = getAttributeValueFromNode(rmodChildNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			NodeList smodLteNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"SMOD\" and @distName=\"MRBTS-" + eNodeBId + "/SMOD-1\"]");

			assertEquals(1, smodLteNodeList.getLength());

			NodeList smodGsmNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"SMOD\" and @distName=\"MRBTS-" + eNodeBId + "/SMOD-2\"]");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList ftmNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"FTM\"]");
			Node ftmNode = ftmNodeList.item(0);
			NodeList ftmChildNodeList = ftmNode.getChildNodes();
			for (int i = 0; i < ftmChildNodeList.getLength(); i++) {
				Node ftmChildNode = ftmChildNodeList.item(i);
				if (ftmChildNode.getNodeName().equals("p")) {
					String nameNodeValue = getAttributeValueFromNode(ftmChildNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			NodeList ipnoNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"IPNO\"]");
			Node ipnoNode = ipnoNodeList.item(0);
			NodeList ipnoChildNodeList = ipnoNode.getChildNodes();
			for (int i = 0; i < ipnoChildNodeList.getLength(); i++) {
				Node ipnoChildNode = ipnoChildNodeList.item(i);
				if (ipnoChildNode.getNodeName().equals("p")) {
					String nameNodeValue = getAttributeValueFromNode(ipnoChildNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList twampNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"TWAMP\"]");
			Node twampNode = twampNodeList.item(0);
			NodeList twampChildNodeList = twampNode.getChildNodes();
			for (int i = 0; i < twampChildNodeList.getLength(); i++) {
				Node ipnoChildNode = twampChildNodeList.item(i);
				if (ipnoChildNode.getNodeName().equals("p")) {
					String nameNodeValue = getAttributeValueFromNode(ipnoChildNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList iprtNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"IPRT\"]");
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
									String nameNodeValue = getAttributeValueFromNode(pNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			NodeList ivifNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"IVIF\" and @distName=\"MRBTS-" + eNodeBId + "/LNBTS-" + eNodeBId
							+ "/FTM-1/IPNO-1/IEIF-1/IVIF-1\"]");
			Node ivifNode = ivifNodeList.item(0);
			NodeList ivifChildNodeList = ivifNode.getChildNodes();
			for (int i = 0; i < ivifChildNodeList.getLength(); i++) {
				Node ivifChildNode = ivifChildNodeList.item(i);
				if (ivifChildNode.getNodeName().equals("p")) {
					String nameNodeValue = getAttributeValueFromNode(ivifChildNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			NodeList ivifNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"IVIF\" and @distName=\"MRBTS-" + eNodeBId + "/LNBTS-" + eNodeBId
							+ "/FTM-1/IPNO-1/IEIF-1/IVIF-2\"]");
			Node ivifNode = ivifNodeList.item(0);
			NodeList ivifChildNodeList = ivifNode.getChildNodes();
			for (int i = 0; i < ivifChildNodeList.getLength(); i++) {
				Node ivifChildNode = ivifChildNodeList.item(i);
				if (ivifChildNode.getNodeName().equals("p")) {
					String nameNodeValue = getAttributeValueFromNode(ivifChildNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			String eNodeBId = lteSite.generalInfo.get("eNodeBId");
			NodeList topfNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"TOPF\" and @distName=\"MRBTS-" + eNodeBId + "/LNBTS-" + eNodeBId
							+ "/FTM-1/TOPB-1/TOPF-1\"]");
			Node topfNode = topfNodeList.item(0);
			NodeList topfChildNodeList = topfNode.getChildNodes();
			for (int i = 0; i < topfChildNodeList.getLength(); i++) {
				Node topfChildNode = topfChildNodeList.item(i);
				if (topfChildNode.getNodeName().equals("list")) {
					String nameNodeValue = getAttributeValueFromNode(topfChildNode, "name");
					if (nameNodeValue.equals("topMasters")) {
						NodeList itemNodeList = topfChildNode.getChildNodes();
						for (int j = 0; j < itemNodeList.getLength(); j++) {
							Node itemNode = itemNodeList.item(j);
							if (itemNode.getNodeName().equals("item")) {
								NodeList pNodeList = itemNode.getChildNodes();
								for (int k = 0; k < pNodeList.getLength(); k++) {
									Node pNode = pNodeList.item(k);
									if (pNode.getNodeName().equals("p")) {
										String pNameNodeValue = getAttributeValueFromNode(pNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			String[] antPort = new String[] { "1", "3", "7", "9", "13", "15" };
			if (lteSite.hardware.get("cell1Ports").equals("1-1")) {
				antPort[0] = "1";
				antPort[1] = "7";
				antPort[2] = "3";
				antPort[3] = "9";
				antPort[4] = "5";
				antPort[5] = "11";
			}
			NodeList itemNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"LCELL\"]/list/item");
			for (int i = 0; i < itemNodeList.getLength(); i++) {
				Node itemNode = itemNodeList.item(i);
				NodeList childNodeList = itemNode.getChildNodes();
				for (int j = 0; j < childNodeList.getLength(); j++) {
					Node childNode = childNodeList.item(j);
					if (childNode.getNodeName().equals("p")) {
						String pNameNodeValue = getAttributeValueFromNode(childNode, "name");
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
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			boolean ftifIsUsed = false;
			String ftifIsUsedStr = lteSite.hardware.get("ftif");
			if (ftifIsUsedStr.equals("DA")) {
				ftifIsUsed = true;
			}
			if (ftifIsUsed) {
				NodeList ppttNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"PPTT\"]");
				NodeList ethlkNodeList = (NodeList) getObjectFromXmlDocument(
						"//cmData/managedObject[@class=\"ETHLK\" and contains(@distName,'ETHLK-1-')]");
				NodeList unitNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"UNIT\"]");
				String actualUnit = "";
				Node unitNode = unitNodeList.item(0);
				NodeList pNodeList = unitNode.getChildNodes();
				for (int i = 0; i < pNodeList.getLength(); i++) {
					Node pNode = pNodeList.item(i);
					if (pNode.getNodeName().equals("p")) {
						String pNameValue = getAttributeValueFromNode(pNode, "name");
						if (pNameValue.equals("unitTypeExpected")) {
							actualUnit = pNode.getTextContent();
						}
					}
				}

				assertEquals(8, ppttNodeList.getLength());
				assertEquals(4, ethlkNodeList.getLength());
				assertEquals("472311A", actualUnit);
			}
		}
	}

	// @Ignore
	@Test
	public void testEditNumberOfAntenna() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList antlNodeList = (NodeList) getObjectFromXmlDocument("//cmData/managedObject[@class=\"ANTL\"]");
			String numberOfRfModules = lteSite.hardware.get("numberOfRfModules");
			int numberOfAntennas = 6 * Integer.valueOf(numberOfRfModules);

			assertEquals(numberOfAntennas, antlNodeList.getLength());
		}
	}

	// @Ignore
	@Test
	public void testEditIpno_Twamp() {
		for (File configFile : allConfigFiles.listOfAllConfigFiles) {
			lteSite = allConfigFiles.listOfAllSites.get(numberOfSite++);
			xmlDocument = createXmlDocument(configFile);
			NodeList itemNodeList = (NodeList) getObjectFromXmlDocument(
					"//cmData/managedObject[@class=\"IPNO\"]/list[@name=\"twampFlag\"]/item/p[@name=\"twampIpAddress\"]");
			Node itemNode = itemNodeList.item(0);
			String itemValue = itemNode.getTextContent();
			String expected = lteSite.transmission.get("cuDestIp");

			assertEquals(expected, itemValue);
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
