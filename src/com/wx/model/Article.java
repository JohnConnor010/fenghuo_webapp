package com.wx.model;

/**
 * Created by JohnC on 2016-08-12.
 */
public class Article
{
    private long id;
    private String postdata;
    private BasicInfo basicInfo;
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    public String getPostdata()
    {
        return postdata;
    }
    
    public void setPostdata(String postdata)
    {
        this.postdata = postdata;
    }
    
    public BasicInfo getBasicInfo()
    {
        return basicInfo;
    }
    
    public void setBasicInfo(BasicInfo basicInfo)
    {
        this.basicInfo = basicInfo;
    }
}
