package server.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import server.Handler;
import server.RequestHandler;

/**
 * 请求处理器池
 * @author Cuber_Q
 *
 */
public class RequestHandlerPool implements HandlerPool{
	private List<RequestHandler> handlers = null;
	private final static int DEFAULT_POOL_SIZE = 10;	//默认数量
	private static int max_pool_size = 30;				//超过最大数量，进入等待队列
	private AtomicInteger index = new AtomicInteger(0);
	
	public RequestHandlerPool(){
		this(DEFAULT_POOL_SIZE, max_pool_size);
	}
	
	public RequestHandlerPool(int max){
		this(DEFAULT_POOL_SIZE, max);
	}
	
	public RequestHandlerPool(int size,int max){
		max_pool_size = max;
		handlers = new ArrayList<RequestHandler>(size);
		initDefaultHandler();
		System.out.println(Thread.currentThread().getName()+"--init RequestHandlerPool success,size="
				+size+", max="+max);
	}
	
	private void initDefaultHandler(){
		for (int i = 0; i < handlers.size(); i++) {
			handlers.add(new RequestHandler());
			index.incrementAndGet();
		}
		
	}
	
	@Override
	public Handler borrowOne() {
		// TODO Auto-generated method stub
		index.getAndIncrement();
		index.compareAndSet(max_pool_size, 0);
		return null;
	}

	@Override
	public Handler returnToPool() {
		// TODO Auto-generated method stub
		return null;
	}
}
