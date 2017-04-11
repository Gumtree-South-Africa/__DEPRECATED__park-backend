package com.ebay.park.sitemap.marshalling;

import com.ebay.park.sitemap.model.Sitemap;
import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URLSet;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;

/**
 * It binds a JAVA object to a String that represent an xml file using JAXB.
 * @author Julieta Salvadó
 */
@Component
public class JAXBMarshaller implements Marshaller {

    private String marshall(JAXBContext context, Sitemap sitemapComponent) throws JAXBException {
        StringWriter writer = new StringWriter();
        javax.xml.bind.Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(sitemapComponent, writer);
        return writer.toString();
    }

    @Override
    public String marshall(URLSet urlSet) {
        Assert.notNull(urlSet, "URLSet object must not be null here");
        try {
            return marshall(JAXBContext.newInstance(URLSet.class), urlSet);
        } catch (JAXBException e) {
            throw new XMLBindingException(e);
        }
    }

    @Override
    public String marshall(SitemapRoot sitemapRoot) {
        Assert.notNull(sitemapRoot, "SitemapRoot object must not be null here");
        try {
            return marshall(JAXBContext.newInstance(SitemapRoot.class), sitemapRoot);
        } catch (JAXBException e) {
            throw new XMLBindingException(e);
        }
    }
}
