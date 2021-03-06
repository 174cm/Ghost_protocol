package server.api_data;

import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * 
 * @author 박성호
 * @brief 축제의 상세 정보를 알려주는 클래스
 */
public class ApiFestivalDetail extends ParsingMaster {
	protected int data_sid;
	protected int date_yyyymm;

	/**
	 *
	 * @param dataSid: 게시물ID
	 */
	public ApiFestivalDetail(int data_sid) {

		urlStart = "http://apis.data.go.kr/6260000/CultureInfoService/getFestivalDetail?";
		this.data_sid = data_sid;

		try {
			start();
		} // try end
		catch (IOException e) {
			logger.warning("[Festival Detail error]");
		} // catch end
	} // ApiFestival end

	@Override
	public void setUrl() {
		urlBuilder.append(urlStart);
		urlBuilder.append("&serviceKey=");
		urlBuilder.append(serviceKey);
		urlBuilder.append("&data_sid=");
		urlBuilder.append(data_sid);
	}

	@Override
	public String setRoot() {
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
				nodeInfo(node, "dataContent", "게시물 내용");
				if (node.getNodeName().equals("mainimgthumb")) {          //idx만 if쓴다. 나중에 상세정보를 부를때 숫자값만 필요하므로 :를 붙이고 나오지않기위해 따로 숫자만 넘긴다.

					resultBuilding.append(node.getTextContent());
					resultBuilding.append("\n");
				} // idx if end
				nodeInfo(node, "dataTitle", "Title");
				nodeInfo(node, "place", "행사 장소");
				nodeInfo(node, "tel", "주최 및 행사 연락처");
				nodeInfo(node, "time", "행사 시작시간");
				nodeInfo(node, "useperiod", "티켓 상세내용");
				nodeInfo(node, "trafin", "교통편");
				nodeInfo(node, "userHomepage", "행사 홈페이지");	
				nodeInfo(node, "wgsx", "위도");
				nodeInfo(node, "wgsy", "경도");
				
			} // 안 for end
		} // 밖 for end
		System.out.println(resultBuilding);
	} // itemLoof end

} // class end
