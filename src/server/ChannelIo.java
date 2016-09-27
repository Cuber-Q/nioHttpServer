package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 封装一个可自动增长的缓冲区的SocketChannel
 * @author Cuber_Q
 *
 */
public class ChannelIo {
	protected SocketChannel sc;
	protected ByteBuffer requestBuffer;
	protected static final int REQUEST_BUFFER_SIZE = 4096;
	
	public ChannelIo(SocketChannel sc,boolean blocking) throws IOException{
		this.sc = sc;
		sc.configureBlocking(blocking);
		requestBuffer = ByteBuffer.allocate(REQUEST_BUFFER_SIZE);
	}
	
	public SocketChannel getSocketChannel(){
		return sc;
	}
	
	//当剩余空间不足5%时，自动扩充为原来的一倍
	public void resizeRequestBuffer(int remaining){
		if(remaining <= requestBuffer.capacity() / 20){
			ByteBuffer newByteBuffer = ByteBuffer.allocate(requestBuffer.capacity()*2);
			requestBuffer.flip();
			newByteBuffer.put(requestBuffer);
			requestBuffer = newByteBuffer;
		}
	}
	
	public int read() throws IOException{
		resizeRequestBuffer(requestBuffer.remaining());
		return sc.read(requestBuffer);
	}
	
	public ByteBuffer getReadBuffer(){
		return requestBuffer;
	}
	
	public int write(ByteBuffer bb) throws IOException{
		return sc.write(bb);
	}
	public void close() throws IOException{
		sc.close();
	}
}

