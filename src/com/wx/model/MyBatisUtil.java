package com.wx.model;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by JohnC on 2016-08-12.
 */
public class MyBatisUtil
{
    private static SqlSessionFactory factory;
    private static Reader reader;
    static
    {
        try
        {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(reader);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static SqlSessionFactory getFactory()
    {
        return factory;
    }
}
