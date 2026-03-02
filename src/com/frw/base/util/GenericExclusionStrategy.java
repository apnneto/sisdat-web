package com.frw.base.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public   class   GenericExclusionStrategy   { 
	
	protected static List<Class<?>> classExclusions = new ArrayList<Class<?>>();
	protected static List<String> fieldExclusions = new ArrayList<String>();
	
	public Gson build(String[] fields, final Class<?>...cls) {
		for(String field : fields){
			fieldExclusions.add(field);
		}
		for(Class<?> c : cls){
			classExclusions.add(c);
		}
		
        GsonBuilder b = new GsonBuilder();
        b.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return classExclusions == null ? false : classExclusions.contains(clazz);
            }

            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return fieldExclusions == null ? false : fieldExclusions.contains(f.getName());
            }
        });
        return b.create();
    }	
	
/* Antigo
 *  
 * private   final   List <String>   _skipFields   =   new   ArrayList <String> ( ) ; 
    private   final   Class <? >   _clazz ;

    *//** 
     * Create a new exclusion strategy for gson. 
     *  
     * @param clazz 
     *            The class (transfer object) on which this exclusion will work 
     * @param fields 
     *            The field names which should not be serialized 
     *//* 
    public   GenericExclusionStrategy ( Class <?>  clazz ,String...fields)   { 
        _clazz   =   clazz ; 
        for   ( String   field   :   fields )   { 
            _skipFields . add ( field ) ; 
        } 
    } 
    
	@Override 
    public   boolean   shouldSkipClass ( Class < ? >   clazz )   { 
        return   false ; 
    } 

    @Override 
    public   boolean   shouldSkipField ( FieldAttributes   f )   { 
        return   f . getDeclaringClass ( )   ==   _clazz &&   _skipFields . contains ( f . getName ( ) ) ; 
    } */
}

