package com.ebay.park.db.entity;

/**
 * Enum for values in the changefreq field in the sitemap according
 * documentation.
 * @author Julieta Salvadó
 *
 */
public enum ChangeFreqDescription {
  //@formatter:off
    ALWAYS("ALWAYS"),
    HOURLY("HOURLY"),
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    YEARLY("YEARLY"),
    NEVER("NEVER");
    //@formatter:on
    
    private final String changefreq;

    private ChangeFreqDescription(String changefreq) {
        this.changefreq = changefreq;
    }

    @Override
    public String toString() {
        return changefreq;
    }
}
