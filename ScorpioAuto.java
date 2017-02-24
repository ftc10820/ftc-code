package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.ArrayList;


/**
 * Created by the Falconeers 10820
 */
@Autonomous(name = "Auto", group = "Experimental")
public class ScorpioAuto extends LinearOpMode {
    private final double diameterOfWheel = 4;
    private final double circOfWheel = diameterOfWheel * Math.PI;
    //    private final double disBetweenWheels = 8;
//    public final static I2cAddr BEACON_I2C_ADDRESS = I2cAddr.create8bit(0x7C);
    private final double RPM = 120;
    float hsvValues[] = {0F, 0F, 0F};
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
//    private DcMotor flywheelLeft;
//    private DcMotor flywheelRight;
//    private DcMotor sweeper;
//    private Servo pushR;
//    private Servo pushL;
//    private ColorSensor lineSensor;
////    private ColorSensor beaconSensor;
//    private TouchSensor touchSensor;
    private GyroSensor gyroSensor;

    private final short FORWARDS = 1;
    private final short BACKWARDS = -1;
    private final short BRAKE = 0;
//    I2cDevice lineSensor;
//    private I2cDevice beaconSensor;
//    byte[] beaconSensorCache;
////    I2cDeviceSynch lineSensorReader;
//    private I2cDeviceSynch beaconSensorReader;



    @Override
    public void runOpMode() throws InterruptedException {
        this.waitForStart();
        frontLeft = hardwareMap.dcMotor.get("motor_frontLeft");
        frontRight = hardwareMap.dcMotor.get("motor_frontRight");
        backLeft = hardwareMap.dcMotor.get("motor_backLeft");
        backRight = hardwareMap.dcMotor.get("motor_backRight");

//        flywheelRight = hardwareMap.dcMotor.get("motor_flywheelRight");
//        flywheelLeft = hardwareMap.dcMotor.get("motor_flywheelLeft");
//        sweeper = hardwareMap.dcMotor.get("sweeper");
//        pushR = hardwareMap.servo.get("LeftBeaconPusher");
//        pushL = hardwareMap.servo.get("RightBeaconPusher");
//        touchSensor = hardwareMap.touchSensor.get("touch");
        run();
////        lineSensorReader = new I2cDeviceSynchImpl(lineSensor, I2cAddr.create8bit(0x3a), false);
//        beaconSensorReader = new I2cDeviceSynchImpl(beaconSensor, I2cAddr.create8bit(0x3a), false);
//
////        lineSensorReader.engage();
//        beaconSensorReader.engage();
//
//        //runtime.reset();
//
////        lineSensorReader.write8(3, 0);    //Set the mode of the color sensor using LEDState
//        beaconSensorReader.write8(3, 1);    //Set the mode of the color sensor using LEDState
//
//        lineSensor = hardwareMap.colorSensor.get("white_color");
//        beaconSensor = hardwareMap.i2cDevice.get("beacon_sensor");
//
        gyroSensor = hardwareMap.gyroSensor.get("gyro");
//
////        beaconSensor.setI2cAddress(BEACON_I2C_ADDRESS);
//
//        pushR.setDirection(Servo.Direction.FORWARD);
//        pushL.setDirection(Servo.Direction.FORWARD);
//
//        pushR.scaleRange(0, .25);
////        pushL.scaleRange(0, .25);
//
//        pushR.setPosition(0);
//        pushL.setPosition(1);
////        pushL.scaleRange(0, .25);
//
//        pushR.setPosition(0);
//        pushL.setPosition(1);
//
//        final float values[] = hsvValues;
//        boolean bPrevState = false;
//        boolean bCurrState = false;
//        boolean bLedOn = false;
////        beaconSensor.enableLed(bLedOn);
//        lineSensor.enableLed(true);
//        run();
    }
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
//    private void goWhiteLine(){
//        final long SCAN_DURATION = Integer.MAX_VALUE;
//        final double SPEED_LEFT = 0.5;
//        double newTime;
//
//        if (isWhiteLine()) {
//            der(SPEED_LEFT, 0);
//            while (isWhiteLine()) ;
//            drive(BRAKE);
//        }
//
//        ArrayList<Long> timeVal = new ArrayList<Long>();
//        der(-SPEED_LEFT, 0);
//        boolean wasWhite = false;
//        boolean isWhite = false;
//        while (timeVal.size() < 2) {
//            isWhite = isWhiteLine();
//            if (isWhite != wasWhite) {
//                timeVal.add(System.currentTimeMillis());
//                wasWhite = isWhite;
//            }
//        }
//        long timeDiff = timeVal.get(1) - timeVal.get(0);
//        long moveTime = timeDiff / 2;
//        der(SPEED_LEFT, 0);
//        waitTime(moveTime);
//        drive(BRAKE);
//    }
//    private boolean isWhiteLine() {
//        int r = lineSensor.red() * 8;
//        int g = lineSensor.green() * 8;
//        int b = lineSensor.blue() * 8;
////        int a = lineSensor.alpha();
////        int argb = lineSensor.argb();
////        telemetry.addData("isWhiteLine-red", r);
////        telemetry.addData("isWhiteLine-green", g);
////        telemetry.addData("isWhiteLine-blue", b);
//////        telemetry.addData("isWhiteLine-alpha", a);
//////        telemetry.addData("isWhiteLine-argb", argb);
////        Color.RGBToHSV( r, g ,b, hsvValues );
////        telemetry.addData("hue", hsvValues[0]);
////        telemetry.addData("saturation", hsvValues[1]);
////        telemetry.addData("brightness", hsvValues[2]);
////        boolean isWhite = hsvValues[1] < 15 && hsvValues[2] > 90;
////        telemetry.addData("isWhite", isWhite);
////        char hex[] = Integer.toHexString(argb).toCharArray();
////        telemetry.addData("hex", Integer.toHexString(argb));
////        int hexTotal = 0;
////        for (char h : hex){
////            hexTotal += hexToInt(h);
////        }
////
////        telemetry.addData("hexTotal", hexTotal);
////        telemetry.update();
//        return r > 150 && b > 150 && g > 150;
//    }
//    //    private int hexToInt(char h) {
////        switch (h){
////            case '1': return 1;
////            case '2': return 2;
////            case '3': return 3;
////            case '4': return 4;
////            case '5': return 5;
////            case '6': return 6;
////            case '7': return 7;
////            case '8': return 8;
////            case '9': return 9;
////            case 'a': return 10;
////            case 'b': return 11;
////            case 'c': return 12;
////            case 'd': return 13;
////            case 'e': return 14;
////            case 'f': return 15;
////            default: return 0;
////        }
////    }
//    int loopTimes = 1;
    private void run(){
//       move(6*12);
//       drive(BRAKE);

// park code
//        waitTime(10000);
//        move(3*12);
//        turn(-45);
//        move(1);

        waitTime(10000);
        move(5*6);

//        pushL.setPosition(0);
}
    //    private boolean
//    private void pushLeft(){
//        pushL.setPosition(1);
//        waitTime(1000);
//    }
//    private void resetServo(){
//        pushR.setPosition(0);
//        pushL.setPosition(0);
//    }
//    private void pushRight() {
//
//        pushR.setPosition(.67);
//        waitTime(1000);
//        pushR.setPosition(1);
//
//    }

//    private void pushBeacon(final char COLOR){
//        der(1,0);
//        waitTime(250);
//        drive(BRAKE);
//
//        beaconSensorCache = beaconSensorReader.read(0x04, 1);
//        int color = beaconSensorCache[0] & 0xFF;
//        der(0,0);
//        waitTime(250);
//        drive(BRAKE);
//        if(COLOR == 'r') {
//            if (9 < color && 11 > color) {
//                pushLeft();
//            } else if(1 < color && color < 4) pushRight();
//        }
//
//
//    }
    private void turn(double deg) {
        //left
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


//
//
//    private void launchBall(){
//        Thread thread = new Thread(){
//            public void run(){
//                double powerMod = -1;
//
//                flywheelLeft.setPower(powerMod);
//                flywheelRight.setPower(powerMod);
//
//                waitTime(1000);
//
//                flywheelLeft.setPower(0);
//                flywheelRight.setPower(0);
//            }
//        };
//        thread.run();
//    }
////    private void sweep() {
////        sweeper.setPower(-gamepad2.left_trigger);
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
