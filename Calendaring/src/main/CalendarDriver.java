package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.*;
import java.lang.Enum;

// Creates .ics Files

/**
 * 
 * Sample .ics file following RFC 5545 for Internet Calendaring taken from: https://tools.ietf.org/html/rfc5545
 * 
 * The following example specifies a group-scheduled meeting that begins at 8:30 AM EST on March 12, 1998 and ends at
 * 9:30 AM EST on March 12, 1998. The "Organizer" has scheduled the meeting with one or more calendar users in a group.
 * A time zone specification for Eastern United States has been specified.
 * 
 * BEGIN:VCALENDAR PRODID:-//RDU Software//NONSGML HandCal//EN VERSION:2.0 BEGIN:VTIMEZONE TZID:America/New_York
 * BEGIN:STANDARD DTSTART:19981025T020000 TZOFFSETFROM:-0400 TZOFFSETTO:-0500 TZNAME:EST END:STANDARD BEGIN:DAYLIGHT
 * DTSTART:19990404T020000 TZOFFSETFROM:-0500 TZOFFSETTO:-0400 TZNAME:EDT END:DAYLIGHT END:VTIMEZONE BEGIN:VEVENT
 * DTSTAMP:19980309T231000Z UID:guid-1.example.com ORGANIZER:mailto:mrbig@example.com
 * ATTENDEE;RSVP=TRUE;ROLE=REQ-PARTICIPANT;CUTYPE=GROUP: mailto:employee-A@example.com DESCRIPTION:Project XYZ Review
 * Meeting CATEGORIES:MEETING CLASS:PUBLIC CREATED:19980309T130000Z SUMMARY:XYZ Project Review DTSTART;
 * TZID=America/New_York:19980312T083000 DTEND;TZID=America/New_York:19980312T093000 LOCATION:1CP Conference Room 4350
 * END:VEVENT END:VCALENDAR
 * 
 * 
 * 
 */

public class CalendarDriver {

  private final static String DESC_STR = "==========================================================================\n"
      + "This program will help you create an iCalendar text file (.ics file) that \n"
      + "follows the Internet Calendaring and Scheduling Core Object Specification \n"
      + "(RFC 5545) found at https://tools.ietf.org/html/rfc5545.\n"
      + "=========================================================================\n";

  // TODO would like to change to enums but cant seem to get working
  // public Enum UnitTime {
  // HOUR, MINUTE, SECOND;
  // }

  private final static Scanner scanner = new Scanner(System.in);
  final static int HOURS = 0;
  final static int MINS = 1;
  final static int SECS = 2;


  protected static boolean isValidDateStr(String date) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      sdf.setLenient(false);
      sdf.parse(date);
    }
    catch (ParseException e) {
      return false;
    }
    catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  protected static boolean isValidUnitStr(int unitTime, String unitStr) {

    if (unitStr.length() != 2) {
      System.out.println("Please follow the format (HH),(MM),(SS).");
      return false;
    }
    try {
      int unitAmt = Integer.parseInt(unitStr);

      switch (unitTime) {
      case HOURS:
        if (unitAmt < 0 || unitAmt >= 24) {
          System.out.println("Hours are only form 00 - 23. Please try again.");
          return false;
        }
        break;
      case MINS:
        if (unitAmt < 0 || unitAmt >= 60) {
          System.out.println("Minutes are only form 00 - 59. Please try again.");
          return false;
        }
        break;
      case SECS:
        if (unitAmt < 0 || unitAmt >= 60) {
          System.out.println("Seconds are only form 00 - 59. Please try again.");
          return false;
        }
        break;
      default:
        System.out.println("Invalid Unit Time provided.  This should not occur.");
        System.exit(1);
      }

    }
    catch (NumberFormatException e) {
      System.out.println("Invald input.  Please try again.");
      return false;
    }

    return true;
  }

  
  //=======================================================
  // PROPERTY HELPER METHODS
  //-----------------------
  // these methods get the .ics properties from the user
  //=======================================================
  protected static String getVersion() {
    boolean invalidInput = true;
    int versionNum = 0;
    String versionStr = "";
    
    while (invalidInput) {
      invalidInput = false;

      System.out.println("Choose a Version" + "\n\t1) 1.0 - vCalendar Format" + "\n\t2) 2.0 - iCalendar Format: ");

      try {
        versionNum = scanner.nextInt();
        scanner.nextLine(); // clear '\n' from buffer

        // scanner.nextLine();

        // error checking
        invalidInput = false;
        switch (versionNum) {
        case 1:
          System.out.println("Sorry we do not support vCalendar Format, " + "please select a different version.");
          invalidInput = true;
          break;
        case 2:
          versionStr = "2.0";
          break;
        default:
          System.out.println("Invalid Version selected.  Please select a number from 1-2.");
          invalidInput = true;
          break;
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid Input. Please try again.");
        invalidInput = true;
        scanner.nextLine(); // clear '\n' from buffer
      }
    } //end while
    
    System.out.println();
    return versionStr;
  } // end getVersion()

  protected static String getTZID(){
      // TODO use TZDB
      System.out.println("Enter Time Zone, country? ex. America");
      String country = scanner.nextLine();
      System.out.println("Time Zone, region? (replace space with '_' ex. New_york");
      String region = scanner.nextLine();
      
      return (country + "/" + region);
  } // end getTZID
  
  protected static String getClassification(){
    boolean invalidInput = true;
    int classNum = 0;
    String classStr = "";
    while (invalidInput) {
      invalidInput = false;

      System.out.println("Choose a Classification\n" + "\t1)PUBLIC\n" + "\t2)PRIVATE\n" + "\t3)CONFIDENTIAL: ");
      classNum = scanner.nextInt();
      // must get rid of trailing newline in scanner...
      scanner.nextLine();

      switch (classNum) {
      case 1:
        classStr = "PUBLIC";
        break;
      case 2:
        classStr = "PRIVATE";
        break;
      case 3:
        classStr = "CONFIDENTIAL";
        break;
      default:
        System.out.println("Invalid classification selected.  Please provide a number from 1-3.");
        invalidInput = true;
      }
    } //end while
    
    System.out.println();
    return classStr;
  } // end class
  
  protected static String getLocation(){
    System.out.println("Enter a Location: ");
    String location = scanner.nextLine();
    
    System.out.println();
    return location;
  }
  
  protected static String getPriority(){
    boolean invalidInput = true;
    int priority = -1;
    
    while (invalidInput) {
      try {
        invalidInput = false;
        System.out.println("Choose a Priority (1-highest, 9-lowest, 0-undefined): ");
        priority = scanner.nextInt();
        scanner.nextLine();
        if (priority < 0 || priority > 9) {
          System.out.println("Invalid input. Please try again.");
          invalidInput = true;
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid input.  Please try again.");
        priority = -1;
        invalidInput = true;
      }
    }
    
    System.out.println();
    return priority+"";
  }
  
  protected static String getSummary(){
    System.out.println("Enter a Summary: ");
    String summary = scanner.nextLine();
    
    System.out.println();
    return summary;
  }
  
  protected static String getDTStart(){
    boolean invalidInput = true;
    String startDate = null;  
    int iStartDate = 0;
    String startTime = null;
    int iStartTime = 0;
    String hourStr = "";
    String minuteStr = "";
    String secondStr = "";
    
 // check for a valid starting date
    while (invalidInput) {
      try {
        invalidInput = false;
        System.out.println("Enter the Start Date (YYYYMMDD): ");
        startDate = scanner.nextLine();
        if (!isValidDateStr(startDate)) {
          System.out.println("Invalid date! Try again.");
          invalidInput = true;
        }
        else {
          iStartDate = Integer.parseInt(startDate);
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
      catch (NumberFormatException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
    }

    // check for a valid start time
    System.out.println("START TIME (Military Time):");

    // get hour input
    invalidInput = true;
    while (invalidInput) {
      try {
        invalidInput = false;
        System.out.print("\tHOURS (HH): ");
        hourStr = scanner.nextLine();

        if (!isValidUnitStr(HOURS, hourStr)) {
          invalidInput = true;
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
    }

    // get minute input
    invalidInput = true;
    while (invalidInput) {
      try {
        invalidInput = false;
        System.out.print("\tMINUTES (MM): ");
        minuteStr = scanner.nextLine();

        if (!isValidUnitStr(MINS, minuteStr)) {
          invalidInput = true;
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
    }

    // get second input
    invalidInput = true;
    while (invalidInput) {
      try {
        invalidInput = false;
        System.out.print("\tSECONDS (SS): ");
        secondStr = scanner.nextLine();

        if (!isValidUnitStr(SECS, secondStr)) {
          invalidInput = true;
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
    }

    startTime = hourStr + minuteStr + secondStr;
    iStartTime = Integer.parseInt(startTime);
    
    return(startDate+"T"+startTime);
  } // end getDTStart()
  
  protected static String getDTEnd(int iStartDate, int iStartTime) {
    boolean invalidInput = true;
    String endDate = null;
    int iEndDate = 0;
    String endTime = null;
    
    int iEndTime = 0;
    String hourStr = "";
    String minuteStr = "";
    String secondStr = "";
    
    // check for valid ending date
    while (invalidInput) {
      try {
        invalidInput = false;
        System.out.println("Enter the End Date (YYYYMMDD): ");
        endDate = scanner.nextLine();
        if (!isValidDateStr(endDate)) {
          System.out.println("Invalid date! Try again.");
          invalidInput = true;
        }
        else {
          iEndDate = Integer.parseInt(endDate);
          // make sure end date is after start date
          if (iEndDate < iStartDate) {
            System.out.println("Can't have the event end before it starts! Enter a later date.");
            invalidInput = true;
          }
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
      catch (NumberFormatException e) {
        System.out.println("Invalid input.  Please try again.");
        scanner.nextLine();
        invalidInput = true;
      }
    }

    // check for a valid end time
    invalidInput = true;
    while (invalidInput) {
      System.out.println("END TIME (Military Time):");
      // get hour input
      invalidInput = true;
      while (invalidInput) {
        try {
          invalidInput = false;
          System.out.print("\tHOURS (HH): ");
          hourStr = scanner.nextLine();

          if (!isValidUnitStr(HOURS, hourStr)) {
            invalidInput = true;
          }
        }
        catch (InputMismatchException e) {
          System.out.println("Invalid input.  Please try again.");
          scanner.nextLine();
          invalidInput = true;
        }
      }

      // get minute input
      invalidInput = true;
      while (invalidInput) {
        try {
          invalidInput = false;
          System.out.print("\tMINUTES (MM): ");
          minuteStr = scanner.nextLine();

          if (!isValidUnitStr(MINS, minuteStr)) {
            invalidInput = true;
          }
        }
        catch (InputMismatchException e) {
          System.out.println("Invalid input.  Please try again.");
          scanner.nextLine();
          invalidInput = true;
        }
      }

      // get second input
      invalidInput = true;
      while (invalidInput) {
        try {
          invalidInput = false;
          System.out.print("\tSECONDS (SS): ");
          secondStr = scanner.nextLine();

          if (!isValidUnitStr(SECS, secondStr)) {
            invalidInput = true;
          }
        }
        catch (InputMismatchException e) {
          System.out.println("Invalid input.  Please try again.");
          scanner.nextLine();
          invalidInput = true;
        }
      }

      endTime = hourStr + minuteStr + secondStr;
      iEndTime = Integer.parseInt(endTime);

      invalidInput = false;
      if (iStartTime >= iEndTime) {
        System.out.println("Can't have the event end before it starts! Enter a later time.");
        invalidInput = true;
      }
    }
    
    System.out.println();
    return (iEndDate + "T" + iEndTime);
  }
  
  //=======================================================
  // WRITER HELPER METHODS
  //-----------------------
  // these methods write their corresponding properties to 
  // meet the RFC5545 Internet Calendaring Standard
  //=======================================================
  private static void writeVersion(BufferedWriter writer, String version) throws IOException{
    writer.write("VERSION:"+version);    
    writer.newLine();
  }
  
  private static void writeTZID(BufferedWriter writer, String tzid) throws IOException {
    writer.write("BEGIN:VTIMEZONE\n");   
    writer.write("TZID:" + tzid + "\n");
    
    writer.write("BEGIN:STANDARD\n");
    writer.write("TZOFFSETFROM:-1000\n" + "TZOFFSETTO:-1000\n" + "DTSTART:19700101T000000\n");
    writer.write("END:STANDARD\n");
    writer.write("END:VTIMEZONE\n");
  }
  
  private static void writeClass(BufferedWriter writer, String classStr) throws IOException {
    writer.write("CLASS:"+classStr);
    writer.newLine();
    // TODO what is iana-name and x-name?????
  }
  
  private static void writeLocation(BufferedWriter writer, String loc) throws IOException{
    writer.write("LOCATION:" + loc);
    writer.newLine();
  }
  
  private static void writePriority(BufferedWriter writer, String priority) throws IOException {
    writer.write("PRIORITY:" + priority);
    writer.newLine();
  }
  
  private static void writeSummary(BufferedWriter writer, String summary) throws IOException {
    writer.write("SUMMARY:" + summary);
    writer.newLine();
  }
  
  private static void writeDTStart(BufferedWriter writer, String dtstart) throws IOException {
    writer.write("DTSTART:" + dtstart);
    writer.newLine();
  }

  private static void writeDTEnd(BufferedWriter writer, String dtend) throws IOException {
    writer.write("DTEND:" + dtend);
    writer.newLine();
  }
  
  private static void createUseriCalFile() {
    BufferedWriter writer;
    boolean invalidInput = true;
    boolean anotherEvent = false;
    String newEventStr = "";

    System.out.println("Please provide the following information...\n");

    try {
      // creating output file
      writer = new BufferedWriter(new FileWriter(new File("event.ics")));
      writer.write("BEGIN:VCALENDAR\n");

      // =========================================
      // Version (section 3.7.4 of RFC 5545)
      // =========================================
      writeVersion(writer, getVersion());

      // =========================================
      // Time zone identifier (3.8.3.1)
      // =========================================
      writeTZID(writer, getTZID());

      do {
        // start a new event
        writer.write("BEGIN:VEVENT\n");

        // =========================================
        // Classification (3.8.1.3).
        // =========================================
        writeClass(writer,getClassification());
        
        // =========================================
        // Location (3.8.1.7)
        // =========================================
        writeLocation(writer,getLocation());

        // =========================================
        // Priority (3.8.1.9)
        // ========================================= 
        writePriority(writer,getPriority());

        // =========================================
        // Summary (3.8.1.12)
        // =========================================
        writeSummary(writer, getSummary());

        // =========================================
        // DTSTART (3.8.2.4)
        // =========================================
        String start = getDTStart();
        int iStartDate = Integer.parseInt(start.substring(0,8));
        int iStartTime = Integer.parseInt(start.substring(9));
        
        writeDTStart(writer, start);
        
        // =========================================
        // DTEND (3.8.2.2)
        // =========================================
        writeDTEnd(writer, getDTEnd(iStartDate, iStartTime));

        // end this event
        writer.write("END:VEVENT\n");

        // =========================================
        // prompt if the user would like to add another event
        // =========================================
        invalidInput = true;
        while (invalidInput) {
          invalidInput = false;
          System.out.print("\nWould you like to add another event? (y/n):");
          newEventStr = scanner.nextLine();
          if (newEventStr.equals("y")) {
            anotherEvent = true;
          }
          else if (newEventStr.equals("n")) {
            anotherEvent = false;
          }
          else {
            System.out.println("Invalid input.  Please provide either 'y' or 'n'.");
            invalidInput = true;
          }
        }

        System.out.println();
      }
      while (anotherEvent);

      writer.write("END:VCALENDAR\n");
      writer.close();

    }
    catch (InputMismatchException e) {
      System.out.println("Invalid input.  Please try again.\n");
      System.err.println(e.getMessage());
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  } // end createiCalFile()


  protected static void getFreeTimes(){
    boolean invalidInput = true;
    boolean notDone = true;
    
    /**
     * Keys are Start Time of a Free Time Slot
     * Values are End Time of a Free Time Slot
     */
    HashMap<String, String> freeTimeSlots = new HashMap<>();
    //start off with the whole day as a single free time slot
    freeTimeSlots.put("000000","235959");
    
    String date = "";
    String timeZone = "";
    
    //TODO create a description str & print it
    
    // get User Input
    while (invalidInput) {
      System.out.println("Date of these event(s) (YYYYMMDD):");
      invalidInput = false;
      date = scanner.nextLine();
      if (!isValidDateStr(date)) {
        System.out.println("Invalid date! Try again.");
        invalidInput = true;
      } // end if
    } // end while
    
    System.out.println("Timezone of event(s): ");
    timeZone = scanner.nextLine();
    // TODO error check these
    
    // "import" .ics event files
    System.out.println("Please provide the .ics file names of the events you have (Press 'Enter' after each file name and enter 'done' when finished.");
    
    invalidInput = true;
    while(invalidInput || notDone){
      invalidInput = false;
      System.out.print("File Name (Must be a .ics file): ");
      String fileName = scanner.nextLine();
      
      if(fileName.equals("done")){
        notDone = false;
      }else{
      
        try{
          // get all the events from the .ics file
          ArrayList<Event> eventsArr = parseiCalFile(fileName, date, timeZone);
for(Event e:eventsArr){
}
          //update the free time hash map
          calculateFreeTimeSlots(freeTimeSlots, eventsArr);
          
        }catch(FileNotFoundException e){
          System.out.println("File not found.  Please try again");
          invalidInput = true;
        }
      } // end if else
    }// end while
    
    //create a .ics file that has an event for each entry in freeTimeSlots HashMap
    // creating output file
    BufferedWriter writer;
    try {
      writer = new BufferedWriter(new FileWriter(new File("free_time.ics")));

      writer.write("BEGIN:VCALENDAR\n");
      writeVersion(writer, "2.0");
      writeTZID(writer,timeZone);
  
      //loop over all the entries and create an event for each
      for (Entry<String, String> entry : freeTimeSlots.entrySet()){
        // start a new event
        writer.write("BEGIN:VEVENT\n");
        writeClass(writer,"PUBLIC");
        writePriority(writer,"9");
        writeSummary(writer, "Free Time");
        writeDTStart(writer, date+"T"+entry.getKey());
        writeDTEnd(writer, date+"T"+entry.getValue());
        writer.write("END:VEVENT\n");
      }// end for
      
      writer.write("END:VCALENDAR\n");
      writer.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    } 
  } // end getFreeTimes
  
  // helper method to get event times in a .ics file
  protected static ArrayList<Event> parseiCalFile (String name, String date, String timeZone) throws FileNotFoundException {
    BufferedReader read = new BufferedReader(new FileReader(name));
    String line = "";
    int eventIndex = 0;
    
    ArrayList<Event> eventsArr = new ArrayList<Event>();
    
    try {
      
      while((line = read.readLine()) != null){
        if(line.equals("BEGIN:VEVENT")){
          Event event = new Event();
          eventsArr.add(event);
          
          boolean eventEnded = false;
          while( (!eventEnded) && ((line = read.readLine()) != null) ){
            //split the line in 2 at first ':' delimiter
            String arr[] = line.split(":", 2);
      
            switch(arr[0]){
              case "DTSTART":
                // format of DTSTART:YYYYMMDDTHHMMSS
                eventsArr.get(eventIndex).setStartDate(line.substring(8, 16));
                eventsArr.get(eventIndex).setStartTime(line.substring(17));
                break;
              
              case "DTEND":
                // format of DTEND:YYYYMMDDTHHMMSS
                eventsArr.get(eventIndex).setEndDate(line.substring(6, 14));
                eventsArr.get(eventIndex).setEndTime(line.substring(15));
                break;  
                
              case "END":
                if(line.equals("END:VEVENT")){
                  eventEnded = true;
                  eventIndex++;
                }
                break;
            } //end switch
          } //end while
        } //end if
      } // end while 
      
      read.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return eventsArr;
  } //end parseiCalFile
  
  protected static void calculateFreeTimeSlots (HashMap<String,String> timeMap, ArrayList<Event> events){
    if( (timeMap==null) || (events==null) ) {
      System.err.println("timeMap or eventsList is null.");
      return;
    }
    
    Iterator<Event> iter = events.iterator();
    
    // loop over all the events
    while(iter.hasNext()){
      Event event = iter.next();
      int eventSTime = Integer.parseInt(event.getStartTime());
      int eventETime = Integer.parseInt(event.getEndTime());

      HashMap<String,String> timeMapCopy = (HashMap<String, String>) timeMap.clone();
      // loop over all the free time slots in the map
      for (Entry<String, String> entry : timeMapCopy.entrySet()){
        int freeSTime = Integer.parseInt(entry.getKey());
        int freeETime = Integer.parseInt(entry.getValue());     
        // compare the start time of the event to each free time slot
        
        // if endTime of freeTimeSlot > event startTime > startTime of freeTimeSlot
        // then it means our event started during a free time slot so we need to adjust the map to say its busy
        if( (eventSTime >= freeSTime) && (eventSTime < freeETime) ){
          // case 1:
          //    the event starts at the same time as the free time but ends before it
          //            |---------free-------------|
          //            |--event--|
          if( (eventSTime == freeSTime) && (eventETime < freeETime)){
            // then we need to replace the time slot with one that starts at the end of the event
            //                    |----free--------|
            timeMap.put(event.getEndTime(), entry.getValue());
            timeMap.remove(entry.getKey());
          }
        
          // case 2:
          //    the event starts at the same time as the free time and ends after it (or at same time)
          //            |---------free-------------|
          //            |--event--------------------------|
          else if( (eventSTime == freeSTime) && (eventETime >= freeETime) ){
            // then we need to just remove the free time slot
            //  
            timeMap.remove(entry.getKey());
          }
          
          // case 3:
          //    the event is within the free time slot
          //            |---------free-------------|
          //                     |--event--|
          else if( eventETime < freeETime){
            // then we need to "replace" this free time slot with 2 new ones
            //          |--free--|         |--free-|
            timeMap.put(event.getEndTime(), entry.getValue());
            timeMap.put(entry.getKey(),event.getStartTime()); 
          }
          
           // case 4:
           //    the event ends at the same time as the free time slot
           //            |---------free-------------|
           //                    |----event---------|
              
          //case 5:
          //    the event is overlapping the free time slot
          //            |--------free--------------|
          //                     |--------event---------------|
            
          else{
           // then just shorten the free time slot to the start of the event 
           // result:  |-free--|
           timeMap.put(entry.getKey(), event.getStartTime());
          }
        } // end if
        
        // if endTime of freeTimeSlot > event endTime > startTime of freeTimeSlot
        // then it means our event started during a free time slot so we need to adjust the map to say its busy
        else if( (eventETime >= freeSTime) && (eventETime < freeETime) ){
          // case 1:
          //    the event is within the free time slot
          //            |---------free-------------|
          //                     |--event--|
          // *accounted for in the previous if case so do nothing.....
          
         // case 2:
         //    the event is starts at the same time as the free time slot
         //            |---------free-------------|
         //            |----event----------|
            
        //case 3:
        //    the event is overlapping the free time slot
        //             |--------free--------------|
        //    |--------event---------------|
            
          if( eventSTime <= freeSTime ){
           // then just shorten the free time slot to the end of the event 
           // result:                      |-free-|
           timeMap.put(event.getEndTime(), entry.getValue());
           timeMap.remove(entry.getKey());
         } // end if           
        }// end else if  
      }// end for loop over hashmap     
    }// end while loop over events  
  }// end calculateFreeTimeSlots()
  
  public static void main(String[] args) {

    boolean invalidInput = true;

    int numMode = 0;

    // TODO
    // split up all this (vvvvvvv) into smaller methods that can be tested easier

    // TODO
    // make prudier commenting
    while(true){
      invalidInput = true;
      while (invalidInput) {
        invalidInput = false;

        System.out.println("What would you like to do?\n" + "\t1) Import Events and Find Free Times Availiable\n"
            + "\t2) Create an iCalendar text file\n"
            + "\t3) Exit\n");

        try {
          numMode = scanner.nextInt();
          scanner.nextLine(); // clear '\n' from buffer

          // error checking
          invalidInput = false;
          switch (numMode) {
          case 1:
            getFreeTimes();
            break;
          case 2:
            // print description of mode
            System.out.println(DESC_STR);
            createUseriCalFile();
            break;
          case 3:
            scanner.close();
            System.out.println("BYE BYE!");
            System.exit(0);
          default:
            System.out.println("Invalid Mode selected.  Please select a number from 1-3.");
            invalidInput = true;
            break;
          } // end switch
        }
        catch (InputMismatchException e) {
          System.out.println("Invalid Input. Please try again.");
          invalidInput = true;
          scanner.nextLine(); // clear '\n' from buffer
        }
      } // end while
    } // end while
  } // end main
} // end class
