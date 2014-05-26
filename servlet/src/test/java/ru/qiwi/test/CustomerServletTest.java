package ru.qiwi.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.testng.annotations.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.context.ContextLoaderListener;
import org.testng.Assert;

import ru.test.Customer;
import ru.test.ResponseErrorCodes;
import ru.test.TestServlet;
import ru.test.XMLParser;


@ContextConfiguration(locations = { "classpath:servlet-context.xml" })
public class CustomerServletTest extends AbstractTestNGSpringContextTests {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public CustomerServletTest(){
		super();
	}

	@Autowired
	private Customer customerImpl;
	
	@Autowired
	private DataSource dataSource;
	
	private TestServlet testServlet = null;
	
	private Server server = null;
	
	public void setupWebContainer() {
		final String dir = "./";
		server = new Server(7777);
		final String staticUrlString = this.getClass().getClassLoader().getResource(dir).toExternalForm();
		Context root = new Context(server, "/", Context.SESSIONS);
		Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("contextConfigLocation", staticUrlString + "servlet-context.xml");
		root.setInitParams(initParams);
		ServletHolder holder = new ServletHolder(testServlet);
		root.addServlet(holder, "/test");
		root.addEventListener(new ContextLoaderListener());
		try {
			server.start();
		} catch (Exception e) {
			log.error("" + e);
		}
	}
	
    @SuppressWarnings("unchecked")
    private void initServlet() throws ServletException {
    	testServlet = new TestServlet();
        TestUtilBean.setPrivateField(TestServlet.class, "dataSource", testServlet,
                this.applicationContext.getBean("dataSource"));
        TestUtilBean.setPrivateField(TestServlet.class, "customerImpl", testServlet,
                        this.applicationContext.getBean("customerImpl"));
    }

    
    public void startServer() throws Exception {
        server.start();
    }

    public void stopServer() throws Exception {
        server.stop();
    }
    
	@Test(groups = { "customer" })
	public void testGettingOfBalance() throws Exception {
		initServlet();
		setupWebContainer();
		Assert.assertFalse(server.isFailed());
		Assert.assertTrue(server.isStarted());
		Assert.assertTrue(server.isRunning());
		try {
			Client client2 = new Client(Protocol.HTTP);
			final Request request2 = new Request(Method.POST, "http://localhost:7777/test");
			request2.getCookies().add("JSESSIONID", "SESSION1");
			Representation bwEvent = this.doRepresentationFromFile("src/test/resources/agt-bal.xml");
			bwEvent.setMediaType(MediaType.APPLICATION_ALL_XML);
			request2.setEntity(bwEvent);
			final Response response2 = client2.handle(request2);
			Assert.assertNotNull(response2, "Check response");
			ru.test.TestResponse response = XMLParser.parseResponse(response2.getEntityAsText());
			Assert.assertEquals(response.getResultCode(), "0");
			Assert.assertEquals(String.valueOf(response.getBalance().setScale(2, RoundingMode.HALF_UP)), "55.66");
			
		} finally {
			stopServer();
		}
	}
	
	
	@Test(groups = { "customer" })
	public void testAddingOfCustomer() throws Exception {
		initServlet();
		setupWebContainer();
		Assert.assertFalse(server.isFailed());
		Assert.assertTrue(server.isStarted());
		Assert.assertTrue(server.isRunning());
		try {
			Client client2 = new Client(Protocol.HTTP);
			final Request request2 = new Request(Method.POST, "http://localhost:7777/test");
			request2.getCookies().add("JSESSIONID", "SESSION1");
			Representation bwEvent = this.doRepresentationFromFile("src/test/resources/new-agt.xml");
			bwEvent.setMediaType(MediaType.APPLICATION_ALL_XML);
			request2.setEntity(bwEvent);
			final Response response2 = client2.handle(request2);
			Assert.assertNotNull(response2, "Check response");
			ru.test.TestResponse response = XMLParser.parseResponse(response2.getEntityAsText());
			Assert.assertEquals(response.getResultCode(), String.valueOf(ResponseErrorCodes.ERROR_1.getErrorCode()));
		} finally {
			stopServer();
		}
	}
    
	@Test(groups = { "customer" })
	public void createCustomerTest() {
		int res = 2500;
		try {
			res = customerImpl.insertCustomer("1234567892", "1245782", dataSource, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(res, ResponseErrorCodes.ERROR_0.getErrorCode());
	}
	
    
	@Test(groups = { "customer" })
	public void getBalanceTest() {
		BigDecimal res = new BigDecimal(0);
		try {
			res = customerImpl.getCustomerBalance("1234567890", dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(String.valueOf(res.setScale(2, RoundingMode.HALF_UP)), "55.66");

	}

	private Representation doRepresentationFromFile(String filePath) {
		StringBuilder initial_request = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			int ch;
			while ((ch = fis.read()) != -1)
				initial_request.append((char) ch);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new StringRepresentation(initial_request.toString());
	}
}




