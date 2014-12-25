/**
 * 
 */
package com.muzongyan.recruit.crawler.mappers;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Select;

import com.muzongyan.recruit.crawler.dtos.JobCategory;

/**
 * @author muzongyan
 *
 */
public interface JobCategoryMapper {

    public void addAll(List<JobCategory> list);

    @Select("SELECT url FROM lagou_job_category")
    public Set<String> getAllUrl();
}
