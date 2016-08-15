package com.wx.model;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * Created by JohnC on 2016-08-12.
 */
public class SQLProvider
{
    public static String getSqlString(Map<String,Object> parameters)
    {
        String datasourcetype = (String)parameters.get("datasourcetype");
        int offset = Integer.valueOf(parameters.get("offset").toString());
        int limit = Integer.valueOf(parameters.get("limit").toString());
        String start = (String)parameters.get("start");
        String end = (String)parameters.get("end");
        return new SQL(){
            {
                SELECT("id,forumname,title,taskurl,sitename,datasourcetype,filePath");
                FROM("basicinfo");
                if(!datasourcetype.equals("0"))
                {
                    WHERE("datasourcetype='" + datasourcetype + "'");
                }
                if(!start.equals("0") && !end.equals("0"))
                {
                    WHERE("createtime >= '" + start + "' AND createtime  <= '" + end + "'");
                }
                else if(!start.equals("0") && end.equals("0"))
                {
                    WHERE ("createtime >= '" + start + "'");
                }
                else if(start.equals("0") && !end.equals("0"))
                {
                    WHERE("createtime  <= '" + end + "'");
                }
            }
        }.toString() + " limit " + offset + "," + limit;
    }
    
    public static String getCountSqlString(Map<String,Object> parameters)
    {
        String datasourcetype = (String)parameters.get("datasourcetype");
        String start = (String)parameters.get("start");
        String end = (String)parameters.get("end");
        return new SQL(){
            {
                SELECT("COUNT(id)");
                FROM("basicinfo");
                if(!datasourcetype.equals("0"))
                {
                    WHERE("datasourcetype='" + datasourcetype+ "'");
                }
                if(!start.equals("0") && !end.equals("0"))
                {
                    WHERE("createtime >= '" + start + "' AND createtime  <= '" + end + "'");
                }
                else if(!start.equals("0") && end.equals("0"))
                {
                    WHERE ("createtime >= '" + start + "'");
                }
                else if(start.equals("0") && !end.equals("0"))
                {
                    WHERE("createtime  <= '" + end + "'");
                }
            }
        }.toString();
    }
    
    public static String getSQLStringByTimestamp(Map<String,Object> parameters)
    {
        String datasourcetype = (String)parameters.get("datasourcetype");
        int offset = Integer.valueOf(parameters.get("offset").toString());
        int limit = Integer.valueOf(parameters.get("limit").toString());
        String start = (String) parameters.get("start").toString();
        String end = (String) parameters.get("end").toString();
        return new SQL(){
            {
                SELECT("id,forumname,title,taskurl,sitename,datasourcetype,filePath,createtime");
                FROM("basicinfo");
                if(!datasourcetype.equals("0"))
                {
                    WHERE("datasourcetype='" + datasourcetype+ "'");
                }
                if(!start.equals("0") && !end.equals("0"))
                {
                    WHERE("createtime >= '" + start + "' AND createtime  <= '" + end + "'");
                }
                else if(!start.equals("0") && end.equals("0"))
                {
                    WHERE ("createtime >= '" + start + "'");
                }
                else if(start.equals("0") && !end.equals("0"))
                {
                    WHERE("createtime  <= '" + end + "'");
                }
            }
        }.toString() + " limit " + offset + "," + limit;
    }
}
