package atos.net.pocLPI.utils;

import java.util.HashMap;
import java.util.List;

/**
 * Singleton pattern sur Enum
 * Usage: FSMParametersSingleton singleton = FSMParametersSingleton.INSTANCE;
 * 
 * @author sspieser
 *
 */
public enum FSMParametersSingleton {
	INSTANCE;
	HashMap<String, List<String[]>> states = new HashMap<String, List<String[]>>();
	
	/**
	 * Retourne les data de parametres pour un type de BP donne
	 * Charge le fichier complet si ce n'est pas deja fait, 
	 * HashMap: clé = le type de BP, valeur = tableau des colonnes de la structure de données de parametrage
	 * 
	 * @param bpType
	 * @return
	 */
    public List<String[]> getStates(String bpType) {
    	if (states.isEmpty()) {
    		states = FSMUtils.getAllStates();
    	}
        return states.get(bpType);
    }
}
