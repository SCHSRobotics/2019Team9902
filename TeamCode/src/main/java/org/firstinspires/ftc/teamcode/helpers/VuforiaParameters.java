package org.firstinspires.ftc.teamcode.helpers;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class VuforiaParameters {
    WebcamName webcamName;
    int cameraMonitorViewId;
    public VuforiaParameters(WebcamName webcamName, int cameraMonitorViewId) {
        this.webcamName = webcamName;
        this.cameraMonitorViewId = cameraMonitorViewId;
    }
}
