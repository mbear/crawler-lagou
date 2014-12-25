/**
 * 
 */
package com.muzongyan.recruit.crawler.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author muzongyan
 *
 */
@Repository
public class JedisDao implements RedisKeys {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 公司详情URL队列入队
     * 
     * @param companyUrl
     */
    public void pushCompanyUrlList(String companyUrl) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(RedisKeys.LIST_COMPANY_URL, companyUrl);
        }
    }

    /**
     * 公司详情URL队列出队
     * 
     * @return
     */
    public String popCompanyUrlLis() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(LIST_COMPANY_URL);
        }
    }

    /**
     * 职位详情URL队列入队
     * 
     * @param jobUrl
     */
    public void pushJobUrlList(String jobUrl) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(RedisKeys.LIST_JOB_URL, jobUrl);
        }
    }

    /**
     * 职位详情URL队列出队
     * 
     * @return
     */
    public String popJobUrlLis() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(LIST_JOB_URL);
        }
    }

    /**
     * 设置职位与分类的映射
     * 
     * @param jobUrl
     * @param categoryUrl
     */
    public void setJobCategoryMap(String jobUrl, String categoryUrl) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(MAP_JOB_CATEGORY, jobUrl, categoryUrl);
        }
    }

    /**
     * 根据职位取得所属分类
     * 
     * @param jobUrl
     * @return
     */
    public String getJobCategory(String jobUrl) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(MAP_JOB_CATEGORY, jobUrl);
        }
    }
}
