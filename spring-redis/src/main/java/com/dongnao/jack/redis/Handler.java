package com.dongnao.jack.redis;

import redis.clients.jedis.Jedis;

public interface Handler {

	public Jedis getConnectionFromSlot(int slot);
}
