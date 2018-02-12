//Sinhov : "The girl told me, take off your jacket. I said, Babes, man's not hot, never hot."
import java.util.Scanner;

class pr{
	int arrivalTime,burstTime,id,waitingTime,turnaroundTime;
	boolean flag=false;
	String name;
}

public class c1 {
	static int ch,n;
	static pr obj[]=new pr[100];
	static String processName[]=new String[100];
	public static void processTypeInput(){
		Scanner sc=new Scanner(System.in);
		System.out.println("Which scheduling algo to use?\n1.FCFS\n2.SJF(Non preemptive)\n3.SJF(Preemptive)\n4.Round Robin");
		ch=sc.nextInt();
	}
	public static void processInput(){
		Scanner sc=new Scanner(System.in);
		System.out.print("Enter no of processes:");
		n=sc.nextInt();
		for(int i=0;i<n;i++){
			System.out.print("Enter processes arrival time, burst time, name");
			obj[i]=new pr();
			obj[i].arrivalTime=sc.nextInt();
			obj[i].burstTime=sc.nextInt();
			obj[i].name=sc.next();
			obj[i].id=i;
		}
	}
	public static void prsort(){
		//Arrays.sort(obj);
		for(int i=0;i<n;i++){
			for(int j=1;j<n;j++){
				if(obj[j-1].arrivalTime>obj[j].arrivalTime){
					pr temp=new pr();
					temp=obj[j-1];
					obj[j-1]=obj[j];
					obj[j]=temp;
				}
			}
		}
	}
	public static void display(){
		for(int i=0;i<n;i++){
			System.out.println("Process name: "+obj[i].name+"\t"+"Waiting Time: "+obj[i].waitingTime+"\t"+"Turnaround Time: "+obj[i].turnaroundTime);
		}
	}
	public static void fcfs(){
		prsort();
		int t=0;
		for(int i=0;i<n;i++){
			if(obj[i].arrivalTime<=t){
				obj[i].waitingTime=t-obj[i].arrivalTime;
				t+=obj[i].burstTime;
			}
			else{
				obj[i].waitingTime=0;
				t+=obj[i].arrivalTime-t+obj[i].burstTime;
			}
			obj[i].turnaroundTime=obj[i].waitingTime+obj[i].burstTime;
		}
	}
	public static int sjfnp_findmin(int t){
		int minval=10000000,val=-1;
		for(int i=0;i<n;i++){
			if(obj[i].burstTime<minval&&obj[i].arrivalTime<=t&&obj[i].flag==false){
				minval=obj[i].burstTime;
				val=i;
			}
		}
		return val;
	}
	public static void sjfnp(){
		prsort();
		int totalBurstTime=0;
		for(int i=0;i<n;i++)
			totalBurstTime+=obj[i].burstTime;
		for(int i=0;i<=obj[n-1].arrivalTime+totalBurstTime;){
			int val=sjfnp_findmin(i);
			if(val==-1){
				i++;
				continue;
			}
			obj[val].waitingTime=i-obj[val].arrivalTime;
			i+=obj[val].burstTime;
			obj[val].turnaroundTime=obj[val].waitingTime+obj[val].burstTime;
			obj[val].flag=true;
		}
	}
	public static void sjfp(){
		prsort();
		pr obj1[]=new pr[100];
		for(int i=0;i<n;i++){
			obj1[i]=new pr();
			obj1[i].arrivalTime=obj[i].arrivalTime;
			obj1[i].burstTime=obj[i].burstTime;
		}
		int totalBurstTime=0,selected,minval=1;
		for(int i=0;i<n;i++)
			totalBurstTime+=obj[i].burstTime;
		for(int i=0;i<=obj[n-1].arrivalTime+totalBurstTime;i++){
			selected=-1;minval=100000000;
			for(int j=0;j<n;j++){
				if(obj1[j].burstTime<=minval&&i>=obj1[j].arrivalTime&&obj1[j].burstTime>=1){
					minval=obj1[j].burstTime;
					selected=j;
				}
			}
			if(selected!=-1){
				obj1[selected].burstTime--;
				if(obj1[selected].burstTime==0){
					obj[selected].waitingTime=Math.max(i-(obj[selected].burstTime+obj[selected].arrivalTime)+1,0);
					obj[selected].turnaroundTime=obj[selected].waitingTime+obj[selected].burstTime;
				}
			}
		}
	}
	public static boolean rr_checker(pr obj1[]){
		for(int i=0;i<n;i++){
			if(obj1[i].burstTime!=0)
				return true;
		}
		return false;
	}
	public static void rr(){
		prsort();
		pr obj1[]=new pr[100];
		for(int i=0;i<n;i++){
			obj1[i]=new pr();
			obj1[i].arrivalTime=obj[i].arrivalTime;
			obj1[i].burstTime=obj[i].burstTime;
		}
		int slice,t=0;
		Scanner sc=new Scanner(System.in);
		slice=sc.nextInt();
		while(rr_checker(obj1)){
			for(int i=0;i<n;i++){
				if(obj[i].arrivalTime<=t&&obj1[i].burstTime!=0){
					if(obj1[i].burstTime-slice<=0){
						obj[i].waitingTime=Math.max(t+obj1[i].burstTime-(obj[i].burstTime+obj[i].arrivalTime),0);
						obj[i].turnaroundTime=obj[i].waitingTime+obj[i].burstTime;
						obj1[i].burstTime=0;
					}
					else{
						obj1[i].burstTime-=slice;
					}
					t+=slice;
				}
			}
		}
	}
	public static void main(String[] Args){
		processTypeInput();
		switch(ch){
		case 1:processInput();fcfs();display();break;
		case 2:processInput();sjfnp();display();break;
		case 3:processInput();sjfp();display();break;
		case 4:processInput();rr();display();break;
		default:System.out.println("Please enter correct value");
		}
	}
}
