package com.ebay.park.elasticsearch.document;

import com.ebay.park.db.entity.BlackList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Elasticsearch entity for {@link BlackList}.
 * @author l.marino on 6/5/15.
 */
@Document(indexName = "black_list", type = "black_list")
@Setting(settingPath = "com/ebay/park/elasticsearch/document/settings/BlackListDocument.json")
public class BlackListDocument implements Serializable {
    private static Logger LOGGER = LoggerFactory.getLogger(BlackListDocument.class);
	private static final long serialVersionUID            = 1L;
	public static final String FIELD_DESCRIPTION          = "description";
	public static final String FIELD_SUFFIX_NGRAMS        = "ngrams";
	public static final String FIELD_SUFFIX_SHINGLES      = "shingles";


	@Id
	private Long blackListId;

	@MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "default_analyzer", searchAnalyzer = "default_analyzer"),
			otherFields = {@InnerField(suffix = FIELD_SUFFIX_SHINGLES, type = FieldType.String, indexAnalyzer =
					"shingle_analyzer", searchAnalyzer = "shingle_analyzer"),
					@InnerField(suffix = FIELD_SUFFIX_NGRAMS, type = FieldType.String, indexAnalyzer =
							"ngram_analyzer", searchAnalyzer = "ngram_analyzer")})
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getBlackListId() {
		return blackListId;
	}

	public void setBlackListId(Long blackListId) {
		this.blackListId = blackListId;
	}

	public BlackListDocument() {
	}

	/**
	 * Generate a new Blacklist Document from a {@link BlackList} Entity.
	 * @param blackList
     *      original Blacklist Entity
	 */
	public BlackListDocument(BlackList blackList) {
		Assert.notNull(blackList, "blacklist must not be null");
		try {
			this.blackListId = blackList.getId();
            this.description = blackList.getDescription();
		} catch (Exception e) {
		    logException(e, blackList);
		}
	}

    private void logException(Exception e, BlackList blackList) {
        StringBuilder builder = new StringBuilder();
        builder.append("BlacklistDocument from Blacklist Conversion failed! ")
            .append(blackList == null ? "[Reason: null Blacklist" : "[Blacklist id " + blackList.getId())
			.append(" / ")
            .append("Blacklist description: ")
            .append(blackList.getDescription())
			.append("]");
        LOGGER.error(builder.toString(), e);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlacklistDocument [id=").append(blackListId)
				.append(", description=").append(description)
				.append("], ");
		return builder.toString();
	}
}
