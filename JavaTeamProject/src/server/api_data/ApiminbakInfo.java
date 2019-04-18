package server.api_data;

import java.io.IOException;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author omnia
 * @brief 민박정보를 알려주는 클래스
 */
public class ApiminbakInfo extends ParsingMaster {
	final Logger logger = Logger.getLogger(ApiminbakDetails.class.getName());
	protected int pageNo;

	/**
	 * 
	 * @param pageNo: pageNo는 페이지의 번호를 나타낸다.
	 */
	public ApiminbakInfo(int pageNo) {
		urlStart = "http://apis.data.go.kr/6260000/BusanPensionInfoService/getPensionListInfo?";
		this.pageNo = pageNo;
		try {
			start();
		} catch (IOException e1) {
			logger.warning("[Starting error]");
		}
	}

	@Override
	public void setUrl() { // URL 세부 설정
		urlBuilder.append(urlStart);
		//urlBuilder.append(Integer.toString(numOfRows)); // 1이지만 타입캐스팅을 통해 string으로 변경해야함. url에서 1로 선언되어잇어서.
		urlBuilder.append("ServiceKey=");
		urlBuilder.append(serviceKey);
		urlBuilder.append("&pageNo=");
		urlBuilder.append(Integer.toString(pageNo));
		urlBuilder.append("&numOfRows=6");
		System.out.println(urlBuilder);
	}

	@Override
	public String setRoot() { // Node의 구성이 각 API마다 다르므로 Override해야 한다.
		return "//body/items/item";
	}

	@Override
	public void itemLoof(NodeList nodeList) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			// 첫 for 문이 아이템 리스트 전체
			NodeList child = nodeList.item(i).getChildNodes();
			for (int j = 0; j < child.getLength(); j++) {
				// 내부에서 도는 각 리스트 내용
				Node node = child.item(j);
				
				nodeInfo(node, "name", "업소명");
				nodeInfo(node, "gubun", "업소종류"); // "gubun"을 만나면, "업소종류"를 앞에 붙이고 출력한다.
				
				if (node.getNodeName().equals("idx")) {          //idx만 if쓴다. 나중에 상세정보를 부를때 숫자값만 필요하므로 :를 붙이고 나오지않기위해 따로 숫자만 넘긴다.
					//resultBuilding.append("일련번호: ");
					resultBuilding.append(node.getTextContent());
					resultBuilding.append("\n");
				} // idx if end
			} // 안 for end
		} // 밖 for end
		System.out.println(resultBuilding); // test
	} // itemLoof end
} // class end
