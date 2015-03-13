import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

// Creates .ics Files

/**
 * 
 *  Sample .ics file following RFC 5545 for Internet Calendaring
 *  taken from: https://tools.ietf.org/html/rfc5545
 *  
 *  The following example specifies a group-scheduled meeting that begins
 *  at 8:30 AM EST on March 12, 1998 and ends at 9:30 AM EST on March 12,
 *  1998.  The "Organizer" has scheduled the meeting with one or more
 *  calendar users in a group.  A time zone specification for Eastern
 *  United States has been specified.
       BEGIN:VCALENDAR
       PRODID:-//RDU Software//NONSGML HandCal//EN
       VERSION:2.0
       BEGIN:VTIMEZONE
       TZID:America/New_York
       BEGIN:STANDARD
       DTSTART:19981025T020000
       TZOFFSETFROM:-0400
       TZOFFSETTO:-0500
       TZNAME:EST
       END:STANDARD
       BEGIN:DAYLIGHT
       DTSTART:19990404T020000
       TZOFFSETFROM:-0500
       TZOFFSETTO:-0400
       TZNAME:EDT
       END:DAYLIGHT
       END:VTIMEZONE
       BEGIN:VEVENT
       DTSTAMP:19980309T231000Z
       UID:guid-1.example.com
       ORGANIZER:mailto:mrbig@example.com
       ATTENDEE;RSVP=TRUE;ROLE=REQ-PARTICIPANT;CUTYPE=GROUP:
        mailto:employee-A@example.com
       DESCRIPTION:Project XYZ Review Meeting
       CATEGORIES:MEETING
       CLASS:PUBLIC
       CREATED:19980309T130000Z
       SUMMARY:XYZ Project Review
       DTSTART;TZID=America/New_York:19980312T083000
       DTEND;TZID=America/New_York:19980312T093000
       LOCATION:1CP Conference Room 4350
       END:VEVENT
       END:VCALENDAR
       
 * 
 *
 */
public class CalendarDriver {

  public static void main(String[] args) {
    // TODO print description of program

    Scanner scanner = new Scanner(System.in);
    BufferedWriter writer;
    boolean invalidInput = true;
    boolean anotherEvent = false;
    String newEventStr = "";
    
    String versionNum = "";
    int classNum = 0;
    String location = "";
    
    
    System.out.println("Please provide the following information...\n");
    
    try {
      writer = new BufferedWriter(new FileWriter(new File("event.ics")));
      writer.write("BEGIN:VCALENDAR\n");
      
      
      //TODO  Jasmine
      //Version (section  3.7.4  of  RFC  5545)  
      System.out.print("Version: ");
      versionNum = scanner.nextLine();
      
      //TODO error check here
      
      writer.write("VERSION:" + versionNum +"\n");
      
      do {
        //start a new event
        writer.write("BEGIN:VEVENT\n");
        
        //TODO Jasmine
        //Classification  (3.8.1.3).  
        //Note  this  is  a  way  of  users  designating  events  as  public  (default),  private,  or  confidential.
        writer.write("CLASS:");
        invalidInput=true;
        while (invalidInput) {  
          invalidInput = false;
          
          System.out.print("Classification\n"
              + "\t1)PUBLIC\n"
              + "\t2)PRIVATE\n"
              + "\t3)CONFIDENTIAL: ");
          classNum = scanner.nextInt();
          //must get rid of trailing newline in scanner...
          scanner.nextLine();       
          
          switch(classNum){
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
          //TODO what is iana-name and x-name?????
        
        
        //TODO Jasmine
        //Location  (3.8.1.7)
        System.out.print("Location: ");
        location = scanner.nextLine();
          //TODO error checking?
        
        writer.write("LOCATION:");
        writer.write(location);
        writer.newLine();
        
        
        
        //TODO Alan
        //Priority  (3.8.1.9)
        System.out.println("priority of event? 1 highest, 9 lowest, 0 undefined.");
        int priority = scanner.nextInt();
        writer.write("PRIORITY:"+ priority);
        writer.newLine();
        
        
        
        //TODO Summary Alan
        //Summary  (3.8.1.12)  
        System.out.println("enter a summary of this event");
        scanner.nextLine();
        String summary = scanner.nextLine();
        writer.write("SUMMARY:"+summary);
        writer.newLine();
        
        
        //TODO Alan
        //for DTSTART  (3.8.2.4)  
        System.out.println("whats the date for the event? (YYYYMMDD)");
        int startYear = scanner.nextInt();
        System.out.println("whats the time for the event? (HHMMSS)");
        int startTime = scanner.nextInt();
        String dtStart = "DTSTART:"+startYear + "T" + startTime;
        writer.write(dtStart);
        writer.newLine();
        
        
        
        //TODO Alan
        //for DTEND  (3.8.2.2)  
        System.out.println("date for end of event? (YYYYMMDD)");
        int endYear = scanner.nextInt();
        System.out.println("time for the end of event? (HHMMSS)");
        int endTime = scanner.nextInt();
        String dtEnd = "DTEND:"+endYear + "T" + endTime;
        writer.write(dtEnd);
        writer.newLine();
        
        
        //TODO Alan
        //for Time  zone  identifier  (3.8.3.1,  and  whatever  other  sections  you  need  to  be able  to  specify  time  zone
        System.out.println("Time Zone, country? ex. America");
        scanner.nextLine();
        String country = scanner.nextLine();
        System.out.println("Time Zone, region? (replace space with '_' ex. New_york");
        String region = scanner.nextLine();
        writer.write("TZID:" + country + "/" + region);
        writer.newLine();
        
        
        //end this event
        writer.write("END:VEVENT\n");
        
        //prompt if the user would like to add another event
        invalidInput=true;
        while(invalidInput){
          invalidInput=false;
          System.out.print("\nWould you like to add another event? (y/n):");
          newEventStr = scanner.nextLine();
          if(newEventStr.equals("y")){
            anotherEvent = true;
          }else if(newEventStr.equals("n")){
            anotherEvent = false;
          }else{
            System.out.println("Invalid input.  Please provide either 'y' or 'n'.");
            invalidInput=true;
          }
        }
        
      }while (anotherEvent);
      
      System.out.println("BYE BYE!");
      
      writer.write("END:VCALENDAR\n");
      writer.close();
      
    } catch (InputMismatchException e){
      System.out.println("Invalid input.  Please try again.\n");
      System.err.println(e.getMessage());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    scanner.close();
    
  }

}
