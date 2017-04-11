package com.ebay.park.util;

import com.ebay.park.db.entity.Group;
import com.ebay.park.service.GeoPaginatedRequest;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Component;

import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;

/**
 * @author Julieta Salvadó
 */
@Component
public class QueryUtils {
    public QueryBuilder buildDistanceFilter(GeoPaginatedRequest request) {
        GeoDistanceQueryBuilder distanceFilter = geoDistanceQuery(Group.FIELD_LOCATION);
        distanceFilter.point(request.getLatitude(), request.getLongitude())
                .distance(request.getRadius(), DistanceUnit.MILES);
        return distanceFilter;
    }
}
