package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.helpers.MecanumEncoders;

@Autonomous(name="MecanumTest", group="Testing")
@Disabled
public class MecanumTest extends LinearOpMode {
    MecanumEncoders mecanumEncoder;
    @Override
    public void runOpMode() {
        DcMotorEx[] driveMotors = {(DcMotorEx)hardwareMap.dcMotor.get("fl"), (DcMotorEx)hardwareMap.dcMotor.get("fr"), (DcMotorEx)hardwareMap.dcMotor.get("bl"), (DcMotorEx)hardwareMap.dcMotor.get("br")};
        mecanumEncoder = new MecanumEncoders(driveMotors);
        telemetry.update();
        waitForStart();
        while(!isStopRequested()) {
            mecanumEncoder.mecanumEncoders(0, 0, 1, true);
            telemetry.addData("Forward", 0);
            telemetry.update();
            mecanumEncoder.mecanumEncoders(0, 0, -1, true);
            telemetry.addData("Backwards", 0);
            telemetry.update();
        }
        }
    }