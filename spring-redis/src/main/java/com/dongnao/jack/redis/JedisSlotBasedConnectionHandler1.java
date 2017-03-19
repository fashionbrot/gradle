package com.dongnao.jack.redis;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;

public  class JedisSlotBasedConnectionHandler1 extends JedisSlotBasedConnectionHandler implements Handler{
	
	

	public JedisSlotBasedConnectionHandler1(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int timeout) {
		super(nodes, poolConfig, timeout);
	}

	public JedisSlotBasedConnectionHandler1(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig,
			int connectionTimeout, int soTimeout) {
		super(nodes, poolConfig, connectionTimeout, soTimeout);
	}

}
