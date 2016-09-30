package backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import server.Request;
import server.Response;

public class StaticFileController extends BackendController{
	private static StaticFileController controller = null;
	private static final String ROOT_PATH = new File("").getAbsolutePath();
	private static Charset charSet = Charset.forName("GBK");
	
	public static StaticFileController getInstance(){
		if(null == controller){
			controller = new StaticFileController();
		}
		return controller;
	}
	
	@Override
	public void handle(Request req, Response resp){
		String url = req.geturl();
		File target = new File(ROOT_PATH + url);
		if(!target.exists()){
			//[404] resource not found 
		}
		if(target.canRead()){
			//resp.setBody(target);
			ByteBuffer bb = ByteBuffer.allocate(1024); 
			try {
				FileChannel in = new FileInputStream(target).getChannel();
				in.read(bb);
				bb.flip();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println(charSet.decode(bb));
		}
		
	}
	
	public static void main(String[] args){
		Request req = new Request();
		req.setUrl("/test.txt");
		getInstance().handle(req, new Response());
	}
}
