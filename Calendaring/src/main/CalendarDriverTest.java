package main;


import static org.junit.Assert.*;
import static java.lang.System.in;
import static java.lang.System.setIn;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import main.CalendarDriver;
import main.Event;
import java.util.Scanner;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CalendarDriverTest {

  @Rule
  public TextFromStandardInputStream systenInMock = emptyStandardInputStream();
  
  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testIsValidDateStr() {
    String date;
    
    //test a blank date
    date = "";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a date that is negative
    date = "-11";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a date that isn't numbers
    date = "date";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a date with too many fields
    date ="123456789";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a date with an invalid year
    date = "00001010";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a date with an invalid month
    date = "20152020";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a date with an invalid day
    date = "20150499";
    assertFalse(CalendarDriver.isValidDateStr(date));
    
    //test a valid date
    date = "20150404";
    assertTrue(CalendarDriver.isValidDateStr(date));
    
  }


  @Test
  public void testIsValidUnitStr() {
    String unit;
    
    //test an empty unit
    unit = "";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    //test a negative unit
    unit = "-1";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    //test a unit that isn't a number
    unit = "unit";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    //test a unit with an invalid length
    unit = "123";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    //test a unit with decimals
    unit = "1.7";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    //test each unit with an invalid value
    unit = "90";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    //test each unit with a valid value
    unit = "22";
    assertTrue(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertTrue(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertTrue(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
    
    unit = "55";
    assertFalse(CalendarDriver.isValidUnitStr(CalendarDriver.HOURS, unit));
    assertTrue(CalendarDriver.isValidUnitStr(CalendarDriver.MINS, unit));
    assertTrue(CalendarDriver.isValidUnitStr(CalendarDriver.SECS, unit));
  }

  @Test
  public void testCalculateFreeTimeSlots() {
    HashMap<String,String> timeSlots = null;
    ArrayList<Event> events = null;
    HashMap <String,String> expectedResult = null;
    
    Event event1;
    Event event2;
    Event event3;
    
    //test that program does not crash on null values
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events, false);
    
    //test when there are no events
    timeSlots = new HashMap<String,String>();
    timeSlots.put("000000", "235959");
    
    events = new ArrayList<>();
    
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events, false);
    
    expectedResult = new HashMap<>();
    expectedResult.put("000000","235959");
    
    assertEquals(expectedResult,timeSlots);
    
    //test with 1 event in between the day
    timeSlots = new HashMap<String,String>();
    timeSlots.put("000000", "235959");
    
    events = new ArrayList<>();
    event1 = new Event();
    event1.setStartTime("050000");
    event1.setEndTime("070000");
    events.add(event1);
    
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events, false);
    
    expectedResult = new HashMap<>();
    expectedResult.put("000000","050000");
    expectedResult.put("070000", "235959");
    
    assertEquals(expectedResult,timeSlots);
    
    //test with 2 event in between the day
    timeSlots = new HashMap<String,String>();
    timeSlots.put("000000", "235959");
    
    events = new ArrayList<>();
    event1 = new Event();
    event1.setStartTime("050000");
    event1.setEndTime("070000");
    events.add(event1);
    
    event2 = new Event();
    event2.setStartTime("123000");
    event2.setEndTime("153000");
    events.add(event2);
    
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events, false);
    
    expectedResult = new HashMap<>();
    expectedResult.put("000000","050000");
    expectedResult.put("070000", "123000");
    expectedResult.put("153000", "235959");
    
    assertEquals(expectedResult,timeSlots);
    
  //test with 2 overlapping event in between the day
    timeSlots = new HashMap<String,String>();
    timeSlots.put("000000", "235959");
    
    events = new ArrayList<>();
    event1 = new Event();
    event1.setStartTime("050000");
    event1.setEndTime("130000");
    events.add(event1);
    
    event2 = new Event();
    event2.setStartTime("123000");
    event2.setEndTime("153000");
    events.add(event2);
    
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events, false);
    
    expectedResult = new HashMap<>();
    expectedResult.put("000000","050000");
    expectedResult.put("153000", "235959");
    
    assertEquals(expectedResult,timeSlots);
    
    //test with 2 event that, in total, span the whole day
    timeSlots = new HashMap<String,String>();
    timeSlots.put("000000", "235959");
    
    events = new ArrayList<>();
    event1 = new Event();
    event1.setStartTime("000000");
    event1.setEndTime("070000");
    events.add(event1);
    
    event2 = new Event();
    event2.setStartTime("030000");
    event2.setEndTime("235959");
    events.add(event2);
    
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events, false);
    
    expectedResult = new HashMap<>();
    
    assertEquals(expectedResult,timeSlots);
    
    //test with 3 event that have a starting time and another ending time at midnight
    timeSlots = new HashMap<String,String>();
    timeSlots.put("000000", "235959");
    
    events = new ArrayList<>();
    event1 = new Event();
    event1.setStartTime("000000");
    event1.setEndTime("043000");
    events.add(event1);
    
    event2 = new Event();
    event2.setStartTime("090000");
    event2.setEndTime("153000");
    events.add(event2);
    
    event3 = new Event();
    event3.setStartTime("114500");
    event3.setEndTime("235959");
    events.add(event3);
    CalendarDriver.calculateFreeTimeSlots(timeSlots, events,false);
    
    expectedResult = new HashMap<>();
    expectedResult.put("043000","090000");
    
    assertEquals(expectedResult,timeSlots);
  }
 
  @Test
  public void testVersion()
  {
    //checks for a returned 2.0 string if option 2 is chosen.
    SystemInMock.provideText("2\n");
    String expectedResult = "2.0";
    assertEquals(expectedResult,CalendarDriver.getVersion());
    
    //enters invalid input(1). should loop back, then receives valid input (2)
    SystemInMock.provideText("1\n2\n");
    expectedResult = "2.0";
    assertEquals(expectedResult,CalendarDriver.getVersion());
  
    // not sure how to test for a input loop in the case of non-valid inputs
  }
 
  @Test
  public void testgetTZID()
  {
    SystemInMock.provideText("America\nHawaii\n");
    String expectedResult = "America/Hawaii";
    assertEquals(expectedResult,CalendarDriver.getTZID());
  }
  
  @Test
  public void testgetClassification()
  {
    SystemInMock.provideText("1\n");
    String expectedResult = "PUBLIC";
    assertEquals(expectedResult,CalendarDriver.getClassification());
    
    SystemInMock.provideText("2\n");
    expectedResult = "PRIVATE";
    assertEquals(expectedResult,CalendarDriver.getClassification());
    
    SystemInMock.provideText("3\n");
    expectedResult = "CONFIDENTIAL";
    assertEquals(expectedResult,CalendarDriver.getClassification());
    
    //test wrong int, should get looped back, then '1' is entered for valid input.
    SystemInMock.provideText("5\n1\n");
    expectedResult = "PUBLIC";
    assertEquals(expectedResult,CalendarDriver.getClassification());
    
  //test invalid input, should get looped back, then '1' is entered for valid input.
    SystemInMock.provideText("x\n1\n");
    expectedResult = "PUBLIC";
    assertEquals(expectedResult,CalendarDriver.getClassification());
    
  }
  
  @Test
  public void testgetLocation()
  {
    SystemInMock.provideText("At home\n");
    String expectedResult = "At home";
    assertEquals(expectedResult,CalendarDriver.getLocation());
  }
  
  @Test
  public void testgetPriority()
  {
    SystemInMock.provideText("1\n");
    String expectedResult = "1";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("2\n");
    expectedResult = "2";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("3\n");
    expectedResult = "3";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("4\n");
    expectedResult = "4";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("5\n");
    expectedResult = "5";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("6\n");
    expectedResult = "6";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("7\n");
    expectedResult = "7";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("8\n");
    expectedResult = "8";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    SystemInMock.provideText("9\n");
    expectedResult = "9";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
    //test invalid input(x), reloops and takes valid input(2)
    SystemInMock.provideText("x\n2\n");
    expectedResult = "2";
    assertEquals(expectedResult,CalendarDriver.getPriority());
    
  //test invalid input(45), reloops and takes valid input(2)
    SystemInMock.provideText("45\n2\n");
    expectedResult = "2";
    assertEquals(expectedResult,CalendarDriver.getPriority());
  }

  @Test
  public void testgetSummary()
  {
    SystemInMock.provideText("this is a test of the summary.\n");
    String expectedResult = "this is a test of the summary.";
    assertEquals(expectedResult,CalendarDriver.getSummary());
  }
  
  @Test
  public void testgetDTStart()
  {
    SystemInMock.provideText("20150505\n10\n10\n10\n");
    String expectedResult = "20150505T101010";
    assertEquals(expectedResult,CalendarDriver.getDTStart());
    
    //enters invalid date first (earlier than today's date: 2011) before a valid entry.
    SystemInMock.provideText("20110505\n20150505\n10\n10\n10\n");
    expectedResult = "20150505T101010";
    assertEquals(expectedResult,CalendarDriver.getDTStart());
  }
  
  @Test
  public void testgetDTEnd()
  {
    SystemInMock.provideText("20150505\n12\n10\n10\n");
    String expectedResult = "20150505T121010";
    assertEquals(expectedResult,CalendarDriver.getDTEnd(20150505,101010));
    
    //provides a time earlier than startTime, causes invalid input and should ask
    //for new parameters.
    SystemInMock.provideText("20150505\n09\n10\n10\n20150505\n12\n10\n10\n");
    expectedResult = "20150505T121010";
    assertEquals(expectedResult,CalendarDriver.getDTEnd(20150505,101010));
    
    
  }
   
  
}
