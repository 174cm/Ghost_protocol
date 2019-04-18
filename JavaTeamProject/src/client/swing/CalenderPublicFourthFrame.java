package client.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

import server.api_data.ApiExhibitDetail;
import server.api_data.ApiFestivalDetail;
import server.api_data.ApiMusicalDetail;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.border.TitledBorder;

import client.ClientMain;

import javax.swing.SwingConstants;

/**
 * 
 * @author 주환오
 * @brief 세번째 프레임에서 기본정보 클릭할 시 그에 맞는 상세정보,그림등을 출력하여 준다. 이 클래스에서는 축제,뮤지컬,전시에 대한
 *        상세정보보기를 연동하여 보여주고있다.
 */
public class CalenderPublicFourthFrame extends JFrame {
	private static final Logger logger = Logger.getLogger(ClientMain.class.getName());
	private JPanel contentPane;
	/**
	 * 
	 * @param number 기본정보에대한 일련번호를 받는다.
	 * @param flag   플래그의 값이 1이면 축제,2이면 전시정보,3이면 뮤지컬정보를 뜻한다.
	 */
	public CalenderPublicFourthFrame(String number, int flag) {
		setResizable(false);
		// 판넬
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1128, 615);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		int idx = Integer.parseInt(number); // 넘어온 일련번호를 정수형으로 바꿔줌.
		String temp = "<html>";
		String title = "<html>"; // 그룹박스에 담을 Title변수 지정
		String detail = "<html>"; // 내용정보를 따로담을것이기때문에 detail 변수 지정
		String[] parts2 = null;

		if (flag == 1) // 축제정보가 넘어왔을 때
		{
			ApiFestivalDetail a = new ApiFestivalDetail(idx);
			parts2 = a.getResult().split("\\n");
		} // if-end
		else if (flag == 2) // 전시정보가 넘어왔을 때
		{
			ApiExhibitDetail b = new ApiExhibitDetail(idx);
			parts2 = b.getResult().split("\\n");
		} // else-if end //축제 세부정보를 일련번호로 불러온다.
		else if (flag == 3) // 뮤지컬정보가 넘어왔을 때
		{
			ApiMusicalDetail c = new ApiMusicalDetail(idx);
			parts2 = c.getResult().split("\\n");
		} // else-if end

		logger.info("idx");
		String[] parts = parts2; // parts2 정보를 다시 parts에 옮겨적는다.

		int length = parts.length; // 길이가 가변적이므로 길이를 파악한다.
		logger.info("length");

		URL url = null; // 이미지 URL 받아올 url생성
		BufferedImage bi = null;
		try {
			url = new URL(parts[3]); // parts[3]은 이미지url
			bi = ImageIO.read(url);
		} catch (IllegalArgumentException ex) { // 잘못된인자예외
			bi = null;
		} catch (MalformedURLException e) { // 프로토콜이 잘못된경우
			bi = null;
		} catch (IOException e) {
			bi = null;
		} // try-catch end

		if (bi != null) // 이미지가 비어있지않다면
		{
			detail = detail + parts[1]; // 디테일 정보 상세정보는 따로 넣을것이기 때문에 detail에 개별적으로 넣어준다.
			title = title + parts[2]; // 제목을 타이틀보더에 적을것이기 때문에 title에 개별적으로 넣어준다.
			temp = temp + parts[2] + "<br />"; // 전체 정보를 출력하는 곳에도 제목을 출력하게 parts[2]를 넣어준다.
												// [3]이 없는이유는 이미지파일이기 때문에 라벨에 띄울 필요가없다.
			title = title.replace("Title : ", ""); // 제목 만 쓰고싶으므로 Title을 빼준다.
			title = title.replace("게시물 제목 : ", "");
		} else // 이미지가 비었을 때
		{
			if (length == 10) { // 전시에서 상세 내용이 없을경우 길이가 10이다
				detail = detail + parts[0]; // 상세내용이지만 출력되지않는다.
				title = title + parts[1]; // 10일경우 [1]이 제목이다.
				temp = temp + parts[1] + "<br />"; //
				title = title.replace("게시물 제목 : ", "");
			} else if (length == 11) // 전시 상세내용이 있을경우 길이가 11이다.
			{
				detail = detail + parts[1]; // 길이가 11일 경우상세내용은 [1]에 있다
				title = title + parts[2]; // 길이가 11일 경우 제목은 [2]에있다.
				temp = temp + parts[2] + "<br />"; // 위의 title을 을따로쓰고 전체적으로 상세정보에 넣는곳에도 제목을 넣어준다.
				title = title.replace("게시물 제목 : ", "");
			} else if (length == 12) // 뮤지컬은 길이가12 항목도 넘어온다.
			{
				detail = detail + parts[1]; // 12일때 상세내용은 [1]에 있다.
				title = title + parts[2]; // [2]에 제목이있다.
				temp = temp + parts[1] + "<br />";
				title = title.replace("게시물 제목 : ", "");
			}
		} // else-end

		for (int i = 4; i < length; i++) {
			temp = temp + parts[i];
			temp = temp + "<br />";
		} // for-end

		// 이미지 그룹 박스
		Box ImageBox = Box.createHorizontalBox();
		ImageBox.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "행사 사진", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN | Font.BOLD, 17), null));
		ImageBox.setBounds(37, 70, 594, 470);
		contentPane.add(ImageBox);

		// 행사사진 라벨(이미지)
		JLabel Festivalimage = new JLabel("");
		ImageBox.add(Festivalimage);
		if (bi != null) // 불러온 이미지가 비어있지 않다면
		{
			Festivalimage.setIcon(new ImageIcon(bi)); // 불러온 url 이미지를 담고있다.
		}

		// 행사 사진 그룹박스
		Box DetailBox = Box.createHorizontalBox();
		DetailBox.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "\uC0C1\uC138 \uC815\uBCF4",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN | Font.BOLD, 17),
				new Color(0, 0, 0)));
		DetailBox.setBounds(661, 70, 420, 218);
		contentPane.add(DetailBox);
		JLabel DetailLable = new JLabel(temp);
		DetailLable.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		DetailBox.add(DetailLable);

		// 게시물 내용 그룹박스
		Box DetailsBox = Box.createHorizontalBox();
		DetailsBox.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "\uAC8C\uC2DC\uBB3C \uB0B4\uC6A9",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN | Font.BOLD, 17),
				new Color(0, 0, 0)));
		DetailsBox.setBounds(661, 298, 420, 242);
		contentPane.add(DetailsBox);

		// 게시물 내용 라벨
		detail = detail.replace("게시물 내용 :", " "); // 게시물내용을 삭제한다 . 타이틀에 적어주었다.
		JLabel Details = new JLabel(detail);
		Details.setHorizontalAlignment(SwingConstants.CENTER);
		Details.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		DetailsBox.add(Details);

		// 맨위 타이틀 제목이들어가는 그룹박스.
		Box TitleGrupBox = Box.createHorizontalBox();
		TitleGrupBox
				.setBorder(new TitledBorder(new LineBorder(new Color(149, 235, 227), 4), title, TitledBorder.LEADING,
						TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN | Font.PLAIN, 25), new Color(0, 0, 0)));
		TitleGrupBox.setBounds(12, 10, 1088, 557);
		contentPane.add(TitleGrupBox);

	}
}
