package xyz.kotlout.kotlout.model.experiment.trial;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;

public abstract class Trial implements Serializable {

  private String experimenterId;
  private Date timestamp;
  private Geolocation location;

  public Trial() {

  }

  public Trial(String experimenterId) {
    this.experimenterId = experimenterId;
    this.timestamp = Calendar.getInstance().getTime();
    this.location = new Geolocation(0.0, 0.0);
  }

  public Trial(String experimenterId, Geolocation location) {
    this.experimenterId = experimenterId;
    this.timestamp = Calendar.getInstance().getTime();
    this.location = location;
  }

  public String getExperimenterId() {
    return experimenterId;
  }

  public void setExperimenterId(String experimenterId) {
    this.experimenterId = experimenterId;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public void setLocation(Geolocation location) {
    this.location = location;
  }

  public Geolocation getLocation() {
    return location;
  }
}
