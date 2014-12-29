/**
 * 
 */
package com.muzongyan.recruit.crawler.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.muzongyan.recruit.crawler.dtos.CompanyDetail;
import com.muzongyan.recruit.crawler.dtos.CompanyDomain;
import com.muzongyan.recruit.crawler.mappers.CompanyDetailMapper;
import com.muzongyan.recruit.crawler.mappers.CompanyDomainMapper;
import com.muzongyan.recruit.crawler.services.CompanyService;

/**
 * @author muzongyan
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Value("${lagou.company}")
    private String companySeed;

    @Autowired
    private CompanyDomainMapper domainMapper;

    @Autowired
    private CompanyDetailMapper detailMapper;

    @Autowired
    private JedisDao jedisDao;

    private List<CompanyDomain> domainList = new ArrayList<CompanyDomain>();

    @Override
    public void fetchDomainList() {
        // 访问 http://www.lagou.com/gongsi 页面
        Document doc;
        try {
            doc = Jsoup.connect(companySeed).timeout(2000).get();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 定位 “行业领域”元素
        Element domain = null;
        Elements dls = doc.select("form#companyListForm > dl.hc_tag dl");
        for (Element dl : dls) {
            if (StringUtils.contains(dl.select("dt").first().text(), "行业领域")) {
                domain = dl;
                break;
            }
        }

        // 解析行业领域名称和链接，并存储到数据库
        if (domain != null) {
            Elements hrefs = domain.select("a");
            domainList.clear();

            for (Element e : hrefs) {
                CompanyDomain cd = new CompanyDomain();
                cd.setName(e.text());
                cd.setUrl(e.attr("href"));
                domainList.add(cd);
            }
        }

        // 批量插入数据库
        domainMapper.addAll(domainList);
    }

    @Override
    public void fetchCompanyList() {

        // 取得所有行业领域的url
        Set<String> urls = new HashSet<String>();
        if (domainList.isEmpty()) {
            // 从数据库中取得所有行业领域链接
            urls = domainMapper.getAllUrl();
        } else {
            for (CompanyDomain domain : domainList) {
                urls.add(domain.getUrl());
            }
        }

        // 遍历行业领域url，访问公司list页面，解析公司详细介绍url
        for (String url : urls) {
            for (int pn = 1;; pn++) {
                System.out.println("parse company list page === " + url + "?pn=" + pn);
                Document doc;
                try {
                    doc = Jsoup.connect(url + "?pn=" + pn).timeout(2000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                if (doc.select("form#companyListForm > div") == null
                        || doc.select("form#companyListForm > div").hasClass("noresult")) {
                    // 暂时没有符合该搜索条件的公司信息
                    break;
                } else {
                    Elements lis = doc.select("form#companyListForm > ul.hc_list > li");
                    for (Element li : lis) {
                        Element company = li.child(0);
                        // Redis 队列 入队
                        jedisDao.pushCompanyUrlList(company.attr("href"));
                    }
                }
            }
        }

    }

    @Override
    public boolean fetchCompanyDetail() {
        String url = jedisDao.popCompanyUrlLis();
        if (!StringUtils.isEmpty(url)) {
            CompanyDetail detail = this.parseCompanyDetial(url);
            detailMapper.add(detail);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析公司页面
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private CompanyDetail parseCompanyDetial(String url) {
        CompanyDetail detail = new CompanyDetail();

        detail.setUrl(url);

        // 访问公司页面，解析各字段
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(2000).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Element contentLeft = doc.select("div.content_l").first();
        Element logo = contentLeft.select("div.c_logo").first();
        Element box = contentLeft.select("div.c_box").first();

        // 公司logo 图片url
        String logoImg = logo.select("img").first().attr("src");
        detail.setLogoImg(logoImg);

        // 公司名称
        String name = box.select("h2").first().text();
        detail.setName(name);

        // 公司全称
        String fullName = box.select("h1.fullname").first().text();
        detail.setFullName(fullName);

        // 口号
        String oneWord = box.select("div.oneword").first().text();
        detail.setOneWord(StringUtils.trimToEmpty(oneWord));

        // 标签
        List<String> labels = new ArrayList<String>();
        Elements spans = box.select("ul#hasLabels span");
        for (Element span : spans) {
            labels.add(span.text());
        }
        detail.setLabels(labels.toString());

        // 介绍
        Element desc = contentLeft.select("div.c_intro").first();
        if (desc != null) {
            detail.setDescription(desc.text());
        }

        Element contentRight = doc.select("div.content_r").first();
        Element tags = contentRight.select("div.c_tags").first();
        Element stages = contentRight.select("dl.c_stages").first();

        Elements trs = tags.select("tr");
        for (Element tr : trs) {
            Element firstTd = tr.select("td").first();
            Element secondTd = firstTd.nextElementSibling();
            switch (firstTd.text()) {
            case "地点":
                // 地点
                detail.setLocation(secondTd.text());
                break;
            case "领域":
                // 规模
                detail.setDomain(secondTd.text());
                break;
            case "规模":
                // 规模
                detail.setScale(secondTd.text());
                break;
            case "主页":
                // 主页
                detail.setHomepage(secondTd.select("a").attr("href"));
                break;
            default:
                break;
            }
        }

        // 融资阶段
        Elements lis = stages.select("ul.stageshow li");
        for (Element li : lis) {
            String tmp = li.text();
            if (StringUtils.contains(tmp, "目前阶段")) {
                detail.setCurrStage(li.select("span").first().text());
            } else if (StringUtils.contains(tmp, "投资机构")) {
                detail.setInvestor(li.select("span").first().text());
            }
        }

        return detail;
    }

}
