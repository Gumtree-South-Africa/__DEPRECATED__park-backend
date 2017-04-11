package com.ebay.park.persistence;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test for {@link OldSitemapFiles}
 * @author Julieta Salvadó
 */
public class OldSitemapFilesTest {
    //FIXME: use Powermockito to avoid directory creation

    private static final String INVALID_STRING_PATH = "$$%& / 54";
    private static final String VALID_STRING_PATH = "valid";
    private static final String VALID_STRING_PATH2 = "anothervalid";
    private static final String VALID_CHILD_STRING_PATH2 = "anothervalid/file.txt" ;
    private static final String VALID_STRING_PATH3 = "newvalid";
    private static final String VALID_CHILD_STRING_PATH3 = "newvalid/file.txt" ;
    private static final String VALID_STRING_PATH4 = "isvalid";

    @After
    public void delete() throws IOException {
        FileUtils.deleteDirectory(new File(VALID_STRING_PATH));
        FileUtils.deleteDirectory(new File(VALID_STRING_PATH2));
        FileUtils.deleteDirectory(new File(VALID_STRING_PATH3));
        FileUtils.deleteDirectory(new File(VALID_STRING_PATH4));
    }

    @Test (expected = NoSuchFileException.class)
    @Ignore
    public void givenErrorWhenAskingEmptyThenException() throws IOException {

        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", INVALID_STRING_PATH);
        tested.init();
        tested.isEmpty();
    }

    @Test
    public void givenEmptyDirectoryWhenAskingEmptyThenTrue() throws IOException {
        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", VALID_STRING_PATH);
        Path emptyPath = get(VALID_STRING_PATH);
        Files.createDirectories(emptyPath);
        tested.init();
        assertTrue(tested.isEmpty());
    }

    @Test
    public void givenNotEmptyDirectoryWhenAskingEmptyThenFalse() throws IOException {
        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", VALID_STRING_PATH2);
        Path childPath = get(VALID_CHILD_STRING_PATH2);
        Files.createDirectories(childPath);
        tested.init();
        assertFalse(tested.isEmpty());
    }

    @Test
    public void givenNotEmptyDirectoryWhenDeletingContentThenDelete() throws IOException {
        //creates not empty directory
        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", VALID_STRING_PATH3);
        Path childPath = get(VALID_CHILD_STRING_PATH3);
        Files.createDirectories(childPath);
        tested.init();

        //when
        tested.deleteContent();

        //then
        assertFalse(tested.exists());
    }

    @Test (expected = NoSuchFileException.class)
    @Ignore
    public void givenNotSuchFileWhenAskingForEmptyThenException() throws IOException {
        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", INVALID_STRING_PATH);
        tested.init();
        tested.isEmpty();
    }

    @Test
    @Ignore
    public void givenNotExistingDirectoryWhenAskingIfExistsThenFalse() {
        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", INVALID_STRING_PATH);
        tested.init();
        assertFalse(tested.exists());
    }

    @Test
    public void givenExistingDirectoryWhenAskingIfExistsThenTrue() throws IOException {
        OldFiles tested = new OldSitemapFiles();
        ReflectionTestUtils.setField(tested, "oldDirectoryPath", VALID_STRING_PATH4);
        Path validPath = get(VALID_STRING_PATH4);
        Files.createDirectories(validPath);
        tested.init();
        assertTrue(tested.exists());
    }

}