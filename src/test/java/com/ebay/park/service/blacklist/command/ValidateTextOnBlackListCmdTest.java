package com.ebay.park.service.blacklist.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.BlackListDao;
import com.ebay.park.db.entity.Item;

/**
 * Considering the phrase 'despedida de soltera' in the blacklist, the following behavior is implemented:
a) when item name or description is 'despedida de soltera', this item goes to PENDING state
b) when item name or description is 'despedidadesoltera', this item goes to ACTIVE state
c) when item name or description is 'despedida dee soltera', this item goes to ACTIVE state
d) when item name or description is 'la despedida de soltera', this item goes to PENDING state
e) when item name or description is 'despedida tremenda de soltera', this item goes to PENDING state
f) when item name or description is 'despedida de soltera!', this item goes to ACTIVE state
g) when item name or description is 'despedida de', this item goes to ACTIVE state
h) when item name or description is 'de soltera', this item goes to ACTIVE state
i) when item name or description is 'despedid@ de soltera', this item goes to ACTIVE state
j) when item name or description is 'despedida de [whitespace] soltera', this item goes to PENDING state
k) when item name or description is 'despedida de [new line] soltera', this item goes to PENDING state
 * @author Julieta Salvadó
 *
 */
public class ValidateTextOnBlackListCmdTest {

	private static final boolean SHAREFACEBOOK = false;
	private static final boolean SHARETWITTER = false;
	private static final String VERSIONPUBLISH = "1";
	private static final double PRICE = 2.0;
    private static final String VALID_DESCRIPTION = "description";
    private static final boolean IS_BLACKLISTED = true;
    private static final String VALID_NAME = "name";
	
	@InjectMocks
	private ValidateTextOnBlackListCmd cmd;
	
	@Mock
	private BlackListDao blackListDao;
	
	private List<LinkedList<String>> index = new LinkedList<LinkedList<String>>();
	
	@Before
    public void setUp(){
        initMocks(this);
        initIndex();
        ReflectionTestUtils.setField(cmd, "index", index);
    }
	
	private void initIndex() {
	    LinkedList<String> phrase1 = new LinkedList<String>() {
            private static final long serialVersionUID = 1L;
            {
	            add("despedida");
	            add("de");
	            add("soltera");
	        }
	    };
	    LinkedList<String> phrase2 = new LinkedList<String>() {
            private static final long serialVersionUID = 1L;

            {
	            add("sex");
	        }
	    };
	    LinkedList<String> phrase3 = new LinkedList<String>() {
            private static final long serialVersionUID = 1L;

            {
                add("viagra");
            }
        };
	            
        index.add(phrase1);
        index.add(phrase2);
        index.add(phrase3);
    }

	//Phrase Cases
	
	/**
	 * Situation a)
	 */
	@Test
    public void testBlacklistedCaseItemNameFullPhrase() {
        Item item1 = createItem("despedida de soltera", VALID_DESCRIPTION);
        boolean response = cmd.execute(item1);
        assertEquals(IS_BLACKLISTED, response);
    }
	
	/**
     * Situation b)
     */
	 @Test
	 public void testBlacklistedCaseItemNamePhraseB() {
	     Item item1 = createItem("despedidadesoltera", VALID_DESCRIPTION);
	     boolean response = cmd.execute(item1);
	     assertEquals(!IS_BLACKLISTED, response);
	 }
	 
	 /**
	  * Situation c)
	  */
	 @Test
     public void testBlacklistedCaseItemNamePhraseC() {
         Item item1 = createItem("despedida dee soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(!IS_BLACKLISTED, response);
     }
	 
	 /**
      * Situation d)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseD() {
         Item item1 = createItem("la despedida de soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(IS_BLACKLISTED, response);
     }
     
     /**
      * Situation e)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseE() {
         Item item1 = createItem("despedida tremenda de soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(IS_BLACKLISTED, response);
     }
     
     /**
      * Situation f)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseF() {
         Item item1 = createItem("despedida de soltera!", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(!IS_BLACKLISTED, response);
     }
     
     /**
      * Situation g)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseG() {
         Item item1 = createItem("despedida tremenda de soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(IS_BLACKLISTED, response);
     }
     
     /**
      * Situation h)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseH() {
         Item item1 = createItem("de soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(!IS_BLACKLISTED, response);
     }
     
     /**
      * Situation i)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseI() {
         Item item1 = createItem("despedid@ de soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(!IS_BLACKLISTED, response);
     }
     
     /**
      * Situation j)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseJ() {
         Item item1 = createItem("despedida de            soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(IS_BLACKLISTED, response);
     }
     
     /**
      * Situation k)
      */
     @Test
     public void testBlacklistedCaseItemNamePhraseK() {
         Item item1 = createItem("despedida tremenda\nde soltera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(IS_BLACKLISTED, response);
     }
	 
	 
	 @Test
     public void testBlacklistedCaseItemNameUppercase() {
         Item item1 = createItem("despediDA dE solTera", VALID_DESCRIPTION);
         boolean response = cmd.execute(item1);
         assertEquals(IS_BLACKLISTED, response);
     }
    
    @Test
    public void testBlacklistedCaseItemDescription() {
        Item item1 = createItem(VALID_NAME, "despedida de soltera");
        boolean response = cmd.execute(item1);
        assertEquals(IS_BLACKLISTED, response);
    }
    
    // Word cases
    @Test
    public void testBlacklistedCaseItemDescriptionWord() {
        Item item1 = createItem(VALID_NAME, "viagra");
        boolean response = cmd.execute(item1);
        assertEquals(IS_BLACKLISTED, response);
    }
    
    @Test
    public void testBlacklistedCaseItemNameWord() {
        Item item1 = createItem("viagra", VALID_DESCRIPTION);
        boolean response = cmd.execute(item1);
        assertEquals(IS_BLACKLISTED, response);
    }
	
	private Item createItem(String name, String description) {
		Item item = new Item(name, PRICE, VERSIONPUBLISH, SHAREFACEBOOK, SHARETWITTER);
		item.setDescription(description);
		return item;
	}

	@Test
	public void testActiveCases() {
	    Item item1 = createItem(VALID_NAME, VALID_DESCRIPTION);
        boolean response = cmd.execute(item1);
        assertEquals(!IS_BLACKLISTED, response);
	}
	
	@Test
    public void testIndexGeneration() {
	    when(blackListDao.findAllBlackListDescription()).thenReturn(Arrays.asList("despedida de soltera", "sex", "viagra"));

	    cmd.generateBlacklistIndex();
	    
	    verify(blackListDao).findAllBlackListDescription();
	}
	
}
