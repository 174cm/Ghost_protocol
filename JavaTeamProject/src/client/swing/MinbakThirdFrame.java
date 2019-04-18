
package client.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.ClientMain;

/**
 * 
 * @author 주환오
 * @brief 1페이지에서 부터 35페이지까지 숙박기본정보를 출력하는 클래스
 */
public class MinbakThirdFrame extends JFrame {
	/**
	 * 
	 * @param data1
	 * @param data2
	 * @param data3
	 * @param data4
	 * @param data5
	 * @param data6   일련번호를 제외한 6개의 정보를 받음
	 * @param numbers 일련번호를 받음
	 * @param m       클라이언트
	 */
	public MinbakThirdFrame(String data1, String data2, String data3, String data4, String data5, String data6,
			String[] numbers, ClientMain m) {
		setResizable(false);

		// 판넬 생성
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 391, 553);
		getContentPane().setLayout(null);

		JButton RightButton = new JButton(">"); // 오른쪽 페이지 이동버튼
		RightButton.setBounds(313, 464, 41, 36);
		RightButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		RightButton.setBackground(new Color(0, 255, 0));
		RightButton.setFocusPainted(true);
		RightButton.setContentAreaFilled(false);
		RightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO >을 누를시 다음 페이지로 넘어가도록 액션리스너에게 전달. 서버에서 다음페이지를 받음.
				m.run("다음페이지 민박");
				String list = m.getMessageReturn();
				String[] resultsplit = list.split("\\n+");
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
					numbers[i] = resultsplit[3 * i + 1]; // 3*i= 3줄이다. 일련번호 업소명 전화번호
				} // for-end
				new MinbakThirdFrame(results[0].toString(), results[1].toString(), results[2].toString(),
						results[3].toString(), results[4].toString(), results[5].toString(), numbers, m)
								.setVisible(true);
			}// actionPerformed
		});// ActionListener-end
		getContentPane().add(RightButton);

		JButton LeftButton = new JButton("<"); // 왼쪽페이지 이동버튼
		LeftButton.setBackground(new Color(240, 240, 240));
		LeftButton.setBounds(20, 464, 41, 36);
		LeftButton.setFocusPainted(false);
		LeftButton.setContentAreaFilled(false);
		LeftButton.addActionListener(new ActionListener() {
			// TODO <을 누를시 이전페이지로 넘어가도록 설정하고 다시 받아옴.
			public void actionPerformed(ActionEvent e) {
				m.run("이전페이지 민박");
				String list = m.getMessageReturn();
				String[] resultsplit = list.split("\\n+");
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
					numbers[i] = resultsplit[3 * i + 1]; // 3*i= 3줄이다. 일련번호 업소명 전화번호
				} // for-end
				new MinbakThirdFrame(results[0].toString(), results[1].toString(), results[2].toString(),
						results[3].toString(), results[4].toString(), results[5].toString(), numbers, m)
								.setVisible(true);
			}// actionPerformed
		});// ActionListener-end
		getContentPane().add(LeftButton);

		JButton Button1 = new JButton(data1);
		Button1.setBounds(20, 46, 334, 65);
		Button1.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		Button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MinbakFourthFrame(numbers[0]).setVisible(true);
			}
		});
		Button1.setHorizontalAlignment(SwingConstants.LEFT);
		Button1.setFocusPainted(false);
		Button1.setContentAreaFilled(false);
		getContentPane().add(Button1);

		JButton Button2 = new JButton(data2);
		Button2.setBounds(20, 116, 334, 65);
		Button2.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		Button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MinbakFourthFrame(numbers[1]).setVisible(true);
			}
		});
		Button2.setHorizontalAlignment(SwingConstants.LEFT);
		Button2.setFocusPainted(false);
		Button2.setContentAreaFilled(false);
		getContentPane().add(Button2);

		JButton Button3 = new JButton(data3);
		Button3.setBounds(20, 186, 334, 65);
		Button3.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		Button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MinbakFourthFrame(numbers[2]).setVisible(true);
			}
		});
		Button3.setHorizontalAlignment(SwingConstants.LEFT);
		Button3.setFocusPainted(false);
		Button3.setContentAreaFilled(false);
		getContentPane().add(Button3);

		JButton Button4 = new JButton(data4);
		Button4.setBounds(20, 256, 334, 65);
		Button4.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		Button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MinbakFourthFrame(numbers[3]).setVisible(true);
			}
		});
		Button4.setHorizontalAlignment(SwingConstants.LEFT);
		Button4.setFocusPainted(false);
		Button4.setContentAreaFilled(false);
		getContentPane().add(Button4);

		JButton Button5 = new JButton(data5);
		Button5.setBounds(20, 326, 334, 65);
		Button5.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		Button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MinbakFourthFrame(numbers[4]).setVisible(true);
			}
		});
		Button5.setHorizontalAlignment(SwingConstants.LEFT);
		Button5.setFocusPainted(false);
		Button5.setContentAreaFilled(false);
		getContentPane().add(Button5);

		JButton Button6 = new JButton(data6);
		Button6.setBounds(20, 396, 334, 65);
		Button6.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		Button6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MinbakFourthFrame(numbers[5]).setVisible(true);
			}
		});
		Button6.setHorizontalAlignment(SwingConstants.LEFT);
		Button6.setFocusPainted(false);
		Button6.setContentAreaFilled(false);
		getContentPane().add(Button6);

		// 민박 정보 그룹박스 설정
		Box MinbakInfoBox = Box.createHorizontalBox();
		MinbakInfoBox.setForeground(Color.WHITE);
		MinbakInfoBox.setFont(new Font("함초롬돋움", Font.PLAIN, 25));
		MinbakInfoBox
				.setBorder(new TitledBorder(new LineBorder(new Color(149, 235, 227), 2), "\uBBFC\uBC15 \uC815\uBCF4",

						TitledBorder.CENTER, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN, 25), new Color(0, 0, 0)));
		MinbakInfoBox.setBackground(Color.WHITE);
		MinbakInfoBox.setBounds(12, 10, 350, 495);
		getContentPane().add(MinbakInfoBox);

	}
}
