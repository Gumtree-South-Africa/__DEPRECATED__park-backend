package com.ebay.park.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Unit test for {@link CurrentSitemapFiles}.
 * @author Julieta Salvadó
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CurrentSitemapFiles.class, Files.class})
public class CurrentSitemapFilesTest {

    @InjectMocks
    private CurrentSitemapFiles tested;

    private static final String VALID_STRING_PATH = "valid";

    @Before
    public void setUp(){
        initMocks(this);
        mockStatic(Files.class);
    }

    @Test
    public void givenEmptyDirectoryWhenAskingEmptyThenTrue() throws IOException {
        ReflectionTestUtils.setField(tested, "currentDirectoryPath", VALID_STRING_PATH);
        when(Files.newDirectoryStream(Paths.get(VALID_STRING_PATH))).thenReturn(getEmptyDirectoryStream());
        tested.init();
        assertTrue(tested.isEmpty());
    }

    @Test
    public void givenNotEmptyDirectoryWhenAskingEmptyThenFalse() throws IOException {
        ReflectionTestUtils.setField(tested, "currentDirectoryPath", VALID_STRING_PATH);
        when(Files.newDirectoryStream(Paths.get(VALID_STRING_PATH))).thenReturn(getNotEmptyDirectoryStream());
        tested.init();
        assertFalse(tested.isEmpty());

    }

    private DirectoryStream<Path> getNotEmptyDirectoryStream() {
        return new DirectoryStream<Path>() {
            @Override
            public Iterator<Path> iterator() {
                return new Iterator<Path>() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Path next() {
                        return null;
                    }
                };
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

    private DirectoryStream<Path> getEmptyDirectoryStream() {
        return new DirectoryStream<Path>() {
            @Override
            public Iterator<Path> iterator() {
                return new Iterator<Path>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Path next() {
                        return null;
                    }
                };
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

    @Test (expected = NullPointerException.class)
    public void givenNotSuchFileWhenAskingForEmptyThenException() throws IOException {
        ReflectionTestUtils.setField(tested, "currentDirectoryPath", VALID_STRING_PATH);
        tested.init();
        tested.isEmpty();
    }

    @Test
    public void givenNotExistingDirectoryWhenAskingIfExistsThenFalse() {
        //given
        ReflectionTestUtils.setField(tested, "currentDirectoryPath", VALID_STRING_PATH);
        tested.init();
        when(Files.exists(Paths.get(VALID_STRING_PATH))).thenReturn(false);

        //when & then
        assertFalse(tested.exists());
    }

    @Test
    public void givenExistingDirectoryWhenAskingIfExistsThenTrue() throws IOException {
        //given
        ReflectionTestUtils.setField(tested, "currentDirectoryPath", VALID_STRING_PATH);
        tested.init();
        when(Files.exists(Paths.get(VALID_STRING_PATH))).thenReturn(true);

        //when & then
        assertTrue(tested.exists());
    }
}