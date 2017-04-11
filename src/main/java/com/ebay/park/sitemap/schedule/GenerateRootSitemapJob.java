package com.ebay.park.sitemap.schedule;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.SitemapFileDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.ChangeFreqDescription;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.persistence.FileSystemPublishException;
import com.ebay.park.persistence.XMLPersister;
import com.ebay.park.sitemap.SitemapComponentBuilder;
import com.ebay.park.sitemap.SitemapFileHelper;
import com.ebay.park.sitemap.marshalling.Marshaller;
import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URLSet;
import com.ebay.park.sitemap.model.URLSetBuilder;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.ParkConstants;
import com.ebay.park.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Scheduled Job that generates the root sitemap file.
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 *
 * @author Julieta Salvadó
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class GenerateRootSitemapJob {

    private static Logger logger = LoggerFactory.getLogger(GenerateRootSitemapJob.class);

    private static final String LANG = "es";

    @Value("${sitemap.users.priority}")
    private double usersPriority;

    @Value("${sitemap.items.priority}")
    private double itemsPriority;

    /**
     * Maximum number of elements in each file.
     */
    @Value("${sitemap.length}")
    private int sitemapLength;

    /**
     * Filename for the root sitemap.
     */
    @Value("${sitemap.root.file.name}")
    private String sitemapRootFileName;

    /**
     * Filename prefix for the users file.
     * It actual name will have an additional number.
     */
    @Value("${sitemap.users.file.name}")
    private String sitemapUsersFileName;

    /**
     * Filename prefix for the items file.
     * It actual name will have an additional number.
     */
    @Value("${sitemap.items.file.name}")
    private String sitemapItemsFileName;

    @Value("${sitemap.locations.categories.file.name}")
    private String sitemapLocationsCategoriesFileName;

    @Value("${sitemap.file.extension}")
    private String sitemapFileExtension;

    @Value("${sitemap.url.nested.host}")
    private String sitemapHost;

    @Value("${sitemap.url.scheme}")
    private String scheme;

    @Autowired
    @Qualifier("rootFileXMLPersisterFileSystem")
    private XMLPersister xMLPersister;

    @Autowired
    @Qualifier("nestedFileXMLPersisterFileSystem")
    private XMLPersister nestedXMLPersister;

    @Autowired
    @Qualifier("JAXBMarshaller")
    private Marshaller marshaller;

    @Autowired
    private InternationalizationUtil i18nUtil;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TextUtils textUtils;

    @Autowired
    private SitemapFileDao sitemapFileDao;

    @Autowired
    private SitemapFileHelper sitemapFileHelper;

    @Autowired
    private URLSetBuilder uRLSetBuilder;

    @Transactional
    @Scheduled(cron = "${scheduler.sitemap.cron.root}")
    public void execute() {
        logger.info("sitemap.xml generation is about to start now...");
        //get internal files
        //When refactoring, move this behaviour out of the job
        regenerateUsersXML();

        //When refactoring, move this behaviour out of the job
        regenerateItemsXML();

        //generate the root file
        SitemapRoot sitemapRoot = SitemapComponentBuilder.generateRootSitemap(getExistingURLSets());
        String fileContent = marshaller.marshall(sitemapRoot);
        try {
            xMLPersister.publishXML(fileContent, String.format("%s.%s", sitemapRootFileName, sitemapFileExtension));
        } catch (IOException e) {
            throw new FileSystemPublishException(e);
        } finally {
            logger.info("sitemap.xml generation is done.");
        }
    }

    private void regenerateItemsXML() {
        boolean itemStop = false;
        sitemapFileHelper.deletePreviousFileNames(sitemapItemsFileName);
        int itemPage = 0;
        while (!itemStop) {
            List<String> itemsURLList = getItemsURLList(itemPage, sitemapLength);
            if (!itemsURLList.isEmpty()) {
                URLSet itemFile = SitemapComponentBuilder.generateURLSet(itemsURLList,
                        ChangeFreqDescription.DAILY, itemsPriority);
                String itemFileName = String.format("%s-%d.%s", sitemapItemsFileName, itemPage + 1, sitemapFileExtension);
                String fileContent = marshaller.marshall(itemFile);
                try {
                    nestedXMLPersister.publishXML(fileContent, itemFileName);
                } catch (IOException e) {
                    throw new FileSystemPublishException(e);
                }
                uRLSetBuilder.updateSitemapFiles(itemFileName);
                itemPage++;
            } else {
                itemStop = true;
            }
        }
    }

    private void regenerateUsersXML() {
        boolean userStop = false;
        sitemapFileHelper.deletePreviousFileNames(sitemapUsersFileName);
        int userPage = 0;
        while (!userStop) {
            List<String> usersURLList = getUsersURLList(userPage, sitemapLength);
            if (!usersURLList.isEmpty()) {
                URLSet userFile = SitemapComponentBuilder.generateURLSet(usersURLList, ChangeFreqDescription.DAILY, usersPriority);
                String userFileName = String.format("%s-%d.%s", sitemapUsersFileName, userPage + 1, sitemapFileExtension);
                uRLSetBuilder.updateSitemapFiles(userFileName);
                String fileContent = marshaller.marshall(userFile);
                try {
                    nestedXMLPersister.publishXML(fileContent, userFileName);
                } catch (IOException e) {
                    throw new FileSystemPublishException(e);
                }
                userPage++;
            } else {
                userStop = true;
            }
        }
    }

    //When refactoring, move this behaviour out of the job
    private List<String> getExistingURLSets() {
        return sitemapFileDao.findAll().stream()
                .map(sitemapFile -> {
                    UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme(scheme)
                    .host(sitemapHost)
                    .path(sitemapFile.getName()).build();
                    return uri.toString();
                    })
                .collect(Collectors.toList());
    }

    //When refactoring, move this behaviour out of the job
    private List<String> getItemsURLList(int page, int pageSize) {
        Stream<Item> stream = StreamSupport.stream(itemDao.findAllByStatus(StatusDescription.ACTIVE,
                new PageRequest(page, pageSize)).spliterator(), false);
        return stream
                .map(item -> textUtils.createItemSEOURL(
                        i18nUtil.internationalize(item.getCategory(), LANG).getName(),
                        item.getName(),
                        item.getId()))
                .collect(Collectors.toList());
    }
    
    //When refactoring, move this behaviour out of the job
    private List<String> getUsersURLList(int page, int pageSize) {
        Stream<User> stream = StreamSupport.stream(userDao.findAll(new PageRequest(page, pageSize)).spliterator(), false);
        return stream
                .map(user -> textUtils.createProfileSEOURL(user.getUsername()))
                .collect(Collectors.toList());
    }
}
