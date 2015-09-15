package com.crackdevelopers.goalnepal.NavigationDrawer;

/**
 * Created by trees on 9/10/15.
 */
public class NavigationRow
{
    String name, icon, stickyTitle; long tournamet_id;

    public NavigationRow(String name, String icon, String stickyTitle, long tournamet_id)
    {
        this.name=name;
        this.tournamet_id=tournamet_id;
        this.icon=icon;
        this.stickyTitle=stickyTitle;
    }
}
