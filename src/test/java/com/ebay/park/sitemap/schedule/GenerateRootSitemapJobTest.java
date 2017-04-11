package com.ebay.park.sitemap.schedule;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import com.ebay.park.persistence.NestedFileXMLPersisterFileSystem;
import com.ebay.park.persistence.RootFileXMLPersisterFileSystem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.SitemapFileDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.sitemap.SitemapFileHelper;
import com.ebay.park.sitemap.marshalling.JAXBMarshaller;
import com.ebay.park.util.TextUtils;

/**
 * Unit test for {@link GenerateRootSitemapJob}.
 * @author Julieta Salvadó
 */
public class GenerateRootSitemapJobTest {
    private static final int LENGTH = 3;

    private static final String FILE_NAME = "sitemap";
    private static final String EXTENSION = "xml";

    @InjectMocks
    @Spy
    private GenerateRootSitemapJob job = new GenerateRootSitemapJob();

    @Mock
    private ItemDao itemDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TextUtils textUtils;

    @Mock
    private GenerateSitemapLocationsURLSetJob locationJob;

    @Mock
    private GenerateSitemapLocationsAndCategoriesURLSetJob locationAndCategoriesJob;

    @Mock
    private RootFileXMLPersisterFileSystem xMLPersister;

    @Mock
    private NestedFileXMLPersisterFileSystem nestedXMLPersister;

    @Mock
    private SitemapFileDao sitemapFileDao;

    @Mock
    private JAXBMarshaller marshaller;

    @Mock
    private SitemapFileHelper sitemapFileHelper;

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(job, "sitemapLength", LENGTH);
        ReflectionTestUtils.setField(job, "sitemapRootFileName", FILE_NAME);
        ReflectionTestUtils.setField(job, "sitemapUsersFileName", FILE_NAME);
        ReflectionTestUtils.setField(job, "sitemapItemsFileName", FILE_NAME);
        ReflectionTestUtils.setField(job, "sitemapFileExtension", EXTENSION);
    }

    @Test
    public void givenEmptyElementswhenExecutingThenUploadSitemapFile() throws JAXBException, IOException {
        when(itemDao.findAllByStatus(StatusDescription.ACTIVE, new PageRequest(0, LENGTH)))
            .thenReturn(new PageImpl<Item>(new ArrayList<Item>()));
        when(userDao.findAll(new PageRequest(0, LENGTH)))
            .thenReturn(new PageImpl<User>(new ArrayList<User>()));

        job.execute();
        verify(xMLPersister).publishXML(anyString(), anyString());
    }

    //TODO add additional cases

}
