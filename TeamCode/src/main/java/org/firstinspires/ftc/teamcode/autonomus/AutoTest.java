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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.helpers.ArmDriver;
import org.firstinspires.ftc.teamcode.helpers.ClosedLoopDriving;
import org.firstinspires.ftc.teamcode.helpers.MecanumDriver;
import org.firstinspires.ftc.teamcode.helpers.MotionController;
import org.firstinspires.ftc.teamcode.helpers.VuforiaNavigation;
import org.firstinspires.ftc.teamcode.helpers.VuforiaParameters;
import org.firstinspires.ftc.teamcode.helpers.VuforiaStone;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.mmPerInch;
//import org.firstinspires.ftc.teamcode.helpers.vuforia;

/**
 Non Linear Main OP mode
 Itmm 10/12/19 with plenty of code from sketchy hacker kid
 */

@TeleOp(name="AutoOP", group="LinearOPMode")
public class AutoTest extends LinearOpMode {
    //starts the class things up here so they can be used in all of the things
    MecanumDriver mecanum;
    ArmDriver grabberArm;
    ClosedLoopDriving closedLoopDriver;
    enum Position {
        LEFT,RIGHT,CENTER,NOT_FOUND;
    }
    @Override public void runOpMode() {
        //make the helper classes
        telemetry.addData("Status", "Start init");

        WebcamName webcam0 = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaParameters params = new VuforiaParameters(webcam0, cameraMonitorViewId);
        VuforiaNavigation vn = new VuforiaNavigation();
        vn.onPreExecute(params);
        vn.execute();
        VuforiaStone vs = new VuforiaStone();
        vs.onPreExecute(params);
        vs.execute();

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

        waitForStart();

        Position p = blockRead(vs);

        if (p == Position.LEFT){
        //drive to left
        }
        else if (p == Position.RIGHT){
            //drive to Right
        }
        else if (p == Position.CENTER){
            //drive to center
        }
        else if (p == Position.NOT_FOUND){
            //drive to not found
        }

        //mecanum.mecanumpower(0, .1f, 0);
        //sleep(100);
        //mecanum.mecanumpower(0, 0, 0);


           //blockMove(true);

           //Thread.sleep(1000);
        //while (!isStopRequested()) {
         boolean positionfound = false;
         int counter = 0;

         while(!positionfound){
             counter++;
             sleep(500);
             telemetry.addData("counter", counter);
             telemetry.update();
             if(counter == 100){
                 break;
             }
             if (vs.translation != null) {
                VectorF position = vs.translation;
                telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f", position.get(0) / mmPerInch, position.get(1) / mmPerInch, position.get(2) / mmPerInch);
                telemetry.update();
                positionfound = true;
            }


             VectorF postiion = vs.translation;
             float[] target = {18, 18};
             //float[] actual = {postiion.get(0), postiion.get(1)};
             float[] actual = {0, 0};
             closedLoopDriver.closedLoopDriving(target, actual, false);

            //if (vs.targetVisible == true) {
             //   telemetry.addData("Target Acquired", ":)");
            //    telemetry.update();
            //}
            //else {
            //    telemetry.addData("No target", ":(");
            //}


        }
         if(!positionfound) {
             telemetry.addData("out of loop", "here");
             telemetry.update();
         }
        vs.cancel(true);
         while (!isStopRequested()){

         }
           blockMove(false);

        // vuforia.stopVuforia();
    }
    private void blockMove(boolean lift) {
        final float targetHeight = 45.0f;
        grabberArm.tiltArm(targetHeight);
        if(lift)
            grabberArm.release();
        grabberArm.extendArm();
        grabberArm.tiltArm(0);
        if(lift)
            grabberArm.grab();
        else
            grabberArm.release();
        grabberArm.tiltArm(targetHeight);
        grabberArm.retractArm();

    }
    private Position blockRead(VuforiaStone vs){
        boolean positionfound = false;
        int counter = 0;

        while(!positionfound) {
            counter++;
            sleep(500);
            telemetry.addData("counter", counter);
            telemetry.update();
            if (counter == 100) {
                vs.cancel(true);
                break;
            }
            if (vs.translation != null) {
                //VectorF position = vs.translation;

                telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f", vs.translation.get(0) / mmPerInch, vs.translation.get(1) / mmPerInch, vs.translation.get(2) / mmPerInch);
                telemetry.update();

                //vs.cancel(true);
                positionfound = true;

                //return Position.CENTER;
                //return position a, b, c

            }

        }

            //closedLoopDriver.closedLoopDriving(target, actual, vs.targetVisible);

            //if (vs.targetVisible == true) {
            //   telemetry.addData("Target Acquired", ":)");
            //    telemetry.update();
            //}
            //else {
            //    telemetry.addData("No target", ":(");
            //}



        if(!positionfound) {
            telemetry.addData("out of loop", "here");
            telemetry.update();

        }
        else{
            VectorF postiion = vs.translation;
            float[] target = {5, 5};
            float[] actual = {postiion.get(0), postiion.get(1)};

        }
        vs.cancel(true);
        return Position.NOT_FOUND;
    }
}
