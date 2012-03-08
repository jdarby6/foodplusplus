package eecs.dietary.assistant;

import java.io.File;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCR {
	
	private TessBaseAPI _tessapi;
	public String readText;
	
	
	OCR(File dir) {
		_tessapi = new TessBaseAPI();
		_tessapi.init(dir.toString(), "eng");
	}
	
	void ReadBitmapImage(Bitmap bitmap) {
		_tessapi.setImage(bitmap);
		readText = _tessapi.getUTF8Text();
	}
	

}
