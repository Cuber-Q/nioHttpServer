package backend;

import server.Request;
import server.Response;

public class DynamicController extends BackendController{
	private static DynamicController  controller = null;
	public static DynamicController getInstance(){
		if(null == controller){
			controller = new DynamicController();
		}
		return controller;
	}
	
	public void handle(Request req, Response resp){
		
	}
}
