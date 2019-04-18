package client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * 
 * @author 박성호
 * @brief 서버 연동되는 클라이언트 
 */
public class ClientMain {
	private SocketChannel socketChannel;
	private static final Logger logger = Logger.getLogger(ClientMain.class.getName());

	public String getMessageReturn() {
		return messageReturn;
	}

	String messageReturn;

	public ClientMain() {
		socketChannel = null;

		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(true);
			logger.info("[서버와 연결 요청]");
			socketChannel.connect(new InetSocketAddress("localhost", Integer.parseInt("5001")));
			logger.info("[서버와 연결 성공]");// LOCALHOST에서 연결
		} // try end
		catch (ConnectException e) {
			// 연결 실패시
			logger.warning("[연결실패] 서버와의 연결을 확인해 주세요.");
		} // catch end
		catch (IOException e) {
			// 여러가지 읽기 쓰기 실패
			logger.warning("[여러가지 읽기 쓰기 실패]");
		} // catch end

	} // ClientMain end

	public void run(String input) {
		logger.info("run");
		ByteBuffer byteBuffer = null;

		Charset charset = StandardCharsets.UTF_8; // Charset.forName("UTF-8);에서 변경
		byteBuffer = charset.encode(input);

		try {
			socketChannel.write(byteBuffer); // 서버에게 클라이언트가 보내는 부분.
		} catch (IOException e) {
			logger.warning("[서버에게 보내기 실패]");
		}

		logger.info("[데이터 보내기 성공");
		byteBuffer = ByteBuffer.allocateDirect(2000);

		try {
			socketChannel.read(byteBuffer);
		} catch (IOException e) {
			logger.warning("[서버에서 클라이언트에게 보낸내용 읽기 실패]");
		}

		byteBuffer.flip();
		String message = charset.decode(byteBuffer).toString();
		messageReturn = message;

	}

	public void send(String data) {
		Thread thread = new Thread(() -> {
			try {
				Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
				ByteBuffer byteBuffer = charset.encode(data);
				socketChannel.write(byteBuffer);
				logger.log(Level.FINEST, "데이터 보내기 완료: {0}", data);

			} catch (IOException e) {
				logger.warning("[서버와 통신 불가].");

			}
		});
		thread.start();
	}
}
