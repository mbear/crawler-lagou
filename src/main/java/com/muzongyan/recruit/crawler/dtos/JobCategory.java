/**
 * 
 */
package com.muzongyan.recruit.crawler.dtos;

import java.sql.Date;

/**
 * 职位分类
 * 
 * @author muzongyan
 *
 */
public class JobCategory {

    private String url;

    private String name;

    private String levelOne;

    private String levelTwo;

    private Date createTime;

    private Date updateTime;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the levelOne
     */
    public String getLevelOne() {
        return levelOne;
    }

    /**
     * @param levelOne
     *            the levelOne to set
     */
    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    /**
     * @return the levelTwo
     */
    public String getLevelTwo() {
        return levelTwo;
    }

    /**
     * @param levelTwo
     *            the levelTwo to set
     */
    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     *            the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
