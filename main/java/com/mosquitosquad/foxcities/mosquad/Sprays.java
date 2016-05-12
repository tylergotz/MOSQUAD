package com.mosquitosquad.foxcities.mosquad;

import java.util.List;

/**
 * Created by Tyler Gotz on 4/25/2016.
 */
public class Sprays
{
    private int numSprays;
    private List<String> appList;

    public Sprays(int numSprays, List<String> appList)
    {
        numSprays = this.numSprays;
        appList = this.appList;
    }

    public int getNumSprays() {
        return numSprays;
    }

    public void setNumSprays(int numSprays) {
        this.numSprays = numSprays;
    }

    public List<String> getAppList() {
        return appList;
    }

    public void setAppList(List<String> appList) {
        this.appList = appList;
    }




}
