package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HttpServer {
	private Selector selector;
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9000;
	
	public HttpServer(){
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			selector = Selector.open();
			ssc.bind(new InetSocketAddress(HOST, PORT));
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT,new AcceptHandler());
			System.out.println("server started...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void service(){
		try {
			while(selector.select()>0){
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()){
					SelectionKey key = keys.next();
					Handler handler = (Handler)key.attachment();
					handler.handle(key);
					keys.remove();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
