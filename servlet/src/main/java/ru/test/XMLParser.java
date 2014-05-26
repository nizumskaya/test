package ru.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLParser {
	
	public static TestRequest parseNewAgtRequest(String rep) throws ParserConfigurationException, SAXException,
					IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream inputStream = new ByteArrayInputStream(rep.getBytes());
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName("request");
		TestRequest request = new TestRequest();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				request.setLogin(getValue("login", element));
				request.setPassword(getValue("password", element));
				request.setRequest_type(RequestType.NEWAGT.getName().equals(getValue("request-type", element)) ? RequestType.NEWAGT
								: RequestType.AGTBAL);
			}
		}
		return request;
	}
	
	private static String getValue(String tag, Element element) {
		if(null != element){
			NodeList list = element.getElementsByTagName(tag);
			if(null != list){
				Node node = element.getElementsByTagName(tag).item(0);
				if(null != node){
					NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
					Node nodech = (Node) nodes.item(0);
					return nodech.getNodeValue();
				}
			}
		}
		return null;
	}
	
	public static String createXMLRequest(TestResponse response) throws ParserConfigurationException,
					TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("response");
		doc.appendChild(rootElement);
		Element resultCode = doc.createElement("result-code");
		resultCode.appendChild(doc.createTextNode(response.getResultCode()));
		rootElement.appendChild(resultCode);
		if (null != response.getBalance()) {
			Element balance = doc.createElement("bal");
			balance.appendChild(doc.createTextNode(String.valueOf(response.getBalance())));
			rootElement.appendChild(balance);
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		Writer outWriter = new StringWriter();
		StreamResult result = new StreamResult(outWriter);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return xmlString;
	}
	
	public static TestResponse parseResponse(String rep) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream inputStream = new ByteArrayInputStream(rep.getBytes());
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();
		NodeList nodes = doc.getElementsByTagName("response");
		TestResponse response = new TestResponse();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				response.setResultCode(getValue("result-code", element));
				String balance = getValue("bal", element);
				if (null != balance) {
					response.setBalance(BigDecimal.valueOf(Double.valueOf(getValue("bal", element))));
				}
			}
		}
		return response;
	}
}
