package eecs.dietary.assistant;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;


class Ingredients {
	List<String> all_ingreds;
	List<String> allergiesSuffered;
	List<String> all_allergies;
	private String dir;

	//Constructor that populates "all_ingreds" list variable
	Ingredients(String dir) {
		this.dir = dir;
		all_ingreds = new ArrayList<String>();
		allergiesSuffered = new ArrayList<String>();
		all_allergies = new ArrayList<String>();
		
		
		all_allergies.add("soy");
		all_allergies.add("eggs");
		all_allergies.add("fish");
		all_allergies.add("milk");
		all_allergies.add("peanuts");
		all_allergies.add("shellfish");
		all_allergies.add("treenuts");
		all_allergies.add("wheat");
		

		try{
			// Open the file that is the first command line parameter
			
			//File myDir = getExternalFilesDir(Environment.MEDIA_MOUNTED);
			
			
			FileInputStream fstream = new FileInputStream(dir+"/eggs.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Add the content to list of ingredients
				all_ingreds.add(strLine);
			}
			//Close the input stream
			in.close();
			
			
			//Repeat for other allergies
			fstream = new FileInputStream(dir+"/fish.txt");
			in = new DataInputStream(fstream);
			
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();
			
			fstream = new FileInputStream(dir+"/milk.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();

			
			
			fstream = new FileInputStream(dir+"/peanuts.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();

			
			fstream = new FileInputStream(dir+"/shellfish.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();

			
			fstream = new FileInputStream(dir+"/soy.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();

			
			
			fstream = new FileInputStream(dir+"/treenuts.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();

			
			
			fstream = new FileInputStream(dir+"/wheat.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				if(strLine.length()>1) {
					all_ingreds.add(strLine);
				}
			}
			in.close();
			
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
	}

	public List<String> returnAll() {
		return all_ingreds;
	}

	public List<String> getAllergies(String ingredient) {
		// TODO Auto-generated method stub
		return all_ingreds;
		//return null;
	}
	public boolean check(String ingredient) {
		for(int i=0; i<allergiesSuffered.size(); i++) {
			List<String> temp = new ArrayList<String>();
			temp = returnSingleAllergy(allergiesSuffered.get(i));
			if(temp.contains(ingredient)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(String ingredient) {
		for(int i=0; i<all_ingreds.size(); i++) {
			if(ingredient.toLowerCase().trim()==all_ingreds.get(i).toLowerCase().trim()) {
				return true;
			}
		}
		return false;
	}
	

	
	
	public List<String> returnSingleAllergy(String allergy) {
		List<String> single_ingred_list = new ArrayList<String>();
		try{
			// Open the file that is the first command line parameter
			FileInputStream fstream;
			if(allergy.equalsIgnoreCase("eggs")) 
				fstream = new FileInputStream(dir+"/eggs.txt");
			else if (allergy.equalsIgnoreCase("fish"))
				fstream = new FileInputStream(dir+"/fish.txt");
			else if (allergy.equalsIgnoreCase("milk"))
				fstream = new FileInputStream(dir+"/milk.txt");
			else if (allergy.equalsIgnoreCase("peanuts"))
				fstream = new FileInputStream(dir+"/peanuts.txt");
			else if (allergy.equalsIgnoreCase("shellfish"))
				fstream = new FileInputStream(dir+"/shellfish.txt");
			else if (allergy.equalsIgnoreCase("soy"))
				fstream = new FileInputStream(dir+"/soy.txt");
			else if (allergy.equalsIgnoreCase("treenuts"))
				fstream = new FileInputStream(dir+"/treenuts.txt");
			else fstream = new FileInputStream(dir+"/wheat.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Add the content to list of ingredients
				single_ingred_list.add(strLine);
			}
			//Close the input stream
			in.close();
			
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return single_ingred_list;
	}
}