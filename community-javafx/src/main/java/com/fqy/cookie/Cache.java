package com.fqy.cookie;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统的会话由客户端进行管理，客户端登录成功，即会话开始，客户端注销或退出，即会话结束。
 *
 * 后端只负责数据，并不管理会话，会话过程中的临时数据，由客户端存储在内存中，通过一个单例的缓存类中的一个 Map 以 Key，Value 的形式进行存储，
 *
 * 一旦客户端关闭，则缓存数据自然清除。后续可能会引入其他方式，如 JWT 或 Redis-token 的形式集中管理会话。
 */

public class Cache {
    /**
     * 存储信息的缓存集合,采用Map类型
     */
    private final ConcurrentHashMap<String,Object> cookie = new ConcurrentHashMap<>();
    /**
     * 静态的缓存对象,全局唯一
     */
    private static volatile Cache cache;
    /**
     * 无参数的构造方法
     */
    private Cache() {
    }

    /**
     * 获取静态缓存对象中的缓存Map
     * @return map
     */
    public static ConcurrentHashMap<String,Object> Cookie(){
        if(cache == null){
            synchronized (Cache.class){
                if(cache == null){
                    cache = new Cache();
                }
            }
        }
        return cache.getCookie();
    }

    private ConcurrentHashMap<String, Object> getCookie() {
        return cookie;
    }

}


