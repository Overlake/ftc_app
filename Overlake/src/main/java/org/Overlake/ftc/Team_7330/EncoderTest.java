package org.overlake.ftc.team_7330;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.util.EmptyStackException;

@TeleOp(name="EncoderTest")
public class EncoderTest extends SynchronousOpMode
{
    // All hardware variables can only be initialized inside the main() function,
    // not here at their member variable declarations.
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    double heading;
    double targetHeading;
    double newPower;

    IBNO055IMU imu;
    IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

    @Override protected void main() throws InterruptedException
    {
        this.composeDashboard();
        telemetry.update();

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
        // telemetry.update();

        // One of the two motors (here, the left) should be set to reversed direction
        // so that it can take the same power level values as the other motor.
        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        try
        {
            turn(90,.5);
        }
        catch (Exception e)
        {
            throw new EmptyStackException();
        }
    }

    // to turn right, degrees and power should both be positive
    // to turn left, degrees - but not power - should be negative
    void turn(double degrees, double power)
    {
        heading = imu.getAngularOrientation().heading;
        targetHeading = heading + degrees;

        if (targetHeading > 180)
        {
            targetHeading -= 360;
        }
        else if (targetHeading < -180)
        {
            targetHeading += 360;
        }

        motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (Math.abs(targetHeading - heading) > .5) {
            telemetry.update();
            newPower = getPower(power, targetHeading, heading);
            this.motorFrontRight.setPower(-newPower);
            this.motorBackRight.setPower(-newPower);
            this.motorFrontLeft.setPower(newPower);
            this.motorBackLeft.setPower(newPower);
            //TODO: add a wait
            heading = imu.getAngularOrientation().heading;
        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    double getPower(double power, double targetHeading, double heading) {
        double diff = targetHeading - heading;
        if (Math.abs(diff) > 180)
        {
            diff += (-360 * (diff / Math.abs(diff)));
        }

        return ((diff / Math.abs(diff)) * (Math.log((Math.min(Math.E - 1, (Math.E - 1) * Math.abs(diff) / 15.0) + 1.0)) * power));
    }

    void composeDashboard()
    {
        telemetry.addLine(
                telemetry.item("BL Run mode ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorBackLeft.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("BR Run mode ", new IFunc<Object>() {
                    public Object value() {
                        return motorBackRight.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FL Run mode ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontLeft.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FR Run mode ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontRight.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("BL Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorBackLeft.getTargetPosition();
                    }
                }));

        telemetry.addLine(
                telemetry.item("BR Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorBackRight.getTargetPosition();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FL Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontLeft.getTargetPosition();
                    }
                }));

        telemetry.addLine(
                telemetry.item("Heading ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return heading;
                    }
                }));

        telemetry.addLine(
                telemetry.item("Target heading ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return targetHeading;
                    }
                }));

        telemetry.addLine(
                telemetry.item("Power ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return newPower;
                    }
                }));
    }

    // Handy functions for formatting data for the dashboard
    String format(double d)
    {
        return String.format("%.1f", d);
    }
}
