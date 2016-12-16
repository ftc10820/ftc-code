package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import static java.lang.Thread.sleep;


/**
 * Created by the Falconeers 10820
 */
public class ScorpioTele extends OpMode{

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

    }

    @Override
    public void loop() {
        double frontL = 0;
        double backL = 0;
        double frontR = 0;
        double backR = 0;

        double velocity = 1;
        double v0 = 0;
        double pi = 3.1459265359;
        double piDiv4 = pi/4;
        double theta = pi/2 + Math.atan2(gamepad1.right_stick_y,gamepad1.right_stick_x);
        if(gamepad1.right_bumper) {
            frontL = velocity * Math.sin(theta + piDiv4) + v0;
            backL = velocity * Math.cos(theta + piDiv4) + v0;
            frontR = velocity * Math.cos(theta + piDiv4) + v0;
            backR =  velocity * Math.sin(theta + piDiv4) + v0;
        }else stopMoving();

        sweep();
        double largestVal =  Math.max(Math.max(Math.abs(frontL),Math.abs(backL)), (Math.max(Math.abs(frontR),Math.abs(backR))));
        if(largestVal > 1){
            frontL /= largestVal;
            backL /= largestVal;
            frontR /= largestVal;
            backR /= largestVal;
        }

        if(gamepad1.right_trigger >= 0.1) launchBall();
        double degServo = 0;
        double startPos = 0;
        if(gamepad2.right_bumper) pushR.setPosition(startPos + degServo);
        if(gamepad2.left_bumper) pushL.setPosition(startPos - degServo);


            if (gamepad1.left_bumper) der();
            else stopMoving();

        frontLeft.setPower(frontL);
        backLeft.setPower(backL);
        frontRight.setPower(frontR);
        backRight.setPower(backR);

    }

    private void der(){
        double velocity = 1;
        double pi = 3.1459265359;
        double piDiv4 = pi/4;
        double theta = pi/2 + Math.atan2(gamepad1.right_stick_y,gamepad1.right_stick_x);
        frontLeft.setPower(velocity * Math.sin(theta + piDiv4));
        backLeft.setPower(velocity * Math.cos(theta + piDiv4));
        frontRight.setPower(velocity * Math.cos(theta + piDiv4));
        backRight.setPower(velocity * Math.sin(theta + piDiv4));
    }

    private void launchBall(){
        double powerMod = gamepad2.right_trigger;
        if(gamepad2.x) {
            flywheelLeft.setPower(-powerMod);
            flywheelRight.setPower(powerMod);
        }
    }
    private void sweep(){
        sweeper.setPower(-gamepad2.left_trigger);
        if(touchSensor.isPressed()) {
            sweeper.setPower(gamepad2.left_trigger);
            telemetry.addData("sweeper: ",sweeper.getCurrentPosition());
        }
//        try {
//            sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        sweeper.setPower(0);
    }
    private void stopMoving(){
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        }
}
