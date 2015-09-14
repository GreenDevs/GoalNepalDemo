package com.crackdevelopers.goalnepal.Leagues;

/**
 * Created by trees on 9/8/15.
 */
public class FixtureRow
{
    String club_a_name, club_b_name, club_a_score, club_b_score, club_a_icon, club_b_icon, match_date, match_time, stage_title;
    long match_id;

    public FixtureRow(String club_a_name, String club_b_name, String club_a_score, String club_b_score, String club_a_icon,
                      String club_b_icon, String match_date, String match_time, String stage_title, long match_id) {
        this.club_a_name = club_a_name;
        this.club_b_name = club_b_name;
        this.club_a_score = club_a_score;
        this.club_b_score = club_b_score;
        this.club_a_icon = club_a_icon;
        this.club_b_icon = club_b_icon;
        this.match_date = match_date;
        this.match_time = match_time;
        this.stage_title = stage_title;
        this.match_id=match_id;
    }
}
