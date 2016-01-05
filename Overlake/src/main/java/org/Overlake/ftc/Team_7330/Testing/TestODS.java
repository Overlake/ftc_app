/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.overlake.ftc.team_7330.Testing;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

@TeleOp(name="TestODS")
public class TestODS extends SynchronousOpMode
{
    OpticalDistanceSensor opticalDistanceSensor;
    double reflectedLight;

    @Override
    public void main() throws InterruptedException {

        // write some device information (connection info, name and type)
        // to the log file.
        hardwareMap.logDevices();

        // get a reference to our DeviceInterfaceModule object.
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("ods");

        // Set up our dashboard computations
        composeDashboard();

        // wait for the start button to be pressed.
        waitForStart();

        while (opModeIsActive())
        {
            reflectedLight = opticalDistanceSensor.getLightDetected();

            this.telemetry.update();
            this.idle();
        }
    }

    //----------------------------------------------------------------------------------------------
    // dashboard configuration
    //----------------------------------------------------------------------------------------------

    void composeDashboard()
    {
        // The default dashboard update rate is a little to slow for us, so we update faster
        telemetry.setUpdateIntervalMs(200);

        // send the info back to driver station using telemetry function.
        telemetry.addLine(
                telemetry.item("Reflected light: ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return reflectedLight;
                    }
                })
        );
    }
}
