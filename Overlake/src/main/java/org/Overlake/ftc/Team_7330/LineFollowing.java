package org.Overlake.ftc.Team_7330;

/**
 * Created by Xiao on 11/8/2015.
 */
public class LineFollowing {
    /////this is for red team currently
    public enum Color {red, blue, white,}
    public enum Side {left, right}

    public boolean isColor (Color color, Side side)
    {
       /* return (color==Color.white&& Values in Range||
                    color==Color.blue&& values in range||
                    color==Color.red&& values in range)*/
    }

    public void followColor(Color followColor)
    {
        if(isColor(followColor,Side.left))
        {
            //turn right: increase left wheel power a bit and then move forward
        }
        else
        {
            //turn left: increase right wheel power a bit and then move forward
        }
    }

    public void followColorUntil(Color oldColor, Color newColor)
    {
        while (!isColor(newColor))
        {
            followColor(oldColor);
        }
        //stop then follow white
        followColor(newColor);
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

