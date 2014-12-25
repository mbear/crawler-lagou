/**
 * 
 */
package com.muzongyan.recruit.crawler.services;

import java.io.IOException;

/**
 * 主要任务：抓取职位信息，过程中抓取职位所属分类信息
 * 
 * @author muzongyan
 *
 */
public interface JobService {

    /**
     * 抓取职位分类信息列表<br/>
     * 访问“http://www.lagou.com/”页面，解析“职位分类”区域的分类名称和链接<br/>
     * 解析结果保存到 mysql table job_category
     * 
     * @exception IOException
     */
    public void fetchCategoryList() throws IOException;

    /**
     * 获取所有职位页面列表<br/>
     * 基于 mysql table job_category 的url字段，通过添加url查询参数pn，并从1开始递增访问到30<br/>
     * 解析具体职位链接保存到 redis list 职位url队列
     * 
     * @exception IOException
     */
    public void fetchJobList() throws IOException;

    /**
     * 抓取职位的详细信息<br/>
     * 基于 redis list 职位url队列，访问职位详细信息网页，解析页面<br/>
     * 解析结果保存到 mysql table job_detail
     * 
     * @exception InterruptedException, IOException
     */
    public boolean fetchJobDetail() throws InterruptedException, IOException;
}
