package org.overlake.ftc.team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.interfaces.Autonomous;
import org.overlake.ftc.team_7330.Autonomous.AutonomousOpMode;
import org.overlake.ftc.team_7330.Testing.HueData;
import org.overlake.ftc.team_7330.Testing.ColorSensorData;
import org.swerverobotics.library.interfaces.IFunc;

/**
 * Created by Ruthie Nordhoff on 1/12/2016.
 */

@Autonomous
public class BeaconBlue extends AutonomousOpMode
{
    String message = "Program starting!";
    double hue = 0;

    @Override protected void main() throws InterruptedException
    {
        initializeAllDevices();
        waitForStart();

        ColorSensorData[] data = ColorSensorData.fromFile(AutonomousOpMode.FILE_NAME);
        double initialHeading = imu.getAngularOrientation().heading;

        // travel most of the way accross the field
        message = "Driving accross the field";
        driveWithEncoders(-3.7, -.7);

        // drive until the color sensor sees blue
        message = "Looking for the blue tape";
        motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        motorFrontRight.setPower(-.15);
        motorBackRight.setPower(-.15);
        motorFrontLeft.setPower(-.15);
        motorBackLeft.setPower(-.15);

        hue = convertColorToHue(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue());
        while (!data[0].blueTape.isHue(hue))
        {
            hue = convertColorToHue(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue());
            waitMs(10);
        }

        message = "Found blue tape; driving a little bit more";
        driveWithEncoders(-.05, -.15);

        // turn to be parallel with the blue tape
        message = "Turning";
        turnToTargetHeading(initialHeading - 45.0, .8);

        //drive until the white line
        message = "Looking for the white tape";
        motorFrontRight.setPower(.15);
        motorBackRight.setPower(.15);
        motorFrontLeft.setPower(.15);
        motorBackLeft.setPower(.15);

        hue = convertColorToHue(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue());
        while(!data[0].whiteTape.isHue(hue))
        {
            hue = convertColorToHue(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue());
            waitMs(10);
        }

        message = "Found white tape; driving a little bit more";
        driveWithEncoders(.05, .15);

        // turn to be parallel with the white tape
        message = "Turning";
        turnToTargetHeading(initialHeading + 45.0, .8);
    }

    void composeDashboard()
    {
        telemetry.setUpdateIntervalMs(200);

        telemetry.addLine(
                telemetry.item("Red: ", new IFunc<Object>() {
                    public Object value() {
                        return sensorRGB.red();
                    }
                }),
                telemetry.item("Green: ", new IFunc<Object>() {
                    public Object value() {
                        return sensorRGB.green();
                    }
                }),
                telemetry.item("Blue: ", new IFunc<Object>() {
                    public Object value() {
                        return sensorRGB.blue();
                    }
                })
        );

        telemetry.addLine(
                telemetry.item("Hue: ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return hue;
                    }
                })
        );

        telemetry.addLine(
                telemetry.item("Message: ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return message;
                    }
                })
        );
    }
}
