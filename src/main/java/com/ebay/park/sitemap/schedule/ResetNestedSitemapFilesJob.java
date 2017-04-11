package com.ebay.park.sitemap.schedule;

import com.ebay.park.persistence.CurrentSitemapFiles;
import com.ebay.park.persistence.OldSitemapFiles;
import com.ebay.park.util.ParkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Schedule Job that deletes the nested sitemap files.
 *
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 * @author Julieta Salvadó
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class ResetNestedSitemapFilesJob {

    private static Logger LOGGER = LoggerFactory.getLogger(ResetNestedSitemapFilesJob.class);

    @Autowired
    private OldSitemapFiles oldDirectory;

    @Autowired
    private CurrentSitemapFiles currentDirectory;

    @Transactional
    @Scheduled(cron = "${scheduler.sitemap.cron.reset}")
    public void execute() {
        LOGGER.info("ResetNestedSitemapFilesJob is about to start...");
        deleteOldFiles();
        prepareForNextSitemapGeneration();
        LOGGER.info("ResetNestedSitemapFilesJob is done!");
    }

    private void prepareForNextSitemapGeneration() {
        currentDirectory.init();
        try {
            if (!currentDirectory.isEmpty()) {
                if (currentDirectory.exists()) {
                    currentDirectory.backup();
                }
                currentDirectory.createEmptyFolders();
            }
        } catch (IOException e) {
            LOGGER.warn("Error when preparing the directory for the next sitemap generation", e);
        }
    }

    private void deleteOldFiles() {
        oldDirectory.init();
        try {
            if (oldDirectory.exists() && !oldDirectory.isEmpty()) {
                oldDirectory.deleteContent();
            }
        } catch (IOException e) {
            LOGGER.warn("Error when deleting the backup sitemap directory", e);
        }
    }
}
