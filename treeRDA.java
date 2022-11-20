/* RDA for trees language
 * Zoe Boyle
 * CSC 4330
 * 11/18/2022
 */
import java.util.*;

public class treeRDA {

   /* Global Variables-----------------------------------------------------------
    * List of tokens and list of keywords in the code
    * Index of both arrays during parsing
    * List of delcared variables
    */
   static ArrayList<Integer> tokenList = new ArrayList<Integer>();
   static ArrayList<String> keywordList = new ArrayList<String>();

   static int index = 0;

   static HashMap<String, String> varList = new HashMap<String, String>();

   /* Main driver and helper methods---------------------------------------------
    * parse - Parsing method takes in both lists and begins syntax analysis
    * nextToken - Moves to the next token
    * typeCheck - confirms that the value assigned to a variable matches the type
    */
   public static void parse(ArrayList<Integer> token, ArrayList<String> keyword) {
      // Populate tokenList keywordList
      tokenList = token;
      keywordList = keyword;      
      // If the program does not start with tree{, it is not the start for this language
      if(tokenList.get(index) != 0) {
         System.err.println("ERROR: invalid start of a program");
         System.exit(1);
      } else {
         tree();
         index = 0;
      }
   }
   
   public static void nextToken() {
      index++;
      if(index >= tokenList.size()) {
         System.err.println("ERROR: expected '}' to end program");
         System.exit(1);
      }
   }
   
   public static void typeCheck(String variable, int value) {
      // Convert the string to int
      String type = "";
      // Find variable and its type
      if(varList.containsKey(variable)) {
         type = varList.get(variable);
      // Throw error if variable is not found
      } else {
         System.err.println("ERROR: variable undeclared");
         System.exit(1);
      }
      // Throw error if number is too large for indicated type
      if((Integer.valueOf(value).byteValue() > 1 && type == "seed") ||
         (Integer.valueOf(value).byteValue() > 2 && type == "twig") ||
         (Integer.valueOf(value).byteValue() > 4 && type == "stick") ||
         (Integer.valueOf(value).byteValue() > 8 && type == "trunk")) {
         System.err.println("ERROR: type mismatch");
         System.exit(1);
      } else {
         return;
      }
   }
   
   /* Indicate start of a trees program------------------------------------------
    * <program> --> tree{ <stmt> }
    */
   public static void tree(){
      // Call statement until the end of the program
      while(index < tokenList.size()-1) {
         statement();
      }
      // If the program does not end with '}' throw error
      if(tokenList.get(index) != 30) {
         System.err.println("ERROR: expected '}'to end program");
         System.exit(1);
      } 
   }

   /* A trees program statement--------------------------------------------------
    * <stmt> --> <forloop><stmt>|<whileloop><stmt>|<var> leaf <stmt>|<dowhile><stmt>|
    *			     <ifstmt><stmt>|<switch><stmt>|<assertion> leaf <stmt>|'null'
    */
   public static void statement() {
      // Error if the program has ended but is still in statement state
      if(index >= tokenList.size()) {
         System.err.println("ERROR: expected '}' to end program");
         System.exit(1);
      }
      nextToken();  
      // Switch case for statements
      switch(tokenList.get(index)) {
         case 1:        // Start of <var>
         case 2:
         case 3:
         case 4:
            var();
            // Throw error if there is no leaf line delimiter
            if(tokenList.get(index) != 34) {
               System.err.println("ERROR: expected 'leaf' delimiter");
               System.exit(1);
            }
            break;
         case 11:       // Start of <ifstmt>
            apple();
            break;
         case 14:       // Start of <switch> 
            climb();
            break;
         case 15:       // Null for case with no arguments
         case 18:
            break;
         case 16:       // start of <forloop>
            growfor();
            break;
         case 17:       // Start of <whileloop> 
            grow();
            break;
         case 19:       // Start of <dowhile>
            rootgrow();
            break;
         case 35:       // Start of <assertion>
            assertion();
            // Throw error if there is no leaf line delimiter
            if(tokenList.get(index) != 34) {
               System.err.println("ERROR: expected 'leaf' delimiter");
               System.exit(1);
            }
            break;
         // Null, or an error if the next token is not a start to a statement while the program is still running.
         default:
            if(index < tokenList.size()-1) {
               System.err.println("ERROR: invalid start to a statement");
               System.exit(1);
            }
            break;
      }
   }

   /* For loop- growfor----------------------------------------------------------
    *	<forloop> --> growfor(<var>,<boolexpr>,<expr>) <stmt> stop|
    *               growfor(<assertion>,<boolexpr>,<expr>) <stmt> stop
    */
   public static void growfor() {
      nextToken();
      // Initiate loop variable
      if(tokenList.get(index) >= 1 && tokenList.get(index) <= 4) {
         var();
      // If variable is already delcared, assign it
      } else if(tokenList.get(index) == 35) {
         assertion();
      // Throw error for no loop variable
      } else {
         System.err.println("ERROR: growfor: need to declare loop variable");
         System.exit(1);
      }
      // If there is no comma delimiter throw error
      if(tokenList.get(index) != 37) {
         System.err.println("ERROR: growfor: expected ','");
         System.exit(1);
      } else {
         nextToken();
      }
      // Throw error if next token is not an identifier
      boolOp();
      // If there is no comma delimiter throw error
      if(tokenList.get(index) != 37) {
         System.err.println("ERROR: growfor: expected ','");
         System.exit(1);
      } else {
         nextToken();
      }
      // Start an expression, if there is no expression throw an error
      if(tokenList.get(index) == 35) {
         assertion();
      } else {
         System.err.println("ERROR: growfor: expression required");
         System.exit(1);
      }
      // If there is no parenthesis delimiter throw error
      if(tokenList.get(index) != 32) {
         System.err.println("ERROR: growfor: expected ')'");
         System.exit(1);
      } else {
         //Otherwise, parce statements in loop body until 'end' 
         while(tokenList.get(index) != 18) {
            statement();
            // If loop has no 'stop' delimiter, throw error
            if(index >= tokenList.size()) {
               System.err.println("ERROR: growfor: expected 'stop'");
               System.exit(1);   
            }
         }
      }
   }

   /*	While loop- grow-----------------------------------------------------------
    *	<whileloop> --> grow(<boolexpr>) <stmt> stop
    */
   public static void grow() {
      nextToken();
      boolOp();
      // If there is no parenthesis delimiter throw error
      if(tokenList.get(index) != 32) {
         System.err.println("ERROR: grow: expected ')'");
         System.exit(1);
      } else {
         //Otherwise, parce statements in loop body until 'end' 
         while(tokenList.get(index) != 18) {
            statement(); 
            // If loop has no 'stop' delimiter, throw error
            if(index >= tokenList.size()) {
               System.err.println("ERROR: grow: expected 'stop'");
               System.exit(1);   
            } else {
               nextToken();
            }
         }
      }    
   }
   
   /* Do while - root grow-------------------------------------------------------
    * <dowhile> 	--> root <stmt> stop grow(<boolexpr>)
    */
   public static void rootgrow() {
      while(tokenList.get(index) != 18) {
         statement();
         // If loop has no 'stop' delimiter, throw error
         if(index >= tokenList.size()) {
            System.err.println("ERROR: rootgrow: expected 'stop'");
            System.exit(1);   
         } else {
            nextToken();
         }
      }
      nextToken();
      // Throw error if condition is missing
      if(tokenList.get(index) != 17) {
         System.err.println("ERROR: rootgrow: condition required for root grow");
         System.exit(1);
      } else {
         nextToken();
         boolOp();
      }
   }

   /*	Swich case- climb -----------------------------------------------------------
    * <switch>	--> climb(id) <case> stop
    */
   public static void climb() {
      nextToken();
      // Throw error if there is no identifier
      if(tokenList.get(index) != 35) {
         System.err.println("ERROR: climb: variable to evaluate is required for switch case");
         System.exit(1);
      } else {
         nextToken();
      }
      // Throw error if there is no closing parenthesis
      if(tokenList.get(index) != 32) {
         System.err.println("ERROR: climb: expected ')'");
         System.exit(1);
      } else {
         nextToken();
      }
      // Go to branch
      if(tokenList.get(index) == 15) {
         nextToken();
         branch();
      }
      // Throw error if no 'stop' delimiter
      if(tokenList.get(index) != 18) {
         System.err.println("ERROR: climb: expected 'stop'");
         System.exit(1);
      }
   }
 	
   /*	A case for the switch case-------------------------------------------------
    * <case> --> branch num: <stmt> <case>|'null'
    */
   public static void branch() {
      if(tokenList.get(index) != 5) {
         System.err.println("ERROR: branch: requires value to evaluate");
         System.exit(1);
      }
      nextToken();
      // Error if there is no colon
      if(tokenList.get(index) != 36) {
         System.err.println("ERROR: climb branch: expected ':'");
         System.exit(1);
      } else {
         statement();
         nextToken();
      }
      // Start a new branch statement
      if(tokenList.get(index) == 15) {
         nextToken();
         branch();
      } else {
         return;
      }
   }
 
   /*	If-else statement- apple-orange--------------------------------------------
    * <ifstmt>	--> apple(<boolexpr>) <stmt> orange <stmt> eat|
    *					 apple(<boolexpr>) <stmt> eat
    */
   public static void apple() {
      nextToken();
      boolOp();
      // Check for closing parenthesis
      if(tokenList.get(index) != 32) {
         System.err.println("ERROR: apple orange: expected ')'");
         System.exit(1);
      } else {
         statement();
      }
      nextToken();
      // Check for an eat or an orange
      if(tokenList.get(index) == 12) {
         statement();
         nextToken();
         // Check if an eat is found after the orange
         if (tokenList.get(index) == 13) {
            return;
         } else {
            System.err.println("ERROR: apple orange: expected 'eat'");
            System.exit(1);
         }
      } else if(tokenList.get(index) == 13) {
         return;
      } else {
         System.err.println("ERROR: apple orange: expected 'eat'");
         System.exit(1);
      }
   }

   /*	Variable declaration-------------------------------------------------------			
    * <var> --> <type> id|<type> id = <expr>				 
    * <type> --> stick|twig|trunk
    */
   public static void var() {
      String intType = "";
      switch(tokenList.get(index)) {
         case 1:          // Twig
            intType = "twig";
            break;
         case 2:          // Stick
            intType = "stick";
            break;
         case 3:          // Trunk
            intType = "trunk";
            break;
         case 4:          // Seed
            intType = "seed";
            break;
         default:
            // Throw error if no type declaration
            System.err.println("ERROR: variable: expected type");
            System.exit(1);
            break;
      }
      nextToken();
      // Throw error for invalid identifier
      if(tokenList.get(index) != 35) {
         System.err.println("ERROR: variable: expected identifier");
         System.exit(1);
      // Check if variable has been declared
      } else if(varList.containsKey(tokenList.get(index))) {
         System.err.println("ERROR: variable: variable already declared");
         System.exit(1);
      }
      // Check for assignment operator
      if(tokenList.get(index+1) == 33) {
         varList.put(keywordList.get(index), intType);
         nextToken();
         nextToken();
      } else {
         // Indicate variable has been declared and the type      
         varList.put(keywordList.get(index), intType);
         nextToken();
         return;
      }
      // Check for expression
      if(tokenList.get(index) == 5 || tokenList.get(index) == 35 || tokenList.get(index) == 31) {
         expression();
      } else {
         System.err.println("ERROR: variable: invalid initial value");
         System.exit(1);
      }
   }

   /* Assertion------------------------------------------------------------------
    * <assertion> --> id = <expr>
    */
   public static void assertion() {
      nextToken();
      // Check for assignment operator
      if(tokenList.get(index) != 33) {
         System.err.println("ERROR: expected");
         System.exit(1);
      } else {
         nextToken();
      }
      // Check for an expression
      if(tokenList.get(index) == 5 || tokenList.get(index) == 35 || tokenList.get(index) == 31) {
         expression();
      } else {
         System.err.println("ERROR: assertion: not an expression");
         System.exit(1);
      }
   }

   /* Expressions----------------------------------------------------------------
    * <expr> --> <term> + <expr>|<term> - <expr>|<term> * <term>|
    *  			  <term> / <expr>|<term> % <expr>|(<expr>)|<term>
    *	<term> --> id|num
    */
	public static void expression() {
      // Check for parenthesis
      if(tokenList.get(index) == 31) {
         nextToken();
         expression();
         // Throw error if no closing parenthesis
         if(tokenList.get(index) != 32) {
            System.err.println("ERROR: expression: expected ')'");
            System.exit(1);
         }
         nextToken();
      } else if(tokenList.get(index) == 5 || tokenList.get(index) == 35) {
         // Check for division, multiplication, or modulo
         if(tokenList.get(index+1) >= 41 && tokenList.get(index+1) <= 45) {
            nextToken();
            nextToken();
            // If there is another number or variable, or an opening parenthesis, call a new expression
            if(tokenList.get(index) == 31 || tokenList.get(index) == 5 || tokenList.get(index) == 35) {
               expression();
            } else {
               System.err.println("ERROR: expression: requires variable, integer literal, or parenthesis after operation");
               System.exit(1);
            }
         // If there is no mathematical operation end expression
         } else {
            nextToken();
            return;
         }
      } else {
         System.err.println("ERROR: expression: invalid expression");
         System.exit(1);
      } 
   }

   /*	Boolean operation----------------------------------------------------------
    * <boolexpr> --> <expr> <op> <expr>
    * <op> --> >|>=|<|<=|==|!=
    */
   public static void boolOp() {
      // Check for starting expression
      if(tokenList.get(index) == 5 || tokenList.get(index) == 35 || tokenList.get(index) == 31) {
         expression();
      } else {
         System.err.println("ERROR: boolean: requires expression for comparison");
         System.exit(1);
      }
      // Check for relational operator
      if(tokenList.get(index) < 21 || tokenList.get(index) > 26) {
         System.err.println("ERROR: boolean: invalid relational operator");
         System.exit(1);
      } else {
         nextToken();
      }
      // Check for ending expression
      if(tokenList.get(index) == 5 || tokenList.get(index) == 35 || tokenList.get(index) == 31) {
         expression();
      } else {
         System.err.println("ERROR: boolean: requires expression for comparison");
         System.exit(1);
      }
   }
}