package eecs.dietary.assistant;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/eecs.dietary.assistant/databases/";

	private static String DB_NAME;

	private SQLiteDatabase myDataBase; 

	private final Context myContext;

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public DataBaseHelper(Context context, String name) {
		super(context, name, null, 1);
		DB_NAME = name;
		this.myContext = context;
	}	

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){
			//do nothing - database already exist
		}else{

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
//			this.getReadableDatabase();
			this.getWritableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

		}catch(SQLiteException e){

			//database does't exist yet.

		}

		if(checkDB != null){

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException{

		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if(myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// Add your public helper methods to access and get content from the database.

	/*
	 ***** Methods for ingredients database *****
	 */
	
	//get all ingredients in alphabetical order
	public Cursor returnAll() {
		return myDataBase.rawQuery("SELECT DISTINCT ingredient FROM all_ingreds ORDER BY ingredient ASC", null);
	}
	
	//get all allergies in alphabetical order
	public Cursor returnAllAllergies() {
		return myDataBase.rawQuery("SELECT DISTINCT allergy FROM all_ingreds ORDER BY allergy ASC", null);
	}
	
	//get all allergies for specified ingredient in alphabetical order
	public Cursor returnAllergyNames(String ingredient) {
		return myDataBase.rawQuery("SELECT allergy FROM all_ingreds WHERE ingredient = '" + ingredient + "' ORDER BY allergy ASC", null);
	}
	
	//get all ingredients associated with a certain allergy
	public Cursor returnIngredientNames(String allergy) {
		return myDataBase.rawQuery("SELECT DISTINCT ingredient FROM all_ingreds WHERE allergy = '" + allergy.toUpperCase() +"'", null);
	}
	
	//look for a specific ingredient (returns the name of the ingredient if it's found)
	public Cursor checkIfIngredientExists(String ingredient) {
		return myDataBase.rawQuery("SELECT ingredient FROM all_ingreds WHERE ingredient = '" + ingredient.toUpperCase() + "'", null);
	}
	
	//look for a specific allergy (returns the name of the allergy if it's found)
	public Cursor checkIfAllergyExists(String allergy) {
		return myDataBase.rawQuery("SELECT allergy FROM all_ingreds WHERE allergy = '" + allergy.toUpperCase() + "'", null);
	}
	
	//insert a new row into the ingredients database
	public void Insert(String allergy, String ingredient) {
		
		// ContentValues class is used to store a set of values that the ContentResolver can process. 
		ContentValues contentValues = new ContentValues();
		
		// Get values from the POJO class and passing them to the ContentValues class
		contentValues.put("allergy", allergy.toUpperCase());
		contentValues.put("ingredient", ingredient.toUpperCase());

		// Now we can insert the data in to relevant table
		long affectedColumnId = myDataBase.insert("all_ingreds", null, contentValues);
		
		Log.d("affectedColumnId", String.valueOf(affectedColumnId));
		
//		if(myDataBase.isReadOnly()) { }
//		else {
//			try {
//				myDataBase.execSQL("INSERT INTO all_ingreds(allergy, ingredient) VALUES('"+allergy+"', '"+ingredient+"')");
//			}
//			catch(SQLException e) {
//			}
//		}
	}
	
	public Cursor returnAllAllergens(List<String> allergies) {
		if(allergies.size() > 0) {
			String query = "SELECT DISTINCT ingredient FROM all_ingreds WHERE allergy = " + allergies.get(0).toUpperCase();
			
			for(int i = 1; i < allergies.size(); i++) {
				query = query + " OR allergy = " + allergies.get(i).toUpperCase();
			}
			
			query = query + " ORDER BY ingredient ASC";
			
			return myDataBase.rawQuery(query, null);
		}
		else return null;
	}
	
	
	
	
	
	/*
	 ***** Methods for barcode database *****
	 */
	
	//look for a upc code, return its info if found
	public Cursor findUPC(String upc) {
		return myDataBase.rawQuery("SELECT brand, name, description, container, size, uom, ingredients FROM barcode_data WHERE upc_a = " + upc, null);
	}
	
	/*
	 * Methods for allergy icons
	 */
	public Cursor findAllergyIcon(String allergy) {
		//ADD CODE HERE
		return null;
	}
	public void InsertAllergyIcon(String allergy, int index) {
		//ADD CODE HERE
	}
	
	public void RemoveAllergyIcon(String allergy) {
		//ADD CODE HERE
	}	
}