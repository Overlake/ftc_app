package org.overlake.ftc.team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.overlake.ftc.team_7330.Testing.HueData;
import org.overlake.ftc.team_7330.Testing.ColorSensorData;
import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.IBNO055IMU;
import org.swerverobotics.library.interfaces.Position;
import org.swerverobotics.library.interfaces.Velocity;

/**
 * Created by jacks on 11/13/2015.
 */
public abstract class AutonomousOpMode extends SynchronousOpMode {
    final int encRotation = 1120;
    ColorSensorData data;

    double heading = 0;
    double targetHeading = 0;
    double newPower = 0;

    IBNO055IMU imu;
    IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

    DeviceInterfaceModule cdim;

    static final int LED_CHANNEL = 5;

    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    ColorSensor sensorRGB;

    public enum Color { Red, Blue, White }
    public enum Side { Left, Right }

    public static final String FILE_NAME = "/sdcard/FIRST/colorSensorData.txt";

    void driveWithEncoders(double revolutions, double power) throws InterruptedException
    {
        // How far are we to move, in ticks instead of revolutions?
        int denc = (int)Math.round(revolutions * encRotation);

        int frontLeftTarget = this.motorFrontLeft.getCurrentPosition() + denc;
        int frontRightTarget = this.motorFrontRight.getCurrentPosition() + denc;
        int backLeftTarget = this.motorBackLeft.getCurrentPosition() + denc;
        int backRightTarget = this.motorBackRight.getCurrentPosition() + denc;

        // Tell the motors where we are going
        this.motorFrontLeft.setTargetPosition(frontLeftTarget);
        this.motorFrontRight.setTargetPosition(frontRightTarget);
        this.motorBackLeft.setTargetPosition(backLeftTarget);
        this.motorBackRight.setTargetPosition(backRightTarget);

        // Set them a-going
        this.motorFrontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorFrontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Wait until they are done
        while (this.motorFrontLeft.isBusy() && this.motorFrontRight.isBusy() && this.motorBackRight.isBusy() && this.motorBackLeft.isBusy())
        {
            telemetry.update();
            this.idle();

            int minDistance = frontLeftTarget - this.motorFrontLeft.getCurrentPosition();
            minDistance = Math.min(minDistance, frontRightTarget - this.motorFrontRight.getCurrentPosition());
            minDistance = Math.min(minDistance, backLeftTarget - this.motorBackLeft.getCurrentPosition());
            minDistance = Math.min(minDistance, backRightTarget - this.motorBackRight.getCurrentPosition());

            double scaledPower = driveGetPower(power, minDistance);

            this.motorFrontLeft.setPower(scaledPower);
            this.motorFrontRight.setPower(scaledPower);
            this.motorBackLeft.setPower(scaledPower);
            this.motorBackRight.setPower(scaledPower);
        }

        // Now that we've arrived, kill the motors so they don't just sit there buzzing
        this.motorFrontLeft.setPower(0);
        this.motorFrontRight.setPower(0);
        this.motorBackLeft.setPower(0);
        this.motorBackRight.setPower(0);

        // Always leave the screen looking pretty
        telemetry.updateNow();
    }

    private double driveGetPower(double power, int distance)
    {
        return power * (((Math.min((double)distance, (double)encRotation) / (double)encRotation) * 0.9) + 0.1);
    }

    // TODO: Make the turn methods 'safer' at lower powers
    // turn takes in a relative nuumber of degrees
    // positive is right
    // negative is left
    void turn(double degrees, double power)
    {
        heading = imu.getAngularOrientation().heading;
        targetHeading = heading + degrees;

        turnToTargetHeading(targetHeading, power);
    }

    // turnToTargetHeading takes in an absolute number of degrees
    // positive is right
    // negative is left
    void turnToTargetHeading(double targetHeading, double power)
    {
        if (targetHeading > 360)
        {
            targetHeading -= 360;
        }
        else if (targetHeading < 0)
        {
            targetHeading += 360;
        }

        motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (Math.abs(computeDegrees(targetHeading, heading)) > 1)
        {
            telemetry.update();
            newPower = turnGetPower(power, computeDegrees(targetHeading, heading));
            telemetry.log.add("heading: " + heading);
            telemetry.log.add("target heading: " + targetHeading);
            telemetry.log.add("power: " + newPower);

            this.motorFrontRight.setPower(-newPower);
            this.motorBackRight.setPower(-newPower);
            this.motorFrontLeft.setPower(newPower);
            this.motorBackLeft.setPower(newPower);

            try
            {
                wait(50);
            }
            catch (Exception e)
            {
            }

            heading = imu.getAngularOrientation().heading;
        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    double computeDegrees(double targetHeading, double heading)
    {
        double diff = targetHeading - heading;
        if (Math.abs(diff) > 180)
        {
            diff += (-360 * (diff / Math.abs(diff)));
        }

        return diff;
    }

    private double turnGetPower(double power, double diff)
    {
        return ((diff / Math.abs(diff)) * (Math.log((Math.min(Math.E - 1, (Math.E - 1) * Math.abs(diff) / 130.0) + 1)) * power));
    }

    void initializeAllDevices()
    {
        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");


        this.motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        parameters.angleunit = IBNO055IMU.ANGLEUNIT.DEGREES;
        parameters.accelunit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "BNO055";
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        // Enable reporting of position using the naive integrator
        imu.startAccelerationIntegration(new Position(), new Velocity());

        hardwareMap.logDevices();

        // get a reference to our DeviceInterfaceModule object.
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        sensorRGB = hardwareMap.colorSensor.get("color");

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, true);
    }

    public static double convertColorToHue(int r, int g, int b)
    {
        double y = Math.sqrt(3) * (g - b);
        double x = 2 * r - g - b;
        return Math.atan2(y, x) * (360.0 / (2 * Math.PI));
    }

    public void waitMs(int ms)
    {
        try
        {
            wait(ms);
        }
        catch (Exception e)
        {
        }
    }
}
