package eecs.dietary.assistant;

import java.util.ArrayList;
import java.util.List;





public class OCRReader {
	
	
		public List<String> ingreds = new ArrayList<String>();
		public List<Integer> confidences = new ArrayList<Integer>();
	
	
		public List<String> FindIngredients(String OCRtext) {
			//List<String> ingreds = new ArrayList<String>();
			List<String> tempingreds = new ArrayList<String>();
			ingreds.clear();
			confidences.clear();
			int pos,LD;
			//for(int i=0; i<DietaryAssistantActivity._Ingredients.allergiesSuffered.size(); i++) {
				tempingreds = DietaryAssistantActivity._Ingredients.returnAll();
				for(int j=0; j<tempingreds.size(); j++) {
				
					pos = FindFirstPositionOf(tempingreds.get(j),OCRtext);
					if(pos+tempingreds.get(j).length() > OCRtext.length()) { } 
					else {
						LD = getLevenshteinDistance(tempingreds.get(j).toUpperCase(),OCRtext.substring(pos, pos+tempingreds.get(j).length()).toUpperCase());
						if( (double) LD / (double)tempingreds.get(j).length() < 0.2) { //change less than 20% of characters
							
							String[] words = tempingreds.get(j).split(" ");
							if(words.length > 1) { 
								boolean valid = true;
								int mean_confidence = 0;
								int totallength = 0;
								int pos2;
								for(int k=0; k<words.length; k++) {
									pos2 = FindFirstPositionOf(words[k],OCRtext.substring(pos,pos+tempingreds.get(j).length()));
									totallength = totallength + words[k].length();
									LD = getLevenshteinDistance(words[k].toUpperCase(),OCRtext.substring(pos2, pos2+words[k].length()).toUpperCase());
									if( (double) LD / (double)words[k].length() < 0.2) { //change less than 20% of characters
										mean_confidence = mean_confidence + words[k].length() * (int)Math.floor(100 *(1 - ((double) LD / (double)words[k].length())));
									}
									else {
										valid = false;
										break;
									}
								}
								if(valid) {
									ingreds.add(tempingreds.get(j));
									confidences.add( mean_confidence / totallength );
								}						
							}
							else {
							
								ingreds.add(tempingreds.get(j));
								confidences.add( (int)Math.floor(100*(1-(double) LD / (double)tempingreds.get(j).length())) );
						
							}
						}
					}
					
			//	}
			}
	
			return ingreds;
		}
	
		private int FindFirstPositionOf(String word, String text) {
		//checks every subset of word.length in text and returns best match using levenshtein distance
		int pos = -1;
		
		String substring = "a";
		int start = 0;
		int minval = 1000;
		int val = 0;
		while((start+word.length()-1)<text.length()) {
			substring = text.substring(start, start+word.length());
			start = start + 1;
			val = getLevenshteinDistance(word.toUpperCase(),substring.toUpperCase());
			if(val < minval) {
				minval = val;
				pos = start-1;
			}
		}
		
		return pos;
	}
	
	
	
	/*	public List<String> RetrieveIngredients(String OCRtext) {
		
		/*int pos = FindFirstPositionOf("Ingredients:",OCRtext); 
		if("Ingredients".length() > OCRtext.length()) { }
		else {
			OCRtext = OCRtext.substring(pos+"Ingredients:".length(),OCRtext.length());
		}
		List<String> cleanedIngredients = new ArrayList<String>();
		
		String[] ingredients = OCRtext.split(",");
		
		
		for(int i=0; i<ingredients.length; i++) {
				cleanedIngredients.add(ingredients[i]);
		}
		
//		IngredientsFound = cleanedIngredients;
		
		return cleanedIngredients;
	}*/
	
	/*	private String[] CleanText(String OCROutput) {
		//List<String> cleanedText = new ArrayList<String>();
		
			String[] ingredients = OCROutput.split(",");
		
		

			return ingredients;
		}
	*/
	private int getLevenshteinDistance (String s, String t) {
	//credit to http://www.merriampark.com/ldjava.htm
		  if (s == null || t == null) {
		    throw new IllegalArgumentException("Strings must not be null");
		  }
				
		  /*
		    The difference between this impl. and the previous is that, rather 
		     than creating and retaining a matrix of size s.length()+1 by t.length()+1, 
		     we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
		     is the 'current working' distance array that maintains the newest distance cost
		     counts as we iterate through the characters of String s.  Each time we increment
		     the index of String t we are comparing, d is copied to p, the second int[].  Doing so
		     allows us to retain the previous cost counts as required by the algorithm (taking 
		     the minimum of the cost count to the left, up one, and diagonally up and to the left
		     of the current cost count being calculated).  (Note that the arrays aren't really 
		     copied anymore, just switched...this is clearly much better than cloning an array 
		     or doing a System.arraycopy() each time  through the outer loop.)

		     Effectively, the difference between the two implementations is this one does not 
		     cause an out of memory condition when calculating the LD over two very large strings.  		
		  */		
				
		  int n = s.length(); // length of s
		  int m = t.length(); // length of t
				
		  if (n == 0) {
		    return m;
		  } else if (m == 0) {
		    return n;
		  }

		  int p[] = new int[n+1]; //'previous' cost array, horizontally
		  int d[] = new int[n+1]; // cost array, horizontally
		  int _d[]; //placeholder to assist in swapping p and d

		  // indexes into strings s and t
		  int i; // iterates through s
		  int j; // iterates through t

		  char t_j; // jth character of t

		  int cost; // cost

		  for (i = 0; i<=n; i++) {
		     p[i] = i;
		  }
				
		  for (j = 1; j<=m; j++) {
		     t_j = t.charAt(j-1);
		     d[0] = j;
				
		     for (i=1; i<=n; i++) {
		        cost = s.charAt(i-1)==t_j ? 0 : 1;
		        // minimum of cell to the left+1, to the top+1, diagonally left and up +cost				
		        d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);  
		     }

		     // copy current distance counts to 'previous row' distance counts
		     _d = p;
		     p = d;
		     d = _d;
		  } 
				
		  // our last action in the above loop was to switch d and p, so p now 
		  // actually has the most recent cost counts
		  return p[n];
		}

}
