package org.badfish.theworldshop.language;


import java.util.LinkedHashMap;

/**
 * @author BadFish
 */
public class LanguageManager {

    private static LinkedHashMap<String,BaseLanguage> LANGUAGE = new LinkedHashMap<>();

    public static void register(String lang,BaseLanguage language){
        LANGUAGE.put(lang, language);
    }

    public static BaseLanguage getLanguage(String lang){
        return LANGUAGE.get(lang);
    }



}
