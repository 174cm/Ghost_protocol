
// FruitsPhangDlg.h: 헤더 파일
//

#pragma once
#pragma comment(lib,"winmm.lib")
#include "mmsystem.h"

//게임판의 한칸을 나타내는 구조체
typedef struct CellType {
	CPoint StartPoint; //과일 생성 좌측 상단 좌표
	CPoint EndPoint;//과일 생성 우측 하단 좌표
	int FruitType; //과일 종류
	BOOL Check;    //과일 존재 여부
	BOOL PhangCheck;//인접 과일과 동일 비교 했는지 체크
	BOOL DeleteFruit;//인접 과일과 같으면 T로 터트리는 조건에 사용

};



// CFruitsPhangDlg 대화 상자
class CFruitsPhangDlg : public CDialogEx
{
// 생성입니다.
private:
	SOCKET mh_socket = INVALID_SOCKET;
	char m_connect_flag = 0; // disconnect : 0 / connected : 1 / connectting.. : 2
public:
	CFruitsPhangDlg(CWnd* pParent = nullptr);	// 표준 생성자입니다.

// 대화 상자 데이터입니다.
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_FRUITSPHANG_DIALOG };
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
	CListBox m_chat_list;
	
public:
	//afx_msg void OnBnClickedSendBtn();
	void SendFrameData(SOCKET ah_socket, char a_message_id, unsigned short int a_body_size, char * ap_send_data);
protected:
	afx_msg LRESULT On25001(WPARAM wParam, LPARAM lParam);
public:
	afx_msg void OnDestroy();
protected:
	afx_msg LRESULT On25002(WPARAM wParam, LPARAM lParam);
public:
	CProgressCtrl m_TimerBar;
	int m_ctlScore;
	CBitmapButton m_StartButton;
	//과일 비트맵
	CBitmap m_FruitObj[6];
	//터진 과일 비트맵
	CBitmap m_PhangObj[6];
	//배경화면 비트맵
	CBitmap m_BackGround;
	//셔플 이미지 비트맵
	CBitmap Shuffle;
	//과일 생성 포인터 배열
	CPoint ObjPosition[8][9];
	int rNum;
	//비트맵을 로드하여 객체에 저장해두는 메소드
	void SetUpBitmap();

	// 배경화면 설정 메소드
	void SetUpBG();

	//과일 생성 포인트 생성 메소드
	void SetUpObjPosition();

	//셀 특성 구조체 초기화 메소드
	void SetUpGridObj();
	//afx_msg void OnBnClickedStart();//게임 시작 처리기
	//CProgressCtrl m_TimerBar;//타이머 바 컨트롤 변수
	//타이머 스레드 관련//
	static UINT StartTimer(LPVOID pParam);//타이머 시작 메소드
	CWinThread *m_timerWorker;//타이머 관리 스레드
	CellType GridObj[8][9];//각 셀의 타입을 나타내는 구조체
	//afx_msg void OnLButtonDown(UINT nFlags, CPoint point); //과일을 마우스로 눌렀을때 이벤트 처리기
	BOOL StartChecker;//게임 시작버튼이 눌렸는지 확인하는 변수
	void ReDrawFruit();//과일을 다시 그리는 메소드
	void LoadMOdeling();//로딩 화면 구성
	void CheckPhang(int i, int j);//과일의 인접여부를 확인하는 메소드
	int AdjCount = 1; //인접한 같은 과일의 개수
	int ShuffleFlag;//셔플 이미지를 출력 여부
	int PhangFlag;//팡 이미지 출력 여부


	void ShuffleFruit();//더이상 터트릴 과일이 없을 때 다시 섞어주는 메소드
	//CBitmapButton m_StartButton;
	//점수 표시판
	//int m_ctlScore;
	
	afx_msg void OnBnClickedStart();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	CString m_ctlNickname;
	CFont m_controlFont;
};

