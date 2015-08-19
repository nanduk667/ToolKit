package com.iamtheonewhoknocks.toolkit;

import java.text.DecimalFormat;
import android.os.Bundle;
import android.app.Activity;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends Activity implements OnClickListener {

	private TextView mCalculatorDisplay;
	private Boolean userIsInTheMiddleOfTypingANumber = false;
	private CalculatorBrain mCalculatorBrain;
	private static final String DIGITS = "0123456789.";

	DecimalFormat df = new DecimalFormat("@###########");

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide the status bar and other OS-level chrome
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);

		mCalculatorBrain = new CalculatorBrain();
		mCalculatorDisplay = (TextView) findViewById(R.id.textView1);

		df.setMinimumFractionDigits(0);
		df.setMinimumIntegerDigits(1);
		df.setMaximumIntegerDigits(8);

		findViewById(R.id.button0).setOnClickListener(this);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
		findViewById(R.id.button5).setOnClickListener(this);
		findViewById(R.id.button6).setOnClickListener(this);
		findViewById(R.id.button7).setOnClickListener(this);
		findViewById(R.id.button8).setOnClickListener(this);
		findViewById(R.id.button9).setOnClickListener(this);

		findViewById(R.id.buttonAdd).setOnClickListener(this);
		findViewById(R.id.buttonSubtract).setOnClickListener(this);
		findViewById(R.id.buttonMultiply).setOnClickListener(this);
		findViewById(R.id.buttonDivide).setOnClickListener(this);
		findViewById(R.id.buttonToggleSign).setOnClickListener(this);
		findViewById(R.id.buttonDecimalPoint).setOnClickListener(this);
		findViewById(R.id.buttonEquals).setOnClickListener(this);
		findViewById(R.id.buttonClear).setOnClickListener(this);

	}

	public void onClick(View v) {

		String buttonPressed = ((Button) v).getText().toString();

		if (DIGITS.contains(buttonPressed)) {

			// digit was pressed
			if (userIsInTheMiddleOfTypingANumber) {

				if (buttonPressed.equals(".")
						&& mCalculatorDisplay.getText().toString()
								.contains(".")) {
					// ERROR PREVENTION
					// Eliminate entering multiple decimals
				} else {
					mCalculatorDisplay.append(buttonPressed);
				}

			} else {

				if (buttonPressed.equals(".")) {
					// ERROR PREVENTION
					// This will avoid error if only the decimal is hit before
					// an operator, by placing a leading zero
					// before the decimal
					mCalculatorDisplay.setText(0 + buttonPressed);
				} else {
					mCalculatorDisplay.setText(buttonPressed);
				}

				userIsInTheMiddleOfTypingANumber = true;
			}

		} else {
			// operation was pressed
			if (userIsInTheMiddleOfTypingANumber) {

				mCalculatorBrain.setOperand(Double
						.parseDouble(mCalculatorDisplay.getText().toString()));
				userIsInTheMiddleOfTypingANumber = false;
			}

			mCalculatorBrain.performOperation(buttonPressed);
			mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));

		}

	}

	public class CalculatorBrain {
		// 3 + 6 = 9
		// 3 & 6 are called the operand.
		// The + is called the operator.
		// 9 is the result of the operation.
		private double mOperand;
		private double mWaitingOperand;
		private String mWaitingOperator;
		private double mCalculatorMemory;

		// operator types
		public static final String ADD = "+";
		public static final String SUBTRACT = "-";
		public static final String MULTIPLY = "*";
		public static final String DIVIDE = "/";
		public static final String TOGGLESIGN = "+/-";
		public static final String CLEAR = "C";

		// public static final String EQUALS = "=";

		// constructor
		public CalculatorBrain() {
			// initialize variables upon start
			mOperand = 0;
			mWaitingOperand = 0;
			mWaitingOperator = "";
			mCalculatorMemory = 0;
		}

		public void setOperand(double operand) {
			mOperand = operand;
		}

		public double getResult() {
			return mOperand;
		}

		// used on screen orientation change
		public void setMemory(double calculatorMemory) {
			mCalculatorMemory = calculatorMemory;
		}

		// used on screen orientation change
		public double getMemory() {
			return mCalculatorMemory;
		}

		public String toString() {
			return Double.toString(mOperand);
		}

		protected double performOperation(String operator) {

			if (operator.equals(CLEAR)) {
				mOperand = 0;
				mWaitingOperator = "";
				mWaitingOperand = 0;
				// mCalculatorMemory = 0;
			} else if (operator.equals(TOGGLESIGN)) {
				mOperand = -mOperand;
			} else {
				performWaitingOperation();
				mWaitingOperator = operator;
				mWaitingOperand = mOperand;
			}

			return mOperand;
		}

		protected void performWaitingOperation() {

			if (mWaitingOperator.equals(ADD)) {
				mOperand = mWaitingOperand + mOperand;
			} else if (mWaitingOperator.equals(SUBTRACT)) {
				mOperand = mWaitingOperand - mOperand;
			} else if (mWaitingOperator.equals(MULTIPLY)) {
				mOperand = mWaitingOperand * mOperand;
			} else if (mWaitingOperator.equals(DIVIDE)) {
				if (mOperand != 0) {
					mOperand = mWaitingOperand / mOperand;
				}
			}

		}
	}
}
