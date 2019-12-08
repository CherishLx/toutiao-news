package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.List;

/**
 * 封装了Jedis的相关API，String,SET,LIST
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    /*Jedis连接池JedisPool*/
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() {
        pool = new JedisPool("localhost", 6379);
    }

    /*封装获取一个Jedis连接getResource()*/
    private Jedis getJedis() {
        return pool.getResource();
    }

    public String get(String key) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            //return getJedis().get(key);
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return null;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    public void set(String key, String value) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*set中添加*/
    public long sadd(String key, String value) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return 0;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*set中删除*/
    public long srem(String key, String value) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return 0;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*是否是set中的一个成员*/
    public boolean sismember(String key, String value) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return false;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*获取set中的元素个数*/
    public long scard(String key) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return 0;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*字符串操作，增加数据项并设置有效时间*/
    public void setex(String key, String value) {
        // 验证码, 防机器注册，记录上次注册时间，有效期3天，10s
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            jedis.setex(key, 10, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*添加元素到list中*/
    public long lpush(String key, String value) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return 0;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*移除并获取列表中最后一个key，该方法可以阻塞或超时等待*/
    public List<String> brpop(int timeout, String key) {
        //Jedis jedis = null;
        Jedis jedis = getJedis();
        try {
            //jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
            return null;
        } finally {
            /*if (jedis != null) {
                jedis.close();
            }*/
            jedis.close();
        }
    }

    /*Object对象默认是把对象转换成字符串String，然后存储*/
    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    /*Object对象利用JSON的JSON.parseObject(value, clazz)转，泛型*/
    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
