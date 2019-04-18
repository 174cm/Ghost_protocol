package client.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import server.api_data.ApiminbakDetails;

/**
 * 
 * @author 주환오
 * @brief 민박 기본정보를 이용하여 상세정보를 출력하여주는 클래스
 *
 */
public class MinbakFourthFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * 
	 * @param 일련번호를 받음
	 */
	public MinbakFourthFrame(String number) {
		setResizable(false);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 547, 233);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setContentPane(contentPane);

		int idx = Integer.parseInt(number); // 넘어온 일련번호를 정수형으로 바꿔줌.
		ApiminbakDetails a = new ApiminbakDetails(idx);
		String[] parts = a.getResult().split("\\n");
		String temp = "<html>";
		for (int i = 0; i < 5; i++) {
			temp = temp + parts[i];
			temp = temp + "<br />";

		}
		contentPane.setLayout(null);

		// 타이틀 보더 설정.
		Box DetailBox = Box.createHorizontalBox();
		DetailBox.setBorder(new TitledBorder(new LineBorder(new Color(149, 235, 227), 2), "\uC0C1\uC138\uC815\uBCF4",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN | Font.BOLD, 15),
				new Color(0, 0, 0)));
		DetailBox.setFont(new Font("Cambria", Font.PLAIN, 15)); // 복사하여 위에붙여넣자 않넣으면안된다.
		DetailBox.setBounds(12, 28, 496, 145);
		contentPane.add(DetailBox);

		JLabel lblNewLabel = new JLabel(temp);
		lblNewLabel.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		DetailBox.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

	}
}
