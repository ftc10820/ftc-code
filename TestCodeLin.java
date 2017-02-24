package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;


/**
 * Created by the Falconeers 10820
 */
@Autonomous(name = "TestCodeLin", group = "Experimental")
public class TestCodeLin extends LinearOpMode {
    private final double diameterOfWheel = 4;
    private final double circOfWheel = diameterOfWheel * Math.PI;
    private final double disBetweenWheels = 8;
    public final static I2cAddr BEACON_I2C_ADDRESS = I2cAddr.create8bit(0x7C);
    private final double RPM = 120;
    float hsvValues[] = {0F,0F,0F};
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor flywheelLeft;
    private DcMotor flywheelRight;
    private DcMotor sweeper;
    private Servo pushR;
    private Servo pushL;
    private ColorSensor lineSensor;
    private ColorSensor beaconSensor;
    private TouchSensor touchSensor;
    private GyroSensor gyroSensor;

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
        sweeper = hardwareMap.dcMotor.get("sweeper");
        pushR = hardwareMap.servo.get("LeftBeaconPusher");
        pushL = hardwareMap.servo.get("RightBeaconPusher");
        touchSensor = hardwareMap.touchSensor.get("touch");



        lineSensor = hardwareMap.colorSensor.get("white_color");
        beaconSensor = hardwareMap.colorSensor.get("beacon_sensor");

        gyroSensor = hardwareMap.gyroSensor.get("gyro");

        beaconSensor.setI2cAddress(BEACON_I2C_ADDRESS);

        pushR.setDirection(Servo.Direction.FORWARD);
        pushL.setDirection(Servo.Direction.FORWARD);

        pushR.scaleRange(0, .25);
//        pushL.scaleRange(0, .25);

        pushR.setPosition(0);
        pushL.setPosition(1);
//        pushL.scaleRange(0, .25);

        pushR.setPosition(0);
        pushL.setPosition(1);

        final float values[] = hsvValues;
        boolean bPrevState = false;
        boolean bCurrState = false;
        boolean bLedOn = false;
        beaconSensor.enableLed(bLedOn);
        lineSensor.enableLed(true);

        while (opModeIsActive()) {
            run();
            // check the status of the x button on either gamepad.
            bCurrState = gamepad1.x;

            // check for button state transitions.
            if ((bCurrState) && (!bPrevState)) {

                // button is transitioning to a pressed state. So Toggle LED
                bLedOn = !bLedOn;
                beaconSensor.enableLed( bLedOn );
            }

            // update previous state variable.
            bPrevState = bCurrState;

            // convert the RGB values to HSV values.
//            Color.RGBToHSV( beaconSensor.red() * 8, beaconSensor.green() * 8, beaconSensor.blue() * 8, hsvValues );

//       send the info back to driver station using telemetry function.
            telemetry.addData( "LED", bLedOn ? "On" : "Off" );
//            while (range.getDistance( DistanceUnit.INCH) >= 0.5);
//            if(lineSensor.blue() >= 150 && lineSensor.red() >=150 && lineSensor.green() >= 150){
//                move(0.1);
//            }else {
//                turn(-0.5);
//            }
            //      telemetry.addData("Clear", colorSensor.alpha());
//            telemetry.addData( "Red  ", beacon.red() );
//            telemetry.addData( "Green", beacon.green() );
//            telemetry.addData( "Blue ", beacon.blue() );
//            telemetry.addData( "Hue", hsvValues[0] );

            //send the info back to driver station using telemetry function.



            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.

            telemetry.update();
            idle(); // Always call i
        }}
    private void move(double d) {
        // d is measured in inches
        if (d == 0) return;
        //                                        sec to milli  rpm   per sec
        long time = (long) (Math.abs(d / (circOfWheel)) * 1000 /(RPM / 60));
        if (d > 0) drive(FORWARDS);
        else drive(BACKWARDS);
        waitTime(time);
        drive(BRAKE);
    }

    //    private long neededTime(double deg) {
//        return (long) (1000 * (((disBetweenWheels / 2) * Math.abs((3 / 4) * deg * Math.PI / 180)) / circOfWheel));
//    }
    private void goWhiteLine(){
        final long SCAN_DURATION = Integer.MAX_VALUE;
        final double SPEED_LEFT = 0.5;

        der(SPEED_LEFT, 0);

        long timeLimit = System.currentTimeMillis() + SCAN_DURATION;
        while (System.currentTimeMillis() < timeLimit && !isWhiteLine());

        if(!isWhiteLine()) {
            der(-SPEED_LEFT, 0);
            timeLimit = System.currentTimeMillis() + 2 * SCAN_DURATION;
            while (System.currentTimeMillis() < timeLimit && !isWhiteLine());
        }

        drive(BRAKE);
        if(isWhiteLine()) {
            telemetry.addData("found white line", true);
            return;
        }

        else
            telemetry.addData("found white line", false);
    }
    private boolean isWhiteLine() {
        int r = lineSensor.red() * 8;
        int g = lineSensor.green() * 8;
        int b = lineSensor.blue() * 8;
//        int a = lineSensor.alpha();
//        int argb = lineSensor.argb();
//        telemetry.addData("isWhiteLine-red", r);
//        telemetry.addData("isWhiteLine-green", g);
//        telemetry.addData("isWhiteLine-blue", b);
////        telemetry.addData("isWhiteLine-alpha", a);
////        telemetry.addData("isWhiteLine-argb", argb);
//        Color.RGBToHSV( r, g ,b, hsvValues );
//        telemetry.addData("hue", hsvValues[0]);
//        telemetry.addData("saturation", hsvValues[1]);
//        telemetry.addData("brightness", hsvValues[2]);
//        boolean isWhite = hsvValues[1] < 15 && hsvValues[2] > 90;
//        telemetry.addData("isWhite", isWhite);
//        char hex[] = Integer.toHexString(argb).toCharArray();
//        telemetry.addData("hex", Integer.toHexString(argb));
//        int hexTotal = 0;
//        for (char h : hex){
//            hexTotal += hexToInt(h);
//        }
//
//        telemetry.addData("hexTotal", hexTotal);
//        telemetry.update();
        return r > 150 && b > 150 && g > 150;
    }
//    private int hexToInt(char h) {
//        switch (h){
//            case '1': return 1;
//            case '2': return 2;
//            case '3': return 3;
//            case '4': return 4;
//            case '5': return 5;
//            case '6': return 6;
//            case '7': return 7;
//            case '8': return 8;
//            case '9': return 9;
//            case 'a': return 10;
//            case 'b': return 11;
//            case 'c': return 12;
//            case 'd': return 13;
//            case 'e': return 14;
//            case 'f': return 15;
//            default: return 0;
//        }
//    }
    int loopTimes = 1;
    private void run(){
        //moves sweepers one flywheels are up to speed then it launches(1/2 a sec)
        launchBall();
        //aligning robot to beacon
        turn(45);
        move(6);
        turn(45);
//      //find white line(lateral motion)
        goWhiteLine();
//        //advance to beacon
        move(2.5);
//        // push red beacon closest to you
        pushBeacon('r');
//        // move back a little
        move(-2.5);
//        // advance to next beacon
        der(1,0);
        while(!isWhiteLine());
        drive(BRAKE);
        // advance to beacon farthest away
        move(2.5);
        // advance to beacon
        turn(-45);
        // push red beacon
        pushBeacon('r');
        // move to parking ramp
        der(1, 0.15);

    }
    //    private boolean
    private void pushLeft(){
        pushL.setPosition(1);
        waitTime(1000);
        pushL.setPosition(0);
    }
    private void resetServo(){
        pushR.setPosition(0);
        pushL.setPosition(0);
    }
    private void pushRight() {


        pushR.setPosition(.67);
        waitTime(1000);
        pushR.setPosition(1);

    }

    private void pushBeacon(final char COLOR){

        der(1,0);
        waitTime(250);
        drive(BRAKE);

        char color = beaconSensor.red() > (beaconSensor.blue() + beaconSensor.green()) ? 'r' : 'b';
        der(0,0);
        waitTime(250);
        drive(BRAKE);
        if(COLOR == color) pushLeft();
        else pushRight();

    }
    private void turn(double deg) {
        if (deg == 0) {
            return;
        }
        while (deg < 0) deg += 360;
        deg = (int) deg % 360;
        if (deg == 0) return;
        if (deg > 180) deg -= 360;

        final int HEADING = gyroSensor.getHeading();
        int target = HEADING + (int) deg;

        while (target < 0) target += 360;
        target = target % 360;

        if (deg < 0) allWheels((short) -1);
        else allWheels((short) 1);
        for (;;){
            int heading = gyroSensor.getHeading();
            if (deg < 0){
                if (heading <= target && (heading > HEADING || HEADING > target)) break;
                else if(heading > HEADING && heading > target) break;
            } else {
                if (heading >= target && (heading < HEADING || HEADING < target)) break;
                else if(heading < HEADING && heading < target) break;
            }

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
//    private void findWhiteLine(){
//        if(lineSensor.blue() >= 240 && lineSensor.red() >= 240 && lineSensor.green() >= 240){
//            move(0.1);
//        } else {
//            turn(-0.5);
//        }
//
//    }

    private void der(double x, double y){
        double velocity = 1;
        double pi = Math.PI;
        double piDiv4 = pi/4;
        double theta = Math.atan2(y,x);
        while(theta < 0) theta += Math.PI * 2;
        frontLeft.setPower(-velocity * Math.sin(theta + piDiv4));
        backLeft.setPower(velocity * Math.cos(theta + piDiv4));
        frontRight.setPower(-velocity * Math.cos(theta + piDiv4));
        backRight.setPower(velocity * Math.sin(theta + piDiv4));
    }


    private void launchBall(){
        Thread thread = new Thread(){
            public void run(){
                double powerMod = -1;

                flywheelLeft.setPower(powerMod);
                flywheelRight.setPower(powerMod);

                waitTime(1000);

                flywheelLeft.setPower(0);
                flywheelRight.setPower(0);
            }
        };
        thread.run();
    }
//    private void sweep() {
//        sweeper.setPower(-gamepad2.left_trigger);
//        if (gamepad2.a) {
//            sweeper.setPower(gamepad2.left_trigger);
//            telemetry.addData("sweeper: ", sweeper.getCurrentPosition());
//        }
//    }
//    private boolean isBeaconSensor(ColorSensor cs){
//        return cs.getI2cAddress() == BEACON_I2C_ADDRESS;
//    }


    private void waitTime(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
