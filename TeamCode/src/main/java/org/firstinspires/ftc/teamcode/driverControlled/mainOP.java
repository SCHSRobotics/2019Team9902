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

import org.firstinspires.ftc.teamcode.helpers.mecanumdriver;

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
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr"); //This gets the actual hardware maps of the motors from the config file. The deviceName is what it is named in the config file
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        DcMotor[] motors = {fr, br, fl, bl}; //This puts the hardwaremapped motors into a list, so that we can send it to the mecanum driver easier
        fl.setDirection(DcMotor.Direction.REVERSE); // These two lines reverse the motor directions on the left side
        bl.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.FORWARD); // These two lines just say that the motors on the right are normal
        bl.setDirection(DcMotor.Direction.FORWARD);
        // Tell the driver that initialization is complete.
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
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        DcMotor[] motors = {hardwareMap.dcMotor.get("fl"),hardwareMap.dcMotor.get("fr"),hardwareMap.dcMotor.get("bl"),hardwareMap.dcMotor.get("br")};
        Servo grabberservo = hardwareMap.servo.get("grabbyboi");
        mecanumdriver mecanum =  new mecanumdriver();
        double deadzone = .1; //The sticks must move more than this in order to actually count for anything
        //This reads the sticks and sets them to what they are
        //Rotationcoeff sets the sensitivity of Rotation. Higher is faster and vice versa
        float rotationcoeff = .5f;
        float y = -gamepad1.left_stick_y;
        float x = -gamepad1.left_stick_x;
        float R = gamepad1.right_stick_x*rotationcoeff;
        //This acutally does the deadzone stuff by seeing if the absolute value of the sticks is greater than the deadzone, and if not sets the value to zero
        if(Math.abs(y)<deadzone)y=0;
        if(Math.abs(x)<deadzone)x=0;
        if(Math.abs(R)<deadzone)R=0;
        // This calls the mecanum driver which does the magic sauce
        mecanum.mecanumpower(motors, y, x, R);
        telemetry.addData("Mecaunm Driver Inputs", "x (%.2f), y (%.2f), R (%.2f)", x, y, R);
        //do the grabber stuff
        int closedposition = 80;
        int openposition = 90;
        if(gamepad1.a) {
            grabberservo.setPosition(closedposition);
        }
        else {
            grabberservo.setPosition(openposition);
        }
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
