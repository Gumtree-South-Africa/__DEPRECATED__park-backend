package com.ebay.park.controller;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.eps.EPSMigrationService;
import com.ebay.park.service.item.dto.MigrateImagesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author l.marino on 7/13/15.
 */
@RestController
@RequestMapping("/migrationcdntoeps")
public class EPSMigrationController {
    private static final String MIGRATION_TOKEN = "migrationToken";

    @Value("${migration.cdn.to.eps}")
    private String tokenSecret;

    @Autowired
    private EPSMigrationService epsMigrationService;

    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse migrateGroups(
            @RequestHeader(MIGRATION_TOKEN) String indexToken,
            @RequestBody MigrateImagesRequest request){

        if (!tokenSecret.equals(indexToken)) {
            return new ServiceResponse(ServiceResponseStatus.FAIL, "Invalid Migration token", null);
        }

        epsMigrationService.migrateGroupsToEps(request);

        return new ServiceResponse(ServiceResponseStatus.SUCCESS, "EPS Group Migration started", true);

    }

    @RequestMapping(value = "/profiles", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse migrateProfiles(
            @RequestHeader(MIGRATION_TOKEN) String indexToken,
            @RequestBody MigrateImagesRequest request){

        if (!tokenSecret.equals(indexToken)) {
            return new ServiceResponse(ServiceResponseStatus.FAIL, "Invalid Migration token", null);
        }

        epsMigrationService.migrateProfilesToEps(request);
        return new ServiceResponse(ServiceResponseStatus.SUCCESS, "EPS Profiles Migration started", true);

    }

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse migrateItems(
            @RequestHeader(MIGRATION_TOKEN) String indexToken,
            @RequestBody MigrateImagesRequest request){

        if (!tokenSecret.equals(indexToken)) {
            return new ServiceResponse(ServiceResponseStatus.FAIL, "Invalid Migration token", null);
        }

        epsMigrationService.migrateItemsToEps(request);

        return new ServiceResponse(ServiceResponseStatus.SUCCESS, "EPS Items Migration started", true);

    }


}