import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.*;

// Creates .ics Files

/**
 * 
 * Sample .ics file following RFC 5545 for Internet Calendaring taken from: https://tools.ietf.org/html/rfc5545
 * 
 * The following example specifies a group-scheduled meeting that begins at 8:30 AM EST on March 12, 1998 and ends at
 * 9:30 AM EST on March 12, 1998. The "Organizer" has scheduled the meeting with one or more calendar users in a group.
 * A time zone specification for Eastern United States has been specified. 
 * 
 * BEGIN:VCALENDAR 
 * PRODID:-//RDU Software//NONSGML HandCal//EN 
 * VERSION:2.0 
 * BEGIN:VTIMEZONE 
 * TZID:America/New_York 
 * BEGIN:STANDARD
 * DTSTART:19981025T020000 
 * TZOFFSETFROM:-0400 
 * TZOFFSETTO:-0500 
 * TZNAME:EST 
 * END:STANDARD 
 * BEGIN:DAYLIGHT
 * DTSTART:19990404T020000 
 * TZOFFSETFROM:-0500 
 * TZOFFSETTO:-0400
 * TZNAME:EDT 
 * END:DAYLIGHT 
 * END:VTIMEZONE 
 * BEGIN:VEVENT
 * DTSTAMP:19980309T231000Z 
 * UID:guid-1.example.com 
 * ORGANIZER:mailto:mrbig@example.com
 * ATTENDEE;RSVP=TRUE;ROLE=REQ-PARTICIPANT;CUTYPE=GROUP: mailto:employee-A@example.com 
 * DESCRIPTION:Project XYZ Review Meeting 
 * CATEGORIES:MEETING 
 * CLASS:PUBLIC 
 * CREATED:19980309T130000Z 
 * SUMMARY:XYZ Project Review
 * DTSTART;
 * TZID=America/New_York:19980312T083000 DTEND;TZID=America/New_York:19980312T093000 LOCATION:1CP Conference
 * Room 4350 END:VEVENT END:VCALENDAR
 * 
 * 
 *
 */
public class CalendarDriver {

	private final static String DESC_STR = 
			"==========================================================================\n"
			+ "This program will help you create an iCalendar text file (.ics file) that \n"
			+ "follows the Internet Calendaring and Scheduling Core Object Specification \n"
			+ "(RFC 5545) found at https://tools.ietf.org/html/rfc5545.\n"
			+ "=========================================================================\n";
	
  public static boolean isValidDateStr(String date) {
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
  
  public static void main(String[] args) {
    // print description of program
	System.out.println(DESC_STR);
	  
    Calendar calendar = Calendar.getInstance();
    int thisYear = calendar.get(Calendar.YEAR)*10000;
    int thisMonth = (calendar.get(Calendar.MONTH) + 1)*100;
    int thisDay = calendar.get(Calendar.DATE);
    System.out.println("this month is:" + thisMonth);
    System.out.println("this year is:" + thisYear);
    System.out.println("this day is " + thisDay);
    int thisDate = thisYear+thisMonth+thisDay;
    System.out.println("this date is: " + thisDate);
    
    Scanner scanner = new Scanner(System.in);
    BufferedWriter writer;
    boolean invalidInput = true;
    boolean anotherEvent = false;
    String newEventStr = "";

    int versionNum = 0;
    int classNum = 0;
    String location = "";
    
    //TODO
    //split up all this (vvvvvvv)  into smaller methods that can be tested easier
    
    //TODO
    //make prudier commenting
    
    
    System.out.println("Please provide the following information...\n");

    try {
      writer = new BufferedWriter(new FileWriter(new File("event.ics")));
      writer.write("BEGIN:VCALENDAR\n");
      
      //=========================================
      //Version (section  3.7.4  of  RFC  5545) 
      //=========================================
      writer.write("VERSION:");
          
      invalidInput = true;
      while(invalidInput){
    	  System.out.print("Version"
    	      		+ "\n\t1) 1.0 - vCalendar Format"
    	      		+ "\n\t2) 2.0 - iCalendar Format: ");

    	  versionNum = scanner.nextInt();
    	  scanner.nextLine();
    	      
    	  invalidInput=false;
    	  switch(versionNum){
    	  	case 1:
    	  		System.out.println("Sorry we do not support vCalendar Format, "
    	  				+ "please select a different version.");
    	  		invalidInput=true;
    	  		break;
    	  	case 2:
    	  		writer.write("2.0");
    	  		break;
    	  	default:
    	  		System.out.println("Invalid Version selected.  Please select a number from 1-2.");
    	  		invalidInput=true;
    	  		break;
    	  }
      }
      writer.newLine();
      System.out.println();
      
      do {
        // start a new event
        writer.write("BEGIN:VEVENT\n");

        //=========================================
        // Classification (3.8.1.3).
        //=========================================
        writer.write("CLASS:");
        invalidInput = true;
        while (invalidInput) {
          invalidInput = false;

          System.out.print("Classification\n" + "\t1)PUBLIC\n" + "\t2)PRIVATE\n" + "\t3)CONFIDENTIAL: ");
          classNum = scanner.nextInt();
          // must get rid of trailing newline in scanner...
          scanner.nextLine();

          switch (classNum) {
          case 1:
            writer.write("PUBLIC");
            break;
          case 2:
            writer.write("PRIVATE");
            break;
          case 3:
            writer.write("CONFIDENTIAL");
            break;
          default:
            System.out.println("Invalid classification selected.  Please provide a number from 1-3.");
            invalidInput = true;
          }
        }
        writer.newLine();
        System.out.println();
        // TODO what is iana-name and x-name?????

        //=========================================
        // Location (3.8.1.7)
        //=========================================
        System.out.print("Location: ");
        location = scanner.nextLine();
        // TODO error checking?

        writer.write("LOCATION:");
        writer.write(location);
        writer.newLine();
        System.out.println();

        //=========================================
        // Priority (3.8.1.9)
        //=========================================
        int priority = 0;
        invalidInput = true;
        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("PRIORITY (1-highest, 9-lowest, 0-undefined.): ");
            priority = scanner.nextInt();
            if (priority < 0 || priority > 9) {
              System.out.println("Invalid input. Please try again.");
              invalidInput = true;
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            priority = -1;
            scanner.nextLine();
            invalidInput = true;
          }
        }
        writer.write("PRIORITY:" + priority);
        writer.newLine();
        System.out.println();

        //=========================================
        // Summary (3.8.1.12)
        //=========================================
        System.out.println("SUMMARY: ");
        scanner.nextLine();
        String summary = scanner.nextLine();
        writer.write("SUMMARY:" + summary);
        writer.newLine();
        System.out.println();

        // TODO Alan
        //=========================================
        // DTSTART (3.8.2.4)
        //=========================================
        invalidInput = true;
        String startYear = null;
        int i_startYear= 0;
        int startTimeH = 0;
        int startTimeM = 0;
        int startTimeS = 0;
        int startTime = 0;

        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("START DATE (YYYYMMDD): ");
            startYear = scanner.nextLine();
            if (!isValidDateStr(startYear)) {
              System.out.println("invalid date! Try again.");
              invalidInput = true;
            }
            else
            {
              i_startYear = Integer.parseInt(startYear);
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            scanner.nextLine();
            invalidInput = true;
          }
        }

          //the following 3 "methods" won't work for numbers like 8 (missing leading 0)
          // I didn't have enough time to figure out how to validate a 24hr format.
          //I'm sure there has to be something on google.
        System.out.println("START TIME:");
        invalidInput = true;
        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("\tHOURS (HH): ");
            startTime = scanner.nextInt();

            if (startTimeH < 0 || startTimeH > 24) {
              System.out.println("Hours are only form 0 - 24. Please try again.");
              invalidInput = true;
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            scanner.nextLine();
            invalidInput = true;
          }
        }
        
        invalidInput = true;
        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("\tMINUTES (MM): ");
            startTime = scanner.nextInt();

            if (startTimeM < 0 || startTimeM > 59) {
              System.out.println("Minutes are only form 0 - 59. Please try again.");
              invalidInput = true;
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            scanner.nextLine();
            invalidInput = true;
          }
        }
        
        invalidInput = true;
        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("\tSECONDS (SS): ");
            startTime = scanner.nextInt();

            if (startTimeS < 0 || startTimeS > 59) {
              System.out.println("Hours are only form 0 - 59. Please try again.");
              invalidInput = true;
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            scanner.nextLine();
            invalidInput = true;
          }
        }
        
        //remember to fix this when you figure out how to validate 24hr format.
        startTime = (startTimeH*10000) + (startTimeM*100) + (startTimeS);
        String dtStart = "DTSTART:" + startYear + "T" + startTime;
        writer.write(dtStart);
        writer.newLine();
        System.out.println();

        // TODO Alan
        // Note I added more restrictions to endtime, may need to store start
        // year and time to compare when we change these to methods. (so the initialization
        // to 0 will have to be erased. replaced with formal parameters I guess).
        //=========================================
        // DTEND (3.8.2.2)
        //=========================================
        invalidInput = true;
        String endYear = null;
        int i_endYear;
        int endTime = 0;

        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("what year does the event end? (YYYYMMDD)");
            endYear = scanner.nextLine();
            if (!isValidDateStr(endYear)) {
              System.out.println("invalid date! Try again.");
              invalidInput = true;
            }
            else
            {
              i_endYear = Integer.parseInt(endYear);
              if (i_endYear < i_startYear)
              {
                System.out.println("Can't have the event ends before it starts! Enter a later date.");
                invalidInput = true;
              }
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            scanner.nextLine();
            invalidInput = true;
          }
        }

        invalidInput = true;
        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("when does the event end? (HHMMSS)");
            endTime = scanner.nextInt();
            //need to fix this check, don't work for numbers like 239999 or 88, etc.
            if (endTime < 0 || endTime > 240000) {
              if (endTime < startTime) {
                System.out.println("Can't end before you start! Enter an earlier time");
                invalidInput = true;
              }
              else {
                System.out.println("Invalid input. Please try again.");
                invalidInput = true;
              }
            }
          }
          catch (InputMismatchException e) {
            System.out.println("Invalid input.  Please try again.");
            scanner.nextLine();
            invalidInput = true;
          }
        }
        String dtEnd = "DTEND:" + endYear + "T" + endTime;
        writer.write(dtEnd);
        writer.newLine();
        System.out.println();

        // TODO Alan
        //=========================================
        // for Time zone identifier (3.8.3.1)
        //=========================================
        System.out.println("Time Zone, country? ex. America");
        scanner.nextLine();
        String country = scanner.nextLine();
        System.out.println("Time Zone, region? (replace space with '_' ex. New_york");
        String region = scanner.nextLine();
        writer.write("TZID:" + country + "/" + region);
        writer.newLine();
        System.out.println();

        // end this event
        writer.write("END:VEVENT\n");

        //=========================================
        // prompt if the user would like to add another event
        //=========================================
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

      System.out.println("BYE BYE!");

      writer.write("END:VCALENDAR\n");
      writer.close();

    }
    catch (InputMismatchException e) {
      System.out.println("Invalid input.  Please try again.\n");
      System.err.println(e.getMessage());
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    scanner.close();

  }

}
