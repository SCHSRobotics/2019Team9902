package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Disabled
@Autonomous(name = "Encoder Test", group = "Testing")
public class EncoderTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addLine("Will Test Motors when start button pressed. Hands off the robot.");
        telemetry.update();
        waitForStart();
        telemetry.clear();
        telemetry.addLine("Running Motor Test");
        telemetry.update();
        DcMotor[] motors = {hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br"), hardwareMap.dcMotor.get("fr")};
        telemetry.addData("Testing This Many Motors:", motors.length);

        for (int i = 0; i < motors.length; i++){
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motors[i].setPower(.1);
            sleep(15);
            motors[i].setPower(0);
            sleep(15);
            if(motors[i].getCurrentPosition() > 0) {
                telemetry.addData("%s Motor Passed", motors[i].getDeviceName());
                telemetry.update();
                break;
            } else if(motors[i].getCurrentPosition() != 0){
                telemetry.addData("This motor may be wired backwards:", motors[i].getDeviceName());
                telemetry.update();
                stop();
            } else {
                motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motors[i].setPower(-.1);
                sleep(15);
                motors[i].setPower(0);
                sleep(15);
            }
            if(motors[i].getCurrentPosition() < 0) {
                telemetry.addData("%s Motor Passed", motors[i].getDeviceName());
                telemetry.update();
                break;
            } else {
                telemetry.addData("Encoder Test Failed:", motors[i].getDeviceName());
                telemetry.update();
                stop();
            }
        }
        telemetry.addLine("All Motors Passed Tests.");
        telemetry.update();
    }
}