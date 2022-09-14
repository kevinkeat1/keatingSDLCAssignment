package keatingSDLCProject;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class keatingSDLCAssignment {
	
	 public static <K, V extends Comparable<V> > Map<K, V> //Sorts the TreeMap
	    valueSort(final Map<K, V> map)
	    {
	        
	        Comparator<K> valueComparator = new Comparator<K>() {
	        	
	        	public int compare(K k1, K k2) {
	        		int comp = map.get(k1).compareTo(map.get(k2));
	                      if (comp == 0)
	                          return 1;
	                      else
	                          return comp;
	                      }
	        	};
	        
	        // SortedMap created using the comparator
	        Map<K, V> sorted = new TreeMap<K, V>(valueComparator);
	        
	        sorted.putAll(map);
	        
	        return sorted;
	    }

	public static void main(String[] args) throws FileNotFoundException {
		
		Path path = Paths.get("C:\\Users\\kevin\\eclipse-workspace\\keatingSDLCProject\\theRaven.txt"); // Gets file
        try {
            String text = Files.readString(path); // throws java.io.IOException
            text = text.toLowerCase();
            Pattern pttrn = Pattern.compile("[a-z]+"); // Sets the word to look for
            Matcher mtchr = pttrn.matcher(text); // Flags each time a match of String word is found
            TreeMap<String, Integer> freq = new TreeMap<>();
            int longest = 0;
            while (mtchr.find()) {
                String word = mtchr.group();
                int letters = word.length();
                if (letters > longest) {
                    longest = letters;
                }
                if (freq.containsKey(word)) { 
                    freq.computeIfPresent(word, (w, c) -> Integer.valueOf(c.intValue() + 1));
                }
                else {
                    freq.computeIfAbsent(word, (w) -> Integer.valueOf(1));
                }
            }
         // Calling the method valueSort
            Map sortedMap = valueSort(freq);
      
            // Get a set of the entries on the sorted map
            Set set = sortedMap.entrySet();
      
            // Get an iterator
            Iterator i = set.iterator();
      
            // Display elements
            while (i.hasNext()) {
                
                Map.Entry mp = (Map.Entry)i.next();
                System.out.print(mp.getKey() + ": ");
                System.out.println(mp.getValue());
                
            }
        }
        catch (IOException xIo) {
            xIo.printStackTrace();
        }
	}

}
