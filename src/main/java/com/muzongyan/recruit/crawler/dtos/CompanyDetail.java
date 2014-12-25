/**
 * 
 */
package com.muzongyan.recruit.crawler.dtos;

import java.sql.Date;

/**
 * 公司信息
 * 
 * @author muzongyan
 *
 */
public class CompanyDetail {

    private String url;

    private String name;

    private String fullName;

    private String logoImg;

    private String oneWord;

    private String labels;

    private String location;

    private String domain;

    private String scale;

    private String homepage;

    private String currStage;

    private String investor;

    private String description;

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
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
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
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName
     *            the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the oneWord
     */
    public String getOneWord() {
        return oneWord;
    }

    /**
     * @param oneWord
     *            the oneWord to set
     */
    public void setOneWord(String oneWord) {
        this.oneWord = oneWord;
    }

    /**
     * @return the labels
     */
    public String getLabels() {
        return labels;
    }

    /**
     * @param labels
     *            the labels to set
     */
    public void setLabels(String labels) {
        this.labels = labels;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the scale
     */
    public String getScale() {
        return scale;
    }

    /**
     * @param scale
     *            the scale to set
     */
    public void setScale(String scale) {
        this.scale = scale;
    }

    /**
     * @return the homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage
     *            the homepage to set
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return the currStage
     */
    public String getCurrStage() {
        return currStage;
    }

    /**
     * @param currStage
     *            the currStage to set
     */
    public void setCurrStage(String currStage) {
        this.currStage = currStage;
    }

    /**
     * @return the investor
     */
    public String getInvestor() {
        return investor;
    }

    /**
     * @param investor
     *            the investor to set
     */
    public void setInvestor(String investor) {
        this.investor = investor;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * @return the logoImg
     */
    public String getLogoImg() {
        return logoImg;
    }

    /**
     * @param logoImg
     *            the logoImg to set
     */
    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

}
