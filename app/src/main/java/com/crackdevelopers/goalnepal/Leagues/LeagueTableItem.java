package com.crackdevelopers.goalnepal.Leagues;

/**
 * Created by trees on 9/7/15.
 */
public class LeagueTableItem
{
    String pGroup, name, club_icon_url, match_played, match_won,
            match_draw, match_lost, goal_finished, goal_accepted, goal_diff, points;

    public LeagueTableItem(String pGroup, String name, String club_icon_url, String match_played,
                           String match_won, String match_draw, String match_lost, String goal_finished,
                           String goal_accepted, String goal_diff, String points)
    {
        this.pGroup = pGroup;
        this.name = name;
        this.club_icon_url = club_icon_url;
        this.match_played = match_played;
        this.match_won = match_won;
        this.match_draw = match_draw;
        this.match_lost = match_lost;
        this.goal_finished = goal_finished;
        this.goal_accepted = goal_accepted;
        this.goal_diff = goal_diff;
        this.points = points;
    }
}
