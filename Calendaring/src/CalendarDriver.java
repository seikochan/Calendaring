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
		String versionNum = "";
		int classNum = 0;
		
		
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
			
			
			//TODO Jasmine
			//Classification  (3.8.1.3).  Note  this  is  a  way  of  users  designating  events  as  public  (default),  private,  or  confidential.
			System.out.print("Classification\n"
					+ "\t1)PUBLIC\n"
					+ "\t2)PRIVATE\n"
					+ "/t3)CONFIDENTIAL: ");
			classNum = scanner.nextInt();
			writer.write("CLASS:");
				//TODO make a loop here
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
					
			}
			
			writer.write("\n");
				//TODO what is iana-name and x-name?????
			
			
			//TODO Jasmine
			//Location  (3.8.1.7)
			
			
			
			//TODO
			//Priority  (3.8.1.9)
			
			
			
			//TODO
			//Summary  (3.8.1.12)  
			
			
			
			//TODO
			//for DTSTART  (3.8.2.4)  
			
			
			
			//TODO
			//for DTEND  (3.8.2.2)  
			
			
			
			//TODO
			//for Time  zone  identifier  (3.8.3.1,  and  whatever  other  sections  you  need  to  be able  to  specify  time  zone
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
