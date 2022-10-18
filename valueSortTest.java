package keatingSDLCProject;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import junit.framework.Assert;

public class valueSortTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		keatingSDLCAssignment test = new keatingSDLCAssignment();
		TreeMap<String, Integer> map = new TreeMap<>(); //Values are out of order for later sorting
		map.put("Two", 2);
		map.put("One", 1);
		map.put("Four", 4);
		map.put("Three", 3);
		
		TreeMap<String, Integer> sortedMap = new TreeMap<>();
		sortedMap.put("One", 1);
		sortedMap.put("Two", 2);
		sortedMap.put("Three", 3);
		sortedMap.put("Four", 4);
		
		Map<String, Integer> actual = test.valueSort(map);
		
		Assert.assertEquals(actual, sortedMap);
	}

}
