package server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;

import backend.BackendDispacther;
import exception.BadRequestExcecption;
/**
 * 负责接收http请求和发送http响应
 * @author Cuber_Q
 *
 */
public class RequestHandler implements Handler{
	private ChannelIo cio;
	private Request request = null;
	private Response response = null;
	private boolean sent=false;//是否已发送response
	
	public RequestHandler(){
		
	}
	
	public RequestHandler(ChannelIo cio) throws Exception{
		if(null == cio){
			throw new Exception("ChannelIo cannot be null");
		}
		this.cio = cio;
	}
	
	@Override
	public void handle(SelectionKey key) {
		System.out.println("[request handling]");
		if(key.isReadable()){
			//recive
			try {
				if(hasRecicved()){
					System.out.println("[prepared to parse request...]");
					parseRequest();
					
					//deliver the request to backend handler
					//todo
					BackendDispacther.dispacth(request, response);
					//prepare to build response
					buildResponse();
					//prepare to send response
					key.channel().register(key.selector(), SelectionKey.OP_WRITE,this);
				}
			} catch (BadRequestExcecption e) {
				// TODO: handle exception
				close();
				key.cancel();
			} catch (ClosedChannelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(key.isWritable()){
			//send
			send();
		}
	}
	
	private boolean hasRecicved(){
		try {
			int cnt = cio.read();
			if(cnt > 0 || cnt == -1) {
				return true;
			}
			return false;
//			return cio.read() == -1 ? true : false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void parseRequest() throws BadRequestExcecption{
		request = RequestParser.parseRequest(cio);
	}
	
	private void buildResponse(){
		response = new Response();
		response.prepared();
	}
	
	private void send(){
		try {
			if(!sent){
				cio.write(response.getBuffer());
				sent = true;
				System.out.println("[response has sent...]");
			}
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.send();
	}

	private void close(){
		try {
			cio.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
