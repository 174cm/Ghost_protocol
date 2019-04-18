package client.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import client.ClientMain;

/**
 * 
 * @author 주환오
 * @brief 음악/무용을 캘린더를 사용하여검색할수있는 클래스이다
 *
 */
public class DancingSecondFrame extends JFrame {

	private JPanel contentPane;
	private static final Logger logger = Logger.getLogger(ClientMain.class.getName());
	ImageIcon icon;
	Image image;

	/**
	 * 
	 * @param client 연결된 클라이언트
	 */
	public DancingSecondFrame(ClientMain client) {

		setResizable(false);
		// 판넬
		icon = new ImageIcon("..\\JavaTeamProject\\image\\뮤지컬연극검색창.png"); // 이미지를 받아옴
		image = icon.getImage(); // icon.getImage()로 image에 넣는다.
		image = image.getScaledInstance(367, 490, java.awt.Image.SCALE_SMOOTH); // image의 크기 재조정
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 378, 518);
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) { // 판넬에 이미지를 넣는함수.
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		contentPane.setBackground(Color.WHITE); // 판넬 배경색을 흰색으로 변경.
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 캘린더
		JDateChooser DancingCalender = new JDateChooser();
		DancingCalender.getCalendarButton().setBackground(new Color(255, 255, 255));
		DancingCalender.setBounds(59, 112, 123, 34);
		contentPane.add(DancingCalender);
		DancingCalender.setDateFormatString("yyyyMMdd");
		DancingCalender.setBackground(getBackground());

		// 검색 버튼
		JButton SearchButton = new JButton("");
		SearchButton.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\검색.png"));
		SearchButton.setBounds(205, 112, 95, 34);
		contentPane.add(SearchButton);
		SearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat df = new SimpleDateFormat("yyyyMM"); // 캘린더의 날짜를 년/월로 받음
				String data = df.format(DancingCalender.getDate()); // 클릭한 날짜정보를 data에 저장
				System.out.println(data);
				client.run("무용음악" + data); // 날짜정보를 넣음
				String list = client.getMessageReturn();
				System.out.println(list);
				logger.info(list);
				if (list.equals("")) {
					// 잘못 받아 온 거
				} // if-end
				else {
					System.out.println(list);
					String[] resultsplit = list.split("\\n+");
					System.out.println(resultsplit.length);
					StringBuilder[] results = new StringBuilder[6];
					for (int i = 0; i < 6; i++) {
						// 출력된부분을 자른다. 업소명과 전화번호를 여기서 출력한다. 여기서는 배열에 넣기때문에 순서를 바꿀 수 있다.
						results[i] = new StringBuilder("<html>");
						results[i].append(resultsplit[4 * i + 2]).append("<br />");
						results[i].append(resultsplit[i * 4 + 4]).append("<br />");
						results[i].append(resultsplit[4 * i + 3]);
						results[i].append("</html>");
					} // for-end

					String numbers[] = new String[6]; // 일련번호를 넘긴다.
					for (int i = 0; i < 6; i++) {
						numbers[i] = resultsplit[4 * i + 1]; // 3*i= 3줄이다. 일련번호 업소명 전화번호
					} // for-end
					new DancingThirdFrame(results[0].toString(), results[1].toString(), results[2].toString(),
							results[3].toString(), results[4].toString(), results[5].toString(), numbers, client)
									.setVisible(true);
				} // else-end
			}// actionPerformed-end
		});// addActionListener-end
		SearchButton.setFont(new Font("서울남산체 M", Font.PLAIN, 14));
		SearchButton.setFocusPainted(false);
		SearchButton.setContentAreaFilled(false);

		// 검색 그룹박스
		Box SearchBox = Box.createHorizontalBox();
		SearchBox.setFont(new Font("아리따-돋움(TTF)-Medium", Font.BOLD, 15));
		SearchBox.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "\uAC80\uC0C9", TitledBorder.CENTER,
				TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN, 18), new Color(0, 0, 0)));
		SearchBox.setBounds(32, 77, 299, 92);
		contentPane.add(SearchBox);

	}
}
