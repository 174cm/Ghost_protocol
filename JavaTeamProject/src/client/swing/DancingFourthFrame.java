package client.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.ClientMain;
import server.api_data.ApiMusicDancingDetail;

/**
 * 
 * @author 주환오
 * @brief 이클래스는 음악/무용에따른 기본정보를 클릭하였을 때 그림,상세정보등을 출력하여준다.
 *
 */
public class DancingFourthFrame extends JFrame {
	private static final Logger logger = Logger.getLogger(ClientMain.class.getName());
	private JPanel contentPane;

	/**
	 * 
	 * @param number 각 기본정보에 대한 일련번호를 뜻한다.
	 */
	public DancingFourthFrame(String number) {
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
		String temp = "<html>"; // 전체정보를 담을 html형 temp를 선언.
		String title = "<html>"; // 제목을 따로 타이틀보더에 넣을것이기 때문에 개별적으로 title로 선언해준다.
		String detail = "<html>"; // 상세내용을 따로 넣을것이기 때문에 개별적으로 선언하여준다.
		ApiMusicDancingDetail a = new ApiMusicDancingDetail(idx); // 정수형으로 반환된 일련번호 값을 음악/무용 디테일 파일을 불러와 넣어준다.

		logger.info("idx");
		String[] parts = a.getResult().split("\\n"); // \n단위로 자른다.

		int length = parts.length; // 길이가 가변적 이므로 길이를 파악한다.
		logger.info("lenght");

		URL url = null; // 이미지 URL 받아올 url생성
		BufferedImage bi = null;

		try {
			url = new URL(parts[2]); // parts[2]은 이미지이다.
			bi = ImageIO.read(url);
		} catch (IllegalArgumentException ex) { //
			bi = null;
		} catch (MalformedURLException e) { // 프로토콜 문제일때
			bi = null;
		} catch (IOException e) {
			bi = null;
		} // try-catch end

		if (bi != null) // 이미지가 비어있지않다면
		{
			title = title + parts[1]; // 비어있지 않을때는 [1]에 제목정보가들어가있다.
			temp = temp + parts[1] + "<br />"; // 위의 title을 을따로쓰고 전체적으로 상세정보에 넣는곳에도 제목을 넣어준다.
			title = title.replace("Title : ", ""); // 제목만 쓰고싶으므로 Title을 빼준다.
			title = title.replace("게시물 제목 : ", ""); // 제목만쓰고싶으므로 게시물 제목을 뺀다.
		} else // 이미지가 parts[2]에 없을 때 [3]에 있을 가능성도 있다.

		{
			try {
				url = new URL(parts[3]); // [3]에 이미지를 넣는다.
				bi = ImageIO.read(url);
			} catch (IllegalArgumentException ex) {
				bi = null;
			} catch (MalformedURLException e) {
				bi = null;
			} catch (IOException e) {
				bi = null;
			} // try-catch end
			detail = detail + parts[1]; // 상세내용은 [1]에 존재한다
			title = title + parts[2]; // 제목은 [2]에 존재한다.
			title = title.replace("게시물 제목 : ", "");
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
		if (bi != null) {
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
						TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN | Font.BOLD, 25), new Color(0, 0, 0)));
		TitleGrupBox.setBounds(12, 10, 1088, 557);
		contentPane.add(TitleGrupBox);

	}
}
