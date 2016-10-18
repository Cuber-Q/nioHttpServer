package server.pool;

import server.Handler;

/**
 * 处理器池接口，用于处理器复用
 * 可用于请求处理器（RequestHandler），连接处理器(AcceptHandler)
 * @author Cuber_Q
 *
 */
public interface HandlerPool {
	
	//借出一个处理器
	public Handler borrowOne();
	
	//归还处理器到池中
	public Handler returnToPool();
	
	
}
