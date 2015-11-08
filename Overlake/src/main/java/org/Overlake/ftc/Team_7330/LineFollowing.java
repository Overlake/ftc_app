package org.Overlake.ftc.Team_7330;

/**
 * Created by Xiao on 11/8/2015.
 */
public class LineFollowing {
    public enum Color {red, blue, white}

    public boolean isColor (Color color)
    {
        //
        return true;
    }

    public void followColor(Color oldColor)
    {
        // weird zig zagging
    }

    public void followColorUntil(Color oldColor, Color newColor)
    {
        while (!isColor(newColor))
        {
            followColor(oldColor);
        }
    }

    public void driveStraightUntil(Color newColor)
    {
        while (!isColor(newColor))
        {
            // drive
        }

        // set the motors to zero
    }
}

