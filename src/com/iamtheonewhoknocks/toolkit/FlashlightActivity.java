package com.iamtheonewhoknocks.toolkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class FlashlightActivity extends Activity {

	ImageButton switchButton;

	private Camera camera;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters params;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN,
	//WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        
		setContentView(R.layout.activity_flashlight);
		
		switchButton = (ImageButton) findViewById(R.id.btnSwitch);
		// First check if device is supporting flashlight or not
		hasFlash = getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

		if (!hasFlash) {
			// device doesn't support flash
			// Show alert message and close the application
			AlertDialog alert = new AlertDialog.Builder(FlashlightActivity.this)
					.create();
			alert.setTitle("Error");
			alert.setMessage("Sorry, your device doesn't support flash light! Do you want to use your screen as alternative?");
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							RelativeLayout wscreen = (RelativeLayout) findViewById(R.id.flashbg);

							wscreen.removeAllViews();
							wscreen.setBackgroundColor(0);
							
							WindowManager.LayoutParams lp = getWindow().getAttributes();
					        lp.screenBrightness = 1;
                            getWindow().setAttributes(lp);
						}
					});

			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// closing the application
							finish();
						}
					});

			alert.show();
			return;
		}

		// get the camera
		getCamera();

		// displaying button image
		toggleButtonImage();

		// Switch button click event to toggle flash on/off
		switchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFlashOn) {
					// turn off flash
					turnOffFlash();
				} else {
					// turn on flash
					turnOnFlash();
				}
			}
		});
	}

	// Get the camera
	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	// Turning On flash
	private void turnOnFlash() {
		if (!isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			// playSound();

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			isFlashOn = true;

			// changing button/switch image
			toggleButtonImage();
		}

	}

	// Turning Off flash
	private void turnOffFlash() {
		if (isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			// playSound();

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.stopPreview();
			isFlashOn = false;

			// changing button/switch image
			toggleButtonImage();
		}
	}

	/*
	 * Toggle switch button images changing image states to on / off
	 */
	private void toggleButtonImage() {
		if (isFlashOn) {
			switchButton.setImageResource(R.drawable.flashlight_on);
		} else {
			switchButton.setImageResource(R.drawable.flashlight_off);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// on pause turn off the flash
		turnOffFlash();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// on resume turn on the flash
		if (hasFlash)
			turnOnFlash();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// on starting the app get the camera params
		getCamera();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// on stop release the camera
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	
	 
	  @Override public boolean onCreateOptionsMenu(Menu menu) {
		  // Inflate the menu; this adds items to the action bar if it is present.
	 // getMenuInflater().inflate(R.menu.flashlight, menu); return true; 
		  menu.add("Use alternative flashlight");
		  return super.onCreateOptionsMenu(menu);
		  }
	  
	  @Override  public boolean onOptionsItemSelected(MenuItem Item)
	  {	 switch (Item.getItemId()) {
      case 0:
    	 
    	  
    	  RelativeLayout wscreen = (RelativeLayout) findViewById(R.id.flashbg);

			wscreen.removeAllViews();
			wscreen.setBackgroundColor(0);
			
			WindowManager.LayoutParams lp = getWindow().getAttributes();
	        lp.screenBrightness = 1;
          getWindow().setAttributes(lp);
          
          return true;
      default:
          return super.onOptionsItemSelected(Item);
  } 

      }
	  
	  }
	  
	 
	  


