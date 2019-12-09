package org.firstinspires.ftc.teamcode.driverControlled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Test Telemetry", group="LinearOpMode")
@Disabled
public class TestTelemetry extends LinearOpMode {
    @Override public void runOpMode() {
        while(!isStopRequested()) {
            telemetry.addLine("Testing123");
        }
    }
    }
