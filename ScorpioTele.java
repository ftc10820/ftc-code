package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.ArrayList;

/**
 * Created by the Falconeers 10820
 */

public class ScorpioTele extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor flywheelLeft;
    private DcMotor flywheelRight;
    private DcMotor sweeper;
    private Servo pushR;
    private Servo pushL;
    private TouchSensor touchSensor;
    double startPos = 0;
    private ColorSensor lineSensor;
    //private GyroSensor gyroSensor;
    private final short BRAKE = 0;

    @Override
    public void init() {
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


        pushR.setDirection(Servo.Direction.FORWARD);
        pushL.setDirection(Servo.Direction.FORWARD);

        pushR.scaleRange(0, .25);
//        pushL.scaleRange(0, .25);

        pushR.setPosition(0);
        pushL.setPosition(1);
    }

    @Override
    public void loop() {
        double frontL = 0;
        double backL = 0;
        double frontR = 0;
        double backR = 0;
        double theta;

        double v0 = 0;
        //double pi = 3.1459265359;
        double piDiv4 = Math.PI / 4;
        boolean isRBPressed = gamepad1.right_bumper;

//        telemetry.addData("pushL pos", pushL.getPosition());
//        telemetry.addData("pushR pos", pushR.getPosition());


        // Forward - -2.3572....
        // Backward - 0.7843....
        // Left - 2.35....
        // Right - -0.786....


//        telemetry.addData("x", gamepad1.right_stick_x);
//        telemetry.addData("y", gamepad1.right_stick_y);
        if (gamepad2.y){
            goWhiteLine();
        }
        if (isRBPressed) {
            if (Math.abs(gamepad1.right_stick_x) >= .1 || Math.abs(gamepad1.right_stick_y) >= .1) {
                theta = Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x);
                while(theta < 0) theta += Math.PI * 2;
                telemetry.addData("theta", theta);
                double velocity = gamepad1.left_bumper ? .5 : 1;
                frontL = -velocity * Math.sin(theta + piDiv4) + v0;
                backL = velocity * Math.cos(theta + piDiv4) + v0;
                frontR = -velocity * Math.cos(theta + piDiv4) + v0;
                backR = velocity * Math.sin(theta + piDiv4) + v0;
            }
        } else {
            double mod = gamepad1.left_bumper? .5 : 1;
            frontL = gamepad1.right_stick_y * mod;
            backL = gamepad1.right_stick_y * mod;
            frontR = -gamepad1.left_stick_y * mod;
            backR = -gamepad1.left_stick_y * mod;
        }
        if (gamepad2.y){
            pushR.setPosition(1);
            pushL.setPosition(0);
        }
        sweep();
        double largestVal = Math.max(Math.max(Math.abs(frontL), Math.abs(backL)), (Math.max(Math.abs(frontR), Math.abs(backR))));
        if (largestVal > 1) {
            frontL /= largestVal;
            backL /= largestVal;
            frontR /= largestVal;
            backR /= largestVal;
        }
        if (gamepad2.right_trigger >= 0.1) launchBall();
        double degServo = gamepad2.left_stick_x;
        telemetry.addData("degServo ", degServo);

        if (gamepad2.right_bumper) {
            pushR.setPosition(1);
            waitTime(1000);
            pushR.setPosition(0);
        }
        if (gamepad1.b){
            telemetry.addData("isWhiteLine",isWhiteLine());
            int r = lineSensor.red() * 8;
            int g = lineSensor.green() * 8;
            int b = lineSensor.blue() * 8;

        telemetry.addData("isWhiteLine-red", r);
        telemetry.addData("isWhiteLine-green", g);
        telemetry.addData("isWhiteLine-blue", b);
        }
        if (gamepad2.left_bumper) {
            pushL.setPosition(.67);
            waitTime(1000);
            pushL.setPosition(1);
        }
//            if (gamepad1.left_bumper) der();
//            //else stopMoving();
        frontLeft.setPower(frontL);
        backLeft.setPower(backL);
        frontRight.setPower(frontR);
        backRight.setPower(backR);
    }


//    private void der(){
//        double velocity = 1;
//        //double pi = 3.1459265359;
//        double piDiv4 = Math.PI / 4;
//        double theta = -Math.PI / 2 + Math.atan2(gamepad1.right_stick_y,gamepad1.right_stick_x);
//        double frontL = 0;
//        double backL = 0;
//        double frontR = 0;
//        double backR = 0;
//        if(gamepad1.right_bumper) {
//            frontL = velocity * Math.sin(theta + piDiv4);
//            backL = velocity * Math.cos(theta + piDiv4);
//            frontR = velocity * Math.cos(theta + piDiv4);
//            backR =  velocity * Math.sin(theta + piDiv4);
//        }
//        double largestVal =  Math.max(Math.max(Math.abs(frontL),Math.abs(backL)), (Math.max(Math.abs(frontR),Math.abs(backR))));
//        if(largestVal > 1){
//            frontL /= largestVal;
//            backL /= largestVal;
//            frontR /= largestVal;
//            backR /= largestVal;
//        }
//    }

    private void launchBall() {
        double powerMod = gamepad2.right_trigger;

        if (gamepad2.x) {
            flywheelLeft.setPower(-powerMod);
            flywheelRight.setPower(powerMod);
        }
        else{
            flywheelLeft.setPower(0);
            flywheelRight.setPower(0);
        }

    }

    private void sweep() {
        sweeper.setPower(gamepad2.left_trigger);
        if (gamepad2.a)
            sweeper.setPower(-gamepad2.left_trigger);
    }
//        try {
//            sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        sweeper.setPower(0);

    private void stopMoving() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
    private void waitTime(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void goWhiteLine(){
        telemetry.addData("init goWhiteLne", true);
        telemetry.update();
        final long SCAN_DURATION = 1000;
        final double SPEED_LEFT = 1;
//        double newTime;

        if (isWhiteLine()) {
            der(-SPEED_LEFT, 0);
            while (isWhiteLine()){
                telemetry.addData(" move out of white line", true);
                telemetry.update();
            }

        }
        drive(BRAKE);
        long timeLimit = System.currentTimeMillis() + SCAN_DURATION;
        der(SPEED_LEFT,0);
        while (System.currentTimeMillis() < timeLimit && !isWhiteLine()) {
            telemetry.addData("moving to line", true);
            telemetry.update();
        }
        drive(BRAKE);

//        der(-SPEED_LEFT, 0);
//        timeLimit = System.currentTimeMillis() + SCAN_DURATION;
//        while (System.currentTimeMillis() < timeLimit && !isWhiteLine()) {
//            telemetry.addData("scan other side", true);
//            telemetry.update();
//            if (isWhiteLine())
//                drive(BRAKE);
//        }



//        ArrayList<Long> timeVal = new ArrayList<Long>();
//        der(-SPEED_LEFT, 0);
//        telemetry.addData("move to middle line", true);
//        telemetry.update();
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


    }
    private void der(double x, double y){
        double velocity = .5;
        double pi = Math.PI;
        double piDiv4 = pi/4;
        double theta = Math.atan2(y,x);
        while(theta < 0) theta += Math.PI * 2;
        frontLeft.setPower(-velocity * Math.sin(theta + piDiv4));
        backLeft.setPower(velocity * Math.cos(theta + piDiv4));
        frontRight.setPower(-velocity * Math.cos(theta + piDiv4));
        backRight.setPower(velocity * Math.sin(theta + piDiv4));
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
//        telemetry.addData("isWhiteLine", "%02x-%02x-%02x", r, g, b);
//        telemetry.update();
        return r > 150 && b > 150 && g > 150;
    }
    private void drive(short s) {
        frontLeft.setPower(-s);
        backLeft.setPower(-s);
        frontRight.setPower(s);
        backRight.setPower(s);
    }
}
