package client.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.ClientMain;
/**
 * 
 * @author 주환오
 * @brief 날짜정보를 이욯아여 기본정보를 4개출력하여주는 클래스 
 *
 */
public class FestivalThirdFrame extends JFrame {

	private JPanel contentPane;
/**
 * 
 * @param data1
 * @param data2
 * @param data3
 * @param data4
 *        일련번호를 제외한 정보를 4개받아옴
 * @param numbers 일련번호를 받음
 * @param m       연결된 클라이언트
 */
	public FestivalThirdFrame(String data1, String data2, String data3, String data4, String[] numbers, ClientMain m) {

		setResizable(false);
		int flag = 1; // 페스티벌 api는 flag를 1로지정한다.
		// 판넬 설정
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 469, 548);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton butt1 = new JButton(data1);
		butt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[0], flag).setVisible(true);
			}
		});
		butt1.setFocusPainted(false);
		butt1.setContentAreaFilled(false);
		butt1.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		butt1.setHorizontalAlignment(SwingConstants.LEFT);
		butt1.setBounds(39, 64, 372, 90);
		contentPane.add(butt1);

		JButton butt2 = new JButton(data2);
		butt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[1], flag).setVisible(true);
			}
		});
		butt2.setFocusPainted(false);
		butt2.setContentAreaFilled(false);
		butt2.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		butt2.setHorizontalAlignment(SwingConstants.LEFT);
		butt2.setBounds(39, 164, 372, 90);
		contentPane.add(butt2);

		JButton butt3 = new JButton(data3);
		butt3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[2], flag).setVisible(true);
			}
		});
		butt3.setFocusPainted(false);
		butt3.setContentAreaFilled(false);
		butt3.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		butt3.setHorizontalAlignment(SwingConstants.LEFT);
		butt3.setBounds(39, 264, 372, 90);
		contentPane.add(butt3);

		JButton butt4 = new JButton(data4);
		butt4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[3], flag).setVisible(true);
			}
		});
		butt4.setFocusPainted(false);
		butt4.setContentAreaFilled(false);
		butt4.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		butt4.setHorizontalAlignment(SwingConstants.LEFT);
		butt4.setBounds(39, 364, 372, 90);
		contentPane.add(butt4);

		// 부산축제 정보 그룹박스
		Box BusanFestivalinfoBox = Box.createHorizontalBox();
		BusanFestivalinfoBox.setFont(new Font("함초롬돋움", Font.PLAIN, 25));
		BusanFestivalinfoBox.setBorder(new TitledBorder(new LineBorder(new Color(149, 235, 227), 4), "부산 축제 정보",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN, 25), new Color(0, 0, 0)));
		BusanFestivalinfoBox.setBounds(12, 25, 430, 452);
		contentPane.add(BusanFestivalinfoBox);

	}
}
