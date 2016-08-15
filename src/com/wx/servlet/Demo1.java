package com.wx.servlet;

import com.wx.mapper.FenghuoMapper;
import com.wx.model.Article;
import com.wx.model.BasicInfo;
import com.wx.model.MyBatisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by JohnC on 2016-08-12.
 */
public class Demo1
{
    public static void main(String[] args)
    {
        SqlSession session = MyBatisUtil.getFactory().openSession();
        try
        {
            FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
            List<BasicInfo> basicInfos = mapper.findByCondition(0,100,"16","0","0");
            for (BasicInfo basicInfo : basicInfos)
            {
                System.out.println("标题：" + basicInfo.getTitle());
                List<Article> articles = basicInfo.getArticles();
                for (Article article : articles)
                {
                    System.out.println("内容：" + article.getPostdata());
                }
                System.out.println(StringUtils.repeat("-", 100));
            }
        }
        finally
        {
            session.close();
        }
    }
}
