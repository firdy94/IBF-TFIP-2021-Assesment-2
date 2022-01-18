package com.ibftfip2021.ssfassesment.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig {
	private static final Logger logger = LogManager.getLogger(AppConfig.class);

	@Autowired
	Environment env;

	@Bean
	@Scope("singleton")
	public RedisTemplate<String, Object> createRedisTemplate() {
		final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setDatabase(
				(Integer.parseInt(env.getProperty("spring.redis.database"))));
		// logger.info(Integer.parseInt(env.getProperty("spring.redis.database")));
		config.setHostName(env.getProperty("spring.redis.host"));
		// logger.info(env.getProperty("spring.redis.host"));
		config.setPort(Integer.parseInt(env.getProperty("spring.redis.port")));
		// logger.info(Integer.parseInt(env.getProperty("spring.redis.port")));
		String password = System.getenv("springredispassword");
		String username = System.getenv("springredisusername");
		config.setPassword(password);
		config.setUsername(username);

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMaxTotal(Integer.parseInt(
				env.getProperty("spring.redis.jedis.pool.max-active")));
		logger.info(Integer.parseInt(
				env.getProperty("spring.redis.jedis.pool.max-active")));
		poolConfig.setMinIdle(Integer
				.parseInt(env.getProperty("spring.redis.jedis.pool.min-idle")));
		logger.info(Integer
				.parseInt(env.getProperty("spring.redis.jedis.pool.min-idle")));
		poolConfig.setMaxIdle(Integer
				.parseInt(env.getProperty("spring.redis.jedis.pool.max-idle")));
		logger.info(Integer
				.parseInt(env.getProperty("spring.redis.jedis.pool.max-idle")));

		final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
		final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
		jedisFac.afterPropertiesSet();

		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		logger.info(jedisClient.toString());
		logger.info(config.toString());
		template.setConnectionFactory(jedisFac);
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(genericJackson2JsonRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
		return template;
	}
}