package com.ebay.park.service.eps;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.item.dto.MigrateImagesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author l.marino on 7/13/15.
 */
@Service
public class EPSMigrationServiceImpl implements EPSMigrationService {

    @Autowired
    @Qualifier("migrateGroupsToEPSCmd")
    private ServiceCommand<MigrateImagesRequest, Void> migrateGroupsToEPSCmd;

    @Autowired
    @Qualifier("migrateProfilesToEPSCmd")
    private ServiceCommand<MigrateImagesRequest, Void> migrateProfilesToEPSCmd;

    @Autowired
    @Qualifier("migrateItemsToEPSCmd")
    private ServiceCommand<MigrateImagesRequest, Void> migrateItemsToEPSCmd;

    @Override
    @Async
    public void migrateGroupsToEps(MigrateImagesRequest request) {
         migrateGroupsToEPSCmd.execute(request);
    }

    @Override
    @Async
    public void migrateProfilesToEps(MigrateImagesRequest request) {
          migrateProfilesToEPSCmd.execute(request);
    }

    @Override
    @Async
    public void migrateItemsToEps(MigrateImagesRequest request) {
         migrateItemsToEPSCmd.execute(request);
    }

}
