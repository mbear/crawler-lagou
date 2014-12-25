/**
 * 
 */
package com.muzongyan.recruit.crawler.dtos;

import java.sql.Date;

/**
 * 职位信息
 * 
 * @author muzongyan
 *
 */
public class JobDetail {

    private String url;

    private String urlCompany;

    private String urlCategory;

    private String name;

    private String salary;

    private String location;

    private String experience;

    private String education;

    private String workPattern;

    private String attract;

    private String publicDate;

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
     * @return the urlCompany
     */
    public String getUrlCompany() {
        return urlCompany;
    }

    /**
     * @param urlCompany
     *            the urlCompany to set
     */
    public void setUrlCompany(String urlCompany) {
        this.urlCompany = urlCompany;
    }

    /**
     * @return the urlCategory
     */
    public String getUrlCategory() {
        return urlCategory;
    }

    /**
     * @param urlCategory
     *            the urlCategory to set
     */
    public void setUrlCategory(String urlCategory) {
        this.urlCategory = urlCategory;
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
     * @return the salary
     */
    public String getSalary() {
        return salary;
    }

    /**
     * @param salary
     *            the salary to set
     */
    public void setSalary(String salary) {
        this.salary = salary;
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
     * @return the experience
     */
    public String getExperience() {
        return experience;
    }

    /**
     * @param experience
     *            the experience to set
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    /**
     * @return the education
     */
    public String getEducation() {
        return education;
    }

    /**
     * @param education
     *            the education to set
     */
    public void setEducation(String education) {
        this.education = education;
    }

    /**
     * @return the workPattern
     */
    public String getWorkPattern() {
        return workPattern;
    }

    /**
     * @param workPattern
     *            the workPattern to set
     */
    public void setWorkPattern(String workPattern) {
        this.workPattern = workPattern;
    }

    /**
     * @return the attract
     */
    public String getAttract() {
        return attract;
    }

    /**
     * @param attract
     *            the attract to set
     */
    public void setAttract(String attract) {
        this.attract = attract;
    }

    /**
     * @return the publicDate
     */
    public String getPublicDate() {
        return publicDate;
    }

    /**
     * @param publicDate
     *            the publicDate to set
     */
    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
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

}
