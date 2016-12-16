package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;


import static android.os.SystemClock.sleep;

/**
 * Created by the Falconeers 10820
 */
public class ScorpioAuto extends LinearOpMode {
    private final double diameterOfWheel = 8;
    private final double circOfWheel = diameterOfWheel * Math.PI;
    private final double disBetweenWheels = 15.85;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor flywheelLeft;
    private DcMotor flywheelRight;
    private DcMotor sweeper;
    private Servo pushRed;
    private Servo pushBlue;
    private ColorSensor whiteLine;
    private ColorSensor beacon;
    private TouchSensor touchSensor;


    private final short FORWARDS = 1;
    private final short BACKWARDS = -1;
    private final short BRAKE = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        this.waitForStart();
        frontLeft = hardwareMap.dcMotor.get("motor_frontLeft");
        frontRight = hardwareMap.dcMotor.get("motor_frontRight");
        backLeft = hardwareMap.dcMotor.get("motor_backLeft");
        backRight = hardwareMap.dcMotor.get("motor_backRight");
        flywheelRight = hardwareMap.dcMotor.get("motor_flywheelRight");
        flywheelLeft = hardwareMap.dcMotor.get("motor_flywheelLeft");
        sweeper = hardwareMap.dcMotor.get("motor_sweeper");
        pushRed = hardwareMap.servo.get("red_servo");
        pushBlue = hardwareMap.servo.get("blue_servo");
        whiteLine = hardwareMap.colorSensor.get("white_color");
        beacon = hardwareMap.colorSensor.get("beacon_sensor");
        touchSensor = hardwareMap.touchSensor.get("touch");

        float hsvValues[] = {0F,0F,0F};
        final float values[] = hsvValues;
        boolean bPrevState = false;
        boolean bCurrState = false;
        boolean bLedOn = false;
        beacon.enableLed(bLedOn);
        whiteLine.enableLed( true );
        while (opModeIsActive()) {

            // check the status of the x button on either gamepad.
            bCurrState = gamepad1.x;

            // check for button state transitions.
            if ((bCurrState) && (!bPrevState)) {

                // button is transitioning to a pressed state. So Toggle LED
                bLedOn = !bLedOn;
                beacon.enableLed( bLedOn );
            }

            // update previous state variable.
            bPrevState = bCurrState;

            // convert the RGB values to HSV values.
            Color.RGBToHSV( beacon.red() * 8, beacon.green() * 8, beacon.blue() * 8, hsvValues );

//       send the info back to driver station using telemetry function.
            telemetry.addData( "LED", bLedOn ? "On" : "Off" );
//            while (range.getDistance( DistanceUnit.INCH) >= 0.5);
            if(whiteLine.blue() >= 150 && whiteLine.red() >=150 && whiteLine.green() >= 150){
                move(0.1);
            }else {
                turn(-0.5);
            }
            //      telemetry.addData("Clear", colorSensor.alpha());
//            telemetry.addData( "Red  ", beacon.red() );
//            telemetry.addData( "Green", beacon.green() );
//            telemetry.addData( "Blue ", beacon.blue() );
//            telemetry.addData( "Hue", hsvValues[0] );
            Color.RGBToHSV( whiteLine.red() * 8, whiteLine.green() * 8, whiteLine.blue() * 8, hsvValues );
            //send the info back to driver station using telemetry function.



            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.

            telemetry.update();
            idle(); // Always call i
        }}
    private void move(double d) {
        long time = (long) Math.abs(d / (circOfWheel));
        if (d > 0) drive(FORWARDS);
        else drive(BACKWARDS);
        waitTime(time);
        drive(BRAKE);
    }

    private long neededTime(double deg) {
        return (long) (1000 * (((disBetweenWheels / 2) * Math.abs((3 / 4) * deg * Math.PI / 180)) / circOfWheel));
    }
    private void run(){
        turn(51.3402);
        move(6.40312);
        turn(360 - 51.3402);

//        while (!touch.isPressed()){
//            whiteLine();
//        }
}
    private void turn(double deg) {
        long time = neededTime(deg);
        if (deg > 0) allWheels((short) -1);
        else allWheels((short) 1);
        try {
            wait(time);
        } catch (Exception ignored) {
        }
        drive(BRAKE);
    }

    private void allWheels(short s) {
        frontLeft.setPower(s);
        backLeft.setPower(s);
        frontRight.setPower(s);
        backRight.setPower(s);
    }

    private void drive(short s) {
        frontLeft.setPower(-s);
        backLeft.setPower(-s);
        frontRight.setPower(s);
        backRight.setPower(s);
    }
    private void whiteLine(){
        if(whiteLine.blue() >= 150 && whiteLine.red() >=150 && whiteLine.green() >= 150){
            move(0.1);
        } else {
            turn(-0.5);
        }

    }
    private void derR(){
        double isDerR = 1;
        frontLeft.setPower(isDerR);
        backLeft.setPower(-isDerR);
        frontRight.setPower(-isDerR);
        backRight.setPower(isDerR);
    }
    private void derL(){
        double isDerL = -1;
        frontLeft.setPower(-isDerL);
        backLeft.setPower(isDerL);
        frontRight.setPower(isDerL);
        backRight.setPower(-isDerL);
    }
    private void launchBall(double powerMod){
        flywheelLeft.setPower(powerMod);
        flywheelRight.setPower(powerMod);

    }
    private boolean rBeaconTest(){
        return beacon.red() > 200;
    }

    private void waitTime(long ms) {
        this.sleep(ms);
    }
    private void sweep() {
        while(!touchSensor.isPressed()) sweeper.setPower(gamepad2.left_trigger);
        sweeper.setPower(-gamepad2.left_trigger);
        sleep(2000);
        sweeper.setPower(0);
    }
}
