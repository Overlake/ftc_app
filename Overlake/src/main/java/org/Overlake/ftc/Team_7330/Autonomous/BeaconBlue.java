package org.overlake.ftc.team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.interfaces.Autonomous;
import org.overlake.ftc.team_7330.Autonomous.AutonomousOpMode;
import org.overlake.ftc.team_7330.Testing.HueData;
import org.overlake.ftc.team_7330.Testing.ColorSensorData;

/**
 * Created by Ruthie Nordhoff on 1/12/2016.
 */

@Autonomous
public class BeaconBlue extends AutonomousOpMode {

    @Override protected void main() throws InterruptedException {
        waitForStart();
        initializeAllDevices();

        ColorSensorData[] data = ColorSensorData.fromFile(AutonomousOpMode.FILE_NAME);
        double initialHeading = imu.getAngularOrientation().heading;

        driveWithEncoders(-3.7, -.7); // not sure how far, travel most of the way across the field

        //drive until the right color sensor sees blue
        motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        motorFrontRight.setPower(-.15);
        motorBackRight.setPower(-.15);
        motorFrontLeft.setPower(-.15);
        motorBackLeft.setPower(-.15);

        while(data[0].blueTape.isHue(convertColor(sensorRGB.red(),sensorRGB.green(),sensorRGB.blue())));
        {
        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);

        driveWithEncoders(-.25, -.3);

        //this won't work going around the circle
        turnToTargetHeading(initialHeading - 45.0, .8);

        driveWithEncoders(.5, .5);

        //drive until the white line
        motorFrontRight.setPower(.15);
        motorBackRight.setPower(.15);
        motorFrontLeft.setPower(.15);
        motorBackLeft.setPower(.15);

        while(!data[0].whiteTape.isHue(convertColor(sensorRGB.red(),sensorRGB.green(),sensorRGB.blue())));
        {
        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);

        turnToTargetHeading(initialHeading + 135.0, .6);
    }

}
