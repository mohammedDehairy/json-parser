/*
 * This source file was generated by the Gradle 'init' task
 */
package testapp;

import java.io.IOException;
import java.io.InputStream;

import json.Exceptions.JSONMappingException;
import json.Exceptions.JSONParsingException;
import json.parsing.JSONParser;

public class App {

    public static void main(String[] args) throws IOException, JSONParsingException, JSONMappingException {
        
        try(InputStream inputStream = App.class.getClassLoader().getResourceAsStream("json-example.json")) {
            Example outPut = new JSONParser().parse(Example.class, inputStream);
    
            System.out.println(outPut.toString()); 
        }
    }
}
