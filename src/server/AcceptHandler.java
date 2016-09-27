package server;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
/**
 * 接收请求，建立连接的处理器
 * @author Cuber_Q
 *
 */
public class AcceptHandler implements Handler{

	@Override
	public void handle(SelectionKey key) {
		if(key.isAcceptable()){
			ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
			try {
				SocketChannel sc = ssc.accept();
				if(null == sc) return;
				sc.configureBlocking(false);
				//连接建立起后，设置处理请求处理器
				RequestHandler rh = new RequestHandler(new ChannelIo(sc, false));
				sc.register(key.selector(), SelectionKey.OP_READ, rh);
				System.out.println("accept...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
