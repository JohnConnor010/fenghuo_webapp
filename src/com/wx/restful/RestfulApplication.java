package com.wx.restful;

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.sun.xml.internal.bind.v2.schemagen.episode.Package;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by JohnC on 2016-08-12.
 */
@ApplicationPath("restful")
public class RestfulApplication extends ResourceConfig
{
    public RestfulApplication()
    {
        packages(FenghuoService.class.getPackage().getName());
        register(JacksonJsonProvider.class);
    }
}
