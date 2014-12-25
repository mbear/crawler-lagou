/**
 * 
 */
package com.muzongyan.recruit.crawler.mappers;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.muzongyan.recruit.crawler.dtos.CompanyDomain;

/**
 * @author muzongyan
 *
 */
public interface CompanyDomainMapper {

    @Insert("INSERT INTO lagou_company_domain(name, url, create_time)"
            + " VALUES (#{domain.name}, #{domain.url}, now())"
            + " ON DUPLICATE KEY UPDATE name = VALUES(name), update_time = now()")
    public void add(@Param("domain") CompanyDomain domain);
    
    public void addAll(List<CompanyDomain> list);

    @Select("SELECT url FROM lagou_company_domain")
    public Set<String> getAllUrl();
    
}
