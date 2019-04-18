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
 * @brief 검색한 날짜별로 기본정보를 출력하는클래스
 *
 */
public class ExhibiThirdFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * 
	 * @param data1
	 * @param data2
	 * @param data3
	 * @param data4
	 * @param data5
	 * @param data6   일련번호를 제외한 정보를 6개를 받아옴.
	 * @param numbers 일련번호를 받음.
	 * @param m       연결된 클라이언트
	 */
	public ExhibiThirdFrame(String data1, String data2, String data3, String data4, String data5, String data6,
			String[] numbers, ClientMain m) {

		setResizable(false);
		int flag = 2; // 전시정보는 flag를 2로설정한다.

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 579, 684);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton button1 = new JButton(data1);
		button1.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		button1.setHorizontalAlignment(SwingConstants.LEFT);
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[0], flag).setVisible(true);
			}
		});
		button1.setBounds(39, 90, 488, 74);
		contentPane.add(button1);
		button1.setFocusPainted(false);
		button1.setContentAreaFilled(false);

		JButton button2 = new JButton(data2);
		button2.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		button2.setHorizontalAlignment(SwingConstants.LEFT);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[1], flag).setVisible(true);
			}
		});
		button2.setBounds(39, 174, 488, 74);
		contentPane.add(button2);
		button2.setFocusPainted(false);
		button2.setContentAreaFilled(false);

		JButton button3 = new JButton(data3);
		button3.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		button3.setHorizontalAlignment(SwingConstants.LEFT);
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[2], flag).setVisible(true);
			}
		});
		button3.setBounds(39, 258, 488, 74);
		contentPane.add(button3);
		button3.setFocusPainted(false);
		button3.setContentAreaFilled(false);

		JButton button4 = new JButton(data4);
		button4.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		button4.setHorizontalAlignment(SwingConstants.LEFT);
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[3], flag).setVisible(true);
			}
		});
		button4.setBounds(39, 342, 488, 74);
		button4.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		contentPane.add(button4);
		button4.setFocusPainted(false);
		button4.setContentAreaFilled(false);

		JButton button5 = new JButton(data5);
		button5.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		button5.setHorizontalAlignment(SwingConstants.LEFT);
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[4], flag).setVisible(true);
			}
		});
		button5.setBounds(39, 426, 488, 74);
		contentPane.add(button5);
		button5.setFocusPainted(false);
		button5.setContentAreaFilled(false);

		JButton button6 = new JButton(data6);
		button6.setFont(new Font("이롭게 바탕체 Medium", Font.PLAIN, 14));
		button6.setHorizontalAlignment(SwingConstants.LEFT);
		button6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CalenderPublicFourthFrame(numbers[5], flag).setVisible(true);
			}
		});
		button6.setBounds(39, 510, 488, 74);
		contentPane.add(button6);
		button6.setFocusPainted(false);
		button6.setContentAreaFilled(false);

		Box BusanExhibiBox = Box.createHorizontalBox();
		BusanExhibiBox.setFont(new Font("함초롬돋움", Font.PLAIN, 25));
		BusanExhibiBox.setBorder(
				new TitledBorder(new LineBorder(new Color(149, 235, 227), 4), "\uBD80\uC0B0 \uC804\uC2DC \uC815\uBCF4",
						TitledBorder.CENTER, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN, 25), null));
		BusanExhibiBox.setBounds(12, 44, 539, 568);
		contentPane.add(BusanExhibiBox);
	}
}
