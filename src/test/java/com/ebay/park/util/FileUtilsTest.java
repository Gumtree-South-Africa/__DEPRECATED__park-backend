package com.ebay.park.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.*;


/**
 * Unit Test for {@link FileUtils}
 * @author Julieta Salvadó
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, Files.class})
public class FileUtilsTest {
    private static final String PATH = "home/user/path";
    private static final String USERNAME = "username";
    private static final String ORIGINAL_FILE_NAME = "file";

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(Files.class);
    }
    @Test (expected = IllegalArgumentException.class)
    public void givenNullPathWhenPreparingDirectoryThenException() throws IOException {
        FileUtils.prepareDirectory(null);
    }

    @Test
    public void givenAnExistingDirectoryWhenPreparingDirectoryThenDirectoryKeepsUntouched() throws IOException {
        //given
        when(Files.exists(Paths.get(PATH))).thenReturn(true);
        //when
        FileUtils.prepareDirectory(PATH);
        //then
        assertTrue(Files.exists(Paths.get(PATH)));
        verifyStatic(times(1));
    }

    @Test
    public void givenNonExistingDirectoryWhenPreparingDirectoryThenDirectoryIsCreated() throws IOException {
        //given
        when(Files.exists(Paths.get(PATH))).thenReturn(false);
        //when
        FileUtils.prepareDirectory(PATH);
        //then
        assertFalse(Files.exists(Paths.get(PATH)));
        verifyStatic(times(1));
    }
}