package com.wx.model;

import java.util.List;

/**
 * Created by JohnC on 2016-08-12.
 */
public class BasicInfo
{
    private long id;
    private String sitename;
    private String title;
    private String forumname;
    private String datasourcetype;
    private String taskurl;
    private List<Article> articles;
    private String filePath;
    private String createtime;
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    public String getSitename()
    {
        return sitename;
    }
    
    public void setSitename(String sitename)
    {
        this.sitename = sitename;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getForumname()
    {
        return forumname;
    }
    
    public void setForumname(String forumname)
    {
        this.forumname = forumname;
    }
    
    public String getDatasourcetype()
    {
        return datasourcetype;
    }
    
    public void setDatasourcetype(String datasourcetype)
    {
        this.datasourcetype = datasourcetype;
    }
    
    public String getTaskurl()
    {
        return taskurl;
    }
    
    public void setTaskurl(String taskurl)
    {
        this.taskurl = taskurl;
    }
    
    public List<Article> getArticles()
    {
        return articles;
    }
    
    public void setArticles(List<Article> articles)
    {
        this.articles = articles;
    }
    
    public String getFilePath()
    {
        return filePath;
    }
    
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
    
    public String getCreatetime()
    {
        return createtime;
    }
    
    public void setCreatetime(String createtime)
    {
        this.createtime = createtime;
    }
}
