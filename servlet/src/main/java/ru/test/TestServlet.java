package ru.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.ReaderRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@WebServlet("/test")
@Component
public class TestServlet extends HttpServlet{
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private DataSource dataSource;
	
	private Customer customerImpl;
	
	private static final long serialVersionUID = -8840381926916828235L;

	@Override
	public void init() throws ServletException {
	    super.init();
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        this.dataSource = (DataSource) wac.getBean("dataSource");
        this.customerImpl = (Customer) wac.getBean("customerImpl");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = 0;
		BigDecimal resultStr = null;
		try {
			result = customerImpl.insertCustomer("login2", "password2", dataSource, false);
			resultStr = customerImpl.getCustomerBalance("login2", dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>" + result + "_" + resultStr.doubleValue() + "</h1>");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DomRepresentation rep = new DomRepresentation(new ReaderRepresentation(request.getReader()));
		synchronized (rep) {
			PrintWriter out = null;
			int resultCode = 999;
			TestRequest xmlRequest = null;
			TestResponse xmlResponse = new TestResponse();
			try {
				xmlResponse = createXMLResponse(xmlRequest, xmlResponse, rep, resultCode);
			} catch (SQLException sqlex) {
				resultCode = ResponseErrorCodes.ERROR_5.getErrorCode();
				xmlResponse.setResultCode(String.valueOf(resultCode));
				log.error(TestConstants.ExceptionMessages.ERROR_SQL_EXECUTION, sqlex);
			} catch (ParserConfigurationException pcex) {
				log.error(TestConstants.ExceptionMessages.ERROR_CONFIGURATION, pcex);
			} catch (SAXException saxex) {
				log.error(TestConstants.ExceptionMessages.ERROR_PARSING, saxex);
			}
			createXMLResult(response, xmlResponse, out);
		}
	}
	
	private TestResponse createXMLResponse(TestRequest xmlRequest, TestResponse xmlResponse, DomRepresentation rep,
					int resultCode) throws ParserConfigurationException, SAXException, IOException, SQLException {
		xmlRequest = ru.test.XMLParser.parseNewAgtRequest(rep.getText());
		if (RequestType.NEWAGT.getName().equals(xmlRequest.getRequest_type().getName())) {
			resultCode = customerImpl
							.insertCustomer(xmlRequest.getLogin(), xmlRequest.getPassword(), dataSource, false);
		} else if (RequestType.AGTBAL.getName().equals(xmlRequest.getRequest_type().getName())) {
			BigDecimal balance = customerImpl.getCustomerBalance(xmlRequest.getLogin(), dataSource);
			if (null == balance) {
				resultCode = ResponseErrorCodes.ERROR_5.getErrorCode();
			} else {
				xmlResponse.setBalance(balance.setScale(2, RoundingMode.HALF_UP));
				resultCode = ResponseErrorCodes.ERROR_0.getErrorCode();
			}
		}
		xmlResponse.setResultCode(String.valueOf(resultCode));
		return xmlResponse;
	}
	
	private void createXMLResult(HttpServletResponse response, TestResponse xmlResponse, PrintWriter out) {
		String result;
		try {
			result = ru.test.XMLParser.createXMLRequest(xmlResponse);
			try {
				response.setStatus(HttpServletResponse.SC_OK);
				out = response.getWriter();
				out.write(result);
			} catch (IOException e) {
				log.error(TestConstants.ExceptionMessages.ERROR_WRITING_INTO_HTTP_RESPONSE, e);
			} finally {
				out.flush();
				out.close();
			}
		} catch (ParserConfigurationException | TransformerException ex) {
			log.error(TestConstants.ExceptionMessages.ERROR_TRANSFORMATION_DATA, ex);
		}
	}
	
	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

	@Override
	public void destroy() {

	}
}
