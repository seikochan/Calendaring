package main;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import main.CalendarDriver;
import main.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CalendarDriverTest {

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
    date = "-1";
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

}
