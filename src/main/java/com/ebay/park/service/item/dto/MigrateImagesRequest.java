package com.ebay.park.service.item.dto;

/**
 * Created by gabriel.sideri on 10/1/15.
 */
public class MigrateImagesRequest {

    private Long fromId;

    private Integer limit;

    public Long getFromId() {
        return (fromId == null ? 1 : fromId) ;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public int getLimit() {
        return (limit == null ? 1 : limit);
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("MigrateImagesRequest [fromId= ")
    		.append(this.fromId).append(", limit= ")
    		.append(this.limit).append("]");
    		
    return builder.toString();
    }
}
