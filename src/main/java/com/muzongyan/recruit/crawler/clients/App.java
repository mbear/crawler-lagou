/**
 * 
 */
package com.muzongyan.recruit.crawler.clients;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.muzongyan.recruit.crawler.dtos.CompanyDetail;
import com.muzongyan.recruit.crawler.dtos.JobDetail;
import com.muzongyan.recruit.crawler.services.CompanyService;
import com.muzongyan.recruit.crawler.services.JobService;
import com.muzongyan.recruit.crawler.services.impl.CompanyServiceImpl;
import com.muzongyan.recruit.crawler.services.impl.JobServiceImpl;

/**
 * 爬取公司信息
 * 
 * @author muzongyan
 *
 */
public class App {

    private static final String CONFIG_PATH = "classpath:spring/applicationContext.xml";
    
    private static final String SOLR_SERVER_URL = "http://123.57.37.142:8983/solr/job";

    static ApplicationContext springContext;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        springContext = new ClassPathXmlApplicationContext(CONFIG_PATH);

        final CountDownLatch companyLatch = new CountDownLatch(1);
        final CountDownLatch jobLatch = new CountDownLatch(1);

        ExecutorService companyProducer = Executors.newSingleThreadExecutor();
        companyProducer.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CompanyService companyService = springContext.getBean(CompanyServiceImpl.class);
                    // 抓取公司的行业领域信息列表
                    companyService.fetchDomainList();
                    // 获取所有公司页面列表
                    companyService.fetchCompanyList();
                } finally {
                    companyLatch.countDown();
                }
            }
        });

        companyProducer.shutdown();

        ExecutorService jobProducer = Executors.newSingleThreadExecutor();
        jobProducer.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JobService jobService = springContext.getBean(JobServiceImpl.class);
                    // 抓取职位分类信息列表
                    jobService.fetchCategoryList();
                    // 获取所有职位页面列表
                    jobService.fetchJobList();
                } finally {
                    jobLatch.countDown();
                }
            }
        });

        jobProducer.shutdown();

        // companyConsumer 开始
        ExecutorService companyConsumer = Executors.newFixedThreadPool(5);
        for (int i = 1; i < 10; i++) {
            companyConsumer.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        companyLatch.await();

                        CompanyService companyService = springContext.getBean(CompanyServiceImpl.class);
                        boolean flag = true;
                        while (flag) {
                            // 抓取公司的详细信息
                            try {
                                flag = companyService.fetchCompanyDetail();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        companyConsumer.shutdown();

        // jobProducer 开始
        ExecutorService jobConsumer = Executors.newFixedThreadPool(5);
        for (int i = 1; i < 10; i++) {
            jobConsumer.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        jobLatch.await();

                        JobService jobService = springContext.getBean(JobServiceImpl.class);
                        CompanyService companyService = springContext.getBean(CompanyServiceImpl.class);
                        
                        SolrServer solr = new HttpSolrServer(SOLR_SERVER_URL);
                        
                        boolean flag = true;
                        while (flag) {
                            try {
                                // 抓取职位的详细信息
                                JobDetail jd = new JobDetail();
                                flag = jobService.fetchJobDetail(jd);
                                
                                // 抓取职位对应的公司的详细信息
                                String companyUrl = jd.getUrlCompany();
                                CompanyDetail cd = companyService.parseCompanyDetail(companyUrl);
                                companyService.saveCompanyDetail(cd);
                                
                                // add jd and cd to solr
                                SolrInputDocument doc = new SolrInputDocument();
                                doc.setField("url", jd.getUrl());
                                doc.setField("name", jd.getName());
                                doc.setField("company_url", cd.getUrl());
                                doc.setField("company_name", cd.getName());
                                doc.setField("company_fullname", cd.getFullName());
                                doc.setField("company_location", cd.getLocation());
                                doc.setField("company_domain", cd.getDomain());
                                doc.setField("company_description", cd.getDescription());
                                doc.setField("salary", jd.getSalary());
                                doc.setField("location", jd.getLocation());
                                doc.setField("experience", jd.getExperience());
                                doc.setField("education", jd.getEducation());
                                doc.setField("work_pattern", jd.getWorkPattern());
                                doc.setField("attract", jd.getAttract());
                                doc.setField("public_date", jd.getPublicDate());
                                doc.setField("description", jd.getDescription());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        
                        solr.commit(true, true);
                    } catch (InterruptedException | SolrServerException | IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        jobConsumer.shutdown();
    }

}
