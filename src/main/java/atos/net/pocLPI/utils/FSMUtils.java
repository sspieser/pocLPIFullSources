package atos.net.pocLPI.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.statefulj.fsm.model.Action;

import atos.net.pocLPI.StateManager;

public class FSMUtils {

	/**
	 * Retourne la liste des parametres de la machine a etat
	 * 
	 * @return
	 */
	public static HashMap<String, List<String[]>> getAllStates() {
		HashMap<String, List<String[]>> states = new HashMap<String, List<String[]>>();
		String csvFile = "states.csv";
        String line = "";
        String cvsSplitBy = ";";
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {

                String[] fields = line.split(cvsSplitBy);
                String type = fields[0];
                
                if (states.get(type) == null || states.get(type) != null && states.get(type).isEmpty()) {
                	ArrayList<String[]> list = new ArrayList<String[]>();
                	list.add(fields);
                	states.put(type,  list);
                } else {
                	ArrayList<String[]>listLast = (ArrayList<String[]>) states.get(type);
                	listLast.add(fields);
                	states.put(type,  listLast);
                }                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		return states;
	}
	
	@SuppressWarnings("unchecked")
	public static Action<StateManager> loadActionClass(String classNm, String param1) {
		Action<StateManager> a = null;
		try {
			Class<?> cl = Class.forName(classNm);
			Constructor<?> con = cl.getConstructor(String.class);
			Object xyz = con.newInstance(param1);
			a = (Action<StateManager>) xyz;
		} catch (ClassNotFoundException | NoSuchMethodException | 
				InvocationTargetException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return a;
	}
}
