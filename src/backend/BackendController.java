package backend;

import server.Request;
import server.Response;

public abstract class BackendController {
	
	
	public abstract void handle(Request req,Response resp);
		
	
	//public abstract BackendController getInstance();
	
}
