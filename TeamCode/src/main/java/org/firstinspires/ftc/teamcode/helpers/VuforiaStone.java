/* Copyright (c) 2019 FIRST. All rights reserved.
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

package org.firstinspires.ftc.teamcode.helpers;

import android.os.AsyncTask;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/**
 * This 2019-2020 OpMode illustrates the basics of using the Vuforia localizer to determine
 * positioning and orientation of robot on the SKYSTONE FTC field.
 * The code is structured as a LinearOpMode
 *
 * When images are located, Vuforia is able to determine the position and orientation of the
 * image relative to the camera.  This sample code then combines that information with a
 * knowledge of where the target images are on the field, to determine the location of the camera.
 *
 * From the Audience perspective, the Red Alliance station is on the right and the
 * Blue Alliance Station is on the left.

 * Eight perimeter targets are distributed evenly around the four perimeter walls
 * Four Bridge targets are located on the bridge uprights.
 * Refer to the Field Setup manual for more specific location details
 *
 * A final calculation then uses the location of the camera on the robot to determine the
 * robot's location and orientation on the field.
 *
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  skystone/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */

public class VuforiaStone extends AsyncTask<VuforiaParameters, VectorF[], String> {
    private VuforiaTrackables targetsSkyStone;
    private boolean targetVisible;
    private OpenGLMatrix lastLocation;
    private List<VuforiaTrackable> allTrackables;

    private VuforiaTrackable[] visibleTargets;


    protected void onPreExecute(VuforiaParameters... params) {
       WebcamName webcamName = params[0].webcamName;
       int cameraMonitorViewId = params[0].cameraMonitorViewId;

       final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
       final boolean PHONE_IS_PORTRAIT = false;

       /*
        * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
        * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
        * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
        * web site at https://developer.vuforia.com/license-manager.
        *
        * Vuforia license keys are always 380 characters long, and look as if they contain mostly
        * random data. As an example, here is a example of a fragment of a valid key:
        *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
        * Once you've obtained a license key, copy the string from the Vuforia web site
        * and paste it in to your code on the next line, between the double quotes.
        */
       final String VUFORIA_KEY =
               "Abo0cgT/////AAABmbDm88xtoUF4mU6ziACe4Joq4soAB7QdoZtrGH22wU+0QIidhXCiCTqj5FbsU/dRJVkXE177O1PWZeAGEiD43rJS4He8f9/mqeF8nzEU4uS/kkf7bIkw2oafZImL15j+fuBlIvOdtSJlb4rPTn2oxiyMNzDWuJ7ovPXZP2rn1hbB2oRpmwTG6AgjQct9bsWBPF59Bohxy00iOz2OtUhoJOeWVlKseiO1qkQ6bS2c0qOAFgmYzfbpkssnRrtqZLyFS0JoIzdsHrr7DHUjV0kxlvNp8UJNXCbZrdNoPH1rTGaYjo7X3eZOCMmzJt7x886wvp5LWBdehe2H097KW8vVp4ooLshQ/sLuu2voA3sn4Aot";

       // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
       // We will define some constants and conversions here
       final float mmPerInch = 25.4f;
       final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor

       // Constant for Stone Target
       final float stoneZ = 2.00f * mmPerInch;

       // Class Members
       lastLocation = null;
       VuforiaLocalizer vuforia = null;

       /**
        * This is the webcam we are to use. As with other hardware devices such as motors and
        * servos, this device is identified using the robot configuration tool in the FTC application.
        */

       targetVisible = false;
       float phoneXRotate = 0;
       float phoneYRotate = 0;
       float phoneZRotate = 0;

       /*
        * Retrieve the camera we are to use.
        */
       /*
        * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
        * If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
        */
       VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

       // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

       parameters.vuforiaLicenseKey = VUFORIA_KEY;

       /**
        * We also indicate which camera on the RC we wish to use.
        */
       parameters.cameraName = webcamName;

       //  Instantiate the Vuforia engine
       vuforia = ClassFactory.getInstance().createVuforia(parameters);

       // Load the data sets for the trackable objects. These particular data
       // sets are stored in the 'assets' part of our application.
        targetsSkyStone = vuforia.loadTrackablesFromAsset("Skystone");

       VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
       stoneTarget.setName("Stone Target");

       // For convenience, gather together all the trackable objects in one easily-iterable collection */
       //allTrackables = new ArrayList<VuforiaTrackable>();
       //allTrackables.addAll(targetsSkyStone);

       /**
        * In order for localization to work, we need to tell the system where each target is on the field, and
        * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
        * Transformation matrices are a central, important concept in the math here involved in localization.
        * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
        * for detailed information. Commonly, you'll encounter transformation matrices as instances
        * of the {@link OpenGLMatrix} class.
        *
        * If you are standing in the Red Alliance Station looking towards the center of the field,
        *     - The X axis runs from your left to the right. (positive from the center to the right)
        *     - The Y axis runs from the Red Alliance Station towards the other side of the field
        *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
        *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
        *
        * Before being transformed, each target image is conceptually located at the origin of the field's
        *  coordinate system (the center of the field), facing up.
        */

       // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
       // Rotated it to to face forward, and raised it to sit on the ground correctly.
       // This can be used for generic target-centric approach algorithms
       stoneTarget.setLocation(OpenGLMatrix
               .translation(0, 0, stoneZ)
               .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));


       //
       // Create a transformation matrix describing where the phone is on the robot.
       //
       // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
       // Lock it into Portrait for these numbers to work.
       //
       // Info:  The coordinate frame for the robot looks the same as the field.
       // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
       // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
       //
       // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
       // pointing to the LEFT side of the Robot.
       // The two examples below assume that the camera is facing forward out the front of the robot.

       // We need to rotate the camera around it's long axis to bring the correct camera forward.
       if (CAMERA_CHOICE == BACK) {
           phoneYRotate = -90;
       } else {
           phoneYRotate = 90;
       }

       // Rotate the phone vertical about the X axis if it's in portrait mode
       if (PHONE_IS_PORTRAIT) {
           phoneXRotate = 90;
       }

       // Next, translate the camera lens to where it is on the robot.
       // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
       final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot-center
       final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
       final float CAMERA_LEFT_DISPLACEMENT = 0;     // eg: Camera is ON the robot's center line

       OpenGLMatrix robotFromCamera = OpenGLMatrix
               .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
               .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

       /**  Let all the trackable listeners know where the phone is.  */

           ((VuforiaTrackableDefaultListener) stoneTarget.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);


   }
    protected String doInBackground(VuforiaParameters... params) {
        targetsSkyStone.activate();
        if (isCancelled()) {
            targetsSkyStone.deactivate();
            return "done";
        }
        return null;
    }


    protected int onProgressUpdate(VectorF... progress){
    // check all the trackable targets to see which one (if any) is visible.
                targetVisible = false;

                    if (((VuforiaTrackableDefaultListener) stoneTarget.getListener()).isVisible()) {
                        targetVisible = true;

                        // getUpdatedRobotLocation() will return null if no new information is available since
                        // the last time that call was made, or if the trackable is not currently visible.
                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) stoneTarget.getListener()).getUpdatedRobotLocation();
                        if (robotLocationTransform != null) {
                            lastLocation = robotLocationTransform;
                        }
                        break;
                    }


                // Provide feedback as to where the robot is located (if we know).
                if (targetVisible) {
                    // express position (translation) of robot in inches.
                    VectorF translation = lastLocation.getTranslation();
                    progress[0] = lastLocation.getTranslation();
                    // express the rotation of the robot in degrees.
                    Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                } else {
                }
            }

            // Disable Tracking when we are done;
        }
