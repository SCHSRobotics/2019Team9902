package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.helpers.ArmDriver;
import org.firstinspires.ftc.teamcode.helpers.BaseGrabber;
import org.firstinspires.ftc.teamcode.helpers.MecanumEncoders;
import org.firstinspires.ftc.teamcode.helpers.StickDriver;

@Autonomous(name="*EXPERIMENTAL* Move Base and Go to Center, from Bottom Left (Blue)", group="EXPERIMENTAL")
@Disabled
public class BaseAndCenterBlueBottomExperimental extends LinearOpMode {
    MecanumEncoders mE;
    ArmDriver gA;

    @Override
    public void runOpMode() {
        DcMotorEx[] driveMotors = {(DcMotorEx) hardwareMap.dcMotor.get("fl"), (DcMotorEx) hardwareMap.dcMotor.get("fr"), (DcMotorEx) hardwareMap.dcMotor.get("bl"), (DcMotorEx) hardwareMap.dcMotor.get("br")};
        driveMotors[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Servo[] handServos = {hardwareMap.servo.get("grabServo"), hardwareMap.servo.get("wristServo"), hardwareMap.servo.get("tiltServo")};
        DcMotor[] intakeMotors = {hardwareMap.dcMotor.get("intakeRight"), hardwareMap.dcMotor.get("intakeLeft")};

        DcMotor[] armMotors = {hardwareMap.dcMotor.get("tiltMotor"), hardwareMap.dcMotor.get("linearMotor")};

        //make the helper classes
        mE = new MecanumEncoders(driveMotors);
        mE.mecanumEncoders(0, 0, 0, false);
        gA = new ArmDriver(armMotors, handServos);
        BaseGrabber baseGrabber = new BaseGrabber(hardwareMap.servo.get("baseServo"));
        StickDriver stickDriver = new StickDriver(hardwareMap.servo.get("stickServo"));

        waitForStart();
        intakeMotors[0].setPower(-1);
        intakeMotors[1].setPower(1);
        mE.mecanumEncoders(0, 5, 0, true);
        intakeMotors[0].setPower(1);
        intakeMotors[1].setPower(-1);
        mE.mecanumEncoders(0, 15, 0, true);
        mE.mecanumEncoders(5, 0, 0, true);
        mE.mecanumEncoders(0, -5, 0, true);
        mE.mecanumEncoders(0, 0, -.25, true);
        mE.mecanumEncoders(0, -45, 0, true);

        baseGrabber.grabBase();
        sleep(750);
        mE.mecanumEncoders(25, 0, 0, true);
        baseGrabber.realseBase();
        mE.mecanumEncoders(0, 15, 0, true);
        mE.mecanumEncoders(-45, 0,0, true);
        mE.mecanumEncoders(0, -15, 0, true);
        mE.mecanumEncoders(30, 0,0, true);
        mE.mecanumEncoders(-30, 0, 0, true);
        mE.mecanumEncoders(0, 0, .26, true);
        mE.mecanumEncoders(48, 0, 0, true);
        stickDriver.stickDown();
        mE.mecanumEncoders(5, 0, 0, true);
        mE.mecanumEncoders(-5, 0, 0, true);
        mE.mecanumEncoders(5, 0, 0, false);
        mE.mecanumEncoders(-5, 0, 0, false);
        mE.mecanumEncoders(5, 0, 0, false);
        mE.mecanumEncoders(-5, 0, 0, false);
        mE.mecanumEncoders(0, -5, 0, true);
        sleep(5000);

    }
}

