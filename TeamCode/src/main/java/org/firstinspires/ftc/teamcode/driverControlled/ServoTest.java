package org.firstinspires.ftc.teamcode.driverControlled;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTest", group="Testing")
public class ServoTest extends LinearOpMode {
    int servoPosition = 15;
    @Override
    public void runOpMode() {
        Servo test = hardwareMap.servo.get("wristServo");
        waitForStart();
        while(!isStopRequested()) {
            if(servoPosition < 181) {
                if(gamepad1.a){
                    servoPosition++;
                }
            }
            if(servoPosition > 1) {
                if(gamepad1.b) {
                    servoPosition--;
                }
            }
            test.setPosition(servoPosition);
            telemetry.addData("Servo:", servoPosition);
            telemetry.update();
            sleep(50);
        }
    }
}
