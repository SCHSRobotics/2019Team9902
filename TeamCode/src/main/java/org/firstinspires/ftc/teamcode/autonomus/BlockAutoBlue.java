package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.helpers.ArmDriver;
import org.firstinspires.ftc.teamcode.helpers.BaseGrabber;
import org.firstinspires.ftc.teamcode.helpers.MecanumEncoders;
import org.firstinspires.ftc.teamcode.helpers.StickDriver;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * In this sample, we demonstrate how to use the {@link OpenCvPipeline#onViewportTapped()}
 * callback to switch which stage of a pipeline is rendered to the viewport for debugging
 * purposes. We also show how to get data from the pipeline to your OpMode.
 */
@TeleOp
public class BlockAutoBlue extends LinearOpMode
{
    OpenCvCamera webcam;
    StageSwitchingPipeline stageSwitchingPipeline;
    MecanumEncoders mE;
    ArmDriver gA;
    @Override
    public void runOpMode()
    {   DcMotorEx[] driveMotors = {(DcMotorEx) hardwareMap.dcMotor.get("fl"), (DcMotorEx) hardwareMap.dcMotor.get("fr"), (DcMotorEx) hardwareMap.dcMotor.get("bl"), (DcMotorEx) hardwareMap.dcMotor.get("br")};
        driveMotors[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Servo[] handServos = {hardwareMap.servo.get("grabServo"), hardwareMap.servo.get("wristServo"), hardwareMap.servo.get("tiltServo")};
        DcMotor[] intakeMotors = {hardwareMap.dcMotor.get("intakeRight"), hardwareMap.dcMotor.get("intakeLeft")};

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        //webcam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);



        webcam.openCameraDevice();
        stageSwitchingPipeline = new StageSwitchingPipeline();
        webcam.setPipeline(stageSwitchingPipeline);
        webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
        mE = new MecanumEncoders(driveMotors);
        mE.mecanumEncoders(0, 0, 0, false);
        BaseGrabber baseGrabber = new BaseGrabber(hardwareMap.servo.get("baseServo"));
        StickDriver stickDriver = new StickDriver(hardwareMap.servo.get("stickServo"));
        stickDriver.stickUp();
        baseGrabber.grabberAllTheWayBack();
        StageSwitchingPipeline.blockPos blockPos = StageSwitchingPipeline.blockPos.LEFT;

        while(!opModeIsActive()) {
            blockPos = stageSwitchingPipeline.getBlockPos();
            telemetry.addData("stuff", blockPos);
            telemetry.update();
        }
        
        waitForStart();
        webcam.stopStreaming();
        webcam.closeCameraDevice();



        mE.mecanumEncoders(-20, 0, 0, true);
        mE.mecanumEncoders(0, 0, -.28, true);
        double leftDist = -11;
        double centerDist = -16;
        double rightDist = -25;
        double distTraveled = 0;
        if(blockPos == StageSwitchingPipeline.blockPos.LEFT) {
            mE.mecanumEncoders(leftDist, 0, 0, true);
            distTraveled = leftDist;
        } else if(blockPos == StageSwitchingPipeline.blockPos.CENTER) {
            mE.mecanumEncoders(centerDist, 0, 0, true);
            distTraveled = centerDist;
        } else if(blockPos == StageSwitchingPipeline.blockPos.RIGHT) {
            mE.mecanumEncoders(rightDist, 0, 0, true);
            distTraveled = rightDist;
        }
        intakeMotors[0].setPower(1);
        intakeMotors[1].setPower(-1);
        mE.mecanumEncoders(0, -11, 0, true);
        mE.mecanumEncoders(13, 0, 0, true, .3);
        intakeMotors[0].setPower(0);
        intakeMotors[1].setPower(0);
        mE.mecanumEncoders(0, 18, 0, true, 1);
        mE.mecanumEncoders((double)60-distTraveled, 0, 0, true);
        mE.mecanumEncoders(0, 0, -.23, true);
        mE.mecanumEncoders(8, 0, 0, true);
        mE.mecanumEncoders(6, 0, 0, true, .5);
        intakeMotors[0].setPower(-0.5);
        intakeMotors[1].setPower(0.5);
        mE.mecanumEncoders(-1, 0, 0, true, .3);
        mE.mecanumEncoders(12, 0, 0, true, .3);
        mE.mecanumEncoders(-15, 0, 0, true);
        mE.mecanumEncoders(0, 0, .5, true);
        mE.mecanumEncoders(-15, 0,0, true);
        baseGrabber.grabBase();
        sleep(750);
        mE.mecanumEncoders(20, 0, 0, true);
        mE.mecanumEncoders(-2, 0, 0, true);
        mE.mecanumEncoders(0, 15, 0, true);
        mE.mecanumEncoders(-15, 0, 0, true);
        mE.mecanumEncoders(0, -15, 0, true);

        while (opModeIsActive())
        {
            telemetry.addData("Num contours found", stageSwitchingPipeline.getBlockPos());
            telemetry.update();
            sleep(100);
        }

    }

    /*
     * With this pipeline, we demonstrate how to change which stage of
     * is rendered to the viewport when the viewport is tapped. This is
     * particularly useful during pipeline development. We also show how
     * to get data from the pipeline to your OpMode.
     */
    static class StageSwitchingPipeline extends OpenCvPipeline
    {
        Mat imageFlipped = new Mat();
        Mat HSVMAT = new Mat();
        Mat ROIOverlayMat = new Mat();
        Mat VExtractedMat = new Mat();
        Mat blockLMat = new Mat();
        Mat blockCMat = new Mat();
        Mat blockRMat = new Mat();

        Rect blockLRect = new Rect(0, 100, 165, 100);
        Rect blockCRect = new Rect(190, 100, 165, 100);
        Rect blockRRect = new Rect(380, 100, 165, 100);

        double[] blockLMean;
        double[] blockCMean;
        double[] blockRMean;

        double min;
        enum blockPos {
            LEFT,
            CENTER,
            RIGHT,
        }
        private blockPos position;
        enum Stage
        {
            HSV,
            ROIOverlay,
            VExtracted,
            blockL,
            blockC,
            blockR,
            RAW_IMAGE,
        }

        private Stage stageToRenderToViewport = Stage.HSV;
        private Stage[] stages = Stage.values();

        @Override
        public void onViewportTapped()
        {
            /*
             * Note that this method is invoked from the UI thread
             * so whatever we do here, we must do quickly.
             */

            int currentStageNum = stageToRenderToViewport.ordinal();

            int nextStageNum = currentStageNum + 1;

            if(nextStageNum >= stages.length)
            {
                nextStageNum = 0;
            }

            stageToRenderToViewport = stages[nextStageNum];
        }

        @Override
        public Mat processFrame(Mat input)
        {

            Imgproc.cvtColor(input, HSVMAT, Imgproc.COLOR_RGB2HSV);
            ROIOverlayMat = input;
            Imgproc.rectangle(ROIOverlayMat, blockLRect.tl(), blockLRect.br(), new Scalar(255, 0, 0), 5);
            Imgproc.rectangle(ROIOverlayMat, blockCRect.tl(), blockCRect.br(), new Scalar(0, 255, 0), 5);
            Imgproc.rectangle(ROIOverlayMat, blockRRect.tl(), blockRRect.br(), new Scalar(0, 0, 255), 5);
            Core.extractChannel(HSVMAT, VExtractedMat, 2);
            blockLMat = new Mat(VExtractedMat, blockLRect);
            blockCMat = new Mat(VExtractedMat, blockCRect);
            blockRMat = new Mat(VExtractedMat, blockRRect);
            blockLMean = Core.mean(blockLMat).val;
            blockCMean = Core.mean(blockCMat).val;
            blockRMean = Core.mean(blockRMat).val;

            min = Math.min(Math.min(blockLMean[0], blockCMean[0]), blockRMean[0]);

            if(blockLMean[0] == min){
                position = blockPos.RIGHT;
            }
            if(blockCMean[0] == min){
                position = blockPos.CENTER;
            }
            if(blockRMean[0] == min){
                position = blockPos.LEFT;
            }
            /*if(blockLMean[0] > blockCMean[0]){
                if(blockCMean[0] > blockRMean[0]){
                    position = blockPos.LEFT;
                } else{
                    position = blockPos.CENTER;
                }
            } else if(blockRMean[0] > blockLMean[0]) {
                position = blockPos.RIGHT;
            }*/

            switch (stageToRenderToViewport)
            {
                case HSV:
                {
                    return HSVMAT;
                }
                case ROIOverlay:
                {
                    return ROIOverlayMat;
                }
                case VExtracted:
                {
                    return VExtractedMat;
                }

                case blockL:
                {
                    return blockLMat;
                }
                case blockC:
                {
                    return blockCMat;
                }
                case blockR:
                {
                    return blockRMat;
                }
                case RAW_IMAGE:
                {
                    return input;
                }

                default:
                {
                    return input;
                }
            }
        }
        public blockPos getBlockPos()
        {
            return position;
        }
    }
}