package org.overlake.ftc.team_7330.Autonomous;

import org.swerverobotics.library.interfaces.Autonomous;
import org.overlake.ftc.team_7330.Autonomous.AutonomousOpMode;
/**
 * Created by Nikhil on 12/10/2015.
 */

@Autonomous
public class TestLineFollowing extends AutonomousOpMode
{
    @Override protected void main() throws InterruptedException
    {
        waitForStart();
        initializeAllDevices();

        while (true)
        {
            followColor(Color.Red, Side.Left, Side.Right);
        }
    }



}
