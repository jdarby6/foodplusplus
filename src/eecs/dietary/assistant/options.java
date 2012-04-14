package eecs.dietary.assistant;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;



public class options extends Activity implements SeekBar.OnSeekBarChangeListener {
	
	SeekBar _ocrQualityBar;
	TextView _ocrQualityValue;
	TextView _ocrEstimatedtime;
	TextView _ocrQualityValueTop;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.options);
		Typeface tf = Typeface.createFromAsset(
		        getBaseContext().getAssets(), "fonts/MODERNA_.TTF");
		_ocrQualityBar = (SeekBar) findViewById(R.id.seekbarOCRquality);
		_ocrEstimatedtime = (TextView) findViewById(R.id.estimatedTime);
		_ocrQualityValue = (TextView) findViewById(R.id.OCRqualitytext);
		_ocrQualityValueTop = (TextView) findViewById(R.id.OCRQualityIntro);
		_ocrQualityValueTop.setTypeface(tf);
		_ocrEstimatedtime.setTypeface(tf);
		_ocrQualityValue.setTypeface(tf);
		_ocrQualityBar.setProgress( DietaryAssistantActivity._OCR.calculateQualityRating());
		_ocrQualityBar.setSecondaryProgress(_ocrQualityBar.getProgress());
		_ocrEstimatedtime.setText(calcEstimateTimeMsg(_ocrQualityBar.getProgress()));
		_ocrQualityValue.setText(Integer.toString(_ocrQualityBar.getProgress()));
		_ocrQualityBar.setOnSeekBarChangeListener(this);
	}
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		_ocrQualityValue.setText(Integer.toString(arg1));
		_ocrQualityBar.setProgress(arg1);
		_ocrEstimatedtime.setText(calcEstimateTimeMsg(arg1));
	}
	public void onStartTrackingTouch(SeekBar arg0) {
		//_ocrQualityBar.setSecondaryProgress(arg0.getProgress());
		
	}
	public void onStopTrackingTouch(SeekBar arg0) {
		_ocrQualityBar.setSecondaryProgress(arg0.getProgress());
		DietaryAssistantActivity._OCR.setQualityRating(arg0.getProgress());
	}
	
	private String calcEstimateTimeMsg(int progress) {

		//need to add code here to determine based on 
		//camera specs of phone, the estimated time
		double estimatedSecs = ((double)5/(double)10000) * Math.pow(progress, 2); //quadratic decay, expected time 5s on 100quality
		String msg = Double.toString(estimatedSecs).substring(0, 3) + " seconds estimated computation time";
		
		return msg;
	}
	
	
	
	
	
}