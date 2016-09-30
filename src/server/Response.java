package server;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Response implements Sendable{
	private static String content = "<html><header></header><body><h1>ok, i got u</h1></body></html>"; 
	private ByteBuffer buffer = null;
	private static Charset charSet = Charset.forName("utf-8");
	private static String CRLF = "\r\n";
	private int code;
	
	static enum CODE{
		OK(200),
		NOT_FOUND(404);
		private int code;
		private CODE(int code){
			this.code = code;
		}
		public int getCode(){
			return this.code;
		}
	}
	public Response(){
		
	}
	
	public ByteBuffer getBuffer(){
		return this.buffer;
	}
	
	public boolean prepared(){
		buffer = setHeaders();
		buffer.put(charSet.encode(content));
		buffer.flip();
		return true;
	}
	
	private ByteBuffer setHeaders(){
		CharBuffer chBuffer = CharBuffer.allocate(1024);
		chBuffer.put("HTTP/1.1 ").put("200").put(CRLF)
				.put("Server: nioHttpServer").put(CRLF)
				.put("Content-type: text").put(CRLF)
				.put("Content-length:").put(content.length()+"").put(CRLF)
				.put(CRLF);
		chBuffer.flip();
		return charSet.encode(chBuffer);
	}
	
	@Override
	public void send() {
		// TODO Auto-generated method stub
		
	}

}
