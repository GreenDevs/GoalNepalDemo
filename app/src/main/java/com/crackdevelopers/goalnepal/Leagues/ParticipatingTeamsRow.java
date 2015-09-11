package com.crackdevelopers.goalnepal.Leagues;

/**
 * Created by trees on 9/9/15.
 */
public class ParticipatingTeamsRow
{
    String icon_url, team_name, team_short_name;
    long team_id;

    public ParticipatingTeamsRow(String icon_url, String team_name, String team_short_name, long team_id) {
        this.icon_url = icon_url;
        this.team_name = team_name;
        this.team_short_name = team_short_name;
        this.team_id = team_id;
    }


}
