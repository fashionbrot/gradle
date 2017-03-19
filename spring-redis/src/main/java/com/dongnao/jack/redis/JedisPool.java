package com.dongnao.jack.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class JedisPool {
    //JedisPool��һ̨Redis��ShardedJedisPool��Redis��Ⱥ��ͨ��һ���Թ�ϣ�㷨���������ݴ浽��̨�ϣ�����һ�ֿͻ��˸��ؾ��⣬
    private static ShardedJedisPool pool;
    private static int MAXTOTAL=300;
    private static int MAXIDLE=200;
    private static int MINIDEL=10;
    private static int MAXWAIRMILLIS=1000;
    private static Boolean TESTONBORROW=true;
    private static Boolean TESTONRETURN=false;
    private static Boolean TESTWHILEIDLE=false;
    //��̬�����ʼ�����ӳ�����
    static {
        try {
            //��ʼ�����ӳز�������
            JedisPoolConfig config=initConfig();
            List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
            String host="192.168.31.29:1000,192.168.31.29:1010,192.168.31.29:2000,192.168.31.29:2020,192.168.31.29:3000,192.168.31.29:3030";//��������ַ,����
            Set<String> hosts=init(host);
            for (String hs:hosts){
                String[] values=hs.split(":");
                JedisShardInfo shard=new JedisShardInfo(values[0],Integer.parseInt(values[1]));
                if(values.length>2){
                    shard.setPassword(values[2]);
                }
                shards.add(shard);
            }
            pool=new ShardedJedisPool(config,shards);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //��ʼ�����ӳز���
    private static JedisPoolConfig initConfig(){
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(MAXTOTAL);
        config.setMaxIdle(MAXIDLE);
        config.setMinIdle(MINIDEL);
        config.setMaxWaitMillis(MAXWAIRMILLIS);
        config.setTestOnBorrow(TESTONBORROW);
        config.setTestOnReturn(TESTONRETURN);
        config.setTestWhileIdle(TESTWHILEIDLE);
        return config;
    }

    private static Set<String> init(String values){
        if(StringUtils.isBlank(values)){
            throw new NullPointerException("redis host not found");
        }
        Set<String> paramter=new HashSet<String>();
        String[] sentinelArray=values.split(",");
        for(String str:sentinelArray){
            paramter.add(str);
        }
        return paramter;
    }

    public static ShardedJedisPool getShardedJedisPool(){
        return pool;
    }
}