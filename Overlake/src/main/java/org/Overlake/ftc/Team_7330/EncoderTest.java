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

    IBNO055IMU imu;
    IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

    @Override protected void main() throws InterruptedException
    {
        this.composeDashboard();

        parameters.angleunit = IBNO055IMU.ANGLEUNIT.DEGREES;
        parameters.accelunit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "BNO055";
        // imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        // Enable reporting of position using the naive integrator
        // imu.startAccelerationIntegration(new Position(), new Velocity());

        // Initialize our hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names you assigned during the robot configuration
        // step you did in the FTC Robot Controller app on the phone.
        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");
        telemetry.update();

        // One of the two motors (here, the left) should be set to reversed direction
        // so that it can take the same power level values as the other motor.
        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }

        double ticks = 5000;
        double rightPower = 0.8;
        double leftPower = 0.8;
        driveWithEncoders(ticks, rightPower, leftPower, false);
    }

    void driveWithEncoders(double distance, double rightPower, double leftPower, boolean useIMU)
    {
        telemetry.update();

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
               this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
               this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
               this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }

        telemetry.update();

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION &&
                this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RUN_TO_POSITION)
        {
            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }

        telemetry.update();

        int tempDistance;
        if (useIMU)
        {
            tempDistance = Integer.MAX_VALUE;
        }
        else
        {
            tempDistance = (int)distance;
        }

        telemetry.update();

        while (this.motorFrontRight.getTargetPosition() != tempDistance &&
               this.motorBackRight.getTargetPosition() != tempDistance &&
               this.motorFrontLeft.getTargetPosition() != tempDistance &&
               this.motorBackLeft.getTargetPosition() != tempDistance)
        {
            this.motorFrontRight.setTargetPosition(tempDistance);
            this.motorBackRight.setTargetPosition(tempDistance);
            this.motorFrontLeft.setTargetPosition(tempDistance);
            this.motorBackLeft.setTargetPosition(tempDistance);
        }

        telemetry.update();

        while (this.motorFrontRight.getPower() != rightPower &&
                this.motorBackRight.getPower() != rightPower &&
                this.motorFrontLeft.getPower() != leftPower &&
                this.motorBackLeft.getPower() != leftPower)
        {
            this.motorFrontRight.setPower(rightPower);
            this.motorBackRight.setPower(rightPower);
            this.motorFrontLeft.setPower(leftPower);
            this.motorBackLeft.setPower(leftPower);
        }

        telemetry.update();

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

        telemetry.update();

        while (this.motorFrontRight.getPower() != 0 &&
                this.motorBackRight.getPower() != 0 &&
                this.motorFrontLeft.getPower() != 0 &&
                this.motorBackLeft.getPower() != 0)
        {
            this.motorFrontRight.setPower(0);
            this.motorBackRight.setPower(0);
            this.motorFrontLeft.setPower(0);
            this.motorBackLeft.setPower(0);
        }

        telemetry.update();

        while (this.motorFrontRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorBackRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorFrontLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS &&
                this.motorBackLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
            this.motorFrontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorFrontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            this.motorBackLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }

        telemetry.update();
    }

    /*
    void turn (double turnDistance, double power, double rightPower, double leftPower)
    {
        double heading = imu.getAngularOrientation().heading;
        double targetHeading = heading + turnDistance;

    }
    */


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
                telemetry.item("FR Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontRight.getTargetPosition();
                    }
                }));
    }

    // Handy functions for formatting data for the dashboard
    String format(double d)
    {
        return String.format("%.1f", d);
    }
}
