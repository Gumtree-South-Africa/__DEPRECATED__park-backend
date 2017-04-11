package com.ebay.park.elasticsearch.document.nested;

import com.ebay.park.elasticsearch.document.UserDocument;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * @author l.marino on 6/19/15.
 */
public class ESNUser {
    private Long id;
    @MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "default_analyzer", searchAnalyzer = "default_analyzer"),
            otherFields = {
                    @InnerField(suffix = UserDocument.FIELD_SUFFIX_NGRAMS, type = FieldType.String, indexAnalyzer =
                            "ngram_analyzer", searchAnalyzer = "ngram_analyzer")})
    private String username;
    private Boolean emailVerified;
    private Boolean mobileVerified;

   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ESNUser() {
    }

    public Boolean getEmailVerified() {
    	return emailVerified;
    }
    
    public void setMailVerified(Boolean emailVerified) {
    	this.emailVerified = emailVerified;
    }
    
    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public ESNUser(Long id, String username, Boolean emailVerified, Boolean mobileVerified) {
        this.id = id;
        this.username = username;
        this.emailVerified = emailVerified;
        this.mobileVerified = mobileVerified;
    }

}