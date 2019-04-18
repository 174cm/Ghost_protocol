package client.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.BoardDAO;
import server.BoardDTO;
/**
 * 
 * @author 고명주
 * @brief 게시글 작성 및 수정, 삭제 창
 */
public class TWrite_board extends JFrame implements ActionListener {

	JPanel panel;
	JTextField tfname, tftitle;// 닉네임창, 제목창
	JTextArea tacontents; // 내용창
	JPasswordField pfPwd; // 비밀번호 입력창
	JButton btnInsert, btncancel_1, btncancel_2, btnUpdate, btnDelete; // 올리기, 취소, 수정, 삭제 버튼
	JComboBox cbcategorize; // 분류 선택

	T_board tboard;

	/**
	 *  글쓰기 생성자
	 */
	public TWrite_board() {

		TWrite_board();// UI 작성
		btnUpdate.setEnabled(false);
		btnUpdate.setVisible(false);
		btnDelete.setEnabled(false);
		btnDelete.setVisible(false);
		btncancel_2.setEnabled(false);
		btncancel_2.setVisible(false);
	}

	
	// 글쓰기용 생성자
	public TWrite_board(T_board tboard) {

		TWrite_board();
		btncancel_2.setEnabled(false);
		btncancel_2.setVisible(false);
		btnUpdate.setEnabled(false);
		btnUpdate.setVisible(false);
		btnDelete.setEnabled(false);
		btnDelete.setVisible(false);
		this.tboard = tboard;
	}

	/**
	 * 
	 * @param name 삭제할 게시글을 찾기 위한 PRIMARY KEY
	 * @param tboard
	 * @brief 수정, 삭제용 생성자(id이용)
	 */
	public TWrite_board(String name, T_board tboard) {
		TWrite_board();
		btncancel_1.setEnabled(false);
		btncancel_1.setVisible(false);
		btnInsert.setEnabled(false);
		btnInsert.setVisible(false);
		this.tboard = tboard;

		System.out.println("name =" + name);

		BoardDAO dao = new BoardDAO();
		BoardDTO vCon = dao.getBoardDTO(name);
		viewData(vCon);
	}

	/**
	 * 
	 * @param vCon PRIMARY KEY(name)을 바탕으로 게시글 정보를 가지고 와서 수정화면에 세팅해주는 메소드
	 */
	private void viewData(BoardDTO vCon) {

		String name = vCon.getName();
		String pwd = vCon.getPwd();
		String title = vCon.getTitle();
		String contents = vCon.getContents();
		String categorize = vCon.getCategorize();

		// 화면에 셋팅
		tfname.setText(name);
		tfname.setEditable(false); // 편집할 수 없게
		pfPwd.setText(""); // 비밀번호는 재입력할 수 있도록 안보준다.
		tftitle.setText(title);// 제목
		tacontents.setText(contents); // 내용
		cbcategorize.setSelectedItem(categorize);
	}
/**
 * @brief 글쓰기 보드 구성
 */
	private void TWrite_board() {

		setLocation(575, 175);

		this.setTitle("게시물");
		panel = new JPanel();
		panel.setLayout(null);

		// 분류 카테고리
		JLabel bcategorize = new JLabel("분류");
		String[] arrcategorize = { "---", "축제", "전시 / 행사", "음악 / 무용", "연극 / 뮤지컬", "숙소", "잡담", "문의" };
		cbcategorize = new JComboBox(arrcategorize);
		bcategorize.setBounds(23, 20, 50, 15);
		cbcategorize.setBounds(90, 10, 100, 30);
		panel.add(bcategorize);
		panel.add(cbcategorize);

		// 작성자 닉네임 설정
		JLabel bname = new JLabel("닉네임 ");
		tfname = new JTextField(20);
		bname.setBounds(23, 60, 50, 15);
		tfname.setBounds(90, 50, 260, 30);
		panel.add(bname);
		panel.add(tfname);
		tfname.setColumns(10);

		// 비밀번호
		JLabel bPwd = new JLabel("비밀번호");
		pfPwd = new JPasswordField(20);
		bPwd.setBounds(23, 110, 70, 15);
		pfPwd.setBounds(90, 100, 260, 30);
		panel.add(bPwd);
		panel.add(pfPwd);
		pfPwd.setColumns(10);

		// 제목
		JLabel btitle = new JLabel("제목");
		tftitle = new JTextField(20);
		btitle.setBounds(23, 160, 50, 15);
		tftitle.setBounds(90, 150, 260, 30);
		panel.add(btitle);
		panel.add(tftitle);
		tftitle.setColumns(10);

		// 내용
		JLabel tcontents = new JLabel("내용");
		tacontents = new JTextArea(5, 40); // 행, 열 설정
		tacontents.setRows(1);
		tacontents.setColumns(10);
		tacontents.setLineWrap(true);
		tacontents.setWrapStyleWord(true);
		tcontents.setBounds(23, 210, 50, 15);
		tacontents.setBounds(90, 200, 260, 289);
		panel.add(tcontents);
		panel.add(tacontents);

		// textarea에 스크롤 붙이기
		JScrollPane spcontents = new JScrollPane(tacontents);
		spcontents.setBounds(90, 200, 260, 289);
		panel.add(spcontents);

		// 버튼
		btnInsert = new JButton("올리기");
		panel.add(btnInsert);
		btnInsert.setBounds(111, 500, 76, 23);
		btnInsert.addActionListener(this);

		btncancel_1 = new JButton("취소");
		panel.add(btncancel_1);
		btncancel_1.setBounds(200, 500, 76, 23);
		btncancel_1.addActionListener(this);

		btncancel_2 = new JButton("취소");
		panel.add(btncancel_2);
		btncancel_2.setBounds(250, 500, 86, 23);
		btncancel_2.addActionListener(this);

		btnUpdate = new JButton("수정하기");
		panel.add(btnUpdate);
		btnUpdate.setBounds(45, 500, 86, 23);
		btnUpdate.addActionListener(this);

		btnDelete = new JButton("삭제하기");
		panel.add(btnDelete);
		btnDelete.setBounds(146, 500, 86, 23);
		btnDelete.addActionListener(this);

		add(panel);
		setSize(392, 600);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);// 현재 창만 닫게 함.

	}
	/**
	 * 액션 리스너 오버라이드 -> 각 버튼이 눌렀을 떄 실행될 코드 작성
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnInsert) {
			insertboard();
			System.out.println("insertboard() 호출 종료");
		} else if (ae.getSource() == btncancel_1 || ae.getSource() == btncancel_2) {
			this.dispose();// 창닫기
		} else if (ae.getSource() == btnUpdate) {
			Updateboard();
		} else if (ae.getSource() == btnDelete) {
			int x = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION);

			if (x == JOptionPane.OK_OPTION) {
				deleteboard();
			} else {
				JOptionPane.showMessageDialog(this, "삭제를 취소하였습니다.");
			}
		}

		tboard.jTableRefresh(); // jTable 내용 갱신 메소드
	}

	// 게시글 삭제 메소드
	private void deleteboard() {
		String name = tfname.getText();
		String pwd = pfPwd.getText();

		if (pwd.length() == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호를 꼭 입력하세요.");
			return;
		}

		BoardDAO dao = new BoardDAO();
		boolean ok = dao.deleteboard(name, pwd);

		if (ok) {
			JOptionPane.showMessageDialog(this, "삭제를 완료하였습니다.");
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "삭제를 실패했습니다.\n비밀번호를 확인하세요.");
		}
	}

	// 게시글 수정 메소드
	private void Updateboard() {
		BoardDTO dto = getViewData();// 화면의 정보를 얻어옴
		BoardDAO dao = new BoardDAO();// 그 정보로 데이터 베이스 수정
		boolean ok = dao.updateboard(dto);

		String pwd = pfPwd.getText();

		if (pwd.length() == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요.");
			return;
		} else {
			if (ok) {
				JOptionPane.showMessageDialog(this, "수정되었습니다.");
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "[수정 실패 : 비밀번호를 확인하세요.]");
			}
		}
	}

	// 게시글 업로드 메소드
	private void insertboard() {
		BoardDTO dto = getViewData();// 사용자가 입력한 값을 얻어옴
		BoardDAO dao = new BoardDAO();
		boolean ok = dao.insertboard(dto);

		if (ok) {
			JOptionPane.showMessageDialog(this, "게시글 작성이 완료되었습니다.");
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "게시글이 정상적으로 업로드 되지 않았습니다.");
		}
	}

	// 사용자가 입력한 값을 얻어오는 메소드
	public BoardDTO getViewData() {

		BoardDTO dto = new BoardDTO();
		String name = tfname.getText();
		String pwd = pfPwd.getText();
		String title = tftitle.getText();
		String contents = tacontents.getText();
		String categorize = (String) cbcategorize.getSelectedItem();

		dto.setName(name);
		dto.setPwd(pwd);
		dto.setTitle(title);
		dto.setContents(contents);
		dto.setCategorize(categorize);

		return dto;
	}

}
