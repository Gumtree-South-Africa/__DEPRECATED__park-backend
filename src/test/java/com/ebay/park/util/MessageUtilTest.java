package com.ebay.park.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.context.MessageSource;

public class MessageUtilTest {

    private static final String SPANISH = "es";
    private static final String TEXT = "some words";
    private static final String INVALID_LANG = "ch";
    private static final String TRANSLATION_ES = "algunas palabras";
    private static final String ENGLISH = "en";
    private static final String TRANSLATION_EN = "some words";

    @InjectMocks
    @Spy
    private MessageUtil util;

    @Mock
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        initMocks(this);
        when(messageSource.getMessage(TEXT, null, Locale.forLanguageTag(SPANISH))).thenReturn(TRANSLATION_ES);
        when(messageSource.getMessage(TEXT, null, Locale.forLanguageTag(ENGLISH))).thenReturn(TRANSLATION_EN);
        when(messageSource.getMessage(TEXT, null, Locale.forLanguageTag(SPANISH))).thenReturn(TRANSLATION_ES);
    }
    
    @Test
    public void givenESLanguageWhenFormattingThenInternationalize() {
        assertEquals(util.formatMessage(TEXT, null, SPANISH), TRANSLATION_ES);
    }

    @Test
    public void givenENLanguageWhenFormattingThenInternationalize() {
        assertEquals(util.formatMessage(TEXT, null, ENGLISH), TRANSLATION_EN);
    }

    @Test
    public void givenInvalidLanguageWhenFormattingThenUseDefault() {
        assertEquals(util.formatMessage(TEXT, null, INVALID_LANG), TRANSLATION_ES);
    }

    @Test
    public void givenNullLanguageWhenFormattingThenUseDefault() {
        assertEquals(util.formatMessage(TEXT, null, null), TRANSLATION_ES);
    }

    @Test
    public void givenESLanguageWhenFormatting2ThenInternationalize() {
        assertEquals(util.formatMessageWithProps(TEXT, null, SPANISH), TRANSLATION_ES);
    }

    @Test
    public void givenENLanguageWhenFormatting2ThenInternationalize() {
        assertEquals(util.formatMessageWithProps(TEXT, null, ENGLISH), TRANSLATION_EN);
    }

    @Test
    public void givenInvalidLanguageWhenFormatting2ThenUseDefault() {
        assertEquals(util.formatMessageWithProps(TEXT, null, INVALID_LANG), TRANSLATION_ES);
    }

    @Test
    public void givenNullLanguageWhenFormatting2ThenUseDefault() {
        assertEquals(util.formatMessageWithProps(TEXT, null, null), TRANSLATION_ES);
    }
}
