package com.kkb.eureka.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.oracle.tools.packager.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexzhang
 * @date 2020/6/24 13:27
 */
public class RateLimitFilter implements Filter {

    AtomicLong CURRENT_METRIC=new AtomicLong(0L);

    ConcurrentHashMap<String,Deque> map=new ConcurrentHashMap<>();
    Deque<Long> queue=new LinkedBlockingDeque<>();

    long MAX_REQUEST=500L
            ;
    private static enum LimitType{
        COUNT,FREQUENCY,RATE_LIMIT;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        LimitType type=LimitType.COUNT;
        boolean limitTriggered=false;
        switch(type){
            case COUNT:limitTriggered=count(servletRequest);break;
            case FREQUENCY:limitTriggered=frequency(servletRequest);break;
            case RATE_LIMIT:limitTriggered=rateLimit(servletRequest);break;
        }
        if(limitTriggered){
            HttpServletResponse response= (HttpServletResponse) servletResponse;
            String ret = new String("request exceed");
            response.getOutputStream().write(ret.getBytes());
            response.flushBuffer();
            return;
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CURRENT_METRIC.decrementAndGet();
            Log.info("release count");
        }

    }

    /**
     * 令牌桶实现
     * @param servletRequest
     * @return
     */
    private boolean rateLimit(ServletRequest servletRequest) {
        RateLimiter limit=RateLimiter.create(100);
        boolean get=limit.tryAcquire();
        return get;
    }

    /**
     * 计时器实现
     * @param servletRequest
     * @return
     */
    private boolean count(ServletRequest servletRequest){
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String url=request.getRequestURI();
        if(CURRENT_METRIC.get()>MAX_REQUEST){
            return false;
        }
        long current=CURRENT_METRIC.incrementAndGet();
        Log.info("current request total is:"+current);
        return true;
    }

    /**
     * 滑动窗口
     * @param servletRequest
     * @return
     */
    protected boolean frequency(ServletRequest servletRequest){
        long now = System.currentTimeMillis();

        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String flushKey=request.getRequestURI();
        Deque<Long> flushEntityDeque=map.get(flushKey);

        if(flushEntityDeque==null){
            Deque newQueue=new LinkedBlockingDeque(100);
            flushEntityDeque=map.putIfAbsent(flushKey, new LinkedBlockingDeque(100));
            flushEntityDeque=flushEntityDeque==null?newQueue:flushEntityDeque;
        }
        flushEntityDeque.addFirst(now);

        long queueSize = queue.size();

        long flushCount = 100; // 窗口大小次数
        long flushInterval = 3 * 1000; // 时间周期

        long firstValidTime = 0L;
        if (queueSize > flushCount) {
            while (queueSize > flushCount) {
                firstValidTime = queue.pollLast();
                queueSize = queue.size();
            }

            if (now - firstValidTime <= flushInterval) {
                Log.info("flush denied!");
                return true;
            }
        }
        return false;
    }
}
