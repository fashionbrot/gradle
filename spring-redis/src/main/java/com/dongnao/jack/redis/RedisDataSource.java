package com.dongnao.jack.redis;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDataSource  {

    private static ThreadLocal<ShardedJedis> jedisLocal = new ThreadLocal<ShardedJedis>();
    private static ShardedJedisPool pool;
    static {
        pool = JedisPool.getShardedJedisPool();
    }

    public  ShardedJedis getClient() {
        ShardedJedis jedis = jedisLocal.get();
        if (jedis == null) {
            jedis = pool.getResource();
            jedisLocal.set(jedis);
        }
        return jedis;
    }

    //πÿ±’¡¨Ω”
    public void returnResource() {
        ShardedJedis jedis = jedisLocal.get();
        if (jedis != null) {
            pool.destroy();
            jedisLocal.set(null);
        }
    }
}