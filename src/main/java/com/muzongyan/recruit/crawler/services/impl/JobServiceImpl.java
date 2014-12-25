/**
 * 
 */
package com.muzongyan.recruit.crawler.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.muzongyan.recruit.crawler.daos.JedisDao;
import com.muzongyan.recruit.crawler.dtos.JobCategory;
import com.muzongyan.recruit.crawler.dtos.JobDetail;
import com.muzongyan.recruit.crawler.mappers.JobCategoryMapper;
import com.muzongyan.recruit.crawler.mappers.JobDetailMapper;
import com.muzongyan.recruit.crawler.services.JobService;

/**
 * @author muzongyan
 *
 */
@Service
public class JobServiceImpl implements JobService {

    @Value("${lagou.job}")
    private String jobSeed;

    @Autowired
    private JedisDao jedisDao;

    @Autowired
    private JobCategoryMapper categoryMapper;

    @Autowired
    private JobDetailMapper detailMapper;

    @Override
    public void fetchCategoryList() throws IOException {
        List<JobCategory> jcs = new ArrayList<JobCategory>();

        // 访问 http://www.lagou.com/ 页面
        Document doc = Jsoup.connect(jobSeed).get();

        // 定位“职位分类”区域
        Elements levelOneCategories = doc.select("div.mainNavs").first().children();
        // 一级分类
        Iterator<Element> iteLevelOne = levelOneCategories.iterator();
        while (iteLevelOne.hasNext()) {
            Element levelOneCategory = (Element) iteLevelOne.next();
            String levelOne = levelOneCategory.select("h2").text();

            // 二级分类
            Iterator<Element> iteLevelTwo = levelOneCategory.select("div.menu_sub").first().children().iterator();
            while (iteLevelTwo.hasNext()) {
                Element levelTwoCategory = (Element) iteLevelTwo.next();
                String levelTwo = levelTwoCategory.select("dt > a").text();

                Iterator<Element> iteCategories = levelTwoCategory.select("dd > a").iterator();
                while (iteCategories.hasNext()) {
                    Element category = (Element) iteCategories.next();
                    String name = category.text();
                    String url = category.attr("href").replaceFirst("\\?labelWords=label", "");

                    JobCategory jc = new JobCategory();
                    jc.setName(name);
                    jc.setUrl(url);
                    jc.setLevelOne(levelOne);
                    jc.setLevelTwo(levelTwo);
                    jcs.add(jc);
                }
            }
        }

        // 所有职位分类保存到 mysql table job_category
        categoryMapper.addAll(jcs);
    }

    @Override
    public void fetchJobList() throws IOException {
        Set<String> urls = categoryMapper.getAllUrl();

        // 遍历职位分类url，访问职位list页面，解析职位详细介绍url
        for (String url : urls) {
            for (int pn = 1; pn <= 30; pn++) {
                Document doc = Jsoup.connect(url + "?pn=" + pn).get();
                if (doc.select("div#container > div.content > div") == null
                        || doc.select("div#container > div.content > div").hasClass("noresult")) {
                    // 暂时没有符合该搜索条件的公司信息
                    break;
                } else {
                    Elements jobs = doc.select("div#container > div.content > ul.hot_pos > li");
                    for (Element job : jobs) {
                        String jobUrl = job.select("div.hot_pos_l > div > a").attr("href");
                        // Redis 队列 入队
                        jedisDao.pushJobUrlList(jobUrl);
                        // Redis 映射 设置
                        jedisDao.setJobCategoryMap(jobUrl, url);
                    }
                }
            }
        }
    }

    @Override
    public boolean fetchJobDetail() throws InterruptedException, IOException {
        String url = jedisDao.popJobUrlLis();
        if (!StringUtils.isEmpty(url)) {
            JobDetail detail = this.parseJobDetail(url);
            detailMapper.add(detail);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析职位页面
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private JobDetail parseJobDetail(String url) throws IOException {
        JobDetail detail = new JobDetail();

        detail.setUrl(url);

        // 设置职位类别
        detail.setUrlCategory(jedisDao.getJobCategory(url));

        // 访问公司页面，解析各字段
        Document doc = Jsoup.connect(url).get();

        Element jobCompany = doc.select("div.content_r").first();

        // company url
        detail.setUrlCompany(jobCompany.select("dl.job_company > dt > a").attr("href"));

        // job detail
        Element jobDetail = doc.select("div.content_l > dl.job_detail").first();
        // job name
        detail.setName(jobDetail.select("dt > h1").attr("title"));

        // job request
        // salary
        Element salaryElement = jobDetail.select("dd.job_request").first().child(0);
        if (salaryElement != null) {
            detail.setSalary(salaryElement.text());
        }

        // location
        Element locationElement = jobDetail.select("dd.job_request").first().child(1);
        if (locationElement != null) {
            detail.setLocation(locationElement.text());
        }

        // experience
        Element experienceElement = jobDetail.select("dd.job_request").first().child(2);
        if (experienceElement != null) {
            detail.setExperience(experienceElement.text());
        }

        // education
        Element educationElement = jobDetail.select("dd.job_request").first().child(3);
        if (educationElement != null) {
            detail.setEducation(educationElement.text());
        }

        // work pattern
        Element workPatternElement = jobDetail.select("dd.job_request").first().child(4);
        if (workPatternElement != null) {
            detail.setWorkPattern(workPatternElement.text());
        }

        // attract
        String attract = jobDetail.select("dd.job_request").text();
        if (StringUtils.contains(attract, "职位诱惑")) {
            attract = StringUtils.substringAfter(attract, "职位诱惑");
            attract = StringUtils.substringBefore(attract, "发布时间");
            attract = StringUtils.remove(attract, ":");
            attract = StringUtils.remove(attract, "：");
            attract = StringUtils.remove(attract, " ");
            detail.setAttract(attract);
        }

        // public date
        String pubDate = jobDetail.select("dd.job_request > div").text();
        if (StringUtils.contains(pubDate, "发布时间")) {
            pubDate = StringUtils.remove(pubDate, "发布时间");
            pubDate = StringUtils.remove(pubDate, ":");
            pubDate = StringUtils.remove(pubDate, "：");
            pubDate = StringUtils.remove(pubDate, " ");
            detail.setPublicDate(pubDate);
        }

        // description
        detail.setDescription(jobDetail.select("dd.job_bt > p").text());

        return detail;
    }
}
