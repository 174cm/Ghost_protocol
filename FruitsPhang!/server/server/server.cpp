
// server.cpp: 응용 프로그램에 대한 클래스 동작을 정의합니다.
//

#include "stdafx.h"
#include "server.h"
#include "serverDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CserverApp

BEGIN_MESSAGE_MAP(CserverApp, CWinApp)
	ON_COMMAND(ID_HELP, &CWinApp::OnHelp)
END_MESSAGE_MAP()


// CserverApp 생성

CserverApp::CserverApp()
{
	// 다시 시작 관리자 지원
	m_dwRestartManagerSupportFlags = AFX_RESTART_MANAGER_SUPPORT_RESTART;

	// TODO: 여기에 생성 코드를 추가합니다.
	// InitInstance에 모든 중요한 초기화 작업을 배치합니다.
}


// 유일한 CserverApp 개체입니다.

CserverApp theApp;


// CserverApp 초기화

BOOL CserverApp::InitInstance()
{
	CWinApp::InitInstance();
	WSADATA temp;
	WSAStartup(0x0202, &temp);
	//소켓버전 (0x0202), 윈도우 매니저에게 이 소켓을 쓰겟다고 등록
	CserverDlg dlg;
	
	m_pMainWnd = &dlg;
	dlg.DoModal();

	WSACleanup();//socket close

	return FALSE;
}

