package client.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import server.BoardDAO;
import javax.swing.ImageIcon;

/**
 * 
 * @author 고명주
 * @brief 게시판 메인 창 -> 글쓰기 버튼 및 게시글 출력 테이블
 */

public class T_board extends JFrame implements MouseListener, ActionListener {
	//////// 변수 선언
	Vector v;
	Vector cols;
	DefaultTableModel model;
	JScrollPane pane;
	JTable jTable = null; // 게시글 출력할 테이블
	JPanel pbtn;
	JButton btnWrite;
	ImageIcon icon;
	Image image;
	JPanel contentPane;

	public T_board() { // 보드 구성
		getContentPane().setBackground(Color.WHITE); // 보드 구성
		setTitle("게시판");
		setSize(620, 640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 닫기 버튼을 눌렀을때 새로 띄운 게시판 창만 닫게 설정

		// 배경에 사진 넣기 위함
		icon = new ImageIcon("..\\JavaTeamProject\\image\\게시판메인배경.png");// 이미지를 받아옴
		// 이미지의 크기 조정을 위한 코드
		image = icon.getImage();// 받아온 이미지를 image에 저장
		image = image.getScaledInstance(605, 602, java.awt.Image.SCALE_SMOOTH);// image의 크기 재조정
		icon = new ImageIcon(image);// 재조정한 이미지로 icon 생성
		contentPane = new JPanel() { // JPanel의 배경으로 icon 삽입
			public void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, null);

				setOpaque(false);
				super.paintComponent(g);
			}
		};
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// BoardDAO
		BoardDAO dao = new BoardDAO();
		v = dao.getcontents_List(); // 테이블 열
		System.out.println("v=" + v);
		cols = getColumn(); // 테이블 컬럼

		// 테이블 생성
		DefaultTableModel model = new DefaultTableModel(v, cols);
		jTable = new JTable(model);
		pane = new JScrollPane(jTable);
		pane.setBounds(31, 68, 537, 507);
		// 테이블 컬럼 수정 금지(이동이나 사이즈 조절)
		jTable.getTableHeader().setReorderingAllowed(false);
		jTable.getTableHeader().setResizingAllowed(false);

		getContentPane().add(pane);
		btnWrite = new JButton("");
		btnWrite.setBounds(427, 10, 141, 32);
		btnWrite.setBorderPainted(false); // 버튼 윤곽선 없앰
		btnWrite.setContentAreaFilled(false); // 버튼 안 채우기 따로 안 함
		// btnWrite.setOpaque(false); //버튼 투명화

		// btnWrite.setFocusPainted(false); //선택되었을 때 생기는 테두리 없앰
		contentPane.add(btnWrite);
		btnWrite.setIcon(new ImageIcon("..\\JavaTeamProject\\image\\글쓰기버튼.png"));
		btnWrite.addActionListener(this); // 글쓰기 버튼 클릭 이벤트 생성
		jTable.addMouseListener(this);// 마우스 클릭 이벤트 생성

		setVisible(true);
	}

	// 테이블 컬럼
	public Vector getColumn() {
		Vector col = new Vector();

		col.add("닉네임");
		col.add("제목");
		col.add("분류");

		return col;
	}

	// 테이블 갱신
	public void jTableRefresh() {
		BoardDAO dao = new BoardDAO();
		DefaultTableModel model = new DefaultTableModel(dao.getcontents_List(), getColumn()) {
			public boolean isCellEditable(int i, int c) { // 테이블을 직접 수정할 수 없도록 함
				return false;
			}
		};
		jTable.setModel(model);
	}

	// 게시판 창 띄우는 메인
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new T_board();
	}

	// 액션 리스너 오버라이드
	public void mouseClicked(MouseEvent e) {
		// 마우스 클릭시 이벤트
		int r = jTable.getSelectedRow();
		String name = (String) jTable.getValueAt(r, 0);

		TWrite_board wb = new TWrite_board(name, this); // 아이디를 기반으로 수정창 생성
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnWrite) {
			new TWrite_board(this);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
