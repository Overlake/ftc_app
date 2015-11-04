package org.overlake.ftc.team_7330;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An example of a synchronous opmode that implements a simple drive-a-bot. 
 */
@TeleOp(name="EncoderTest")
@Disabled
public class EncoderTest extends SynchronousOpMode
{
    // All hardware variables can only be initialized inside the main() function,
    // not here at their member variable declarations.
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    @Override protected void main() throws InterruptedException
    {
        // Configure the dashboard however we want it
        this.configureDashboard();

        // Initialize our hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names you assigned during the robot configuration
        // step you did in the FTC Robot Controller app on the phone.
        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorRight");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorLeft");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorLeft");

        // One of the two motors (here, the left) should be set to reversed direction
        // so that it can take the same power level values as the other motor.
        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        // Wait until we've been given the ok to go
        this.waitForStart();

        int ticks = 5000;
        double rightPower = 0.8;
        double leftPower = 0.8;
        driveEncoder(ticks, rightPower, leftPower);
    }

    void driveEncoder(int ticks, double rightPower, double leftPower)
    {
        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        while (this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
            try
            {
                idle();
            }
            catch (Exception e)
            {
            }
        }

        // Configure the knobs of the hardware according to how you've wired your
        // robot. Here, we assume that there are no encoders connected to the motors,
        // so we inform the motor objects of that fact.
        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        this.motorFrontRight.setTargetPosition(ticks);
        this.motorBackRight.setTargetPosition(ticks);
        this.motorFrontLeft.setTargetPosition(ticks);
        this.motorBackLeft.setTargetPosition(ticks);

        this.motorFrontRight.setPower(rightPower);
        this.motorBackRight.setPower(rightPower);
        this.motorFrontLeft.setPower(leftPower);
        this.motorBackLeft.setPower(leftPower);

        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        while (this.motorFrontRight.isBusy() && this.motorBackRight.isBusy() &&
               this.motorFrontLeft.isBusy() && this.motorBackLeft.isBusy())
        {
        }

        this.motorFrontRight.setPower(0);
        this.motorBackRight.setPower(0);
        this.motorFrontLeft.setPower(0);
        this.motorBackLeft.setPower(0);

        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    void configureDashboard()
    {
    }

    // Handy functions for formatting data for the dashboard
    String format(double d)
    {
        return String.format("%.1f", d);
    }
}
