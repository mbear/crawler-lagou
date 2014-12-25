/**
 * 
 */
package com.muzongyan.recruit.crawler.clients;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

    static ApplicationContext springContext;

    /**
     * @param args
     */
    public static void main(String[] args) {

        int task = 0;
        if (args == null || args.length == 0) {
            System.out.println("Please input your task num:");
            System.out.println("1. company producer");
            System.out.println("2. company consumer");
            System.out.println("3. job producer");
            System.out.println("4. job consumer");
            return;
        } else {
            task = Integer.parseInt(args[0]);
        }

        springContext = new ClassPathXmlApplicationContext(CONFIG_PATH);

        switch (task) {
        case 1:
            ExecutorService es1 = Executors.newSingleThreadExecutor();
            es1.execute(new Runnable() {

                @Override
                public void run() {
                    CompanyService companyService = springContext.getBean(CompanyServiceImpl.class);
                    try {
                        // 抓取公司的行业领域信息列表
                        companyService.fetchDomainList();
                        // 获取所有公司页面列表
                        companyService.fetchCompanyList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            es1.shutdown();
            break;

        case 2:
            ScheduledExecutorService ses1 = Executors.newScheduledThreadPool(3);
            for (int i = 1; i < 3; i++) {
                ses1.execute(new Runnable() {

                    @Override
                    public void run() {
                        CompanyService companyService = springContext.getBean(CompanyServiceImpl.class);
                        boolean flag = true;
                        while (flag) {
                            try {
                                // 抓取公司的详细信息
                                flag = companyService.fetchCompanyDetail();
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            ses1.shutdown();
            break;

        case 3:
            ExecutorService es2 = Executors.newSingleThreadExecutor();
            es2.execute(new Runnable() {

                @Override
                public void run() {
                    JobService jobService = springContext.getBean(JobServiceImpl.class);
                    try {
                        // 抓取职位分类信息列表
                        jobService.fetchCategoryList();
                        // 获取所有职位页面列表
                        jobService.fetchJobList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            es2.shutdown();
            break;

        case 4:
            ScheduledExecutorService ses2 = Executors.newScheduledThreadPool(5);
            for (int i = 1; i < 5; i++) {
                ses2.execute(new Runnable() {

                    @Override
                    public void run() {
                        JobService jobService = springContext.getBean(JobServiceImpl.class);
                        boolean flag = true;
                        while (flag) {
                            try {
                                // 抓取职位的详细信息
                                flag = jobService.fetchJobDetail();
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            ses2.shutdown();
            break;

        default:
            break;
        }
    }

}
