package org.overlake.ftc.team_7330;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Created by Rohan on 10/16/2015.
 */
@TeleOp(name="TelemetryTest")
public class TelemetryTest extends SynchronousOpMode {

    //----------------------------------------------------------------------------------------------
    // main() loop
    //----------------------------------------------------------------------------------------------

    int loopCount = 0;

    @Override public void main() throws InterruptedException
    {
        this.composeDashboard();

        while (true)
        {
            loopCount++;
            telemetry.update();
        }
    }

    //----------------------------------------------------------------------------------------------
    // dashboard configuration
    //----------------------------------------------------------------------------------------------

    void composeDashboard()
    {
        // The default dashboard update rate is a little to slow for us, so we update faster
        telemetry.setUpdateIntervalMs(200);

        telemetry.addLine(
                telemetry.item("loop count: ", new IFunc<Object>() {
                    public Object value() {
                        return loopCount;
                    }
                }));
    }
}
