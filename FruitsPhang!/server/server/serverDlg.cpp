
// serverDlg.cpp: 구현 파일
//

#include "stdafx.h"
#include "server.h"
#include "serverDlg.h"
#include "afxdialogex.h"
#define MAX_USER_COUNT 20
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


// CserverDlg 대화 상자



CserverDlg::CserverDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_SERVER_DIALOG, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CserverDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_SERVER_LOG, m_event_list);
}

BEGIN_MESSAGE_MAP(CserverDlg, CDialogEx)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_MESSAGE(25001, &CserverDlg::On25001)
	ON_MESSAGE(25002, &CserverDlg::On25002)
	ON_WM_DESTROY()
	ON_BN_CLICKED(IDC_BUTTON1, &CserverDlg::OnBnClickedButton1)
END_MESSAGE_MAP()


// CserverDlg 메시지 처리기

BOOL CserverDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	
	//SetIcon(m_hIcon, TRUE);
	SetIcon(m_hIcon, FALSE);

	mh_listen_socket = socket(AF_INET, SOCK_STREAM, 0);
	//AF_INET : 인터넷 기본 TCP 주소, 0은 2번째 인자 버전과 자동으로 프로토콜을 연결해줌1
	sockaddr_in srv_addr;
	srv_addr.sin_family = AF_INET;
	srv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	srv_addr.sin_port = htons(55555);

	fontBtn.CreatePointFont(100, _T("Hack"));
	GetDlgItem(IDC_SERVER_LOG)->SetFont(&fontBtn);
	bind(mh_listen_socket, (LPSOCKADDR)&srv_addr, sizeof(srv_addr));
	AddEventString("Server Open.");
	listen(mh_listen_socket, 1);
	WSAAsyncSelect(mh_listen_socket, m_hWnd, 25001, FD_ACCEPT);//비동기


	for (int i = 0; i < MAX_USER_COUNT; i++) {//비어있는 소켓을 찾는다
		m_user_list[i].h_socket = INVALID_SOCKET;
	}


	return TRUE;  // 포커스를 컨트롤에 설정하지 않으면 TRUE를 반환합니다.
}

void CserverDlg::OnSysCommand(UINT nID, LPARAM lParam)
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

void CserverDlg::OnPaint()
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
		CDialogEx::OnPaint();
	}
}

// 사용자가 최소화된 창을 끄는 동안에 커서가 표시되도록 시스템에서
//  이 함수를 호출합니다.
HCURSOR CserverDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CserverDlg::AddEventString(const char *ap_string) {
	//리스트박스에 문자 출력 및 커서 자동이동하는 함수
	while (m_event_list.GetCount() > 3000) {
		m_event_list.DeleteString(0);
	}
	int index = m_event_list.InsertString(-1, ap_string);
	m_event_list.SetCurSel(index);
	}

afx_msg LRESULT CserverDlg::On25001(WPARAM wParam, LPARAM lParam)
{//wParam -> 이벤트가 발생한 소켓의 핸들이 넘어옴, lParam에 소켓에 발생한 메세지가 옴FD_ACCEPT 
	sockaddr_in client_addr;
	int sockaddr_in_size = sizeof(sockaddr_in);
	SOCKET h_socket = accept(mh_listen_socket, (LPSOCKADDR)&client_addr, &sockaddr_in_size);
	//소켓 자리에 wParam을 적어도 됨
	//2번 째 인자는 접속하는 client의 ip를 알 수 있게하는 역할(발신자 표시)
	//h_socket은 클론 소켓으로, listen 소켓을 복제함. listen 소켓은 사용되지않고 accept 소켓만 사용됨

	int i=0;//for문 밖에서도 i를 사용하기 위해서
	int count = 0;
	CString str;
	for (i = 0; i < MAX_USER_COUNT; i++) {//비어있는 소켓을 찾는다
	
		str.Format("client is connecting..");
		AddEventString(str);
		str.Format("is [%d] socket empty?",i);
		AddEventString(str);

		m_user_list[i].socketNum = count;
		count++;

		if (m_user_list[i].h_socket == INVALID_SOCKET) {
			str.Format("[%d] socket is available.",i);
			AddEventString(str);
			break;
		}
		str.Format("nope, search next socket..");
		AddEventString(str);
	}
	
	if (i < MAX_USER_COUNT) {//빈 방이 있는 상태
		m_user_list[i].h_socket = h_socket;//해당 소켓을 저장
		
		strcpy(m_user_list[i].ip_address, inet_ntoa(client_addr.sin_addr));
		//client address 복사
		WSAAsyncSelect(m_user_list[i].h_socket, m_hWnd, 25002, FD_READ | FD_CLOSE);
		//FD_WRITE는 서버에서 데이터 보낼떄 이 메세지에는 비동기를 걸지 않음-> 보내는 시점을 서버가 알기 때문
		//FD_READ 상대편이 데이터를 보낼 때 발생되는 메세지
		//FD_CLOSE 상대편이 끊을 때
		// | 비트 연산자를 사용해야함, or를 하면 오버라이트 되서 마지막에 쓰인 메세지만 받게됨
		str.Format("[%d] socket, Client is connected. ip : %s", m_user_list[i].socketNum, m_user_list[i].ip_address);
		AddEventString(str);
		str.Format("ip : %s", m_user_list[i].ip_address);
		AddEventString(str);

	}
	else {//꽉 찬 상태
		AddEventString("There's no available socket!");
		closesocket(h_socket);//accept용 소켓 클로즈->필요가 없으므로
		//끊어진 client 다시 접속 시도, -> 끊고,, 악순환 반복 따라서 리슨 소켓을 닫으면 아예 못들어옴.
	}
	return 0;
}


afx_msg LRESULT CserverDlg::On25002(WPARAM wParam, LPARAM lParam)
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
						if (retry > 5)break;
					}
				}
			}
			if (message_id == 1) {
				int i;
				for (i = 0; i < MAX_USER_COUNT; i++) {//어느 소켓에서 보냈는지 찾는과정

					if (m_user_list[i].h_socket == wParam)break;
				}
				//i번째 소켓에서 보낸 것을 확인함

				AfxExtractSubString(m_user_list[i].UserName, p_body_data, 0, '/');
				AfxExtractSubString(m_user_list[i].score, p_body_data, 1, '/');

				CString str2;//i번째 소켓의 주소와 받은 데이터를 str2에 연결
				str2.Format("%s : %s", m_user_list[i].UserName, m_user_list[i].score);
				AddEventString(str2);//서버 리스트 박스에 출력

				//접속한 모든 클라이언트에서 보냄(broad casting)
				for (i = 0; i < MAX_USER_COUNT; i++) {
					if (m_user_list[i].h_socket != INVALID_SOCKET) {//유효하지않은 소켓이 아니라면
						SendFrameData(m_user_list[i].h_socket, 1, str2.GetLength() + 1, (char *)(const char*)str2);
						//바디 사이즈는 NULL공간을 포함해야하므로 +1
						//CString의 char*캐스팅은 (char *)(const char *)로 가능
						//여기서 i는 위의 i의 관계없음
					}
				}
			}
			if (p_body_data != NULL)delete[] p_body_data;
			WSAAsyncSelect(wParam, m_hWnd, 25002, FD_READ | FD_CLOSE);
		}

	}
	else {//FD_CLOSE
		closesocket(wParam);
		for (int i = 0; i < MAX_USER_COUNT; i++) {
			if (m_user_list[i].h_socket == wParam) {
				m_user_list[i].h_socket = INVALID_SOCKET;
				str.Format("Client disconnected : %s", m_user_list[i].ip_address);
				AddEventString(str);
				break;
			}
		}
	}
	return 0;
}

void CserverDlg::OnDestroy()
{
	CDialogEx::OnDestroy();

	// TODO: 여기에 메시지 처리기 코드를 추가합니다.
	for (int i = 0; i < MAX_USER_COUNT; i++) {
		if (m_user_list[i].h_socket != INVALID_SOCKET) {
			closesocket(m_user_list[i].h_socket);
		}

	}
}

void CserverDlg::SendFrameData(SOCKET ah_socket, char a_message_id,
	unsigned short int a_body_size, char* ap_send_data)//헤더와 데이터의 content를 보내는 함수
{
	char *p_send_data = new char[4 + a_body_size];//헤더 4 byte
	*p_send_data = 27;//key의 고유값을 27로 통일
	*(p_send_data + 1) = a_message_id;//message_id
	*(unsigned short *)(p_send_data + 2) = a_body_size;
	//일시적으로 char*를 short*에 맞게 변위를 조절

	memcpy(p_send_data + 4, ap_send_data, a_body_size);//데이터를 복사
	send(ah_socket, p_send_data, a_body_size + 4, 0);// 클라이언트에게 전송
						//send(동기화함수->다 진행될때까지 벗어나지 못함)
	delete[] p_send_data;//전송후 삭제
}


void CserverDlg::OnBnClickedButton1()
{
	CString str2;//i번째 소켓의 주소와 받은 데이터를 str2에 연결함
	CString str1;
	CString winner;
	CString looser;
	CString draw;
	CString unknown;
	int rank[2] = { 0, };
	for (int i = 0; i < MAX_USER_COUNT; i++) {
		str1.Format("%s : %s", m_user_list[0].UserName, m_user_list[0].score);
		str2.Format("%s : %s", m_user_list[1].UserName, m_user_list[1].score);
		
		rank[0] = _ttoi(m_user_list[0].score);
		rank[1] = _ttoi(m_user_list[1].score);
		
		if (m_user_list[i].h_socket != INVALID_SOCKET) {//유효하지않은 소켓이 아니라면
			unknown.Format(_T("*******Ranking*******"));
			SendFrameData(m_user_list[i].h_socket, 1, unknown.GetLength() + 1, (char *)(const char*)unknown);

			if (rank[0] > rank[1]) {
				winner.Format(_T("winner : %s congraduation!"), m_user_list[0].UserName);
				looser.Format(_T("looser : %s cheer up!"), m_user_list[1].UserName);
				SendFrameData(m_user_list[i].h_socket, 1, str1.GetLength() + 1, (char *)(const char*)str1);
				SendFrameData(m_user_list[i].h_socket, 1, str2.GetLength() + 1, (char *)(const char*)str2);
				SendFrameData(m_user_list[i].h_socket, 1, winner.GetLength() + 1, (char *)(const char*)winner);
				SendFrameData(m_user_list[i].h_socket, 1, looser.GetLength() + 1, (char *)(const char*)looser);

			}
			else if (rank[0] == rank[1]) {
				draw.Format(_T("Draw!"));
				SendFrameData(m_user_list[i].h_socket, 1, str1.GetLength() + 1, (char *)(const char*)str1);
				SendFrameData(m_user_list[i].h_socket, 1, str2.GetLength() + 1, (char *)(const char*)str2);
				SendFrameData(m_user_list[i].h_socket, 1, draw.GetLength() + 1, (char *)(const char*)draw);
			
			}
			else {
				winner.Format(_T("winner : %s congraduation!"), m_user_list[1].UserName);
				looser.Format(_T("looser : %s cheer up!"), m_user_list[0].UserName);
				SendFrameData(m_user_list[i].h_socket, 1, str2.GetLength() + 1, (char *)(const char*)str2);
				SendFrameData(m_user_list[i].h_socket, 1, str1.GetLength() + 1, (char *)(const char*)str1);
				SendFrameData(m_user_list[i].h_socket, 1, winner.GetLength() + 1, (char *)(const char*)winner);
				SendFrameData(m_user_list[i].h_socket, 1, looser.GetLength() + 1, (char *)(const char*)looser);

			}
		}
	}
	AddEventString(str1); 
	AddEventString(str2);


	// TODO: 여기에 컨트롤 알림 처리기 코드를 추가합니다.
}
