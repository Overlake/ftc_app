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

    IBNO055IMU imu;
    IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

    @Override protected void main() throws InterruptedException
    {
        // Configure the dashboard however we want it
        this.configureDashboard();

        parameters.angleunit = IBNO055IMU.ANGLEUNIT.DEGREES;
        parameters.accelunit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "BNO055";
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        // Enable reporting of position using the naive integrator
        imu.startAccelerationIntegration(new Position(), new Velocity());

        // Initialize our hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names you assigned during the robot configuration
        // step you did in the FTC Robot Controller app on the phone.
        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");

        // One of the two motors (here, the left) should be set to reversed direction
        // so that it can take the same power level values as the other motor.
        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        // Wait until we've been given the ok to go
        this.waitForStart();

        double ticks = 5000;
        double meters = 2;
        double rightPower = 0.8;
        double leftPower = 0.8;
        driveWithEncoders(ticks, rightPower, leftPower, false);
    }

    void driveWithEncoders(double distance, double rightPower, double leftPower, boolean useIMU)
    {
        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
               this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
               this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
               this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
        }

        // Configure the knobs of the hardware according to how you've wired your
        // robot. Here, we assume that there are no encoders connected to the motors,
        // so we inform the motor objects of that fact.
        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS &&
               this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS &&
               this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS &&
               this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS)
        {
        }

        int tempDistance;
        if (useIMU)
        {
            tempDistance = Integer.MAX_VALUE;
        }
        else
        {
            tempDistance = (int)distance;
        }

        this.motorFrontRight.setTargetPosition(tempDistance);
        this.motorBackRight.setTargetPosition(tempDistance);
        this.motorFrontLeft.setTargetPosition(tempDistance);
        this.motorBackLeft.setTargetPosition(tempDistance);

        while (this.motorFrontRight.getTargetPosition() != tempDistance &&
               this.motorBackRight.getTargetPosition() != tempDistance &&
               this.motorFrontLeft.getTargetPosition() != tempDistance &&
               this.motorBackLeft.getTargetPosition() != tempDistance)
        {
        }

        this.motorFrontRight.setPower(rightPower);
        this.motorBackRight.setPower(rightPower);
        this.motorFrontLeft.setPower(leftPower);
        this.motorBackLeft.setPower(leftPower);

        while (this.motorFrontRight.getPower() != rightPower &&
                this.motorBackRight.getPower() != rightPower &&
                this.motorFrontLeft.getPower() != leftPower &&
                this.motorBackLeft.getPower() != leftPower)
        {
        }

        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION)
        {
        }

        if (useIMU)
        {
            // TODO: is this x or y?
            while (imu.getPosition().y < distance)
            {
            }
        }
        else
        {
            while (this.motorFrontRight.isBusy() && this.motorBackRight.isBusy() &&
                   this.motorFrontLeft.isBusy() && this.motorBackLeft.isBusy()) {
            }
        }

        this.motorFrontRight.setPower(0);
        this.motorBackRight.setPower(0);
        this.motorFrontLeft.setPower(0);
        this.motorBackLeft.setPower(0);

        while (this.motorFrontRight.getPower() != 0 &&
                this.motorBackRight.getPower() != 0 &&
                this.motorFrontLeft.getPower() != 0 &&
                this.motorBackLeft.getPower() != 0)
        {
        }

        this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
        }
    }

    void turn (double distance, double power)
    {

        double heading = imu.getAngularOrientation().heading;
        double targetHeading = heading + distance;

        if(targetHeading > 180)
        {
            targetHeading -= 180;
        }
        else if(targetHeading <0)
        {
            targetHeading += 180;
        }
        while(Math.abs(heading - targetHeading) > 0)
        {
            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

            while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                    this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                    this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                    this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
            {
            }

            // Configure the knobs of the hardware according to how you've wired your
            // robot. Here, we assume that there are no encoders connected to the motors,
            // so we inform the motor objects of that fact.
            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

            while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS &&
                    this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS &&
                    this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS &&
                    this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RUN_USING_ENCODERS)
            {
            }

            int tempDistance;
            if (targetHeading > heading)
            {
                tempDistance = Integer.MAX_VALUE;
            }
            else
            {
                tempDistance = Integer.MIN_VALUE;
            }

            this.motorFrontRight.setTargetPosition(tempDistance);
            this.motorBackRight.setTargetPosition(tempDistance);
            this.motorFrontLeft.setTargetPosition(tempDistance);
            this.motorBackLeft.setTargetPosition(tempDistance);

            while (this.motorFrontRight.getTargetPosition() != tempDistance &&
                    this.motorBackRight.getTargetPosition() != tempDistance &&
                    this.motorFrontLeft.getTargetPosition() != tempDistance &&
                    this.motorBackLeft.getTargetPosition() != tempDistance)
            {
            }

            this.motorFrontRight.setPower(rightPower);
            this.motorBackRight.setPower(rightPower);
            this.motorFrontLeft.setPower(leftPower);
            this.motorBackLeft.setPower(leftPower);

            while (this.motorFrontRight.getPower() != rightPower &&
                    this.motorBackRight.getPower() != rightPower &&
                    this.motorFrontLeft.getPower() != leftPower &&
                    this.motorBackLeft.getPower() != leftPower)
            {
            }

            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

            while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                    this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                    this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                    this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION)
            {
            }


            //return (Math.min(x,15)/15); **/
        }
        this.motorFrontRight.setPower(0);
        this.motorBackRight.setPower(0);
        this.motorFrontLeft.setPower(0);
        this.motorBackLeft.setPower(0);

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
