package org.firstinspires.ftc.robotcontroller.internal;

/*
Modern Robotics Color Sensors Example with color number
Created 9/29/2016 by Colton Mehlhoff of Modern Robotics using FTC SDK 2.2
Reuse permitted with credit where credit is due

Configuration:
I2CDevice "ca" (MRI Color Sensor with I2C address 0x3a (0x1d 7-bit)
I2CDevice "cc" (MRI Color Sensor with default I2C address 0x3c (0x1e 7-bit)

ModernRoboticsI2cColorSensor class is not being used because it can not access color number.
ColorSensor class is not being used because it can not access color number.

To change color sensor I2C Addresses, go to http://modernroboticsedu.com/mod/lesson/view.php?id=96
Support is available by emailing support@modernroboticsinc.com.
*/

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;


@TeleOp(name = "Color Sensors", group = "MRI")
// @Autonomous(...) is the other common choice
//@Disabled
public class MRI_Try extends LinearOpMode {

    private byte[] beaconSensorCache;

    private I2cDevice lineSensor;
    private I2cDevice beaconSensor;
    private I2cDeviceSynch lineSensorReader;
    private I2cDeviceSynch beaconSensorReader;


    boolean LEDState = true;     //Tracks the mode of the color sensor; Active = true, Passive = false


    @Override
    public void runOpMode() throws InterruptedException {
        this.waitForStart();
        //the below lines set up the configuration file
        lineSensor = hardwareMap.i2cDevice.get("white_color");
        beaconSensor = hardwareMap.i2cDevice.get("beacon_sensor");

        lineSensorReader = new I2cDeviceSynchImpl(lineSensor, I2cAddr.create8bit(0x3a), false);
        beaconSensorReader = new I2cDeviceSynchImpl(beaconSensor, I2cAddr.create8bit(0x3c), false);

        lineSensorReader.engage();
        beaconSensorReader.engage();

        //runtime.reset();

        lineSensorReader.write8(3, 0);    //Set the mode of the color sensor using LEDState
        beaconSensorReader.write8(3, 1);    //Set the mode of the color sensor using LEDState


        while (opModeIsActive()) {
            //display values
            //lineSensorCache = lineSensorReader.read(0x04, 1);
            beaconSensorCache = beaconSensorReader.read(0x04, 1);

            //telemetry.addData("1 #A", lineSensorCache[0] & 0xFF);
            telemetry.addData("2 #C", beaconSensorCache[0] & 0xFF);

            //telemetry.addData("3 A", lineSensorReader.getI2cAddress().get8Bit());
            telemetry.addData("4 A", beaconSensorReader.getI2cAddress().get8Bit());
//            Thread.sleep(3000);
            telemetry.update();
        }
    }
}

