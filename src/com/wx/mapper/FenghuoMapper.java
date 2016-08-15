package com.wx.mapper;

import com.wx.model.Article;
import com.wx.model.BasicInfo;
import com.wx.model.DownloadZip;
import com.wx.model.SQLProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by JohnC on 2016-08-12.
 */
public interface FenghuoMapper
{
    /*@Select("SELECT id,forumname,title,taskurl,sitename,datasourcetype FROM basicinfo")*/
    @SelectProvider(type = SQLProvider.class,method = "getSqlString")
    @Results({
        @Result(property = "id",column = "id"),
            @Result(property = "forumname",column = "forumname"),
            @Result(property = "title",column = "title"),
            @Result(property = "taskurl",column = "taskurl"),
            @Result(property = "sitename",column = "sitename"),
            @Result(property = "datasourcetype",column = "datasourcetype"),
            @Result(property = "articles",javaType = List.class,column = "id",
                    many = @Many(select = "findByBasicInfoId")),
            @Result(property = "filePath",column = "filePath"),
            @Result(property = "createtime",column = "createtime")
    })
    List<BasicInfo> findByCondition(@Param("offset") int offset,
                                    @Param("limit") int limit,
                                    @Param("datasourcetype") String datasourcetype,
                                    @Param("start") String start,
                                    @Param("end") String end);
    
    @SelectProvider(type = SQLProvider.class,method = "getCountSqlString")
    int countByCondition(@Param("datasourcetype") String datasourcetype,
                         @Param("start") String start,
                         @Param("end") String end);
    
    @Select("SELECT * FROM article WHERE basicinfo_id=#{basicinfo_id}")
    @Results({
        @Result(property = "id",column = "id"),
            @Result(property = "postdata",column = "postdata")
    })
    List<Article> findByBasicInfoId(@Param("basicinfo_id") int basicinfo_id);
    
    @SelectProvider(type = SQLProvider.class,method = "getSQLStringByTimestamp")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "forumname",column = "forumname"),
            @Result(property = "title",column = "title"),
            @Result(property = "taskurl",column = "taskurl"),
            @Result(property = "sitename",column = "sitename"),
            @Result(property = "datasourcetype",column = "datasourcetype"),
            @Result(property = "articles",javaType = List.class,column = "id",
                    many = @Many(select = "findByBasicInfoId")),
            @Result(property = "filePath",column = "filePath"),
            @Result(property = "createtime",column = "createtime")
    })
    List<BasicInfo> findByLimitAndTimestamp(@Param("offset") int offset,@Param("limit") int limit,
                                            @Param("datasourcetype") String datasourcetype,
                                            @Param("start") String start,@Param("end") String end);
    
    @Insert("INSERT INTO download_zip (title,filePath,fileName) VALUES (#{title},#{filePath},#{fileName})")
    int insertDownloadZip(DownloadZip zip);
    
    @Select("SELECT * FROM download_zip ORDER BY id DESC")
    @Results({
        @Result(property = "id",column = "id"),
            @Result(property = "title",column = "title"),
            @Result(property = "filePath",column = "filePath"),
            @Result(property = "fileName",column = "fileName")
    })
    List<DownloadZip> findAll();
    
    @Delete("DELETE FROM download_zip WHERE id=#{id}")
    int deleteById(@Param("id") int id);
    
    @Select("SELECT * FROM download_zip WHERE id=#{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "title",column = "title"),
            @Result(property = "filePath",column = "filePath"),
            @Result(property = "fileName",column = "fileName")
    })
    DownloadZip findDownloadZipById(@Param("id") int id);
}
