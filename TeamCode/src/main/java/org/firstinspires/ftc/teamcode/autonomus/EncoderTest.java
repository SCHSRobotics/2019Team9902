package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Encoder Test", group = "Testing")
public class EncoderTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotor[] motors = {hardwareMap.dcMotor.get("fl"), hardwareMap.dcMotor.get("bl"), hardwareMap.dcMotor.get("br"), hardwareMap.dcMotor.get("fr"), hardwareMap.dcMotor.get("tiltArm"), hardwareMap.dcMotor.get("linearMotor")};
        for (int i = 0; i < motors.length; i++){
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motors[i].setPower(.1);
            sleep(5);
            motors[i].setPower(0);
            if(motors[i].getCurrentPosition() != 0) {
                break;
            } else{
                motors[i].setPower(-.1);
                sleep(5);
                motors[i].setPower(0);
            }
            if(motors[i].getCurrentPosition() != 0) {
                break;
            } else {
                telemetry.addData("Encoder Test Failed:", motors[i].getDeviceName());
                stop();
            }
        }
    }
}