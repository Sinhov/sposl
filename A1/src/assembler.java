//Sinhov : "The girl told me, take off your jacket. I said, Babes, man's not hot, never hot."
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class assembler {
    public static int checker(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ','&&s.charAt(i+1)=='=')
                return 4;
            else if(s.charAt(i) == ','&&s.charAt(i+1)!='=')
            	return 3;
        }
        if (s.charAt(0) == '\'')
            return 0;
        else if (s.charAt(0) >= 48 && s.charAt(0) <= 58)
            return 1;
        else
            return 2;
    }

    public static void main(String[] Args) throws IOException {
        BufferedReader b1 = new BufferedReader(new FileReader("input.txt"));
        BufferedReader b2 = new BufferedReader(new FileReader("optab.txt"));
        FileWriter f3 = new FileWriter("symtab.txt");
        FileWriter f4 = new FileWriter("intermediate.txt");
        FileWriter f5 = new FileWriter("littab.txt");
        FileWriter f6 = new FileWriter("pooltab.txt");
        HashMap<String, String> statementType = new HashMap<String, String>();
        HashMap<String, String> opcode = new HashMap<String, String>();
        HashMap<String, Integer> symNumber = new HashMap<String, Integer>();
        HashMap<Integer, String> symSymbol = new HashMap<Integer, String>();
        HashMap<String, Integer> symAddress = new HashMap<String, Integer>();
        HashMap<String, Integer> symLength = new HashMap<String, Integer>();
        HashMap<String, Integer> conditionCodes = new HashMap<String, Integer>();
        HashMap<Integer, String> litSymbol = new HashMap<Integer, String>();
        HashMap<String, Integer> litNumber = new HashMap<String, Integer>();
        conditionCodes.put("LT", 1);
        conditionCodes.put("LE", 2);
        conditionCodes.put("EQ", 3);
        conditionCodes.put("GT", 4);
        conditionCodes.put("GE", 5);
        conditionCodes.put("ANY", 6);
        String s;
        int symtabPointer = 1, addr = 0, offset,littabPointer=1,pooltabPointer=1;
        while ((s = b2.readLine()) != null) {
            String[] word = s.split("\\s");
            statementType.put(word[0], word[1]);
            opcode.put(word[0], word[2]);
        }
        while ((s = b1.readLine()) != null) {
            String[] word = s.split("\\s");
            if(word[0].compareToIgnoreCase("LTORG")==0){
            	for(int i=pooltabPointer,j=0;j<litSymbol.size();i++,j++){
            		f4.write("(DL,01)(C,"+Integer.parseInt(litSymbol.get(i))+")\n");
            		f5.write(litSymbol.get(i)+"\t\t"+ addr++ +"\n");
            	}
            	f6.write(""+pooltabPointer+"\n");
            	pooltabPointer=littabPointer;
            	litNumber.clear();
            	litSymbol.clear();
            	continue;
            }
            if(word[0].compareToIgnoreCase("END")==0&&word.length==1){
            	f4.write("(" + statementType.get(word[0]) + "," + opcode.get(word[0]) + ")\n");
            	continue;
            }
            if (statementType.containsKey(word[0])) {
                offset = 0;
            } else {
                if (word[1].compareToIgnoreCase("DS") == 0)
                    symLength.put(word[0], Integer.parseInt(word[2]));
                else
                    symLength.put(word[0], 1);
                if (!symNumber.containsKey(word[0])) {
                    symNumber.put(word[0], symtabPointer);
                    symSymbol.put(symtabPointer, word[0]);
                    symtabPointer++;
                }
                symAddress.put(word[0], addr);
                offset = 1;
            }
            if(word[0 + offset].compareToIgnoreCase("EQU") != 0)
            	f4.write("(" + statementType.get(word[0 + offset]) + "," + opcode.get(word[0 + offset]) + ")");
            if (statementType.get(word[0 + offset]).compareToIgnoreCase("AD") == 0) {
                if (word[0 + offset].compareToIgnoreCase("START") == 0)
                    addr = Integer.parseInt(word[1 + offset]);
            } else {
                if (word[0 + offset].compareToIgnoreCase("DS") == 0)
                    addr += Integer.parseInt(word[1 + offset]);
                else
                    addr++;
            }
            if (word[0 + offset].compareToIgnoreCase("STOP") != 0) {
                int checkerVal = checker(word[1 + offset]);
                if (checkerVal == 0) {
                    f4.write("(C," + Integer.parseInt(word[1 + offset].substring(1, word[1 + offset].length() - 1)) + ")\n");
                } else if (checkerVal == 1) {
                    f4.write("(C," + Integer.parseInt(word[1 + offset]) + ")\n");
                } else if (checkerVal == 2) {
                    if (word[0 + offset].compareToIgnoreCase("EQU") == 0 || word[0 + offset].compareToIgnoreCase("ORIGIN") == 0) {
                        String s1 = "";
                        int i = 0, val, op2 = 0;
                        char sign = ' ';
                        while (i < word[1 + offset].length() && (Character.isAlphabetic(word[1 + offset].charAt(i)) || Character.isDigit(word[1 + offset].charAt(i)))) {
                            s1 += word[1 + offset].charAt(i);
                            i++;
                        }
                        if (i != word[1 + offset].length()) {
                            op2 = Integer.parseInt(word[1 + offset].substring(s1.length() + 1, word[1 + offset].length()));
                            if (word[1 + offset].charAt(s1.length()) == '+') {
                                val = symAddress.get(s1) + op2;
                                sign = '+';
                            } else {
                                val = symAddress.get(s1) - op2;
                                sign = '-';
                            }
                        } else {
                            val = symAddress.get(s1);
                        }
                        if (word[0 + offset].compareToIgnoreCase("EQU") == 0)
                            symAddress.put(word[0], val);
                        else
                            addr = val;
                        if (i != word[1 + offset].length()&&word[0 + offset].compareToIgnoreCase("EQU") != 0) {
                            f4.write("(S," + symNumber.get(s1) + ")" + sign + op2 + "\n");
                        } else if(word[0 + offset].compareToIgnoreCase("EQU") != 0) {
                            f4.write("(S," + symNumber.get(s1) + ")\n");
                        }
                    } else {
                        if (symNumber.containsKey(word[1 + offset])) {
                            f4.write("(S," + symNumber.get(word[1 + offset]) + ")\n");
                        } else {
                            f4.write("(S," + symNumber.get(word[1 + offset]) + ")\n");
                            symLength.put(word[1 + offset], 1);
                            symNumber.put(word[1 + offset], symtabPointer);
                            symSymbol.put(symtabPointer, word[1 + offset]);
                            symtabPointer++;
                        }
                    }
                } else if(checkerVal==3) {
                    String symbols[] = word[1 + offset].split(",");
                    if (word[0 + offset].compareToIgnoreCase("BC") == 0) {
                        if (symNumber.containsKey(symbols[1])) {
                            f4.write("(" + conditionCodes.get(symbols[0]) + ")(S," + symNumber.get(symbols[1]) + ")\n");
                        } else {
                            f4.write("(" + conditionCodes.get(symbols[0]) + ")(S," + symtabPointer + ")\n");
                            symLength.put(symbols[1], 1);
                            symNumber.put(symbols[1], symtabPointer);
                            symSymbol.put(symtabPointer, symbols[1]);
                            symtabPointer++;
                        }
                    } else {
                        if (symNumber.containsKey(symbols[1])) {
                            f4.write("(" + ((int) (word[1 + offset].charAt(0)) - 64) + ")(S," + symNumber.get(symbols[1]) + ")\n");
                        } else {
                            f4.write("(" + ((int) (word[1 + offset].charAt(0)) - 64) + ")(S," + symtabPointer + ")\n");
                            symLength.put(symbols[1], 1);
                            symNumber.put(symbols[1], symtabPointer);
                            symSymbol.put(symtabPointer, symbols[1]);
                            symtabPointer++;
                        }
                    }
                }
                else{
                	String symbols[] = word[1 + offset].split(",");
                	symbols[1]=symbols[1].substring(2,symbols[1].length()-1);
                    if (word[0 + offset].compareToIgnoreCase("BC") == 0) {
                        if (litNumber.containsKey(symbols[1])) {
                            f4.write("(" + conditionCodes.get(symbols[0]) + ")(L," + litNumber.get(symbols[1]) + ")\n");
                        } else {
                            f4.write("(" + conditionCodes.get(symbols[0]) + ")(L," + littabPointer + ")\n");
                            litNumber.put(symbols[1], littabPointer);
                            litSymbol.put(littabPointer, symbols[1]);
                            littabPointer++;
                        }
                    } else {
                        if (litNumber.containsKey(symbols[1])) {
                            f4.write("(" + ((int) (word[1 + offset].charAt(0)) - 64) + ")(L," +litNumber.get(symbols[1]) + ")\n");
                        } else {
                            f4.write("(" + ((int) (word[1 + offset].charAt(0)) - 64) + ")(L," + littabPointer + ")\n");
                            litNumber.put(symbols[1], littabPointer);
                            litSymbol.put(littabPointer, symbols[1]);
                            littabPointer++;
                        }
                    }
                }
            } else {
                f4.write("\n");
            }
        }
        for (int i = 1; i < symtabPointer; i++)
            f3.write(symSymbol.get(i) + "\t\t\t" + symAddress.get(symSymbol.get(i)) + "\t\t\t" + symLength.get(symSymbol.get(i)) + "\n");
        if(litNumber.size()!=0){
        	for(int i=pooltabPointer,j=0;j<litSymbol.size();i++,j++){
        		f4.write("(DL,01)(C,"+Integer.parseInt(litSymbol.get(i))+")\n");
        		f5.write(litSymbol.get(i)+"\t\t"+ addr++ +"\n");
        	}
        	f6.write(""+pooltabPointer+"\n");
        	litNumber.clear();
        	litSymbol.clear();
        }
        f3.close();
        f4.close();
        f5.close();
        f6.close();
        b1.close();
        b2.close();
    }
}
