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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.helpers.armDriver;
import org.firstinspires.ftc.teamcode.helpers.mecanumdriver;
import org.firstinspires.ftc.teamcode.helpers.vuforia;

/**
 Non Linear Main OP mode
 Itmm 10/12/19 with plenty of code from sketchy hacker kid
 */

@TeleOp(name="Main OP", group="Iterative Opmode")
public class mainOP extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime(); // just starts the elasped time thing for the hertz calc

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Start init");
        DcMotor tiltMotor = hardwareMap.get(DcMotor.class, "tiltMotor");
        DcMotor linearMotor = hardwareMap.get(DcMotor.class, "linearMotor");
        // Tell the driver that initialization is complete.
        tiltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tiltMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Status", "initeded");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        vuforia vuforia = new vuforia();
        vuforia.vuforiaPosition(webcamName, cameraMonitorViewId);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        DcMotor[] driveMotors = {hardwareMap.dcMotor.get("fl"),hardwareMap.dcMotor.get("fr"),hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br")};
        Servo[] handServos = {hardwareMap.servo.get("grabServo"), hardwareMap.servo.get("wristServo")};
        DcMotor[] ArmMotors = {hardwareMap.dcMotor.get("liftArm"), hardwareMap.dcMotor.get("linearMotor")};

        //make the helper classes
        mecanumdriver mecanum =  new mecanumdriver();
        armDriver grabberArm = new armDriver();
        double deadzone = .1; //The sticks must move more than this in order to actually count for anything
        //This reads the sticks and sets them to what they are
        //Rotationcoeff sets the sensitivity of Rotation. Higher is faster and vice versa
        float rotationcoeff = -1.25f;
        float y = -gamepad1.left_stick_y;
        float x = -gamepad1.left_stick_x;
        float R = gamepad1.right_stick_x*rotationcoeff;
        //This acutally does the deadzone stuff by seeing if the absolute value of the sticks is greater than the deadzone, and if not sets the value to zero
        if(Math.abs(y)<deadzone)y=0;
        if(Math.abs(x)<deadzone)x=0;
        if(Math.abs(R)<0)R=0;
        // This calls the mecanum driver which does the magic sauce
        mecanum.mecanumpower(driveMotors, y, x, R);
        telemetry.addData("Mecaunm Driver Inputs", "x (%.2f), y (%.2f), R (%.2f)", x, y, R);
        //do the grabber stuff
        int closedposition = 80;
        int openposition = 90;
       /*if(gamepad1.dpad_down){
            liftArm.setPower(128);
        } else {
            liftArm.setPower(0);
        }
        if(gamepad1.dpad_up){
            liftArm.setPower(-128);
        } else {
            liftArm.setPower(0);
        }*/
        //debug stuff
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr"); //This gets the actual hardware maps of the motors from the config file. The deviceName is what it is named in the config file
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        telemetry.addData("Motor Power", "fr (%.2f), br (%.2f),  fl (%.2f), bl (%.2f)", fr.getPower(), br.getPower(), fl.getPower(), bl.getPower());



         //end debug stuff
        //everything below here to the end of the loop should just be hertz calculation stuff for performance measurement
        double hertz;
        // find the hertz of the control loop by using a timer
        hertz = 1/(runtime.time());
        telemetry.addData("Hertz", "Hertz: (%.2f)", hertz); //Show it to the user
        runtime.reset(); //Reset the Timer
    }

    /*
     * Code after Driver hits end. Probably nothing here
     */
    @Override
    public void stop() {

    }

}
