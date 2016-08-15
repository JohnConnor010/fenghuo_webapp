package com.wx.restful;

import com.wx.mapper.FenghuoMapper;
import com.wx.model.Article;
import com.wx.model.BasicInfo;
import com.wx.model.DownloadZip;
import com.wx.model.MyBatisUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by JohnC on 2016-08-12.
 */
@Path("service")
public class FenghuoService
{
    @Context HttpServletRequest request;
    
    @GET
    @Path("pagination/{page}/{datasourcetype}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BasicInfo> doPagination(@PathParam("page") int page,
                                        @PathParam("datasourcetype") String datasourcetype,
                                        @PathParam("start") String start,
                                        @PathParam("end") String end)
    {
        List<BasicInfo> results = new ArrayList<>();
        SqlSession session = MyBatisUtil.getFactory().openSession();
        try
        {
            FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
            int limit = 10;
            int offset = (page - 1) * limit;
            results = mapper.findByCondition(offset,limit,datasourcetype,start,end);
            for (BasicInfo result : results)
            {
                result.setDatasourcetype(getTypeString(result.getDatasourcetype()));
            }
        }
        finally
        {
            session.close();
        }
        return results;
    }
    
    @GET
    @Path("pagination/countBy/{datasourcetype}/{start}/{end}")
    public int getCount(@PathParam("datasourcetype") String datasourcetype,
                        @PathParam("start") String start,
                        @PathParam("end") String end)
    {
        int result = 0;
        SqlSession session = MyBatisUtil.getFactory().openSession();
        try
        {
            FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
            int count = mapper.countByCondition(datasourcetype,start,end);
            result = (int)Math.ceil((double)count / 10);
        }
        finally
        {
            session.close();
        }
        return result;
    }
    
    @GET
    @Path("article/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> getArticleByBasicInfoBy(@PathParam("id") int id)
    {
        List<Article> results = new ArrayList<>();
        SqlSession session = MyBatisUtil.getFactory().openSession();
        try
        {
            FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
            results = mapper.findByBasicInfoId(id);
        }
        finally
        {
            session.close();
        }
    
        return results;
    }
    
    @POST
    @Path("create/zip")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createZIP(Map<String,Object> map)
    {
        String zipFilePath = "";
                
        int offset = Integer.valueOf(map.get("offset").toString());
        int limit = Integer.valueOf(map.get("limit").toString());
        String type = map.get("type").toString();
        String start = map.get("start").toString();
        String end = map.get("end").toString();
        long startTimestamp = 0l;
        if(!start.equals(""))
        {
            start = map.get("start").toString() + " 00:00:00";
            startTimestamp = Timestamp.valueOf(start).getTime() / 1000;
        }
        long endTimestamp = 0l;
        if(!end.equals(""))
        {
            end = map.get("end").toString() + " 00:00:00";
            endTimestamp = Timestamp.valueOf(end).getTime()/ 1000;
        }
        
        String savePath = request.getServletContext().getRealPath("") + "download";
        String saveDir = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        
        java.nio.file.Path path = Paths.get(savePath,saveDir);
        try
        {
            if (!Files.exists(path))
            {
                Files.createDirectories(path);
            }
            SqlSession session = MyBatisUtil.getFactory().openSession();
            try
            {
                FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
                List<BasicInfo> basicInfos = mapper.findByLimitAndTimestamp(offset,limit,type,String.valueOf(startTimestamp),String.valueOf(endTimestamp));
                for (BasicInfo basicInfo : basicInfos)
                {
                    String fileName = basicInfo.getFilePath().replace(".xml",".txt");
                    java.nio.file.Path filePath = Paths.get(path.toString(),fileName);
                    List<String> lines = new ArrayList<>();
                    lines.add("标题：" + basicInfo.getTitle());
                    List<Article> articles = basicInfo.getArticles();
                    for (Article article : articles)
                    {
                        lines.add("内容：" + article.getPostdata());
                    }
                    Files.write(filePath,lines,StandardCharsets.UTF_8);
                    System.out.println("文件：" + filePath.toString() + " 成功");
                }
                String archiveName = saveDir;
                java.nio.file.Path archiveFolder = Paths.get(savePath,"archive");
                if(!Files.exists(archiveFolder))
                {
                    Files.createDirectories(archiveFolder);
                }
                try
                {
                    File sourceFolder = path.toFile();
                    File destFolder = archiveFolder.toFile();
                    Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.ZIP);
                    archiver.create(archiveName,destFolder,sourceFolder);
                    if(Files.exists(path))
                    {
                        FileUtils.deleteDirectory(path.toFile());
                    }
                    zipFilePath = "./download/archive/" + saveDir + ".zip";
                    String startStr = start != "" ? start : "空";
                    String endStr = end != "" ? end : "空";
                    String title = "数据来源为“" + getTypeString(type)+ "”，起始时间为" + startStr + "和终止时间为" + endStr
                            + "，" + "第" + offset + "条值第" + limit + "条数据";
                    DownloadZip downloadZip = new DownloadZip();
                    downloadZip.setTitle(title);
                    downloadZip.setFileName(saveDir + ".zip");
                    downloadZip.setFilePath(zipFilePath);
                    mapper.insertDownloadZip(downloadZip);
                    session.commit();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
    
            }
            finally
            {
                session.close();
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return zipFilePath;
    }
    
    @GET
    @Path("zip/getall")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String,Object>> getZips()
    {
        List<Map<String,Object>> results = new ArrayList<>();
        SqlSession session = MyBatisUtil.getFactory().openSession();
        try
        {
            FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
            List<DownloadZip> zips = mapper.findAll();
            for (DownloadZip zip : zips)
            {
                Map<String,Object> map = new HashMap<>();
                map.put("id",zip.getId());
                map.put("title",zip.getTitle());
                map.put("fileName",zip.getFileName());
                map.put("action","<a href=\"" + zip.getFilePath() + "\" target=\"_blank\">下载</a>");
                results.add(map);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            session.close();
        }
        return results;
    
    }
    
    @POST
    @Path("zip/delete")
    public int deleteById(String ids)
    {
        int result = 0;
        SqlSession session = MyBatisUtil.getFactory().openSession();
        try
        {
            FenghuoMapper mapper = session.getMapper(FenghuoMapper.class);
            String downloadPath = request.getServletContext().getRealPath("");
            for(String id : ids.split(","))
            {
                String filePath = mapper.findDownloadZipById(Integer.valueOf(id)).getFilePath().replace("./","");
                result += mapper.deleteById(Integer.valueOf(id));
                java.nio.file.Path zipFilePath = Paths.get(downloadPath, filePath);
                if(Files.exists(zipFilePath))
                {
                    Files.delete(zipFilePath);
                }
                session.commit();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            session.close();
        }
        return result;
    }
    
    public String getTypeString(String type)
    {
        switch(type)
        {
            case "1":
                return "新闻";
            case "16":
                return "论坛";
            case "256":
                return "博客";
            case "65536":
                return "视频";
            case "1048576":
                return "微博";
            case "268435456":
                return "微信";
            case "0":
                return "所有";
            default:
                return "";
        }
    }
}
