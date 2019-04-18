package server.api_data;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * 
 * @author 박성호
 * @brief Abstract class를 통한 OPEN API class 전체 통제
 */
public abstract class ParsingMaster {
	protected String urlStart = "";
	protected String serviceKey = "BWsoL%2Bg4wkuJ0NN1quyYULZ1n0iG95th%2BlgxlN81XYx04BNzSW5Eh0kSBVlduhLB%2B9MtT8Dqr%2BhYHebuERHaWw%3D%3D";
	protected String readResult = "";
	protected String result = "";
	protected int numOfRows = 6;
	protected static final Logger logger = Logger.getLogger(ParsingMaster.class.getName());// logger 사용

	StringBuilder urlBuilder = new StringBuilder("");
	StringBuilder resultBuilding = new StringBuilder("\n");

	public abstract void setUrl(); // 각 클래스에서 다시 재정의.

	public void setReadResult() throws IOException {
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
		BufferedReader bufferRead = new BufferedReader(
				// 텍스트를 읽거나 쓸 떄는 Charset 객체를 사용해야 하는데,
				new InputStreamReader(urlconnection.getInputStream(), StandardCharsets.UTF_8.name()));
		String line;
		while ((line = bufferRead.readLine()) != null) {
			readResult = readResult + line.trim();
		} // while end
	} // setReadResult end

	public abstract String setRoot(); // 선언해두고 나중에 상세히 구성하겠다를 보여줌.
	// setRoot는 html의 body/items를 다룬다.

	public abstract void itemLoof(NodeList nodeList);
	// parsethis로 for문을 순환

	/**
	 * @param node: 파싱할 내용을 찾을 주소
	 * @param item: 파싱할 내용
	 * @param itemName: 파싱할 내용을 설명해주는 매개변수
	 */
	public void nodeInfo(Node node, String item, String itemName) {
		// node: node, item: 파싱할 내용, itemName: 파싱할 내용을 설명해주는 매개변수.
		if (node.getNodeName().equals(item)) { // item을 찾음.
			resultBuilding.append(itemName); // 찾아서 붙인다.
			resultBuilding.append(" : ");
			resultBuilding.append(node.getTextContent());
			resultBuilding.append("\n");
		} // if end
	} // nodeInfo end

	public void start() throws IOException {
		setUrl(); // URL 출력
		setReadResult(); // Page 전체 출력
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			InputSource is = new InputSource(new StringReader(readResult));
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is); // readResult를 초기화 안하면 안돌아간다
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile(setRoot()); // root로 경로를 지정.
			// 노드 리스트를 받아 놓는다
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			itemLoof(nodeList);
		} // try end

		catch (MalformedURLException e) {
			logger.warning("[Info request error]");
		} catch (UnsupportedEncodingException e) {
			logger.warning("[encoding error]");
		} catch (IOException e) {
			logger.warning("[exception handling"); // 예외처리
		} catch (XPathExpressionException e) {
			logger.warning("[root error]");
		} catch (ParserConfigurationException e) {
			logger.warning("[parse error]");
		} catch (SAXException e) {
			logger.warning("[SAX exception]");
		}
		result = resultBuilding.toString();
	} // start end

	public String getResult() {
		return result;
	} // getResult end

} // ParsingMaster end
