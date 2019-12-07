/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.autonomus;

import android.os.AsyncTask;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaSkyStone;
import org.firstinspires.ftc.teamcode.helpers.ArmDriver;
import org.firstinspires.ftc.teamcode.helpers.ClosedLoopDriving;
import org.firstinspires.ftc.teamcode.helpers.MecanumDriver;
import org.firstinspires.ftc.teamcode.helpers.MotionController;
import org.firstinspires.ftc.teamcode.helpers.Position;
import org.firstinspires.ftc.teamcode.helpers.VuforiaNavigation;
import org.firstinspires.ftc.teamcode.helpers.VuforiaParameters;
import org.firstinspires.ftc.teamcode.helpers.VuforiaStone;

import java.util.HashMap;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.helpers.Position.position.CENTER;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.LEFT;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.NOT_FOUND;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.RIGHT;

//import org.firstinspires.ftc.teamcode.helpers.vuforia;

/**
 Non Linear Main OP mode
 Itmm 10/12/19 with plenty of code from sketchy hacker kid
 */

@Autonomous(name="AutoOP", group="Testing")
public class AutoTest extends LinearOpMode {
    public int cameraMonitorViewId;
    //starts the class things up here so they can be used in all of the things
    MecanumDriver mecanum;
    ArmDriver grabberArm;
    ClosedLoopDriving closedLoopDriver;
    public int posOffset = 0; //The block positioning offset that we have. this will be different for different starting points

    @Override
    public void runOpMode() {
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //make the helper classes
        telemetry.addData("Status", "Start init");
        WebcamName webcam0 = hardwareMap.get(WebcamName.class, "Webcam 1");
        VuforiaNavigation vn = new VuforiaNavigation();
        VuforiaStone vs = new VuforiaStone();
        vs.setup(webcam0);
        //vn.setup(webcam0);
        DcMotor[] driveMotors = {hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br")};
        driveMotors[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Servo[] handServos = {hardwareMap.servo.get("grabServo"), hardwareMap.servo.get("wristServo")};
        DcMotor[] armMotors = {hardwareMap.dcMotor.get("tiltMotor"), hardwareMap.dcMotor.get("linearMotor")};
        //AccelerationSensor[] IMUs = {hardwareMap.accelerationSensor.get("imu0"), hardwareMap.accelerationSensor.get("imu1")};

        //make the helper classes
        closedLoopDriver = new ClosedLoopDriving(driveMotors);
        grabberArm = new ArmDriver(armMotors, handServos);
        //MotionController motionController = new MotionController(IMUs, driveMotors);
        telemetry.addData("Status", "initeded");



        telemetry.addLine("Position");
        vs.execute(webcam0);
        waitForStart();
        while(!isStopRequested()) {
            telemetry.addLine("Position");
            Position.position p = vs.stonePos;
            if (p == LEFT) {
                telemetry.addData("Position Left", vs.tStoneX);
                //drive to left
            } else if (p == RIGHT) {
                telemetry.addData("Position Right", vs.tStoneX);
                //drive to Right
            } else if (p == CENTER) {
                telemetry.addData("Position Center", vs.tStoneX);
                //drive to center
            } else if (p == NOT_FOUND) {
                telemetry.addData("Position NF", vs.tStoneX);
                //drive to not found
            }
            telemetry.update();
        }
        vs.cancel(true);
        //vn.execute(webcam0);
    }
    private void blockMove(boolean lift){
        final float targetHeight = 45.0f;
        grabberArm.tiltArm(targetHeight);
        if (lift) {
            grabberArm.release();
            grabberArm.extendArm();
            grabberArm.tiltArm(0);
        }
        if (lift) {
            grabberArm.grab();
        }
        else {
            grabberArm.release();
            grabberArm.tiltArm(targetHeight);
            grabberArm.retractArm();
        }
    }
}