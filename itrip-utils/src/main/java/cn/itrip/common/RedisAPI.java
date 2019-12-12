package cn.itrip.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class RedisAPI {

    public JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 测试 redis OK
     * @param args
     */
    public static void main(String[] args) {
        RedisAPI redisAPI = new RedisAPI();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxActive(300);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxWait(1000);
        jedisPoolConfig.setTestOnBorrow(true);

        redisAPI.jedisPool = new JedisPool(jedisPoolConfig,"192.168.137.130",6379,3000,"123456",0);

//        System.out.println(redisAPI.set("name","张三"));
        redisAPI.delete("name");
    }

    /**
     * 设值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,int seconds,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, value);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //返还到连接池
            if (jedis != null) returnResource(jedisPool, jedis);
        }
        return false;
    }

    public boolean set(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //返还到连接池
            if (jedis != null) returnResource(jedisPool, jedis);
        }
        return false;
    }
    /**
     * 返还到连接池
     *
     * @param pool
     * @param redis
     */
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            if (jedis != null) {
                returnResource(jedisPool,jedis);
            }
        }
        return value;
    }

    /**
     * 查询key的有效期，当key不存在时，返回-2,当key存在但没有设置有效时间时，返回-1
     * 否则以秒为单位，返回key的剩余时间
     * 注意：在Redis2.8以前，当key不存在，或者key没有设置有效时间时，命令都返回-1
     * @param key
     * @return 剩余多少秒
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            if (jedis != null) {
                returnResource(jedisPool,jedis);
            }
        }
        return -2L;
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 返还到连接池
            if (jedis != null) {
                returnResource(jedisPool,jedis);
            }
        }
    }

    /**
     * 判断某个key是否存在
     * @param key
     * @return
     */
    public boolean exist(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //返还到连接池
            if (jedis != null) returnResource(jedisPool, jedis);
        }
        return false;
    }
}
