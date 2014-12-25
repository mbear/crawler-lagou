/**
 * 
 */
package com.muzongyan.recruit.crawler.daos;

/**
 * @author muzongyan
 *
 */
public interface RedisKeys {

    // 公司URL队列
    String LIST_COMPANY_URL = "lagou:companylist";
    
    // 职位URL队列
    String LIST_JOB_URL = "lagou:joblist";
    
    // 职位与分类映射
    String MAP_JOB_CATEGORY = "lagou:jobmap";
}
