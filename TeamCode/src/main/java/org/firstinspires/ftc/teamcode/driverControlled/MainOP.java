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

package org.firstinspires.ftc.teamcode.driverControlled;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.helpers.ArmDriver;
import org.firstinspires.ftc.teamcode.helpers.BaseGrabber;
import org.firstinspires.ftc.teamcode.helpers.MecanumDriver;
import org.firstinspires.ftc.teamcode.helpers.StickDriver;

/**
 Non Linear Main OP mode
 Itmm 10/12/19 with plenty of code from sketchy hacker kid
 */

@TeleOp(name="Main OP", group="LinearOPMode")
public class MainOP extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime(); // just starts the elasped time thing for the hertz calc
    //starts the class things up here so they can be used in all of the things
    private byte intakeDebounce = 0;
    @Override public void runOpMode() {
        //make the helper classes
        telemetry.addData("Status", "Start init");
        WebcamName webcam0 = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        DcMotor[] driveMotors = {hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br")};
        driveMotors[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Servo[] handServos = {hardwareMap.servo.get("grabServo"), hardwareMap.servo.get("wristServo")};
        DcMotor[] armMotors = {hardwareMap.dcMotor.get("tiltMotor"), hardwareMap.dcMotor.get("linearMotor")};
        DcMotor[] intakeMotors = {hardwareMap.dcMotor.get("intakeRight"), hardwareMap.dcMotor.get("intakeLeft")};
        intakeMotors[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotors[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DcMotor linearMotor = hardwareMap.dcMotor.get("linearMotor");
        linearMotor.setTargetPosition(0);
        linearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //AccelerationSensor[] IMUs = {hardwareMap.accelerationSensor.get("imu0"), hardwareMap.accelerationSensor.get("imu1")};

        //make the helper classes
        MecanumDriver mecanum = new MecanumDriver(driveMotors);
        ArmDriver grabberArm = new ArmDriver(armMotors, handServos);
        BaseGrabber baseGrabber = new BaseGrabber(hardwareMap.servo.get("baseServo"));
        StickDriver stickDriver = new StickDriver(hardwareMap.servo.get("stickServo"));
        //MotionController motionController = new MotionController(IMUs, driveMotors);
        telemetry.addData("Status", "initeded");
       // vuforia.vuforiaPosition(webcam0, cameraMonitorViewId); //this hangs the program on it.  Want to run in a background task

        waitForStart();
        Boolean intakeOn = false;
        while (!isStopRequested()) {
            double deadzone = .1; //The sticks must move more than this in order to actually count for anything
            //This reads the sticks and sets them to what they are
            //Rotationcoeff sets the sensitivity of Rotation. Higher is faster and vice versa
            float rotationcoeff = -1.25f;
            float y = -gamepad1.left_stick_y;
            float x = -gamepad1.left_stick_x;
            float R = gamepad1.right_stick_x * rotationcoeff;
            //This acutally does the deadzone stuff by seeing if the absolute value of the sticks is greater than the deadzone, and if not sets the value to zero
            if (Math.abs(y) < deadzone) y = 0;
            if (Math.abs(x) < deadzone) x = 0;
            if (Math.abs(R) < 0) R = 0;
            // This calls the mecanum driver which does the magic sauce
            if(gamepad1.left_bumper) {
                mecanum.mecanumpower(y/2, x/2, R/2);
            } else {
                mecanum.mecanumpower(y, x, R);
            }

            // do the inputs stuff
           //testController(telemetry);
            //linear acutator
            grabberArm.linearArmChg(-gamepad2.right_stick_y);
            telemetry.addData("LinearArmPosition", grabberArm.armPos);
            //tiltArm
            grabberArm.tiltArm(-gamepad2.left_stick_y);
            if(gamepad2.right_trigger != 0) {
                grabberArm.turnGrabber(gamepad2.right_trigger);
            } else if (gamepad2.left_trigger != 0) {
                grabberArm.turnGrabber(-gamepad2.left_trigger);
            }
            telemetry.addLine("Servo");
            telemetry.addData("Servo Position", grabberArm.wristPos);
            telemetry.update();
            //grabber itself
            if (gamepad2.a) {
                grabberArm.open();
            }
            if (gamepad2.b) {
                grabberArm.grab();
            }

            //
            if(gamepad1.right_bumper) {
                intakeMotors[0].setPower(1);
                intakeMotors[1].setPower(-1);
            }else {
                intakeMotors[0].setPower(0);
                intakeMotors[1].setPower(0);
            }
            if(gamepad1.dpad_up){
                baseGrabber.realseBase();
            }
            if(gamepad1.dpad_down){
                baseGrabber.grabBase();
            }
            if(gamepad1.dpad_left){
                stickDriver.stickUp();
            }
            if(gamepad1.dpad_right){
                stickDriver.stickDown();
            }
            if(gamepad1.left_bumper){
                baseGrabber.grabberAllTheWayBack();
            }
            double hertz = 1 / (runtime.time());
            //telemetry.addData("Hertz", "Hertz: (%.2f)", hertz); //Show it to the user
            //telemetry.update();
            runtime.reset(); //Reset the Timer
        }
    }
    public void testController(Telemetry telemetry){
        if (gamepad2.right_stick_y != 0) {
            telemetry.addData("rightStick", "rightStick Y: (%.2f)", gamepad2.right_stick_y); //Show it to the user
            telemetry.update();
        }
        if (gamepad2.right_stick_x != 0) {
            telemetry.addData("rightStick", "rightStick X: (%.2f)", gamepad2.right_stick_x); //Show it to the user
            telemetry.update();
        }
        if (gamepad2.left_stick_y != 0) {
            telemetry.addData("leftStick", "leftStick Y: (%.2f)", gamepad2.left_stick_y); //Show it to the user
            telemetry.update();
        }
        if (gamepad2.left_stick_x != 0) {
            telemetry.addData("leftStick", "leftStick X: (%.2f)", gamepad2.left_stick_x); //Show it to the user
            telemetry.update();
        }


        if (gamepad2.left_trigger != 0) {
            telemetry.addData("leftTrigger", "leftTrigger Y: (%.2f)", gamepad2.left_trigger); //Show it to the user
            telemetry.update();
        }
        if (gamepad2.right_trigger != 0) {
            telemetry.addData("rightTrigger", "rightTrigger X: (%.2f)", gamepad2.right_trigger); //Show it to the user
            telemetry.update();
        }

        if (gamepad1.left_trigger != 0) {
            telemetry.addData("leftTrigger", "leftTrigger pad1 Y: (%.2f)", gamepad1.left_trigger); //Show it to the user
            telemetry.update();
        }
        if (gamepad1.right_trigger != 0) {
            telemetry.addData("rightTrigger", "rightTrigger pad1 X: (%.2f)", gamepad1.right_trigger); //Show it to the user
            telemetry.update();
        }





    }
}
