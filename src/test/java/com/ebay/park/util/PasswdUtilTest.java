/*
 * Copyright eBay, 2014
 */
package com.ebay.park.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author juan.pizarro
 */
public class PasswdUtilTest {

  @Test
  public void testHashPasswordSuccessfully() {
    // given
    byte[] expectedHash =
        new byte[]{ 108, -95, 61, 82, -54, 112, -56, -125, -32, -16, -69, 16, 30, 66, 90, -119,
            -24, 98, 77, -27, 29, -78, -46, 57, 37, -109, -81, 106, -124, 17, -128, -112 };
    String passwordToHash = "abc123";

    
    // when
    byte[] actualHash = new PasswdUtil().hash(passwordToHash);

    System.out.println(new String(actualHash));
    
    // then
    assertArrayEquals(
        expectedHash, actualHash);
  }

  @Test
  public void testHashDifferectPasswordsSuccessfully() {
    // given
    byte[] expectedHash =
        new byte[]{ 108, -95, 61, 82, -54, 112, -56, -125, -32, -16, -69, 16, 30, 66, 90, -119,
            -24, 98, 77, -27, 29, -78, -46, 57, 37, -109, -81, 106, -124, 17, -128, -112 };
    String passwordToHash1 = "abc123";
    String passwordToHash2 = "abc321";
    PasswdUtil passwdUtil = new PasswdUtil();

    // when
    byte[] actualHash1 = passwdUtil.hash(passwordToHash1);
    byte[] actualHash2 = passwdUtil.hash(passwordToHash2);

    // then
    assertFalse("Expected hash and actual hash should be different", Arrays.equals(expectedHash, actualHash2));
    assertFalse("Actual hashes should be different", Arrays.equals(actualHash1, actualHash2));
  }
}
