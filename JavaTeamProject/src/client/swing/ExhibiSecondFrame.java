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
 * @brief 전시 정보를 캘린더를 사용하여 검색할수있는 클래스
 *
 */
public class ExhibiSecondFrame extends JFrame {

	private JPanel contentPane;
	private final Box SearchBox = Box.createHorizontalBox();
	private final JDateChooser ExhibiCalender = new JDateChooser();
	private final JButton ExhibiSearch = new JButton("");
	private static final Logger logger = Logger.getLogger(ClientMain.class.getName());
	ImageIcon icon;
	Image image;

	/**
	 * 
	 * @param 연결된 클라이언트.
	 */
	public ExhibiSecondFrame(ClientMain client) {

		setResizable(false);
		icon = new ImageIcon("..\\JavaTeamProject\\image\\전시검색창.png"); // 이미지를 받아옴
		image = icon.getImage(); // icon.getImage(); 을 image에 넣음.
		image = image.getScaledInstance(365, 480, java.awt.Image.SCALE_SMOOTH); // image의 크기 재조정
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 383, 532);
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) { // 판넬에 이미지를 넣는함수.
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		SearchBox.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "\uAC80\uC0C9", TitledBorder.CENTER,
				TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN, 18), null));
		SearchBox.setBounds(35, 78, 299, 92);
		contentPane.add(SearchBox);

		ExhibiCalender.getCalendarButton().setBackground(new Color(255, 255, 255));
		ExhibiCalender.setBounds(69, 112, 123, 34);
		ExhibiCalender.setBackground(getBackground());
		ExhibiCalender.setDateFormatString("yyyyMMdd");
		contentPane.add(ExhibiCalender);

		ExhibiSearch.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\검색.png"));
		ExhibiSearch.setFont(new Font("서울남산체 M", Font.PLAIN, 14));
		ExhibiSearch.setBounds(209, 112, 95, 34);
		contentPane.add(ExhibiSearch);
		ExhibiSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat df = new SimpleDateFormat("yyyyMM"); // 캘린더의 날짜를 년/월로 받음
				String data = df.format(ExhibiCalender.getDate()); // 클릭한 날짜정보를 data에 저장
				System.out.println(data);
				client.run("전시" + data); // 날짜정보를 넣음
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
					new ExhibiThirdFrame(results[0].toString(), results[1].toString(), results[2].toString(),
							results[3].toString(), results[4].toString(), results[5].toString(), numbers, client)
									.setVisible(true);
				} // else-end
			}// actionPerformed-end
		});// addActionListener-end
	}

}
