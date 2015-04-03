import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
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
  private final static int HOURS = 0;
  private final static int MINS = 1;
  private final static int SECS = 2;


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


  private static boolean isValidUnitStr(int unitTime, String unitStr) {

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


  private static void createiCalFile() {
    BufferedWriter writer;
    boolean invalidInput = true;
    boolean anotherEvent = false;
    String newEventStr = "";

    int versionNum = 0;
    int classNum = 0;
    String location = "";
    int priority = 0;

    String startDate = null;
    String endDate = null;
    int iStartDate = 0;
    int iEndDate = 0;

    String startTime = null;
    String endTime = null;
    int iStartTime = 0;
    int iEndTime = 0;
    String hourStr = "";
    String minuteStr = "";
    String secondStr = "";

    // print description of mode
    System.out.println(DESC_STR);

    System.out.println("Please provide the following information...\n");

    try {
      // creating output file
      writer = new BufferedWriter(new FileWriter(new File("event.ics")));
      writer.write("BEGIN:VCALENDAR\n");

      // =========================================
      // Version (section 3.7.4 of RFC 5545)
      // =========================================
      writer.write("VERSION:");

      invalidInput = true;
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
            writer.write("2.0");
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
      }
      writer.newLine();
      System.out.println();

      // =========================================
      // Time zone identifier (3.8.3.1)
      // =========================================
      writer.write("BEGIN:VTIMEZONE\n");

      // TODO use TZDB
      System.out.println("Enter Time Zone, country? ex. America");
      String country = scanner.nextLine();
      System.out.println("Time Zone, region? (replace space with '_' ex. New_york");
      String region = scanner.nextLine();
      writer.write("TZID:" + country + "/" + region + "\n");

      writer.write("BEGIN:STANDARD\n");
      writer.write("TZOFFSETFROM:-1000\n" + "TZOFFSETTO:-1000\n" + "DTSTART:19700101T000000\n");
      writer.write("END:STANDARD\n");
      writer.write("END:VTIMEZONE\n");

      do {
        // start a new event
        writer.write("BEGIN:VEVENT\n");

        // =========================================
        // Classification (3.8.1.3).
        // =========================================
        writer.write("CLASS:");
        invalidInput = true;
        while (invalidInput) {
          invalidInput = false;

          System.out.println("Choose a Classification\n" + "\t1)PUBLIC\n" + "\t2)PRIVATE\n" + "\t3)CONFIDENTIAL: ");
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

        // =========================================
        // Location (3.8.1.7)
        // =========================================
        System.out.println("Enter a Location: ");
        location = scanner.nextLine();

        writer.write("LOCATION:" + location);
        writer.newLine();
        System.out.println();

        // =========================================
        // Priority (3.8.1.9)
        // =========================================
        invalidInput = true;
        while (invalidInput) {
          try {
            invalidInput = false;
            System.out.println("Choose a Priority (1-highest, 9-lowest, 0-undefined): ");
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

        // =========================================
        // Summary (3.8.1.12)
        // =========================================
        System.out.println("Enter a Summary: ");
        scanner.nextLine();
        String summary = scanner.nextLine();

        writer.write("SUMMARY:" + summary);
        writer.newLine();
        System.out.println();

        // =========================================
        // DTSTART (3.8.2.4)
        // =========================================

        // check for a valid starting date
        invalidInput = true;
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
        writer.write("DTSTART:" + startDate + "T" + startTime);
        writer.newLine();
        System.out.println();

        // =========================================
        // DTEND (3.8.2.2)
        // =========================================

        // check for valid ending date
        invalidInput = true;
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

        writer.write("DTEND:" + iEndDate + "T" + endTime);
        writer.newLine();
        System.out.println();

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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  } // end createiCalFile()


  private static void getFreeTimes(){
    //TODO create a descrition str & print it
    
  } // end getFreeTimes
  
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
            + "\t2) Create an iCalendar text file\n");

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
            createiCalFile();
            break;
          case 3:
            scanner.close();
            System.out.println("BYE BYE!");
            System.exit(0);
          default:
            System.out.println("Invalid Mode selected.  Please select a number from 1-2.");
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
