package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class ScorpionAuto extends OpMode {
    private final double diameterOfWheel = 16.25;
    private final double circOfWheel = diameterOfWheel * Math.PI;
    private final double disBetweenWheels = 42;

    private DcMotorController controller;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private Thread thread;

    private final short FORWARDS = 1;
    private final short BACKWARDS = -1;
    private final short BRAKE = 0;

    @Override
    public void init() {
        controller = hardwareMap.dcMotorController.get("drive_controller");
        frontLeft = hardwareMap.dcMotor.get("motor_frontLeft");
        frontRight = hardwareMap.dcMotor.get("motor_frontRight");
        backLeft = hardwareMap.dcMotor.get("motor_backLeft");
        backRight = hardwareMap.dcMotor.get("motor_backRight");

        drive(FORWARDS);

        try {
            wait(10000);
        } catch (Exception e) {
        }

        drive(BRAKE);

        try {
            wait(3000);
        } catch (InterruptedException e) {
        }

        drive(BACKWARDS);
    }

    private void move(double d) {
        long time = (long) Math.abs(d / (circOfWheel));
        if (d > 0) drive(FORWARDS);
        else drive(BACKWARDS);
        try {
            wait(time);
        } catch (InterruptedException e) {
        }
        drive(BRAKE);
    }

    private long neededTime(int deg) {
        return (long) (1000 * (((disBetweenWheels / 2) * Math.abs((3 / 4) * deg * Math.PI / 180)) / circOfWheel));
    }

    private void turn(int deg) {
        long time = neededTime(deg);
        if (deg > 0) allWheels((short) -1);
        else allWheels((short) 1);
        try {
            wait(time);
        } catch (InterruptedException e) {
        }
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

    @Override
    public void loop() {
    }
}
