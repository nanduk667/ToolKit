package com.iamtheonewhoknocks.toolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		flashlightButton();
		calculatorButton();

		mirrorButton();

		converterButton();
		mgglassButton();
		timerButton();

	}

	private void flashlightButton() {
		ImageButton flButton = (ImageButton) findViewById(R.id.flashButton);
		flButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						FlashlightActivity.class));

			}
		});
	}

	private void calculatorButton() {
		ImageButton flButton = (ImageButton) findViewById(R.id.calcButton);
		flButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						CalculatorActivity.class));

			}
		});
	}

	private void mirrorButton() {
		ImageButton mrButton = (ImageButton) findViewById(R.id.mirrorButton);
		mrButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MirrorActivity.class));

			}
		});
	}

	private void converterButton() {
		ImageButton flButton = (ImageButton) findViewById(R.id.converterButton);
		flButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						ConverterActivity.class));

			}
		});
	}

	private void mgglassButton() {
		ImageButton mrButton = (ImageButton) findViewById(R.id.mglassButton);
		mrButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MagnifyActivity.class));

			}
		});
	}

	private void timerButton() {
		ImageButton mrButton = (ImageButton) findViewById(R.id.timerButton);
		mrButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, TimerActivity.class));

			}
		});
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */
}
