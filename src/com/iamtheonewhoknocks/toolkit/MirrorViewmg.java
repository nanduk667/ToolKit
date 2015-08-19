package com.iamtheonewhoknocks.toolkit;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MirrorViewmg extends SurfaceView implements SurfaceHolder.Callback {
	// Fields -----------------------------------------------------------------
	private static SurfaceHolder holder;
	private Camera camera;
	final Activity mActivity;
	private int cameraId = 0;
	 int currentZoomLevel = 0;
	 private boolean inPreview=false;
	 Parameters params = camera.getParameters();
//	public Activity activity= MirrorView.this;

	// Constructor ------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public MirrorViewmg(Activity activity, Camera camera) {
		super(activity);
		this.camera = camera;
		 mActivity = activity;
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	private Camera.Size getBestPreviewSize(int width, int height,Camera.Parameters parameters){   
        Camera.Size result=null;   
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) 
        {   
            if (size.width<=width && size.height<=height)
            {
                if (result==null) {  
                    result=size;   
                }   else {  
                    int resultArea=result.width*result.height; 
                    int newArea=size.width*size.height;  
                    if (newArea>resultArea) {   
                        result=size;   
                    }  
                }   
            }   
        }   
        return(result);   
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

        params = camera.getParameters();       
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        Camera.Size size=getBestPreviewSize(width, height,                                           
                params);       
        if (size!=null) {      
            params.setPreviewSize(size.width, size.height);
            camera.setParameters(params);       
            camera.startPreview();       
            inPreview=true;   
            ZoomControls zoomControls = (ZoomControls) findViewById(R.id.CAMERA_ZOOM_CONTROLS);

            if (params.isZoomSupported()) {
                final int maxZoomLevel = params.getMaxZoom();
                Log.i("max ZOOM ", "is " + maxZoomLevel);
                zoomControls.setIsZoomInEnabled(true);
                zoomControls.setIsZoomOutEnabled(true);

                zoomControls.setOnZoomInClickListener(new OnClickListener(){
                    public void onClick(View v){
                        if(currentZoomLevel < maxZoomLevel){
                            currentZoomLevel++;
                            //mCamera.startSmoothZoom(currentZoomLevel);
                            params.setZoom(currentZoomLevel);
                            camera.setParameters(params);
                        }
                    }
                });

                zoomControls.setOnZoomOutClickListener(new OnClickListener(){
                    public void onClick(View v){
                        if(currentZoomLevel > 0){
                            currentZoomLevel--;
                            params.setZoom(currentZoomLevel);
                            camera.setParameters(params);
                        }
                    }
                });    
            }
            else
                zoomControls.setVisibility(View.GONE);
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
