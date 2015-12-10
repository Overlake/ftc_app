package org.Overlake.ftc.Team_7330;

/**
 * Created by Xiao on 11/8/2015.
 */
public class LineFollowing {

    public enum Color {red, blue, white,}
    public enum Side {left, right}

    /*Ruthie's ideas for how this is all going to work
        1)drive straight most of the way to the parking zone, quickly
        2)while(don't see our team's color) drive slowly
        3)after we find our blue/red line:
            while(don't see white) follow our team's color's line
        4)after we find the white line:
            a)follow white line for an undetermined
                OR:
            b)use imu to turn to face buttons directly
        4)do something to approach and push buttons
    */

    public boolean isColor (Color color, Side sensorSide, Side colorSide)
    {
       /*1)find out what the RGB sensor sees
         2)compare it to a range of values for the given color we are looking for
         3)return if the sensor is seeing the color we want
        */
    }

    public void followColor(Color color, Side sensorSide, Side colorSide)
    {
        if(isColor(color, sensorSide, colorSide))
        {
            //turn towards non-color (opposite of colorSide)
        }
        else
        {
            //turn towards color (same direction as colorSide)
        }
    }

}

