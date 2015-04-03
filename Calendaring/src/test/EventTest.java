package test;

import static org.junit.Assert.*;
import main.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventTest {

  private Event event;
  
  /**
   * Sets up test fixture.
   * Called before every test case method.
   */
  @Before
  public void setUp(){
    event = new Event();
  }
  
  /**
   * Tears down test fixture.
   * Called after every test case method.
   */
  @After
  public void tearDown(){
    event = null;
  }

  @Test
  public void testSetStartDate() {
    assertEquals("Default event should have an empty start date.", "", event.getStartDate());
    
    String sDate = "20152021";
    event.setStartDate(sDate);
    assertEquals(sDate, event.getStartDate());
  }

  @Test
  public void testSetEndDate() {
    assertEquals("Default event should have an empty end date.", "", event.getEndDate());
    
    String eDate = "20152027";
    event.setEndDate(eDate);
    assertEquals(eDate, event.getEndDate());
  }
  
  @Test
  public void testSetStartTime() {
    assertEquals("Default event should have an empty start time.", "", event.getStartTime());
    
    String sTime = "061500";
    event.setStartTime(sTime);
    assertEquals(sTime, event.getStartTime());
  }

  @Test
  public void testSetEndTime() {
    assertEquals("Default event should have an empty end time.", "", event.getEndTime());
    
    String eTime = "123722";
    event.setEndTime(eTime);
    assertEquals(eTime, event.getEndTime());
  }

}
