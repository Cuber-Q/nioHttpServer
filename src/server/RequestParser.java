package server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import exception.BadRequestExcecption;
/**
 * 解析请求
 * byteBuffer -> request对象
 * @author Cuber_Q
 *
 */
public class RequestParser {
	private static Charset charSet = Charset.forName("GBK");
	public static String LINE_SEPARATOR = "\r\n";
	public static String SECTION_END = "\r\n\r\n";
	public static String HEADER_SEPRATOR = ":";
	
	public static Request parseRequest(ChannelIo cio) 
			throws BadRequestExcecption{
		Request request = new Request();
		
		//decode
		cio.requestBuffer.flip();
		String reqStr = decode(cio.getReadBuffer());
		System.out.println("source request = ");
		System.out.println(reqStr);
		
		//split startLine, header and body
		String[] startLine = splitStartLine(reqStr);

		//construct request obj
		request.setMethod(startLine[0]);
		request.setUrl(startLine[1]);
		request.setHttpVersion(startLine[2]);
		request.setHeaders(createHeaderMap(reqStr));
		request.setParamters(createParamtersMap(startLine[1],reqStr));
		return request;
	}
	
	private static String decode(ByteBuffer bb){
		return charSet.decode(bb).toString();
	}
	
	//从原始信息中切分出起始行
	private static String[] splitStartLine(String reqStr) 
			throws BadRequestExcecption{
		//POST /post HTTP/1.1
		checkRequest(reqStr);
		int index = reqStr.indexOf(LINE_SEPARATOR);
		String[] result = reqStr.substring(0,index).split("\\ ");
		if(result.length != 3)
			throw new BadRequestExcecption("start line invalid, reqStr="+reqStr);
		reqStr = reqStr.substring(index + 2);
		return result;
	}
	
	//封装头部信息
	private static Map<String, String> createHeaderMap(String reqStr)
			throws BadRequestExcecption{
		checkRequest(reqStr);
		String[] headerLines = getHeadersLines(reqStr);
		if(headerLines.length <= 0) return null;
		
		Map<String, String> headerMap = new HashMap<>();
		for(String line : headerLines){
			String[] header = line.split("\\"+HEADER_SEPRATOR);
			headerMap.put(header[0], header[1].trim());
		}
		return headerMap;
	}
	/**
	 *  Host: 127.0.0.1:8081
		Content-Type: application/x-www-form-urlencoded
		Origin: file://
		Connection: keep-alive
		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,**;q=0.8
		User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17
		Accept-Language: zh-cn
		Accept-Encoding: gzip, deflate
		Content-Length: 11
		
		param=lalala
	 * @param reqStr
	 * @return
	 * @throws BadRequestExcecption
	 */
	//从原始请求报文中切分出头部信息
	private static String[] getHeadersLines(String reqStr)
		throws BadRequestExcecption{
		checkRequest(reqStr);
		int headerStart = reqStr.indexOf(LINE_SEPARATOR)+2;
		if(hasRequestBody(reqStr)){
			int headerEnd = reqStr.indexOf(SECTION_END);
			String[] result = reqStr.substring(headerStart,headerEnd)
									.split("\\"+LINE_SEPARATOR);
//			reqStr = reqStr.substring(index + 4);
			return result;
		}
		
		return reqStr.substring(headerStart,reqStr.length()-2).split("\\"+LINE_SEPARATOR);
	}
	
	private static void checkRequest(String reqStr) throws BadRequestExcecption{
		if(null == reqStr 
				|| "".equals(reqStr)
				|| reqStr.indexOf("\r\n") < 0){
			throw new BadRequestExcecption("reqStr is "+reqStr);
		}
	}
	
	private static boolean hasRequestBody(String reqStr){
		if(reqStr.contains("Content-Length:")) return true;
		return false;
	}
	
	//封装请求参数
	private static Map<String, String> createParamtersMap(String url
														,String reqStr) {
		//从请求url中切分出参数
		Map<String, String> paramMap = new HashMap<>();
		int paramIndex = url.indexOf("?");
		if(paramIndex != -1){
			paramStr2Map(url,paramIndex,url.length()-1,paramMap);
		}
		//从请求消息体中切出参数
		if(hasRequestBody(reqStr)){
			int start = reqStr.indexOf(SECTION_END)+SECTION_END.length();
			paramStr2Map(reqStr,start,reqStr.length(),paramMap);
		}
		return paramMap;
	}
	
	private static void paramStr2Map(String paramStr
				,int start,int end,Map<String, String> paramMap){
		paramStr = paramStr.substring(start,end);
		String[] paramArr = paramStr.split("\\&");
		for(String paramPair : paramArr){
			String[] pair = paramPair.split("\\=");
			if(pair.length != 2){
				continue;
			}
			paramMap.put(pair[0].trim(),pair[1].trim());
		}
		
	}
	
}
