import server.HttpServer;

public class StartUp {
	public static void main(String[] args){
		HttpServer server = new HttpServer();
		server.service();
	}
}
