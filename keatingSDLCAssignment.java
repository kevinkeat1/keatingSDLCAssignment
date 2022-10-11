package keatingSDLCProject;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map.*;

public class keatingSDLCAssignment {
	
	static TreeMap<String, Integer> freq = new TreeMap<>();
	static Map<String, Integer> sortedMap = valueSort(freq);
	
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
	 
	 public void init() {
		 JFrame frame = new JFrame(); //Initial panel, will have button that when clicked will display values
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(500, 600);
			frame.setTitle("Word Occurance");
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
			
			JPanel occurances = new JPanel();
			
			String[] columns = new String[] {"Word", "Number"}; //So I can put treemap into the UI
			DefaultTableModel defaultModel = new DefaultTableModel(columns, 0);
			
			Set<Entry<String, Integer>> entries = sortedMap.entrySet();
			
			for(Entry<String, Integer> entry:entries) {
				defaultModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
			}
			
			JTable myTable = new JTable(defaultModel);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(myTable);
			occurances.add(scrollPane);
			frame.add(occurances);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(new JLabel());
			JButton display = new JButton("Display words");
			display.addActionListener(displayListener());
			display.setPreferredSize(new Dimension(140, 30));
			buttonPanel.add(display);
			frame.add(buttonPanel);
					
			frame.setVisible(true);
	 }
	 
	 private static ActionListener displayListener(){ //Gets clicked and data is sent
		 return new ActionListener() {
			 @Override
				public void actionPerformed(ActionEvent e) {
				 freq.clear(); //Will add together each time button is clicked otherwise
				 Path path = Paths.get("C:\\Users\\kevin\\eclipse-workspace\\keatingSDLCProject\\theRaven.txt"); // Gets file
			        try {
			        	
			            String text = Files.readString(path); // throws java.io.IOException
			            text = text.toLowerCase();
			            Pattern pttrn = Pattern.compile("[a-z]+"); // Sets the word to look for
			            Matcher mtchr = pttrn.matcher(text); // Flags each time a match of String word is found
			            
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
		 };
	 }

	public static void main(String[] args) throws FileNotFoundException {
		
		new keatingSDLCAssignment().init();
	}

}
