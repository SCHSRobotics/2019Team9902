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

import android.graphics.PostProcessor;
import android.os.AsyncTask;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.autonomus.AutoTest;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.CENTER;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.LEFT;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.NOT_FOUND;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.RIGHT;


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
public class VuforiaStone extends AsyncTask<WebcamName, Integer, Void> {
    //delete this later
    AutoTest autoTest = new AutoTest(); //DELETE
    //dividing line
    int line1 = 1;
    int line2 = 2;
    public boolean stoneFound = false;
    public Position.position stonePos;
    public int stoneNum = 0;
    public double tStoneX;
    double tStoneY;
    double tStoneZ;
    private VuforiaTrackables targets;
    private VuforiaTrackable stoneTarget;
    public boolean targetVisible;
    public void setup(WebcamName webcamName) {
        final String VUFORIA_KEY =
                "Abo0cgT/////AAABmbDm88xtoUF4mU6ziACe4Joq4soAB7QdoZtrGH22wU+0QIidhXCiCTqj5FbsU/dRJVkXE177O1PWZeAGEiD43rJS4He8f9/mqeF8nzEU4uS/kkf7bIkw2oafZImL15j+fuBlIvOdtSJlb4rPTn2oxiyMNzDWuJ7ovPXZP2rn1hbB2oRpmwTG6AgjQct9bsWBPF59Bohxy00iOz2OtUhoJOeWVlKseiO1qkQ6bS2c0qOAFgmYzfbpkssnRrtqZLyFS0JoIzdsHrr7DHUjV0kxlvNp8UJNXCbZrdNoPH1rTGaYjo7X3eZOCMmzJt7x886wvp5LWBdehe2H097KW8vVp4ooLshQ/sLuu2voA3sn4Aot";
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(autoTest.cameraMonitorViewId);

        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcamName;

        //  Instantiate the Vuforia engine
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targets = vuforia.loadTrackablesFromAsset("Skystone");

        stoneTarget = this.targets.get(0);
        stoneTarget.setName("Stone Target");
    }

    protected Void doInBackground(WebcamName... webcamName){
        this.targets.activate();
        while (!isCancelled()) {
            if (((VuforiaTrackableDefaultListener) stoneTarget.getListener()).isVisible()) {

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) stoneTarget.getListener()).getFtcCameraFromTarget();
                VectorF trans = pose.getTranslation();
                //Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                // Extract the X, Y, and Z components of the offset of the target relative to the robot
                stoneFound = true;
                tStoneX = trans.get(0);
                tStoneY = trans.get(1);
                tStoneZ = trans.get(2);
                if(tStoneX < line1){
                    stoneNum = 1;
                } else if(line1 < tStoneX){
                    if(tStoneX < line2){
                        stoneNum = 2;
                    } else{
                        stoneNum = 3;
                    }
                }
                stoneNum = stoneNum+Position.stoneOffset;
                if(stoneNum == 4) {
                    stoneNum = 1;
                }
                if(stoneNum == 5) {
                    stoneNum = 2;
                }
                if(stoneNum == 1){
                    stonePos = LEFT;
                } else if(stoneNum == 2){
                     stonePos = CENTER;
                } else if(stoneNum == 3){
                    stonePos = RIGHT;
                } else if(stoneNum == 0){
                    stonePos = NOT_FOUND;
                }
                // Extract the rotational components of the target relative to the robot
                //not nessiary rn but maybe useful later
                    /* double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;*/
                break;
            }
        }
        this.targets.deactivate();
        return null;
    }
}