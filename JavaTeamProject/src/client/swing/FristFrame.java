package client.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.ClientMain;

/**
 * 
 * @author 주환오
 * @brief 각 카테고리별로 버튼이 존재하는 메인 프레임클래스
 *
 */
public class FristFrame extends JFrame {
	private JPanel contentPane;
	ImageIcon icon;
	Image image;

	/**
	 * 
	 * @param client 연결된 클라이언트
	 */
	public FristFrame(ClientMain client) {
		setResizable(false); // 크기고정
		// 판넬
		icon = new ImageIcon("..\\JavaTeamProject\\image\\메인 화면1.png"); // 이미지를 받아옴icon.getImage();
		image = icon.getImage();
		image = image.getScaledInstance(792, 707, java.awt.Image.SCALE_SMOOTH); // image의 크기 재조정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면이 전체 다꺼진다.
		setBounds(100, 100, 792, 707);
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) { // 판넬에 이미지를 넣는함수.
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		contentPane.setBackground(Color.WHITE); // 배경색을 흰색으로 지정한다.
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2)); // 판넬을 LineBorder로 지정.
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 민박 검색 버튼.
		JButton searchMinbak = new JButton("");
		searchMinbak.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\숙박버튼.png"));
		searchMinbak.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
		searchMinbak.addActionListener(new ActionListener() { // 민박정보 버튼 클릭함수
			public void actionPerformed(ActionEvent e) {
				new MinbakSecondFrame().setVisible(true); // 클릭시 민박 세컨드프레임이 보이게한다.
			}
		});
		searchMinbak.setBounds(510, 563, 107, 50);
		contentPane.add(searchMinbak);
		searchMinbak.setFocusPainted(false); // 이미지 테두리 효과를 없앰
		searchMinbak.setContentAreaFilled(false); // 버튼과 이미지 사이 공백의 효과를 없앰

		// 축제 검색 버튼
		JButton searchFestival = new JButton("");
		searchFestival.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\축제버튼.png"));
		searchFestival.setFont(new Font("함초롬돋움", Font.PLAIN, 14));
		searchFestival.addActionListener(new ActionListener() { // 축제 정보 버튼 클릭함수.
			public void actionPerformed(ActionEvent e) {
				new FestivalSecondFrame(client).setVisible(true); // 축제 두번째 프레임에 클라이언트를 넘겨주고 있다.
			}
		});
		searchFestival.setBounds(34, 563, 107, 50);
		searchFestival.setFocusPainted(false); // 이미지 테두리 효과를 없앰
		searchFestival.setContentAreaFilled(false); // 버튼과 이미지 사이 공백의 효과를 없앰
		contentPane.add(searchFestival);

		// 메뉴 박스
		Box MenuBox = Box.createHorizontalBox();
		MenuBox.setFont(new Font("함초롬돋움", Font.PLAIN, 18));
		MenuBox.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 3, true), "\uBA54\uB274",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("함초롬돋움", Font.PLAIN, 18), new Color(0, 0, 0)));
		MenuBox.setBounds(12, 499, 752, 160);
		contentPane.add(MenuBox);

		// 전시버튼
		JButton ExhibitionButton = new JButton("");
		ExhibitionButton.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\전시버튼.png"));
		ExhibitionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ExhibiSecondFrame(client).setVisible(true);
			}
		});
		ExhibitionButton.setBounds(272, 563, 107, 50);
		contentPane.add(ExhibitionButton);

		// 뮤지컬버튼
		JButton MusicalButton = new JButton("");
		MusicalButton.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\뮤지컬버튼.png"));
		MusicalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MusicalSecondFrame(client).setVisible(true);
			}
		});
		MusicalButton.setBounds(391, 563, 107, 50);
		contentPane.add(MusicalButton);

		// 음악 무용 버튼
		JButton DancingButton = new JButton("");
		DancingButton.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\연극버튼.png"));
		DancingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DancingSecondFrame(client).setVisible(true);

			}
		});
		DancingButton.setBounds(153, 563, 107, 50);
		contentPane.add(DancingButton);

		JButton Post = new JButton("");
		Post.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new T_board().setVisible(true);

			}
		});
		Post.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\게시판버튼.png"));
		Post.setBounds(629, 563, 107, 50);
		contentPane.add(Post);

	}
}
