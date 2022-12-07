/* Lexer for Trees Language 
 * Zoe Boyle
 * CSC 4330
 * 11/19/2022
 */
import java.util.*;
import java.io.*;

public class lexer {
   /*----------------------Global variables----------------------*/
   // Full lexeme to be tokenized
   static String lexeme;
    
   // Map for tokens and token values
   static HashMap<String, Integer> tokenMap = new HashMap<String,Integer>();
   
   // List of tokens and list of keywords in the code
   static ArrayList<Integer> tokenList = new ArrayList<Integer>();
   static ArrayList<String> keywordList = new ArrayList<String>();
   
   // Code file to parse through and scanner
   static File codeFile1 = new File("C:\\Users\\kowah\\Desktop\\test1.txt");
   static File codeFile2 = new File("C:\\Users\\kowah\\Desktop\\test2.txt");
   
   /*------------------------Main driver------------------------*/
   public static void main(String [] args) throws FileNotFoundException {
      // Opening code file
      tokenMapInit(tokenMap);
      // Error if file is not found
      if(!codeFile1.exists()) {
         System.err.println("ERROR: No file found");
		   System.exit(1);
      } else {
         Scanner sc = new Scanner(codeFile1);
         while(sc.hasNext()) {
            lexeme = sc.next();
            lookup(lexeme);
         }     
      }
	   System.out.println(tokenList.toString());
      treeRDA.parse(tokenList,keywordList);
      
      tokenList.clear();
      keywordList.clear();
        
      if(!codeFile2.exists()) {
         System.err.println("ERROR: No file found");
		   System.exit(1);
      } else {
         Scanner sc = new Scanner(codeFile2);
         while(sc.hasNext()) {
            lexeme = sc.next();
            lookup(lexeme);
         }     
      }
	   System.out.println(tokenList.toString());
      treeRDA.parse(tokenList,keywordList); 
   }
   
   /*-------------Method to find lexeme in token map-------------*/
   public static void lookup(String lexeme) {
      if(tokenMap.containsKey(lexeme)) {
		   keywordList.add(lexeme);
         tokenList.add(tokenMap.get(lexeme));
      // Error for a long lexeme
      } else if(lexeme.length() >= 98) {
         System.err.println("ERROR: " + lexeme + " is too long to be a valid token");
		   System.exit(1);
      // Check if keyword is a valid identifier
      } else if(lexeme.toLowerCase().matches("([a-z]_?){6,8}")) {
		   keywordList.add(lexeme.toLowerCase());
		   lexeme = "id";
		   tokenList.add(tokenMap.get(lexeme));
     // Check if keyword is a number
	  } else if(lexeme.matches("[0-9]+")) {
         keywordList.add(lexeme);
		   lexeme = "digit";
         tokenList.add(tokenMap.get(lexeme));
      // error for invalid symbol  
      } else {
		   System.err.println("ERROR: " + lexeme + " is not a valid symbol");
		   System.exit(1);
      }
   }
      
   /*----------------Method to populate token map----------------*/
   public static void tokenMapInit(HashMap<String,Integer> tokenMap) {
     
      // Program start word
      tokenMap.put("tree{",0);
         
      // Integer types
      tokenMap.put("twig",1);      // int
      tokenMap.put("stick",2);     // long
      tokenMap.put("trunk",3);     // short
      tokenMap.put("seed",4);      // byte
      tokenMap.put("digit",5);     // real number value
        
      // selection statements and loops
      tokenMap.put("apple(",11);   // if statement
      tokenMap.put("orange",12);   // else statement
      tokenMap.put("eat",13);      // end of if-else
      tokenMap.put("climb(",14);   // switch case statement
      tokenMap.put("branch",15);   // case
      tokenMap.put("growfor(",16); // for loop
      tokenMap.put("grow(",17);    // while loop
      tokenMap.put("stop",18);     // end loop
	   tokenMap.put("root",19);	   // start do while
      
      // Boolean operations
      tokenMap.put("<",21);        // less than
      tokenMap.put("<=",22);       // less than or equal to
      tokenMap.put(">",23);        // greater than
      tokenMap.put(">=",24);       // greater than or equal to
      tokenMap.put("==",25);       // equal to
      tokenMap.put("!=",26);       // not equal to
     
      // Other symbols
      tokenMap.put("}",30);        // right curly brace
      tokenMap.put("(",31);        // left parenthesis
      tokenMap.put(")",32);        // right parenthesis
      tokenMap.put("=",33);        // assignment operator
      tokenMap.put("leaf",34);     // Line delimiter
      tokenMap.put("id",35);       // identifiers
      tokenMap.put(":",36);        // colon
	   tokenMap.put(",",37);		   // comma
     
      // Arithmetic operations
      tokenMap.put("+",41);        // Addition
      tokenMap.put("-",42);        // Subtraction
      tokenMap.put("*",43);        // Multiplication
      tokenMap.put("/",44);        // Division
      tokenMap.put("%",45);        // Modulo
   }
}