package com.iamtheonewhoknocks.toolkit;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MagnifyActivity extends Activity { // same as MirrorActivity with
												// different camPreview
	// Fields -----------------------------------------------------------------
	private Camera camera = null;
	private MirrorViewmg camPreview = null;
	private FrameLayout previewLayout = null;
	private int cameraId = 0 ;

	// Methods ----------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magnify);
		// Find out if we even have a camera
		if (!getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Toast.makeText(this, "No camera feature on this device",
					Toast.LENGTH_SHORT).show();
		} else {

			// Get the ID of the back facing camera
			cameraId = findFirstBackFacingCamera();
			
			// If we have a valid camera
			if (cameraId < 1) {

				// Get the preview frame and strip it of all of it's views
				previewLayout = (FrameLayout) findViewById(R.id.camPreviewmg);
				previewLayout.removeAllViews();

				// Start the camera
				startCameraInLayout(previewLayout, cameraId);

			} else {
				Toast.makeText(this, "No back facing camera found",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onPause() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (camera == null && previewLayout != null) {
			previewLayout.removeAllViews();
			startCameraInLayout(previewLayout, cameraId);
		}
	}

	private int findFirstBackFacingCamera() {
		int foundId = -1;
		int numCams = Camera.getNumberOfCameras();
		for (int camId = 0; camId < numCams; camId++) {

			// Get the camera information
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(camId, info);

			// Check to see if this is a back facing camera
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				foundId = camId;
				break;
			}
		}
		return foundId;
	}

	private void startCameraInLayout(FrameLayout layout, int cameraId) {
		camera = Camera.open(cameraId);
		if (camera != null) {

			// Create a new MirrorView object and
			// add the view to the FrameLayout
			camPreview = new MirrorViewmg(this, camera);
			layout.addView(camPreview);

		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.magnify, menu); return true; }
	 */

}
