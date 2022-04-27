package com.bookstore.utility;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class USConstants {
	
	public final static String US = "US";
	
	public final static Map<String, String> mapOfUSStates = new HashMap<String, String>() {{
			put("AL", "Alabama");
			put("AK", "Alaska");
			put("AZ", "Arizona");
	}};
	
	public final static List<String> listOfUSStatesCode = new ArrayList<>(mapOfUSStates.keySet());
	public final static List<String> listOfUSStatesName= new ArrayList<>(mapOfUSStates.values());
}
