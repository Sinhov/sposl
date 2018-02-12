//Sinhov : "The girl told me, take off your jacket. I said, Babes, man's not hot, never hot."
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class macroPass2 {
	public static void main(String[] Args) throws IOException{
		BufferedReader b1 = new BufferedReader(new FileReader("intermediate.txt"));
		BufferedReader b2 = new BufferedReader(new FileReader("mnt.txt"));
		BufferedReader b3 = new BufferedReader(new FileReader("mdt.txt"));
		BufferedReader b4 = new BufferedReader(new FileReader("kpdt.txt"));
		FileWriter f1 = new FileWriter("Pass2.txt");
		HashMap<Integer,String> aptab=new HashMap<Integer,String>();
		HashMap<String,Integer> aptabInverse=new HashMap<String,Integer>();
		String s,s1,s2;
		int i,pp,kp;
		while((s=b1.readLine())!=null){
			s1=b2.readLine();
			String word[]=s1.split("\t");
			pp=Integer.parseInt(word[1]);
			kp=Integer.parseInt(word[2]);
			if(s.split("\\s").length==1){
				while((s2=b3.readLine())!=null&&s2.compareToIgnoreCase("MEND")!=0){
					f1.write("+ ");
					for(i=0;i<s2.length();i++)
						f1.write(s2.charAt(i));
				}
				continue;
			}
			String actualParams[]=s.split("\\s")[1].split(",");
			i=1;
			for(int j=0;j<pp;j++){
				aptab.put(i, actualParams[i-1]);
				aptabInverse.put(actualParams[i-1],i);
				i++;
			}
			for(int j=0;j<kp&&(s2=b4.readLine())!=null;j++){
				String kpdt[]=s2.split("\t");
				aptab.put(i,kpdt[1]);
				aptabInverse.put(kpdt[0],i);
				i++;
			}
			i=pp+1;
			while(i<=actualParams.length){
				String initializedParams[]=actualParams[i-1].split("=");
				aptab.put(aptabInverse.get(initializedParams[0].substring(1,initializedParams[0].length())),initializedParams[1].substring(1,initializedParams[1].length()));
				i++;
			}
			while((s2=b3.readLine())!=null&&s2.compareToIgnoreCase("MEND")!=0){
				f1.write("+ ");
				for(i=0;i<s2.length();i++){
					if(s2.charAt(i)=='#')
						f1.write(aptab.get(Integer.parseInt(String.valueOf(s2.charAt(++i)))));
					else
						f1.write(s2.charAt(i));
				}
				f1.write("\n");
			}
			aptab.clear();
			aptabInverse.clear();
		}
		b1.close();
		b2.close();
		b3.close();
		b4.close();
		f1.close();
	}
}
