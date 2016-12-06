package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

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
    private Servo pushRed;
    private Servo pushBlue;
    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("motor_frontLeft");
        frontRight = hardwareMap.dcMotor.get("motor_frontRight");
        backLeft = hardwareMap.dcMotor.get("motor_backLeft");
        backRight = hardwareMap.dcMotor.get("motor_backRight");
        flywheelRight = hardwareMap.dcMotor.get("motor_flywheelRight");
        flywheelLeft = hardwareMap.dcMotor.get("motor_flywheelLeft");
        sweeper = hardwareMap.dcMotor.get("motor_sweeper");
        pushRed = hardwareMap.servo.get("red_servo");
        pushBlue = hardwareMap.servo.get("blue_servo");

    }

    @Override
    public void loop() {
        double rsy = gamepad1.right_stick_y;
        double rsx = gamepad1.right_stick_x;
        double frontL = rsy + rsx;
        double backL = rsy + rsx;
        double frontR = -rsy - rsx;
        double backR = -rsy - rsx;

        double largestVal =  Math.max(Math.max(Math.abs(frontL),Math.abs(backL)), (Math.max(Math.abs(frontR),Math.abs(backR))));
        if(largestVal > 1){
            frontL /= largestVal;
            backL /= largestVal;
            frontR /= largestVal;
            backR /= largestVal;
        }

        sweeper.setPower(gamepad2.left_trigger);
        if(gamepad1.right_trigger >= 0.1) launchBall();
        double degServo = 0;
        double startPos = 0;
        if(gamepad2.right_bumper){
            pushBlue.setPosition(startPos + degServo);
        }
        if(gamepad2.left_bumper){
            pushRed.setPosition(startPos - degServo);
        }


        if(gamepad1.right_bumper){
            derR();
        }
        else if(gamepad1.left_bumper){
            derL();
        }else{
            frontLeft.setPower(frontL);
            backLeft.setPower(backL);
            frontRight.setPower(frontR);
            backRight.setPower(backR);
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
    private void launchBall(){
        double powerMod = gamepad2.right_trigger;
        if(gamepad2.x) {
            flywheelLeft.setPower(powerMod);
            flywheelRight.setPower(powerMod);
        }
    }


}
