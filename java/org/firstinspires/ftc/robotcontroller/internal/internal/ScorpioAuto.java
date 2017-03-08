package org.firstinspires.ftc.robotcontroller.internal.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by the Falconeers 10820
 */

public class ScorpioAuto extends OpMode {

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
    //private GyroSensor gyroSensor;

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("motor_frontLeft");
        frontRight = hardwareMap.dcMotor.get("motor_frontRight");
        backLeft = hardwareMap.dcMotor.get("motor_backLeft");
        backRight = hardwareMap.dcMotor.get("motor_backRight");
        flywheelRight = hardwareMap.dcMotor.get("motor_flywheelRight");
        flywheelLeft = hardwareMap.dcMotor.get("motor_flywheelLeft");
        sweeper = hardwareMap.dcMotor.get("sweeper");
        pushL = hardwareMap.servo.get("LeftBeaconPusher");
        pushR = hardwareMap.servo.get("RightBeaconPusher");
        touchSensor = hardwareMap.touchSensor.get("SweeperLimitSwitch");
        //gyroSensor = hardwareMap.gyroSensor.get("");

        pushR.setDirection(Servo.Direction.REVERSE);
        pushL.setDirection(Servo.Direction.FORWARD);

        pushR.scaleRange(0.5, 0.75);
        pushL.scaleRange(0.5, 0.75);

        pushL.setPosition(0);
        pushR.setPosition(0);

    }

    @Override
    public void loop() {
        double frontL = 0;
        double backL = 0;
        double frontR = 0;
        double backR = 0;
        double theta;
        double velocity = 1;
        double v0 = 0;
        double pi = 3.1459265359;
        double piDiv4 = pi / 4;
        boolean isRBPressed = gamepad1.right_bumper;

        telemetry.addData("pushL pos", pushL.getPosition());
        telemetry.addData("pushR pos", pushR.getPosition());


        // Forward - -2.3572....
        // Backward - 0.7843....
        // Left - 2.35....
        // Right - -0.786....


//        telemetry.addData("x", gamepad1.right_stick_x);
//        telemetry.addData("y", gamepad1.right_stick_y);
        if (isRBPressed) {
            if (Math.abs(gamepad1.right_stick_x) >= .1 || Math.abs(gamepad1.right_stick_y) >= .1) {
                theta = Math.atan2(-gamepad1.right_stick_y, gamepad1.right_stick_x);
                while(theta < 0) theta += Math.PI * 2;
                telemetry.addData("theta", theta);
                frontL = -velocity * Math.sin(theta + piDiv4) + v0;
                backL = velocity * Math.cos(theta + piDiv4) + v0;
                frontR = -velocity * Math.cos(theta + piDiv4) + v0;
                backR = velocity * Math.sin(theta + piDiv4) + v0;
            }
        } else {
            frontL = gamepad1.right_stick_y;
            backL = gamepad1.right_stick_y;
            frontR = -gamepad1.left_stick_y;
            backR = -gamepad1.left_stick_y;
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
            pushL.setPosition(1);
            waitTime(1000);
            pushL.setPosition(0);
        }
        if (gamepad2.left_bumper) {
            pushL.setPosition(1);
            waitTime(1000);
            pushL.setPosition(0);
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
    }

    private void sweep() {
        sweeper.setPower(-gamepad2.left_trigger);
        if (gamepad2.a) {
            sweeper.setPower(gamepad2.left_trigger);
            telemetry.addData("sweeper: ", sweeper.getCurrentPosition());
        }
//        try {
//            sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        sweeper.setPower(0);
    }

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

}
