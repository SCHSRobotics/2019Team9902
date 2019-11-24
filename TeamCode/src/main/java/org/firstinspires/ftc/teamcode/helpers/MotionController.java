package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

public class MotionController {
    AccelerationSensor imu0;
    AccelerationSensor imu1;
    public MotionController(AccelerationSensor[] I1, DcMotor[] M1){
        imu0 = I1[0];
        imu1 = I1[1];
        BNO055IMU.Parameters imu0p = new BNO055IMU.Parameters();
        BNO055IMU.Parameters imu1p = new BNO055IMU.Parameters();
        imu0p.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imu0p.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu0p.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imu0p.loggingEnabled      = false;
        imu0p.loggingTag          = "IMU";
        imu1p.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imu1p.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu1p.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imu1p.loggingEnabled      = false;
        imu1p.loggingTag          = "IMU";
    }
    private void detectCollision() {
    }
}
