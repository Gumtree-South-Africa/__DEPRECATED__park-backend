package com.ebay.park.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Calendar;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

public class DataCommonUtilTest {

    private static final String DATE_31MAY2016 = "1464720119";
    private static final String DATE_31MAY2016MILLISECONDS = "1464720119203";
    private static final int YEAR_31MAY2016 = 2016;
    private static final int MINUTE_31MAY2016 = 41;
    private static final int HOUR_31MAY2016 = 15;
    private static final int DAY_31MAY2016 = 31;

    @Test
    @Ignore
  //FIXME the timezone difference makes this test to fail
    public void givenValidEntryWhenParsingThenParse() {
        Date date = DataCommonUtil.parseUnixTime(DATE_31MAY2016);

        assertNotNull(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        assertEquals(Calendar.MAY, cal.get(Calendar.MONTH));
        assertEquals(DAY_31MAY2016, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(HOUR_31MAY2016, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(MINUTE_31MAY2016, cal.get(Calendar.MINUTE));
        assertEquals(YEAR_31MAY2016, cal.get(Calendar.YEAR));
    }

    @Test
    @Ignore
    //FIXME the timezone difference makes this test to fail
    public void givenEntryWithMillisecondsWhenParsingThenParse() {
        Date date = DataCommonUtil.parseUnixTime(DATE_31MAY2016MILLISECONDS);

        assertNotNull(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        assertEquals(Calendar.MAY, cal.get(Calendar.MONTH));
        assertEquals(DAY_31MAY2016, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(HOUR_31MAY2016, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(MINUTE_31MAY2016, cal.get(Calendar.MINUTE));
        assertEquals(YEAR_31MAY2016, cal.get(Calendar.YEAR));
    }
}
