package client.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import client.ClientMain;

/**
 * 
 * @author 주환오
 * @brief 검색버튼으로 숙박정보를 출력하기위한 클래스
 */
public class MinbakSecondFrame extends JFrame {

	private JPanel contentPane;
	private static final Logger logger = Logger.getLogger(ClientMain.class.getName());
	ImageIcon icon;
	Image image;

	public MinbakSecondFrame() {

		setResizable(false);
		ClientMain m = new ClientMain(); // 클라이언트 메인 생성
		// 판넬

		icon = new ImageIcon("..\\JavaTeamProject\\image\\[크기변환]민박검색창.png"); // 이미지를 받아옴icon.getImage();
		image = icon.getImage();
		image = image.getScaledInstance(383, 532, java.awt.Image.SCALE_SMOOTH); // image의 크기 재조정
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 나가기 버튼을 눌렀을때 전체 종료되는것이아니라 현재 창만 닫는다.
		setBounds(100, 100, 386, 528);
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) { // 판넬에 이미지를 넣는함수.
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		contentPane.setBackground(Color.WHITE); // 배경을 흰색으로한다.
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 민박검색 버튼 설정
		JButton SearchButton = new JButton("");
		SearchButton.setBounds(136, 429, 130, 40);
		contentPane.add(SearchButton);
		SearchButton.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\숙소검색버튼.png"));
		SearchButton.addActionListener(new ActionListener() { // 민박검색 버튼 클릭함수
			public void actionPerformed(ActionEvent e) {
				m.run("민박"); // 민박을 클라이언트의 run함수를 통해 서버에게 보낸다.
				String list = m.getMessageReturn(); // 받아온 메시지를 list에 넣는다.
				logger.info(list);
				if (list.equals("")) { // 잘못 받아왔을 때
				} // if-end
				else {
					System.out.println(list);
					String[] resultsplit = list.split("\\n+"); // 받아온 list값을 split함수로 \n 로 자른다.
					System.out.println(resultsplit.length);
					StringBuilder[] results = new StringBuilder[6];
					for (int i = 0; i < 6; i++) {
						// 출력된부분을 자른다. 업소명과 전화번호를 여기서 출력한다. 여기서는 배열에 넣기때문에 순서를 바꿀 수 있다.
						results[i] = new StringBuilder("<html>");
						results[i].append(resultsplit[i * 3 + 2]).append("<br />");
						results[i].append("<br />");
						results[i].append(resultsplit[3 * i + 3]);
						results[i].append("</html>");
					} // for-end

					String numbers[] = new String[6]; // 일련번호를 넘긴다.
					for (int i = 0; i < 6; i++) {
						numbers[i] = resultsplit[3 * i + 1]; // 3*i= 3줄이다. 일련번호
					} // for-end

					new MinbakThirdFrame(results[0].toString(), results[1].toString(), results[2].toString(),
							results[3].toString(), results[4].toString(), results[5].toString(), numbers, m)
									.setVisible(true);
				} // else-end
			}// actionPerformed-end
		});// ActionListener -end

		SearchButton.setFont(new Font("365쉬는시간", Font.PLAIN, 14));
		SearchButton.setFocusPainted(false);
		SearchButton.setContentAreaFilled(false);

		// 체크인 그룹 박스안의 라벨 설정
		JLabel checkinlabel = new JLabel("");
		checkinlabel.setBounds(41, 88, 277, 160);
		contentPane.add(checkinlabel);
		checkinlabel.setHorizontalAlignment(SwingConstants.CENTER);
		checkinlabel.setFont(new Font("야놀자 야체Regular", Font.PLAIN, 23));
	}
}
