package server.api_data;

import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author 박성호
 * @brief 뮤지컬의 정보를 알려주는 클래스
 */
public class ApiMusicalInfo extends ParsingMaster {
	protected String date_yyyymm;
	/**
	 * 
	 * @param date_yyyymm 요구명세에서 요구하는 날짜
	 */
	public ApiMusicalInfo(String date_yyyymm) {
		urlStart = "http://apis.data.go.kr/6260000/CultureInfoService/getMusicalList?";
		this.date_yyyymm = date_yyyymm;

		try {
			start();
		} // try end
		catch (IOException e) {
			logger.warning("[Musical error]");
		} // catch end
		
	} // MusicalInfo end

	@Override
	public void setUrl() {
		
		urlBuilder.append(urlStart);
		urlBuilder.append("&serviceKey=");
		urlBuilder.append(serviceKey);
		urlBuilder.append("&date_yyyymm=");
		urlBuilder.append(date_yyyymm);
		urlBuilder.append("&numOfRows=6");
	} //setUrl end

	@Override
	public String setRoot() {
		// TODO Auto-generated method stub
		return "//body/items/item";
	} //setRoot end

	@Override
	public void itemLoof(NodeList nodeList) {
		// TODO Auto-generated method stub
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
			} // 안 for end
		} // 밖 for end
		System.out.println(resultBuilding);
	} // itemLoof end

}
