
// serverDlg.h: 헤더 파일
//

#pragma once


struct UserData {
	CString score;
	CString UserName;
	int socketNum;
	SOCKET h_socket;
	char ip_address[16];

};

// CserverDlg 대화 상자
class CserverDlg : public CDialogEx
{
// 생성입니다.
public:
	CserverDlg(CWnd* pParent = nullptr);	// 표준 생성자입니다.
	CFont fontBtn;
// 대화 상자 데이터입니다.
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_SERVER_DIALOG };
#endif

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV 지원입니다.


// 구현입니다.
protected:
	HICON m_hIcon;

	// 생성된 메시지 맵 함수
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	void AddEventString(const char * ap_string);
	DECLARE_MESSAGE_MAP()
private:
	CListBox m_event_list;
	SOCKET mh_listen_socket;
	UserData m_user_list[100];
protected:
	afx_msg LRESULT On25001(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT On25002(WPARAM wParam, LPARAM lParam);
public:
	afx_msg void OnDestroy();
	void SendFrameData(SOCKET ah_socket, char a_message_id, unsigned short int a_body_size, char * ap_send_data);
	afx_msg void OnBnClickedButton1();
};
