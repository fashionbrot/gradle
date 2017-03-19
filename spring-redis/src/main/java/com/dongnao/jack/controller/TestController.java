package com.dongnao.jack.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dongnao.jack.redis.JedisSlotBasedConnectionHandler1;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.jedis.Transaction;
import redis.clients.util.JedisClusterCRC16;

@Controller
@RequestMapping("/test")
public  class TestController {
	
	
	
	@Autowired
	JedisCluster jedisCluster;
	
	private static final String watch_key="count2";
	

	@RequestMapping("/invoke")
	public @ResponseBody String test() {
		/*jedisCluster.set("count","100");
		RedisDataSource data=new RedisDataSource();
		data.getClient().m;*/
		/*System.out.println(jedisCluster.echo("count"));
		return "OK";*/
		System.out.println();
		jedisCluster.set(watch_key, "100");
		return jedisCluster.get(watch_key);
	}
	
	
	@RequestMapping("/getWatch")
	public @ResponseBody String getWatch() {
		long start=System.currentTimeMillis();
		String value=jedisCluster.get(watch_key);
		System.out.println(System.currentTimeMillis()-start);
		return value;
		
	}
	
	private static JedisSlotBasedConnectionHandler handler=null ;
	
	@Autowired
	private JedisSlotBasedConnectionHandler1 JedisSlotBasedConnectionHandler;
	
	
	@RequestMapping("/miao")
	public @ResponseBody String miaosha() {
		
		
		long start =System.currentTimeMillis();
		JedisSlotBasedConnectionHandler headler=getHeader();
		int solt=JedisClusterCRC16.getSlot(watch_key);
		Jedis jedis=null;
		try {
			 jedis=headler.getConnectionFromSlot(solt);
		} catch (Exception e) {
			headler=null;
			 headler=getHeader();
			headler.renewSlotCache();
			jedis=headler.getConnectionFromSlot(solt);
		}
		
		
		int count = Integer.parseInt(jedis.get(watch_key));
		if(count==0){
			return "fail";
		}
		jedis.watch(watch_key);
		Transaction transaction = jedis.multi();
		transaction.set(watch_key, String.valueOf(count - 1));
		List<Object> result = transaction.exec();
        if (result == null || result.isEmpty()) {
            System.out.println("Transaction error...");
        }
        jedis.close();
        System.out.println(System.currentTimeMillis()-start);
		return jedis.get(watch_key);
	}
	
	public static JedisSlotBasedConnectionHandler  getHeader(){
		if(handler!=null){
			return handler;
		}
		
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		HostAndPort p=new HostAndPort("192.168.31.29", 1000);
		HostAndPort p1=new HostAndPort("192.168.31.29", 2000);
		HostAndPort p2=new HostAndPort("192.168.31.29", 3000);
		HostAndPort p3=new HostAndPort("192.168.31.29", 1010);
		HostAndPort p4=new HostAndPort("192.168.31.29", 2020);
		HostAndPort p5=new HostAndPort("192.168.31.29", 3030);
		nodes.add(p);
		/*nodes.add(p1);
		nodes.add(p2);
		nodes.add(p3);
		nodes.add(p4);
		nodes.add(p5);*/
		//new JedisSlotBasedConnectionHandler(nodes,null, 10000);
		
		int solt=JedisClusterCRC16.getSlot(watch_key);
		//JedisClusterInfoCache.
		GenericObjectPoolConfig config=new GenericObjectPoolConfig();
		config.setMaxWaitMillis(-1);
		config.setMaxTotal(1000);
		config.setMinIdle(8);
		config.setMaxIdle(100);
		handler=new JedisSlotBasedConnectionHandler(nodes, config, solt);
		return handler;
	}
	
	
	public static void main(String[] args) {
		 String script = "local food=redis.call('hget',KEYS[1],'food');"
	            + "food=food+ARGV[1];"
	            + "redis.call('hset',KEYS[1],'food',food);"
	            + "local diamond=redis.call('hget',KEYS[1],'diamond');"
	            + "diamond=diamond+ARGV[2];"
	            + "redis.call('hset',KEYS[1],'diamond',diamond);";
		
		JedisPool jedisPool = new JedisPool("192.168.31.29", 6381);
		// ���jedis ���Ӷ���
		Jedis jedis = jedisPool.getResource();
		String key="count";
		jedis.watch(key);
		System.out.println(jedis.get("count"));
		jedis.unwatch();
		jedis.close();
	}
	
}
