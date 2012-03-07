package eecs.dietary.assistant;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


class Ingredients {
	List<String> all_ingreds;
	List<String> allergiesSuffered;
	List<String> all_allergies;
	

	//Constructor that populates "all_ingreds" list variable
	Ingredients() {
		all_ingreds = new ArrayList<String>();
		allergiesSuffered = new ArrayList<String>();
		all_allergies = new ArrayList<String>();
		all_allergies.add("soy");
		all_allergies.add("milk");
		all_allergies.add("nut");
		
	//	allergiesSuffered.add("milk");
		all_ingreds.add("milk");
		all_ingreds.add("leche");
		all_ingreds.add("cheese");
		all_ingreds.add("milk");
		all_ingreds.add("leche");
		all_ingreds.add("cheese");
		all_ingreds.add("milk");
		all_ingreds.add("leche");
		all_ingreds.add("cheese");
		all_ingreds.add("milk");
		all_ingreds.add("leche");
		all_ingreds.add("cheese");
		all_ingreds.add("cheese milk");
	/*	try{
			// Open the file that is the first command line parameter
			FileInputStream fstream = new FileInputStream("eggs.txt");
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
			fstream = new FileInputStream("fish.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
			fstream = new FileInputStream("milk.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
			fstream = new FileInputStream("peanuts.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
			fstream = new FileInputStream("shellfish.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
			fstream = new FileInputStream("soy.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
			fstream = new FileInputStream("treenuts.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
			fstream = new FileInputStream("wheat.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			while ((strLine = br.readLine()) != null) {
				all_ingreds.add(strLine);
			}
			in.close();
			
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}*/
		
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
			if(ingredient.toLowerCase().trim()==allergiesSuffered.get(i).toLowerCase().trim()) {
				return true;
			}
			//else {
		//		return false;
		//	}
		}
		return false;
	}
	

	
	
/*	public List<String> returnSingleAllergy(String allergy) {
		List<String> single_ingred_list = new ArrayList<String>();
		try{
			// Open the file that is the first command line parameter
			FileInputStream fstream;
			if(allergy.equalsIgnoreCase("eggs")) 
				fstream = new FileInputStream("eggs.txt");
			else if (allergy.equalsIgnoreCase("fish"))
				fstream = new FileInputStream("fish.txt");
			else if (allergy.equalsIgnoreCase("milk"))
				fstream = new FileInputStream("milk.txt");
			else if (allergy.equalsIgnoreCase("peanuts"))
				fstream = new FileInputStream("peanuts.txt");
			else if (allergy.equalsIgnoreCase("shellfish"))
				fstream = new FileInputStream("shellfish.txt");
			else if (allergy.equalsIgnoreCase("soy"))
				fstream = new FileInputStream("soy.txt");
			else if (allergy.equalsIgnoreCase("treenuts"))
				fstream = new FileInputStream("treenuts.txt");
			else fstream = new FileInputStream("wheat.txt");
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
	}*/
}