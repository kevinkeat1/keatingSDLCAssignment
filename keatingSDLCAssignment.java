package keatingSDLCProject;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map.*;
import java.util.logging.Logger;

/**
 * 
 * @author Kevin
 *
 */
public class keatingSDLCAssignment {
	
	static TreeMap<String, Integer> freq = new TreeMap<>();
	private static DefaultListModel<String> textFeild;
	static JTable myTable = new JTable(); 
	private static DefaultTableModel defaultModel;
	private static List<String> data = null;
	private static final String fileName = "theRaven.txt";
	
	/**
	 * This method sorts the Map
	 * @param <K>
	 * K is each word of the Map
	 * @param <V>
	 * V is the number of times each string of K
	 * occurs
	 * @param map
	 * Map that will be displayed
	 * @return
	 * Returns a sorted Map that will be displayed
	 */
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
	 
	 /**
	  * Constructs the gui and is where data is displayed
	  */
	 public void init() {
		 JFrame frame = new JFrame(); //Initial panel, will have button that when clicked will display values
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(500, 600);
			frame.setTitle("Word Occurance");
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
			
			JPanel occurances = new JPanel();
			
			String[] columns = new String[] {"Word", "Number"}; //So I can put treemap into the UI
			defaultModel = new DefaultTableModel(columns, 0);
			
			
			
			myTable = new JTable(defaultModel); //Will be where the data goes
			JScrollPane scrollPane = new JScrollPane(); //User can scroll through the list
			scrollPane.setViewportView(myTable);
			occurances.add(scrollPane);
			frame.add(occurances);
			
			JPanel buttonPanel = new JPanel(); //Click this and data will fill into table
			buttonPanel.add(new JLabel());
			JButton display = new JButton("Display words");
			display.addActionListener(displayData());
			display.setPreferredSize(new Dimension(140, 30));
			buttonPanel.add(display);
			
			JButton transmit = new JButton("Send data");
			transmit.addActionListener(transmitData());
			transmit.setPreferredSize(new Dimension(140, 30));
			buttonPanel.add(transmit);
			frame.add(buttonPanel);
					
			frame.setVisible(true);
	 }
	 
	 private static ActionListener displayData(){ //Gets clicked and data is sent
		 return new ActionListener() {
			 @Override
				public void actionPerformed(ActionEvent e) {
				 int rowCount = defaultModel.getRowCount();
				 System.out.println(rowCount);
				 for (int i = rowCount - 1; i >= 0; i--) {
					    defaultModel.removeRow(i);
					}
				 freq.clear(); //Will add together each time button is clicked otherwise
				 
				 Path path = Paths.get(fileName); // Gets file
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
			        	   Set<Entry<String, Integer>> entries = sortedMap.entrySet();
			   			
			   			for(Entry<String, Integer> entry:entries) {
			   				defaultModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
			   			}
			                Map.Entry mp = (Map.Entry)i.next(); //Will run out of memory otherwise
			            }
			        }
			        catch (IOException xIo) {
			            xIo.printStackTrace();
			        }
			 }
		 };
	 }
	 
	 private static ActionListener transmitData() { //Sends data to mySQL database, added for a little bit of flaire
		 return new ActionListener() {
			 @Override
				public void actionPerformed(ActionEvent e) {
				 try {
					getConnection();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }
		 };
	 }
	
	 /**
	  * 
	  * @param args
	  * For main method
	 * @throws Exception 
	  */
	public static void main(String[] args) throws Exception {
		/**
		 * This is the start of the main method
		 * 
		 * code execution
		 */
		new keatingSDLCAssignment().init();
	}
	
	public static Connection getConnection() throws Exception{
		class DBConnection {

			final private String url = "jdbc:mysql://localhost/";
			final private String databaseName = "wordOccurances";
			final private String user = "root";
			final private String password = "cop2805";

			public Connection Connect() {
			    Connection c = null;
			    try {
			        c = DriverManager.getConnection(url + databaseName, user, password);
			    } catch (SQLException ex) {
			        
			    }
			    return c;
			}}
		
		Connection con = new DBConnection().Connect();
		String query = "INSERT INTO word (word) VALUES (?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(query);
			List<String> array = new ArrayList<String>();
			Scanner scnr = new Scanner(new FileReader(fileName));
			String str;
			
			while(scnr.hasNext()) {
				str = scnr.next();
				String noPunct = str.toLowerCase().replaceAll("[^\\s\\w]", "");
				array.add(noPunct);
			}
			
			String[] data = array.toArray(new String[0]);
			
			for(String words:data) {
				stmt.setString(1, words);
				stmt.execute();
			}
			
			stmt.close();
	        con.close();
			
			System.out.println("Connected");
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return con;
		
	}
}
