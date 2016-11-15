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
//        float modPower = gamepad1.left_trigger;
        frontLeft.setPower(gamepad1.right_trigger);
        backLeft.setPower(gamepad1.right_trigger);
        frontRight.setPower(gamepad1.left_trigger);
        backRight.setPower(gamepad1.left_trigger); // changed
//        if(gamepad1.y) elevator.setPower(1);
//        if(gamepad1.right_trigger >= 0.8f) launchBall();
//        if(gamepad1.right_bumper) pushBlue.setPosition(pushBlue.getPosition());
//        if(gamepad1.left_bumper) pushRed.setPosition(pushRed.getPosition());
    }
//    private void launchBall(){
//        double powerMod = 1f;
//        flywheelLeft.setPower(powerMod);
//        flywheelRight.setPower(powerMod);
//    }


}
