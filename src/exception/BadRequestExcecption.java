package exception;

public class BadRequestExcecption extends  Exception{
	private static final long serialVersionUID = 574308401L;
	
	public BadRequestExcecption(String s){
		super(s);
	}
}
