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
//    private DcMotor flywheelLeft;
//    private DcMotor flywheelRight;
//    private Servo elevator;
//    private Servo pushRed;
//    private Servo pushBlue;
    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("motor_frontLeft");
        frontRight = hardwareMap.dcMotor.get("motor_frontRight");
        backLeft = hardwareMap.dcMotor.get("motor_backLeft");
        backRight = hardwareMap.dcMotor.get("motor_backRight");
//        flywheelRight = hardwareMap.dcMotor.get("motor_flywheelRight");
//        flywheelLeft = hardwareMap.dcMotor.get("motor_flywheelLeft");
//        elevator = hardwareMap.servo.get("servo_elevator");
//        pushRed = hardwareMap.servo.get("red_servo");
//        pushBlue = hardwareMap.servo.get("blue_servo");
//        pushBlue.setPosition(0.0);
//        pushRed.setPosition(0.0);

    }

    @Override
    public void loop() {
        float modPower = gamepad1.right_bumper ? -1 : 1;
//        setPowers(gamepad1.left_trigger);
        double rt = gamepad1.right_trigger;
        frontLeft.setPower(gamepad1.right_stick_y);
        backLeft.setPower(gamepad1.right_stick_y);
        frontRight.setPower(-gamepad1.right_stick_y);
        backRight.setPower(-gamepad1.right_stick_y);
        if(gamepad1.right_bumper){
            derR();
        }
        if(gamepad1.left_bumper){
            derL();
        }
//        if(gamepad1.right_trigger >= 0.8f) launchBall();
//        if(gamepad1.right_bumper) pushBlue.setPosition(pushBlue.getPosition());
//        if(gamepad1.left_bumper) pushRed.setPosition(pushRed.getPosition());
    }
    private void derR(){
        double isDerR = 1;
        frontLeft.setPower(isDerR);
        backLeft.setPower(isDerR);
        frontRight.setPower(isDerR);
        backRight.setPower(isDerR);
    }
    private void derL(){
        double isDerL = -1;
        frontLeft.setPower(isDerL);
        backLeft.setPower(isDerL);
        frontRight.setPower(isDerL);
        backRight.setPower(isDerL);
    }
//    private void launchBall(){
//        double powerMod = 1f;
//        flywheelLeft.setPower(powerMod);
//        flywheelRight.setPower(powerMod);
//    }


}

