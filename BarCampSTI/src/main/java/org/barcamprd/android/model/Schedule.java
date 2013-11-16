package org.barcamprd.android.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vinrosa on 10/18/13.
 */
public class Schedule implements Serializable {
    public static final String EXTRA_ID = "SCHEDULE";
    private int id;
    private String name;
    private String description;
    private String place;
    private Speaker speaker;
    private Date schedule;
    private Integer speakerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(Integer speakerId) {
        this.speakerId = speakerId;
    }
}
