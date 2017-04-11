package com.ebay.park.elasticsearch.document;

import com.ebay.park.db.entity.*;
import com.ebay.park.util.LocationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link ItemDocument}.
 * @author Julieta Salvadó
 */
public class ItemDocumentTest {

    private static final double INVALID_LATITUDE = 91;
    private static final double INVALID_LONGITUDE = 181;
    private static final double VALID_LATITUDE = 80;
    private static final double VALID_LONGITUDE = 170;
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "Tandil";
    private static final String ITEM_NAME = "name";
    private static final String ZIPCODE = "7000";
    private static final long ID1 = 1L;
    private static final long ID2 = 2L;

    @Mock
    private Item item;

    @Mock
    private Category category;

    @Mock
    private User user;

    @Mock
    private LocationUtil locationUtil;

    @Before
    public void setup() {
        initMocks(this);
        when(item.getCategory()).thenReturn(category);
        when(item.getLongitude()).thenReturn(VALID_LONGITUDE);
        when(item.getLatitude()).thenReturn(VALID_LATITUDE);
        when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
        when(item.getPublishedBy()).thenReturn(mock(User.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullItemWhenConvertingFromItemToItemDocumentThenException() {
        new ItemDocument(null);
    }

    @Test
    public void givenInvalidLatitudeWhenConvertingFromItemToItemDocumentThenNullLocation() {
        when(item.getLatitude()).thenReturn(INVALID_LATITUDE);

        ItemDocument itemDocument = new ItemDocument(item);
        assertThat("Location must not be NOT copied", itemDocument.getLocation(), is(nullValue()));
    }

    @Test
    public void givenInvalidLongitudeWhenConvertingFromItemToItemDocumentThenNullLocation() {
        when(item.getLongitude()).thenReturn(INVALID_LONGITUDE);

        ItemDocument itemDocument = new ItemDocument(item);
        assertThat("Location must not be NOT copied", itemDocument.getLocation(), is(nullValue()));
    }

    @Test
    public void givenInvalidLongitudeAndLatitudeWhenConvertingFromItemToItemDocumentThenNullLocation() {
        when(item.getLatitude()).thenReturn(INVALID_LONGITUDE);
        when(item.getLatitude()).thenReturn(INVALID_LATITUDE);

        ItemDocument itemDocument = new ItemDocument(item);
        assertThat("Location must not be NOT copied", itemDocument.getLocation(), is(nullValue()));
    }


    @Test
    public void givenValidItemWhenConvertingFromItemToItemDocumentThenCreate() {
        when(item.getDescription()).thenReturn(DESCRIPTION);
        Date lastMod = new Date();
        when(item.getLastModificationDate()).thenReturn(lastMod);
        when(item.getLocation()).thenReturn(LOCATION);
        when(item.getLocationName()).thenReturn(LOCATION);
        when(item.getName()).thenReturn(ITEM_NAME);
        Date published = new Date();
        when(item.getPublished()).thenReturn(published);
        when(item.getZipCode()).thenReturn(ZIPCODE);

        ItemDocument itemDocument = new ItemDocument(item);

        assertThat("Location must be copied", itemDocument.getLocation(), is(notNullValue()));
        assertThat("Category must be copied", itemDocument.getCategory(), is(notNullValue()));
        assertThat("Description must be copied", itemDocument.getDescription(), is(DESCRIPTION));
        assertThat("Count of reports must be copied", itemDocument.getCountOfReports(), is(notNullValue()));
        assertThat("Deleted value be copied", itemDocument.getDeleted(), is(notNullValue()));
        assertThat("Id must be copied", itemDocument.getItemId(), is(notNullValue()));
        assertThat("Last modification date must be copied", itemDocument.getLastModificationDate(), is(lastMod));
        assertThat("Location name must be copied", itemDocument.getLocationName(), is(notNullValue()));
        assertThat("Item name must be copied", itemDocument.getName(), is(ITEM_NAME));
        assertThat("Pending moderation value must be copied", itemDocument.getPendingModeration(), is(notNullValue()));
        assertThat("Price must be copied", itemDocument.getPrice(), is(notNullValue()));
        assertThat("Published date must be copied", itemDocument.getPublished(), is(published));
        assertThat("Publisher must be copied", itemDocument.getPublishedBy(), is(notNullValue()));
        assertThat("Item status be copied", itemDocument.getStatus(), is(StatusDescription.ACTIVE.toString()));
        assertThat("Zipcode must be copied", itemDocument.getZipcode(), is(ZIPCODE));
    }

    @Test
    public void givenValidItemWithEmptyItemGroupsWhenConvertingFromItemToItemDocumentThenCreate() {
        when(item.getItemGroups()).thenReturn(new ArrayList<ItemGroup>());
        ItemDocument itemDocument = new ItemDocument(item);

        assertThat("Item group must be empty", itemDocument.getItemGroups().size(), is(0));
    }

    @Test
    public void givenValidItemWithItemGroupsWhenConvertingFromItemToItemDocumentThenCreate() {
        Group group = mock(Group.class);
        ItemGroup itemGroup1 = mock(ItemGroup.class);
        ItemGroup itemGroup2 = mock(ItemGroup.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        when(itemGroup1.getItem()).thenReturn(item1);
        when(itemGroup2.getItem()).thenReturn(item2);
        when(item1.getId()).thenReturn(ID1);
        when(item2.getId()).thenReturn(ID2);
        when(itemGroup1.getGroup()).thenReturn(group);
        when(itemGroup2.getGroup()).thenReturn(group);

        when(item.getItemGroups()).thenReturn(Arrays.asList(itemGroup1, itemGroup2));
        ItemDocument itemDocument = new ItemDocument(item);

        assertThat("Two item group must be copied", itemDocument.getItemGroups().size(), is(2));
    }
}