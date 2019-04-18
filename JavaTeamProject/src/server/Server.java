package server;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.swing.ExhibiSecondFrame;

import java.text.SimpleDateFormat;

import server.api_data.ApiExhibitInfo;
import server.api_data.ApiFestivalInfo;
import server.api_data.ApiMusicDancingInfo;
import server.api_data.ApiMusicalInfo;
import server.api_data.ApiminbakInfo;

/**
 * @author 박성호
 * @brief 멀티쓰레드를 적용한 Server 쓰레드를 하드웨어의 CPU idle 갯수만큼 생성하고, 요청시 명령하여 처리한다.
 * @version Server ver3.0 final
 */
public class Server {
	int pageNo = 1;
	ExecutorService executorService;
	ServerSocketChannel serverSocketChannel;
	List<Client> connections = new LinkedList<Client>();
	public String str1;
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	class Client {// 쓰레드 서버에서 요청받을 시 클라이언트를 내보냄.
		SocketChannel socketChannel;
		int i = 0;

		Client(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
			receive();
		}

		void receive() { // receive는 서버를 켜면 하루종일 돌아간다.
			/*
			 * new Runnable() public void run() : 람다식은 다음 부분을 생략한다.
			 */
			Runnable runnable = () -> {
				while (!Thread.interrupted()) {
					try {
						ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
						int readByteCount = socketChannel.read(byteBuffer);
						if (readByteCount == -1) {
							throw new IOException();
						}
						String message = "[ 요청 처리" + socketChannel.getRemoteAddress() + " : "
								+ Thread.currentThread().getName() + "]";
						logger.log(Level.FINEST, message); // syosut대신 사용
						byteBuffer.flip();
						Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
						String data = charset.decode(byteBuffer).toString();
						logger.log(Level.FINEST, data);
						if (data.contains("[서버 종료]")) { // 데이터가 서버 종료라는 단어를 담고있다면
							stopServer(); // stopServer를 실행한
							break;
						}
						// 다음페이지가 들어오면 pageNo를 증가시킨다.
						if (data.contains("다음페이지")) { // 데이터가 "검색 실행"이라는 단어를 담고있다면.
							pageNo++;

						}
						// 이전페이지가 들어오면 pageNo를 감소시킨다.
						if (data.contains("이전페이지")) {
							pageNo--;
							if (pageNo < 1) {
								pageNo = 1;
							}
						} // NUM OF ROWS가 6개니까 TOTAL211을 6으로 나누면 35.
						if (data.contains("민박")) { // 데이터가 "검색 실행"이라는 단어를 담고있다면.
							if (pageNo > 35)
								pageNo = 35;
							ApiminbakInfo ap = new ApiminbakInfo(pageNo); // pageNo를 대입
							Client.this.send(ap.getResult());
						}

						if (data.contains("검색 실행")) { // 데이터가 "검색 실행"이라는 단어를 담고있다면.

							ApiminbakInfo ap1 = new ApiminbakInfo(1); // pageNo=1
							Client.this.send(ap1.getResult()); // 파싱된 내용을 클라이언트로 넘기는 중이다.
							// 검색실행이라는 단어를 찾으면 ApiminbakInfo의 첫페이지를 불러온다.
						}
						if (data.contains("date")) {

							String[] parts = data.split("date");
							ApiFestivalInfo ap2 = new ApiFestivalInfo(parts[1]); // pageNo=1
							Client.this.send(ap2.getResult());
							System.out.println("date넘기기 성공");
						}
						// 전시 파싱
						if (data.contains("전시")) {
							String[] parts = data.split("전시");
							ApiExhibitInfo Ae = new ApiExhibitInfo(parts[1]); // pageNo=1
							Client.this.send(Ae.getResult());
							System.out.println("전시 넘기기 성공");
						}
						// 뮤지컬 파싱
						if (data.contains("뮤지컬")) {
							String[] parts = data.split("뮤지컬");
							ApiMusicalInfo Am = new ApiMusicalInfo(parts[1]); // pageNo=1
							Client.this.send(Am.getResult());
							System.out.println("뮤지컬 넘기기 성공");
						}
						if (data.contains("무용음악")) {
							String[] parts = data.split("무용음악");
							ApiMusicDancingInfo Ad = new ApiMusicDancingInfo(parts[1]); // pageNo=1
							Client.this.send(Ad.getResult());
							System.out.println("무용음악 넘기기 성공");
						}

					} catch (IOException e) {
						try {
							if (socketChannel.isConnected())
								socketChannel.close();
							logger.warning("[클라이언트와 통신이 안 됨 : ]" + socketChannel.getRemoteAddress() + ": "
									+ Thread.currentThread().getName() + "]");
						} catch (IOException e1) {
							Thread.currentThread().interrupt();
							connections.remove(Client.this);
							logger.warning("[서버를 강제로 종료하셨습니다.]");
							break;
						} // e1 catch end
					} // e catch end
				} // while end
			}; // Runnable end
			executorService.submit(runnable);
		} // receive end

		void send(String data) {
			Runnable runnable = () -> {
				try {
					Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
					ByteBuffer byteBuffer = charset.encode(data);
					socketChannel.write(byteBuffer);
				} catch (IOException e) {
					try {
						String message = "[클라이언트 통신 안 됨 :" + socketChannel.getRemoteAddress() + " : "
								+ Thread.currentThread().getName() + "]";
						logger.warning(message);
						connections.remove(Client.this);
						if (socketChannel.isConnected())
							socketChannel.close();
					} catch (IOException e1) {
						logger.warning("[IOException 발생]");
					} // e1 end
				} // e end
			}; // Runnable end
			executorService.submit(runnable);
		} // send end
	} // Client end

	void startServer() {// 서버 동작.
		try {
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			// cpu코어 수에 맞게 쓰레드를 생성하여 관리한다.
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			serverSocketChannel.bind(new InetSocketAddress(5001));
			// 쓰레드는 5001번 포트만 접속하는것이 아니라, 비어있는 아무 포트나 연결하여 접속한다.
		} catch (IOException e) {
			logger.warning("[예외가 발생하여 서버를 강제 종료합니다.]");
			if (serverSocketChannel.isOpen()) {
				stopServer();
				return;
			} // if end
		} // e catch end

		Runnable runnable = () -> {
			while (true) {
				try {
					SocketChannel socketChannel = serverSocketChannel.accept();
					String message = "[연결 수락 : " + socketChannel.getRemoteAddress() + " : "
							+ Thread.currentThread().getName() + " ]";
					logger.log(Level.FINEST, message);
					Client client = new Client(socketChannel);
					connections.add(client);
					logger.log(Level.FINEST, "[ 연결 개수: {0}", connections.size() + " ]");

					if (connections.size() >= Runtime.getRuntime().availableProcessors()) {
						// 코어 수 이상 불러질일은 잘 없을테지만 예외처리를 하기위해 넣음. 서버가 종료되는 조건문
						logger.warning("[Caution! 사용자 하드웨어 코어 수 이상의 연결을 시도하셨습니다.]");
						stopServer();
						break;
					} // if end
				} // try end
				catch (IOException e) {
					logger.warning("[오류가 발생하여 강제 종료합니다.]");
					if (serverSocketChannel.isOpen()) {
						stopServer();
					} // if end
					break;
				} // catch end
			} // while end
		}; // runnable end
		executorService.submit(runnable);// 스레드풀에서 처리한다
	} // startServer end

	void stopServer() {

		try {
			Iterator<Client> iterator = connections.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socketChannel.close();
				iterator.remove();
			} // while end

			if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
				serverSocketChannel.close();
			} // if end

			if (executorService != null && executorService.isShutdown()) {
				executorService.isShutdown();
			} // if end

			logger.warning("[서버 멈춤]");
		} // try end
		catch (IOException e) {
			logger.warning("[서버가 멈추는 도중 I/OException 발생]");
		} // catch end
	} // stopServer end
} // class end
