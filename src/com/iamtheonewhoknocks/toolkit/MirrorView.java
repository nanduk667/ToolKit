package com.iamtheonewhoknocks.toolkit;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MirrorView extends SurfaceView implements SurfaceHolder.Callback {
	// Fields -----------------------------------------------------------------
	private static SurfaceHolder holder;
	private Camera camera;
	final Activity mActivity;
	private int cameraId = 0;
//	public Activity activity= MirrorView.this;

	// Constructor ------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public MirrorView(Activity activity, Camera camera) {
		super(activity);
		this.camera = camera;
		 mActivity = activity;
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	// Methods ----------------------------------------------------------------
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// If the surface does not exist, just exit this method
		if (holder.getSurface() == null) {
			return;
		}

		// Otherwise, stop the camera...
		try {
			camera.stopPreview();
		} catch (Exception ex) {
			Toast.makeText(this.getContext(), ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

		// ... and reset the display window

		try {
			setCameraDisplayOrientationAndSize(mActivity,0,camera);
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (Exception ex) {
			Toast.makeText(this.getContext(), "Error starting camera preview",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {

			// Set the camera preview display and
			// start the preview capture
			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (Exception ex) {
			Toast.makeText(this.getContext(), "Error starting camera preview",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	
	
	private static void setCameraDisplayOrientationAndSize(Activity activity, int cameraId,
            android.hardware.Camera camera) {
		int result;
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = rotation * 90;

		// If this is a front facing camera
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;
		} else {
			result = (info.orientation - degrees + 360) % 360;
		}

		// Set the display orientation of the camera and
		// get the preview size
		camera.setDisplayOrientation(result);
		Camera.Size previewSize = camera.getParameters().getPreviewSize();

		// Set the width and height of the preview window
		// with a dependency of the rotation angle
		if (result == 90 || result == 270) {
			holder.setFixedSize(previewSize.height, previewSize.width);
		} else {
			holder.setFixedSize(previewSize.width, previewSize.height);
		}
	}

}
