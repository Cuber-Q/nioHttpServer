package server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Request {
	//request method
	static enum Method{
		GET("GET"),
		POST("POST");
		
		private String menthodName;
		private Method(String menthodName){
			this.menthodName = menthodName;
		}
		public String getName(){
			return this.menthodName;
		}
		public static Method getInstance(String s){
			if(null == s || s == "") return null;
			for(Method method : Method.values()){
				if(s.equalsIgnoreCase(method.menthodName)){
					return method;
				}
			}
			return null;
		}
	}
	
	private Method method = null;
	private String url = null;
	private String httpVersion = null;
	private Map<String, String> requestHeaders;
	private Map<String, String> paramters;
	
	public void setMethod(String s){
		this.method = Method.getInstance(s);
	}
	public Method getMethod(){
		return this.method;
	}
	public void setUrl(String s){
		this.url = s;
	}
	public String geturl(){
		return this.url;
	}
	public void setHttpVersion(String s){
		this.httpVersion = s;
	}
	
	public void setHeader(String key, String value){
		if(null == requestHeaders){
			requestHeaders = new HashMap<>();
		}
		requestHeaders.put(key, value);
	}
	public void setHeaders(Map<String, String>map){
		if(null == requestHeaders){
			requestHeaders = new HashMap<>();
		}
		requestHeaders.putAll(map);
	}
	public String getHeader(String key){
		return requestHeaders.get(key);
	}
	public Map<String, String> getHeaders(){
		return requestHeaders;
	}
	
	public void setParamters(String key, String value){
		if(null == paramters){
			paramters = new HashMap<>();
		}
		paramters.put(key, value);
	}
	public void setParamters(Map<String, String>map){
		paramters.putAll(map);
	}
	public String getParamters(String key){
		return paramters.get(key);
	}
	public Map<String, String> getParamters(){
		return paramters;
	}
}
