package backend;

import java.util.Map;

import exception.BadRequestExcecption;
import server.Request;
import server.Response;

public class BackendDispacther {
	public static void dispacth(Request req, Response resp) throws BadRequestExcecption{
		BackendController controller = decideController(req);
		if(null == controller){
			//404 response code
			throw new BadRequestExcecption("[404]");
		}
		controller.handle(req, resp);
	}
	
	private static BackendController decideController(Request req){
		String url = req.geturl();
		if(url.contains(".jpg")
				|| url.contains(".png")
				|| url.contains(".js")
				|| url.contains(".css")
				|| url.contains(".html")
				|| url.contains(".text")
				){
			return StaticFileController.getInstance();
		}
		Map<String, String> paramMap = req.getParamters();
		if(null != paramMap && paramMap.isEmpty()){
			return DynamicController.getInstance();
		}
		return null;
	}
}
