package client;

import client.swing.FristFrame;
/**
 * 
 * @author 박성호
 * @brief 클라이언트를 구동시키는 클래스
 */
public class ClientRunner {

	public static void main(String[] args) {
		ClientMain client = new ClientMain();
		FristFrame frame = new FristFrame(client);
		frame.setVisible(true);
	}

}
