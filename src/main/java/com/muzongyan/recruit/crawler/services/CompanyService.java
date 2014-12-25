/**
 * 
 */
package com.muzongyan.recruit.crawler.services;

import java.io.IOException;

/**
 * 主要任务：抓取公司信息，过程中抓取公司的行业领域信息
 * 
 * @author muzongyan
 *
 */
public interface CompanyService {

    /**
     * 抓取公司的行业领域信息列表<br/>
     * 访问“www.lagou.com/gongsi”页面，解析“行业领域”区域的行业领域名称和链接<br/>
     * 解析结果保存到 mysql table company_domain
     * 
     * @exception IOException
     */
    public void fetchDomainList() throws IOException;

    /**
     * 获取所有公司页面列表<br/>
     * 基于 mysql table company_domain 的url字段，通过添加url查询参数pn，并从1开始递增访问<br/>
     * 解析 hc_list li href 保存到 redis list 公司url队列
     * 
     * @exception IOException
     */
    public void fetchCompanyList() throws IOException;

    /**
     * 抓取公司的详细信息<br/>
     * 基于 redis list 公司url队列，访问公司详细信息网页，解析页面<br/>
     * 解析结果保存到 mysql table company_detail
     * 
     * @exception InterruptedException, IOException
     */
    public boolean fetchCompanyDetail() throws InterruptedException, IOException;
}
