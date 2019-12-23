package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.helpers.StickDriver;

@Autonomous(name="Just The Stick", group="aaaaaaa")
public class JustTheStick extends LinearOpMode {
    @Override
    public void runOpMode(){
        waitForStart();
        StickDriver stickDriver = new StickDriver(hardwareMap.servo.get("stickServo"));
        stickDriver.stickDown();
        sleep(10000);
    }
}
