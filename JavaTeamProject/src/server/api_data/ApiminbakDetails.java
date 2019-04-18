package server.api_data;

import java.io.IOException;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author omnia
 * @brief 민박 상세 정보를 알려주는 클래스
 */
public class ApiminbakDetails extends ParsingMaster {
	final Logger logger = Logger.getLogger(ApiminbakDetails.class.getName());
	private int idx; // 일련번호 선언 현재 api문서에서는 일련번호를 가지고 정보를 넘긴다.

	/**
	 * 
	 * @param idx: idx는 요구명세에서 요구하는 일련번호
	 */ 
	public ApiminbakDetails(int idx) {
		urlStart = "http://apis.data.go.kr/6260000/BusanPensionInfoService/getPensionDetailsInfo?";
		this.idx = idx;

		try {
			start();
		} catch (IOException e) {
			logger.warning("[Details error]");
		}
	}

	@Override
	public void setUrl() { // URL 세부 설정
		urlBuilder.append(urlStart);
		urlBuilder.append("&serviceKey=");
		urlBuilder.append(serviceKey);
		urlBuilder.append("&pageNo=1"); // 1로 고정을 하면, 형변환을 할 필요가 없다.
		urlBuilder.append("&numOfRows=");
		urlBuilder.append(Integer.toString(numOfRows)); // 1이지만 타입캐스팅을 통해 string으로 변경해야함. url에서 1로 선언되어잇어서.
		urlBuilder.append("&idx=");
		urlBuilder.append(Integer.toString(idx));
	} // details 에서는 numOfRows가 필요가없다.(상세 내용이기 때문에)

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
				nodeInfo(node, "phone", "전화번호"); // phone을 만나면, "전화번호"를 앞에 붙이고 출력한다.
			//	nodeInfo(node, "idx", "일련번호");
				nodeInfo(node, "addrRoad", "도로명주소");
				nodeInfo(node, "room", "방 갯수");
			} // 안 for end
		} // 밖 for end
		System.out.println(resultBuilding); // test
	} // itemLoof end
} // class end
