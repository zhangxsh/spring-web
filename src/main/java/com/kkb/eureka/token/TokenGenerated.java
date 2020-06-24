package com.kkb.eureka.token;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 扩展点：<br/>
 * 1、如果是基于本地缓存发放令牌，则需要根据整体机器的数量进行一个分发，如3000w的额度，30台机器，每台100w的额度
 * 2、如果是基于redis，基本上可以实现8-10w的qps，如果并发还不够，则类似步骤1，使用多个redis分片，将令牌进行分片横向扩展<>br</>
 *    注意做redis的高可用，或者使用一个单独的服务来封装令牌服务做到服务的高可用。
 *    当redis挂了后，配合本地缓存进行一个降级和限流。
 * 3、lua类似，lua中的逻辑要尽可能的少，完成基本的不可能在外部完成的任务即可。
 *
 *
 * @author alexzhang
 * @date 2020/6/24 14:58
 */
public class TokenGenerated {

    private RedisTemplate<String, String> template;

    AtomicLong totalStock=null;

    @PostConstruct
    public void loadStockCache(){
        //从配置中心或者其他服务加载缓存令牌，如果是单机版，则根据设置，将总的额度进行拆分，如30台机器，每台机器100w的额度。
        long totalCount=10000000L;
        totalStock=new AtomicLong(totalCount);
    }

    /**
     * 由调用触发同步缓存
     */
    public void loadStockCacheManual(){
        //用于手工强制同步外部持久化的token数据
        //TODO:xx
    }

    /**
     * 本地缓存发放令牌许可
     * @param uid
     * @param totalAmount
     * @return
     */
    public boolean getTokenByLocalCache(String uid,long totalAmount){
        if(totalStock.addAndGet(-totalAmount)>0){
            return true;
        }else{
            totalStock.addAndGet(totalAmount);
            return false;
        }
    }


    /**
     * 使用分布式缓存获取令牌
     * @param uid
     * @param totalAmount
     * @return
     */
    public boolean getTokenByDistributionCache(String uid,long totalAmount){
        long exists=template.opsForValue().decrement("total_amount", totalAmount);
        if(exists<0){
            template.opsForValue().increment("total_amount", totalAmount);
            return false;
        }
        return true;
    }

    /**
     * 使用lua脚本获取令牌
     * @param uid
     * @param totalAmount
     * @return
     */
    public boolean getTokenByDistributionCacheOfLUA(String uid,long totalAmount){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("stock.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        Long result = template.execute(redisScript, Collections.singletonList("X"),totalAmount);
        if(result<0){
            return false;
        }
        return true;
    }


    private void sendMsgAsync(String uid,String nodeId,Long amount){
        ExecutorService es= Executors.newFixedThreadPool(100);
        es.submit(()->{
            //send sync message
        });
        //异步发送到消息队列，进行令牌持久化的更新动态调整
        //如某实例挂掉重启，则需要从外部持久化token池中重新加载令牌
        //需要实时查看每个实例消耗了多少token，对量进行一个预估
        //监控使用
        //某些情况下可以直接进行下单操作
    }
}
