package com.ebay.park.service.eps;

import com.ebay.park.service.item.dto.MigrateImagesRequest;

/**
 * @author l.marino on 7/13/15.
 */
public interface EPSMigrationService {

    public void migrateGroupsToEps(MigrateImagesRequest request);

    public void migrateProfilesToEps(MigrateImagesRequest request);

    public void migrateItemsToEps(MigrateImagesRequest request);

}
