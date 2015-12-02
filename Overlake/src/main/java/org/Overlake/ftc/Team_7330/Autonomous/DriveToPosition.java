package org.overlake.ftc.team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;
/**
 * This simple OpMode illustrates how to drive autonomously a certain distance using encoders.
 *
 * The OpMode works with both legacy and modern motor controllers. It expects two motors,
 * named "motorLeft" and "motorRight".
 */
@Autonomous(name="Drive to position", group="Swerve Examples")
public class DriveToPosition extends SynchronousOpMode
{
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    IBNO055IMU imu;
    IBNO055IMU.Parameters   parameters = new IBNO055IMU.Parameters();

    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;

    //----------------------------------------------------------------------------------------------
    // Main loop
    //----------------------------------------------------------------------------------------------

    @Override protected void main() throws InterruptedException
    {
        this.composeDashboard();
        // We are expecting the IMU to be attached to an I2C port on  a core device interface
        // module and named "imu". Retrieve that raw I2cDevice and then wrap it in an object that
        // semantically understands this particular kind of sensor.
        parameters.angleunit      = IBNO055IMU.ANGLEUNIT.DEGREES;
        parameters.accelunit      = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag     = "BNO055";
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        // Enable reporting of position using the naive integrator
        imu.startAccelerationIntegration(new Position(), new Velocity());

        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");

        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        // Reset the encoders to zero.
        //
        // Note: we can do this, or not do this. The rest of the code is based only on *increments*
        // to the positions, so we could live with whatever the encoders happen to presently read
        // just fine. That said, it's a little easier to interpret telemetry if we start them off
        // at zero, so we do that. But try commenting these lines out, and observe that the code
        // continues to work just fine, even as you run the OpMode multiple times.
        this.motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        // Drive forward a while. The parameters here are arbitrary; they're just for illustration



        //drive forward a little bit

        runToPosition(5, 5);

        turn(90.0 - imu.getAngularOrientation().heading, .6);

        //drive forward remaining distance to the wall

        //drop climbers
    }

    void runToPosition(double x, double y)
    {
        double robotHeading = imu.getAngularOrientation().heading;
        //does arctan work in radians or degrees??
        double targetHeading = Math.atan2(x - imu.getPosition().x, y - imu.getPosition().y);

        turn(targetHeading - robotHeading, 0.8);

        this.motorFrontRight.setPower(0.6);
        this.motorFrontRight.setPower(0.6);
        this.motorFrontRight.setPower(0.6);
        this.motorFrontRight.setPower(0.6);

        while ((Math.abs(imu.getPosition().x - x) > 0.05 && Math.abs(imu.getPosition().y - y) > 0.05))
        {
            robotHeading = imu.getAngularOrientation().heading;
            targetHeading = Math.atan2(x - imu.getPosition().x, y - imu.getPosition().y);
            //also we are going to get crashes if our position is exactly Y, bc arctan won't exist
            //in this if statement, i'm worried if target heading = 179 and robotHeading = -179
            //if we ever are going directly downwards or pass the roundabout point
            if (Math.abs(targetHeading - robotHeading) > 2.5)
            {
                turn(targetHeading - robotHeading, 0.8);
            }
        }

        this.motorFrontRight.setPower(0);
        this.motorFrontRight.setPower(0);
        this.motorFrontRight.setPower(0);
        this.motorFrontRight.setPower(0);
    }

    void turn(double heading, double power)
    {

    }

    void composeDashboard()
    {
    }
}
