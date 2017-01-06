package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
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
    public final static I2cAddr BEACON_I2C_ADDRESS = I2cAddr.create8bit(0x7C);

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor flywheelLeft;
    private DcMotor flywheelRight;
    private DcMotor sweeper;
    private Servo pRed;
    private Servo pBlue;
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
        sweeper = hardwareMap.dcMotor.get("motor_sweeper");
        pRed = hardwareMap.servo.get("red_servo");
        pBlue = hardwareMap.servo.get("blue_servo");
        lineSensor = hardwareMap.colorSensor.get("white_color");
        beaconSensor = hardwareMap.colorSensor.get("beacon_sensor");
        touchSensor = hardwareMap.touchSensor.get("touch");
        gyroSensor = hardwareMap.gyroSensor.get("gyro");
        
        beaconSensor.setI2cAddress(BEACON_I2C_ADDRESS);
        
        
        float hsvValues[] = {0F,0F,0F};
        final float values[] = hsvValues;
        boolean bPrevState = false;
        boolean bCurrState = false;
        boolean bLedOn = false;
        beaconSensor.enableLed(bLedOn);
        lineSensor.enableLed( true );
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
            Color.RGBToHSV( beaconSensor.red() * 8, beaconSensor.green() * 8, beaconSensor.blue() * 8, hsvValues );

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
            Color.RGBToHSV( lineSensor.red() * 8, lineSensor.green() * 8, lineSensor.blue() * 8, hsvValues );
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

//    private long neededTime(double deg) {
//        return (long) (1000 * (((disBetweenWheels / 2) * Math.abs((3 / 4) * deg * Math.PI / 180)) / circOfWheel));
//    }
    private void goWhiteLine(){
        double hueTotal = lineSensor.blue() + lineSensor.green() + lineSensor.red();
        boolean isGoingLeft = true;
        while (hueTotal < 175*3 ){
            if (isGoingLeft) {
                    int modX = -1;
                    int modY = 0;
                    der(modX, modY);
                for(int i = 0; i<=5; i++) {
                    hueTotal = lineSensor.blue() + lineSensor.green() + lineSensor.red();
                    if(hueTotal < 175*3) waitTime(100);
                    else break;
                }
                der(0, 0);
                isGoingLeft = false;
            }else{
                int modX = 1;
                int modY = 0;
                der(modX, modY);
                for(int i = 0; i <= 11; i++) {
                    waitTime(100);

                }
                der(0, 0);
                isGoingLeft = true;

            }
        }
        
    }
    private boolean isWhiteLine() {
        return lineSensor.blue() + lineSensor.green() + lineSensor.red() >= 175*3;
    }
    private void run(){
        launchBall();
        turn(45);
        move(6);
        turn(45);
        goWhiteLine();
        // move
        move(2.5);
        pushBeacon('r');
        move(-2.5);
        der(1,0);
        while(!isWhiteLine());
        drive(BRAKE);
        move(2.5);
        turn(-45);
        pushBeacon('r');
        move(5.66);
//        while (!touch.isPressed()){
//            lineSensor();
//        }
    }
//    private boolean
    private void pushRed(){
        pRed.setPosition(130);
    }
    private  void resetServo(){
        pRed.setPosition(0);
        pBlue.setPosition(0);
    }
    private void pushBlue(){
        pBlue.setPosition(130);
    }
    private void pushBeacon(final char COLOR){
    if((COLOR == 'r') && (beaconSensor.red() < (beaconSensor.blue() + beaconSensor.green()))) pushRed();
        if((COLOR == 'b') && (beaconSensor.blue() < (beaconSensor.red() + beaconSensor.green()))) pushBlue();
    }
    private void turn(double deg) {
//        long time = neededTime(deg);

//        try {
//            wait(time);
//        } catch (Exception ignored) {
//        }
        telemetry.addData("turn: ", gyroSensor.getHeading());

        int heading = gyroSensor.getHeading();
        int target = heading +  (int) deg;
        while (target < 0) target += 360;
        target %= 360;

        if (deg < 0) allWheels((short) -1);
        else allWheels((short) 1);

        while(gyroSensor.getHeading() != target);

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
    private void findWhiteLine(){
        if(lineSensor.blue() >= 175 && lineSensor.red() >= 175 && lineSensor.green() >= 175){
            move(0.1);
        } else {
            turn(-0.5);
        }

    }
    private void der(double x, double y){
        double velocity = 1;
        double pi = 3.1459265359;
        double piDiv4 = pi/4;
        double theta = -pi/2 + Math.atan2(y,x);
        frontLeft.setPower(velocity * Math.sin(theta + piDiv4));
        backLeft.setPower(velocity * Math.cos(theta + piDiv4));
        frontRight.setPower(velocity * Math.cos(theta + piDiv4));
        backRight.setPower(velocity * Math.sin(theta + piDiv4));
    }


    private void launchBall(){
        Thread thread = new Thread(){
            public void run(){
                double powerMod = 1;

                flywheelLeft.setPower(-powerMod);
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
