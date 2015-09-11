package com.crackdevelopers.goalnepal.Miscallenous.Preferences;

/**
 * Created by trees on 9/10/15.
 */
public class PreferenceRow
{
    String name;
    long id;
    boolean checked;

    public PreferenceRow(String name, long id, boolean checked)
    {
        this.name=name;
        this.id=id;
        this.checked=checked;
    }
}
