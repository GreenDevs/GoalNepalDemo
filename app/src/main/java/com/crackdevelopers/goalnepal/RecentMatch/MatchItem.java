package com.crackdevelopers.goalnepal.RecentMatch;

import java.util.Date;

/**
 * Created by trees on 8/24/15.
 */
public class MatchItem
{

    String iconA, iconB, nameA, nameB, match_time,match_venue, scoreA, scoreB, match_status;
    String date;
    long match_id;

    public MatchItem(String iconA, String iconB, String nameA, String nameB, String match_time,
                     String match_venue, String scoreA, String scoreB, String match_status, String date, long match_id)
    {
        this.iconA = iconA;
        this.iconB = iconB;
        this.nameA = nameA;
        this.nameB = nameB;
        this.match_time = match_time;
        this.match_venue = match_venue;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.match_status = match_status;
        this.date = date;
        this.match_id = match_id;
    }
}
