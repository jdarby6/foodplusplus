package eecs.dietary.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BarcodeView extends Activity {
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
		startActivityForResult(intent, 0);

	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				//May use this line below, maybe not. Commented out to avoid warning.
//				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan

				//temporary message to demonstrate that the barcode was read successfully
				Toast.makeText(this, "Got this number from barcode: " + contents, Toast.LENGTH_LONG).show();
				
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

}