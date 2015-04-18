package main;

public class Event {

  //TODO add error checking so that even can only have valid dates and times
  
  private String startDate;
  private String endDate;
  
  private String startTime;
  private String endTime;
  
  private int priority;

  public Event(){
    startDate = "";
    endDate = "";
    startTime = "";
    endTime = "";
    priority = 0;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
  
  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
  
  @Override
  public String toString(){
    return (startDate+"T"+startTime+"-"+endDate+"T"+endTime);
  }
}
