package server.api_data;

import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author 박성호
 * @brief 축제정보를 알려주는 클래스 
 */
public class ApiFestivalInfo extends ParsingMaster {
	
	protected String date_yyyymm;

	/**
	 * 
	 * @param date_yyyymm: 20180911을 검색하면 해당 년월에 축제로 검색함.
	 */
	public ApiFestivalInfo(String i) {
		urlStart = "http://apis.data.go.kr/6260000/CultureInfoService/getFestivalList?";
		this.date_yyyymm = i;

		try {
			start();
		} // try end
		catch (IOException e) {
			logger.warning("[Details error]");
		} // catch end
	} // ApiFestival end

	@Override
	public void setUrl() {
		urlBuilder.append(urlStart);
		urlBuilder.append("numOfRows=4");
	//	urlBuilder.append(Integer.toString(numOfRows));
		urlBuilder.append("&serviceKey=");
		urlBuilder.append(serviceKey);
		urlBuilder.append("&date_yyyymm=");
		urlBuilder.append(date_yyyymm);
	}

	@Override
	public String setRoot() { // Node의 구성이 각 API마다 다르므로 Override해야 한다.
		return "//body/items/item";
	}

	@Override
	public void itemLoof(NodeList nodeList) { // 각 API마다 파싱할 데이터가 다르기 때문에 Override해야 한다.
		for (int i = 0; i < nodeList.getLength(); i++) {
			// 첫 for 문이 아이템 리스트 전체
			NodeList child = nodeList.item(i).getChildNodes();
			for (int j = 0; j < child.getLength(); j++) {
				// 내부에서 도는 각 리스트 내용
				Node node = child.item(j);
				if (node.getNodeName().equals("dataSid")) {          //idx만 if쓴다. 나중에 상세정보를 부를때 숫자값만 필요하므로 :를 붙이고 나오지않기위해 따로 숫자만 넘긴다.
					//resultBuilding.append("일련번호: ");
					resultBuilding.append(node.getTextContent());
					resultBuilding.append("\n");
				} // idx if end
				nodeInfo(node, "dataTitle", "게시물 제목");
				nodeInfo(node, "startDate", "행사 시작일");
				nodeInfo(node, "endDate", "행사 종료일");
				// nodeInfo(node, "dataContent", "게시물내용");
				// nodeInfo(node, "phone", "전화번호"); // phone을 만나면, "전화번호"를 앞에 붙이고 출력한다.
			} // for end
		} // for end
		System.out.println(resultBuilding);
	} // itemLoof end

}
