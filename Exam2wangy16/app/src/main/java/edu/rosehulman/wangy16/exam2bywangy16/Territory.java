package edu.rosehulman.wangy16.exam2bywangy16;

import android.support.v7.widget.CardView;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


//@JsonIgnoreProperties(ignoreUnknown = true)
public class Territory {
    public String name;
    public String capital;
    public int area;
    public String nickname;
    public String governor;
    public String abbreviation;
    // … other fields and methods as needed
    public Territory() { /* Empty constructor needed for Jackson json formatter */ }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGovernor() {
        return governor;
    }

    public void setGovernor(String governor) {
        this.governor = governor;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

}
