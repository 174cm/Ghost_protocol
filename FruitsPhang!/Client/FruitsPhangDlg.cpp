
// FruitsPhangDlg.cpp: 구현 파일
//

#include "stdafx.h"
#include "FruitsPhang.h"
#include "FruitsPhangDlg.h"
#include "afxdialogex.h"

#define  _WINSOCK_DEPERCATED_NO_WARNNINGS 
#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// 응용 프로그램 정보에 사용되는 CAboutDlg 대화 상자입니다.

class CAboutDlg : public CDialogEx
{
public:
	CAboutDlg();

// 대화 상자 데이터입니다.
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_ABOUTBOX };
#endif

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 지원입니다.

// 구현입니다.
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialogEx(IDD_ABOUTBOX)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialogEx)
END_MESSAGE_MAP()


// CFruitsPhangDlg 대화 상자



CFruitsPhangDlg::CFruitsPhangDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_FRUITSPHANG_DIALOG, pParent)
	, m_ctlScore(0)
	, m_ctlNickname(_T(""))
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CFruitsPhangDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_CHAT_LIST, m_chat_list);
	DDX_Control(pDX, IDC_PROGRESS1, m_TimerBar);
	DDX_Text(pDX, IDC_SCORE, m_ctlScore);
	DDX_Control(pDX, IDC_START, m_StartButton);
	DDX_Text(pDX, IDC_NICKNAME, m_ctlNickname);
}

BEGIN_MESSAGE_MAP(CFruitsPhangDlg, CDialogEx)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//ON_BN_CLICKED(IDC_SEND_BTN, &CFruitsPhangDlg::OnBnClickedSendBtn)
	ON_MESSAGE(25001, &CFruitsPhangDlg::On25001)
	ON_MESSAGE(25002, &CFruitsPhangDlg::On25002)
	ON_WM_DESTROY()

	//ON_BN_CLICKED(IDC_START, &CFruitsPhangDlg::OnClickedStart)
	ON_BN_CLICKED(IDC_START, &CFruitsPhangDlg::OnBnClickedStart)
	ON_WM_LBUTTONDOWN()
END_MESSAGE_MAP()


// CFruitsPhangDlg 메시지 처리기

BOOL CFruitsPhangDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// 시스템 메뉴에 "정보..." 메뉴 항목을 추가합니다.

	// IDM_ABOUTBOX는 시스템 명령 범위에 있어야 합니다.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != nullptr)
	{
		BOOL bNameValid;
		CString strAboutMenu;
		bNameValid = strAboutMenu.LoadString(IDS_ABOUTBOX);
		ASSERT(bNameValid);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}
	SetIcon(m_hIcon, TRUE);            // 큰 아이콘을 설정합니다.
	SetIcon(m_hIcon, FALSE);        // 작은 아이콘을 설정합니다.
	mh_socket = socket(AF_INET, SOCK_STREAM, 0);//소켓 생성
	struct sockaddr_in srv_addr;
	memset(&srv_addr, 0, sizeof(struct sockaddr_in));
	srv_addr.sin_family = AF_INET;
	srv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	srv_addr.sin_port = htons(55555);
	WSAAsyncSelect(mh_socket, m_hWnd, 25001, FD_CONNECT);
	m_connect_flag = 1;//접속중 상태를 나타내는 플래그값

	//AddEventString("서버에 접속을 시도합니다...");

	connect(mh_socket, (LPSOCKADDR)&srv_addr, sizeof(srv_addr));
	//서버가 실행되고(listen) 있어야됨, 커넥트 함수가 실패하면 최대 28초 동안 응답없음 상태에 빠짐

	SetUpBitmap();//비트맵 지정
	SetUpObjPosition();//과일 생성 포인트 지정


	m_TimerBar.SetRange(0, 10000);
	SetUpGridObj();
	StartChecker = FALSE;
	m_timerWorker = NULL;
	m_ctlScore = 0;

	//버튼 이미지 삽입
	m_StartButton.LoadBitmaps(IDB_STARTBUTT, IDB_STARTPRESS, NULL, NULL);
	m_StartButton.SizeToContent();

	PhangFlag = 0;
	ShuffleFlag = 0;
	m_controlFont.CreatePointFont(200, "굴림");
	GetDlgItem(IDC_SCORE)->SetFont(&m_controlFont);
	GetDlgItem(IDC_NICKNAME)->SetFont(&m_controlFont);
	PlaySoundW((LPCWSTR)IDR_BGM, NULL, SND_ASYNC | SND_RESOURCE);
	return TRUE;  // 포커스를 컨트롤에 설정하지 않으면 TRUE를 반환합니다.
}

void CFruitsPhangDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialogEx::OnSysCommand(nID, lParam);
	}
}

// 대화 상자에 최소화 단추를 추가할 경우 아이콘을 그리려면
//  아래 코드가 필요합니다.  문서/뷰 모델을 사용하는 MFC 응용 프로그램의 경우에는
//  프레임워크에서 이 작업을 자동으로 수행합니다.

void CFruitsPhangDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // 그리기를 위한 디바이스 컨텍스트입니다.

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 클라이언트 사각형에서 아이콘을 가운데에 맞춥니다.
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 아이콘을 그립니다.
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		
		
		if (!StartChecker) {
			LoadMOdeling();
		}
		//배경 초기화
		SetUpBG();
		//과일 생성
		ReDrawFruit();

		CDialogEx::OnPaint();
	}
}

// 사용자가 최소화된 창을 끄는 동안에 커서가 표시되도록 시스템에서
//  이 함수를 호출합니다.
HCURSOR CFruitsPhangDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CFruitsPhangDlg::AddEventString(const char * ap_string)
{
	while (m_chat_list.GetCount() > 500) {
		m_chat_list.DeleteString(0);
	}
	int index = m_chat_list.InsertString(-1, ap_string);
	m_chat_list.SetCurSel(index);

}

void CFruitsPhangDlg::SendFrameData(SOCKET ah_socket, char a_message_id, unsigned short int a_body_size, char * ap_send_data)
{
	char *p_send_data = new char[4 + a_body_size];
	*p_send_data = 27;//헤더 구성
	*(p_send_data + 1) = a_message_id;
	*(unsigned short *)(p_send_data + 2) = a_body_size;

	memcpy(p_send_data + 4, ap_send_data, a_body_size);
	send(ah_socket, p_send_data, a_body_size + 4, 0);
	delete[] p_send_data;
}

afx_msg LRESULT CFruitsPhangDlg::On25001(WPARAM wParam, LPARAM lParam)
{
	if (WSAGETSELECTERROR(lParam)) {
		m_connect_flag = 0;
		closesocket(mh_socket);
		mh_socket = INVALID_SOCKET;
		//AddEventString("서버에 접속을 실패했습니다.");
	}
	else {
		m_connect_flag = 2;
		WSAAsyncSelect(mh_socket, m_hWnd, 25002, FD_READ | FD_CLOSE);
		//AddEventString("서버에 접속했습니다.");
	}
	return 0;
}

void CFruitsPhangDlg::OnDestroy()
{
	CDialogEx::OnDestroy();
	if (mh_socket != INVALID_SOCKET) {
		closesocket(mh_socket);
		mh_socket = INVALID_SOCKET;
	}
}

afx_msg LRESULT CFruitsPhangDlg::On25002(WPARAM wParam, LPARAM lParam)
{
	CString str;
	if (WSAGETSELECTEVENT(lParam) == FD_READ) {
		WSAAsyncSelect(wParam, m_hWnd, 25002, FD_CLOSE);
		char key;
		recv(wParam, &key, 1, 0);
		if (key == 27) {
			char message_id;
			recv(wParam, &message_id, 1, 0);
			unsigned short body_size;
			recv(wParam, (char *)&body_size, 2, 0);
			char* p_body_data = NULL;
			if (body_size > 0) {
				p_body_data = new char[body_size];
				int total = 0, x, retry = 0;
				while (total < body_size) {
					x = recv(wParam, p_body_data + total, body_size - total, 0);
					if (x == SOCKET_ERROR)break;
					total = total + x;
					if (total < body_size) {
						Sleep(50);
						retry++;
						if (retry > 5)break;//재시도 5번 넘으면 break
					}
				}
			}
			if (message_id == 1) {//서버와 다른 부분-1
				AddEventString(p_body_data);
			}//여기까지-1
			if (p_body_data != NULL)delete[] p_body_data;

			WSAAsyncSelect(wParam, m_hWnd, 25002, FD_READ | FD_CLOSE);
		}
	}
	else {//FD_CLOSE
		//서버와 다른 부분-2
		closesocket(mh_socket);//클라이언트 소켓 클로즈
		mh_socket = INVALID_SOCKET;
		m_connect_flag = 0;//연결 해제 flag 변경
		AddEventString("서버가 연결을 해제 했습니다.~");
		//여기까지-2
	}
	return 0;
}

//비트맵 로드
void CFruitsPhangDlg::SetUpBitmap()
{
	m_BackGround.LoadBitmap(IDB_BG);
	Shuffle.LoadBitmap(IDB_SHUFFLE);
	m_FruitObj[0].LoadBitmap(IDB_FRUIT1);
	m_FruitObj[1].LoadBitmap(IDB_FRUIT2);

	m_FruitObj[2].LoadBitmap(IDB_FRUIT3);
	m_FruitObj[3].LoadBitmap(IDB_FRUIT4);
	m_FruitObj[4].LoadBitmap(IDB_FRUIT5);

	m_PhangObj[0].LoadBitmap(IDB_PHANG1);
	m_PhangObj[1].LoadBitmap(IDB_PHANG2);

	m_PhangObj[2].LoadBitmap(IDB_PHANG3);
	m_PhangObj[3].LoadBitmap(IDB_PHANG4);
	m_PhangObj[4].LoadBitmap(IDB_PHANG5);
}
// 배경화면 설정
void CFruitsPhangDlg::SetUpBG()
{
	CClientDC dc(this);
	CDC MemDC;
	MemDC.CreateCompatibleDC(&dc);
	CBitmap *pOldBG = MemDC.SelectObject(&m_BackGround);
	dc.BitBlt(0, 0, 797, 960, &MemDC, 0, 0, SRCCOPY);
	dc.SelectObject(pOldBG);
	CBitmap BGS;
	BGS.LoadBitmap(IDB_BGSIDE);
	pOldBG = MemDC.SelectObject(&BGS);
	dc.TransparentBlt(795, 0, 270, 960, &MemDC, 0, 0, 270, 960, RGB(255, 255, 255));
	dc.SelectObject(pOldBG);

	CBitmap score;
	score.LoadBitmap(IDB_SCORE);
	pOldBG = MemDC.SelectObject(&score);
	dc.TransparentBlt(850, 220, 159, 57, &MemDC, 0, 0, 159, 57, RGB(255, 255, 255));
	dc.SelectObject(pOldBG);

	CBitmap nickname;
	nickname.LoadBitmap(IDB_NICKNAME);
	pOldBG = MemDC.SelectObject(&nickname);
	dc.TransparentBlt(850, 350, 159, 57, &MemDC, 0, 0, 159, 57, RGB(255, 255, 255));
	dc.SelectObject(pOldBG);

	CBitmap ranking;
	ranking.LoadBitmap(IDB_RANKING);
	pOldBG = MemDC.SelectObject(&ranking);
	dc.TransparentBlt(850, 500, 159, 57, &MemDC, 0, 0, 159, 57, RGB(255, 255, 255));
	dc.SelectObject(pOldBG);

	CBitmap title;
	title.LoadBitmap(IDB_TITLE);
	pOldBG = MemDC.SelectObject(&title);
	dc.TransparentBlt(155, 140, 306, 169, &MemDC, 0, 0, 306, 169, RGB(255, 255, 255));
	dc.SelectObject(pOldBG);
}
//로딩화면을 모델링함
void CFruitsPhangDlg::LoadMOdeling()
{
	CClientDC dc(this);
	CDC MemDC;
	CBitmap *oldBitmap;
	MemDC.CreateCompatibleDC(&dc);
	CBitmap load[4];
	load[0].LoadBitmap(IDB_LOAD1);
	load[1].LoadBitmap(IDB_LOAD2);
	load[2].LoadBitmap(IDB_LOAD3);
	load[3].LoadBitmap(IDB_LOAD4);
	oldBitmap = MemDC.SelectObject(&load[0]);
	dc.TransparentBlt(400, 400, 199, 94, &MemDC, 0, 0, 199, 94, RGB(255, 255, 255));
	dc.SelectObject(oldBitmap);
	Sleep(700);

	oldBitmap = MemDC.SelectObject(&load[1]);
	dc.TransparentBlt(400, 400, 231, 94, &MemDC, 0, 0, 231, 94, RGB(255, 255, 255));
	dc.SelectObject(oldBitmap);
	Sleep(700);


	oldBitmap = MemDC.SelectObject(&load[2]);
	dc.TransparentBlt(400, 400, 267, 93, &MemDC, 0, 0, 267, 93, RGB(255, 255, 255));
	dc.SelectObject(oldBitmap);
	Sleep(700);

	oldBitmap = MemDC.SelectObject(&load[3]);
	dc.TransparentBlt(400, 400, 311, 93, &MemDC, 0, 0, 311, 93, RGB(255, 255, 255));
	dc.SelectObject(oldBitmap);
	Sleep(700);

	SetUpBG();

}

//과일 배치 좌표계 설정
void  CFruitsPhangDlg::SetUpObjPosition() {
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 9; j++) {
			ObjPosition[i][j].SetPoint(98 + (i * 51), 422 + (j * 53));
		}
	}
}
//과일 오브젝트 특성 초기화(존재 여부, 인접과일과 같은지 체크 여부, 체크하여 터트릴지 체크 여부, 시작,끝좌표계)
void CFruitsPhangDlg::SetUpGridObj()
{
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 9; j++) {
			GridObj[i][j].Check = FALSE;
			GridObj[i][j].PhangCheck = FALSE;
			GridObj[i][j].DeleteFruit = FALSE;
			GridObj[i][j].StartPoint.SetPoint(ObjPosition[i][j].x, ObjPosition[i][j].y);
			GridObj[i][j].EndPoint.SetPoint(ObjPosition[i][j].x + 51, ObjPosition[i][j].y + 53);
		}
	}
}
UINT CFruitsPhangDlg::StartTimer(LPVOID pParam)
{
	CFruitsPhangDlg *pDlg = (CFruitsPhangDlg *)pParam;
	if (pDlg->m_timerWorker != NULL) {
		for (int i = 0; i < 10000; i += 10) {
			pDlg->m_TimerBar.SetPos(i);
			Sleep(10);
		}
		AfxMessageBox(_T("TIME OUT!!!"));
		pDlg->StartChecker = FALSE;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				pDlg->GridObj[i][j].PhangCheck = TRUE;
			}
		}
		pDlg->Invalidate(FALSE);
		pDlg->m_timerWorker = NULL;
		CString str;
		str.Format(_T("%d"), pDlg->m_ctlScore);
		CString str2;
		pDlg->GetDlgItemText(IDC_NICKNAME, str2);

		CString temp;

		temp = str2+"/"+str;
		str = temp;
		if (pDlg->m_connect_flag == 2) {
			pDlg->SendFrameData(pDlg->mh_socket, 1, str.GetLength() + 1, (char *)(const char *)str);

			pDlg->GotoDlgCtrl(pDlg->GetDlgItem(IDC_CHAT_EDIT));
			pDlg->SetDlgItemText(IDC_CHAT_LIST, str);
			//특정 대화상자 컨트롤로 보낸다. -> 반전
		}
	}
	return 0;
}

void CFruitsPhangDlg::ReDrawFruit()
{
	if (StartChecker) {

		CClientDC dc(this);
		CDC MemDC;
		CBitmap *oldBitmap;
		MemDC.CreateCompatibleDC(&dc);
		srand(time(NULL));
		//과일이 터진 뒤 랜덤으로 재생성하여 그리는 반복문
		if (ShuffleFlag == 1) {
			PlaySoundW((LPCWSTR)IDR_PHANG, NULL, SND_ASYNC | SND_RESOURCE);
			oldBitmap = MemDC.SelectObject(&Shuffle);
			dc.TransparentBlt(ObjPosition[0][3].x + 28, ObjPosition[0][3].y + 10, 335, 112, &MemDC, 0, 0, 335, 112, RGB(255, 255, 255));
			dc.SelectObject(oldBitmap);

			Sleep(500);
			SetUpBG();
			ShuffleFlag = 0;
		}
		//터진 과일을 그리는 부분
		if (PhangFlag == 1) {
			PlaySoundW((LPCWSTR)IDR_PHANG, NULL, SND_ASYNC | SND_RESOURCE);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 9; j++) {
					if (GridObj[i][j].DeleteFruit) {
						oldBitmap = MemDC.SelectObject(&m_PhangObj[GridObj[i][j].FruitType]);
					}
					else {
						oldBitmap = MemDC.SelectObject(&m_FruitObj[GridObj[i][j].FruitType]);
					}
					dc.TransparentBlt(ObjPosition[i][j].x, ObjPosition[i][j].y, 41, 41, &MemDC, 0, 0, 41, 41, RGB(255, 255, 255));
					dc.SelectObject(oldBitmap);

				}
			}
			PhangFlag = 0;
			Sleep(300);
			SetUpBG();
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				//랜덤 과일 생성용 난수 발생
				rNum = rand() % 5;
				if (GridObj[i][j].DeleteFruit) {
					GridObj[i][j].FruitType = rNum;

				}
				GridObj[i][j].Check = TRUE;
				GridObj[i][j].DeleteFruit = FALSE;
				GridObj[i][j].PhangCheck = FALSE;
				oldBitmap = MemDC.SelectObject(&m_FruitObj[GridObj[i][j].FruitType]);
				dc.TransparentBlt(ObjPosition[i][j].x, ObjPosition[i][j].y, 41, 41, &MemDC, 0, 0, 41, 41, RGB(255, 255, 255));
				dc.SelectObject(oldBitmap);
			}
		}
	}
	ShuffleFruit();//과일을 배치한 뒤 터트릴 대상이있나 확인
}

//인접한 과일과 같은지 체크하는 함수
void CFruitsPhangDlg::CheckPhang(int i, int j)
{
	if ((-1 < i && i < 8) && (-1 < j && j < 9)) {
		GridObj[i][j].PhangCheck = TRUE;

		//기준 위치 위쪽 오브젝트 체크
		//인덱스 범위체크
		if ((-1 < i) && (-1 < j - 1) && ((i < 8) && (j - 1 < 9))) {
			//검사하지 않은 오브젝트만 체크
			if (GridObj[i][j - 1].PhangCheck == FALSE) {
				if (GridObj[i][j].FruitType == GridObj[i][j - 1].FruitType) {
					GridObj[i][j].DeleteFruit = TRUE;
					GridObj[i][j - 1].DeleteFruit = TRUE;
					AdjCount++;//인접한 과일 개수 증가
					CheckPhang(i, j - 1);
				}
			}
		}
		//기준 위치 왼쪽 오브젝트 체크
		//인덱스 범위체크
		if ((-1 < i - 1) && (-1 < j) && ((i - 1 < 8) && (j < 9))) {
			//검사하지 않은 오브젝트만 체크
			if (GridObj[i - 1][j].PhangCheck == FALSE) {
				if (GridObj[i][j].FruitType == GridObj[i - 1][j].FruitType) {
					GridObj[i][j].DeleteFruit = TRUE;
					GridObj[i - 1][j].DeleteFruit = TRUE;
					AdjCount++;//인접한 과일 개수 증가
					CheckPhang(i - 1, j);
				}
			}
		}

		//기준 위치 아랫쪽 오브젝트 체크
		//인덱스 범위체크
		if ((-1 < i) && (-1 < j + 1) && ((i < 8) && (j + 1 < 9))) {
			//검사하지 않은 오브젝트만 체크
			if (GridObj[i][j + 1].PhangCheck == FALSE) {
				if (GridObj[i][j].FruitType == GridObj[i][j + 1].FruitType) {
					GridObj[i][j].DeleteFruit = TRUE;
					GridObj[i][j + 1].DeleteFruit = TRUE;
					AdjCount++;//인접한 과일 개수 증가
					CheckPhang(i, j + 1);
				}
			}
		}
		//기준 위치 오른쪽 오브젝트 체크
		//인덱스 범위체크
		if ((-1 < i + 1) && (-1 < j) && ((i + 1 < 8) && (j < 9))) {
			//검사하지 않은 오브젝트만 체크
			if (GridObj[i + 1][j].PhangCheck == FALSE) {
				if (GridObj[i][j].FruitType == GridObj[i + 1][j].FruitType) {
					GridObj[i][j].DeleteFruit = TRUE;
					GridObj[i + 1][j].DeleteFruit = TRUE;
					AdjCount++;//인접한 과일 개수 증가
					CheckPhang(i + 1, j);
				}
			}
		}
	}
}

void CFruitsPhangDlg::ShuffleFruit()
{
	ShuffleFlag = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 9; j++) {
			AdjCount = 1;
			CheckPhang(i, j);
			//터트릴 과일이 있으면
			if (AdjCount >= 3) {
				for (int ii = 0; ii < 8; ii++) {
					for (int jj = 0; jj < 9; jj++) {
						GridObj[ii][jj].PhangCheck = FALSE;
						GridObj[ii][jj].DeleteFruit = FALSE;
					}
				}
				ShuffleFlag = 0;
				goto EXIT;//루프를 강제로 탈출하고 EXIT에 해당하는 문장 실행
			}
			//터트릴 과일이 없으면
			if (AdjCount < 3) {
				ShuffleFlag = 1;
				for (int ii = 0; ii < 8; ii++) {
					for (int jj = 0; jj < 9; jj++) {
						GridObj[ii][jj].PhangCheck = FALSE;
						GridObj[ii][jj].DeleteFruit = FALSE;
					}
				}
			}
		}
	}

EXIT:
	{

	}
	//터트릴 과일이 없어서 섞어야 하는 경우
	if (ShuffleFlag == 1) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				GridObj[i][j].DeleteFruit = TRUE;
			}
		}
		UpdateWindow();
	}
}

void CFruitsPhangDlg::OnBnClickedStart()
{
	// TODO: 여기에 컨트롤 알림 처리기 코드를 추가합니다.
	if (!StartChecker) {
		PlaySoundW((LPCWSTR)IDR_START, NULL, SND_ASYNC | SND_RESOURCE);
		UpdateData(TRUE);
		StartChecker = TRUE;
		CClientDC dc(this);
		CDC MemDC;
		CBitmap *oldBitmap;
		MemDC.CreateCompatibleDC(&dc);
		srand(time(NULL));//난수 발생 시드 초기화
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				//랜덤 과일 생성용 난수 발생
				rNum = rand() % 5;

				//과일 이미지 출력
				if (!GridObj[i][j].Check) {
					oldBitmap = MemDC.SelectObject(&m_FruitObj[rNum]);
					dc.TransparentBlt(ObjPosition[i][j].x, ObjPosition[i][j].y, 41, 41, &MemDC, 0, 0, 41, 41, RGB(255, 255, 255));
					dc.SelectObject(oldBitmap);
					GridObj[i][j].Check = TRUE;

				}
				GridObj[i][j].FruitType = rNum;


			}
		}
		//타이머 스레드 작동
		if (!m_timerWorker) {
			m_timerWorker = AfxBeginThread(StartTimer, this);
		}

		UpdateData(TRUE);
	}
}


void CFruitsPhangDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	if (StartChecker) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				//바둑판 처럼 배치된 특정 과일을 하나 눌렀을 때
				if ((((point.x > GridObj[i][j].StartPoint.x) && (point.y > GridObj[i][j].StartPoint.y)) && ((point.x < GridObj[i][j].EndPoint.x) && (point.y < GridObj[i][j].EndPoint.y)))) {
					//ShuffleFruit();
					AdjCount = 1;
					CheckPhang(i, j);

					//같은 과일이 3개 이상 인접한 부분을 눌렀을 경우 터트림
					if (AdjCount >= 3) {
						PhangFlag = 1;
						UpdateData(TRUE);
						m_ctlScore += AdjCount * AdjCount;
						UpdateData(FALSE);
						Invalidate(FALSE);
						goto EXIT;
					}

					//같은 과일이 3개 미만 이지만 2개가 인접하여 DeleteFruit를 변경시킨 경우
					//다시 플래그를 초기화 시켜주기 위함
					if (AdjCount < 3) {
						for (int k = 0; k < 8; k++) {
							for (int l = 0; l < 9; l++) {
								GridObj[k][l].DeleteFruit = FALSE;
								GridObj[k][l].PhangCheck = FALSE;
							}
						}
					}

				}
			}
		}
	}
EXIT:

	CDialogEx::OnLButtonDown(nFlags, point);
}
