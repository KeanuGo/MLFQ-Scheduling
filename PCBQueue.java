import java.util.LinkedList;
import java.util.*;
public class PCBQueue{
	protected ArrayList<PCB> pcb;
	protected Queue<PCB> q = new LinkedList<>();
	protected Queue<Integer> q1 = new LinkedList<>();
	
	public PCBQueue(ArrayList<PCB> pcb){
		this.pcb= pcb;
	}
	public void insert(PCB p){
		pcb.add(p);
	}
	
	public void FCFS(boolean preempt){
		
        PCB temp= new PCB(0,0,0,0);
	
		sortByArrival(temp);
		
		if(!preempt){
			q1.add(MLFQ.z);
			for(int i=0; i<pcb.size(); i++){
				enqueue(pcb.get(i));
				MLFQ.z+=(pcb.get(i)).getBurst_time();
				pcb.get(i).setCompletion_time(MLFQ.z);
				pcb.get(i).setTurnaround_time(pcb.get(i).getCompletion_time() - pcb.get(i).getArrival_time());
				pcb.get(i).setWait_time(pcb.get(i).getTurnaround_time() - pcb.get(i).getorigBurst_time());
				q1.add(MLFQ.z);
				if(i+1<pcb.size()){
					if(MLFQ.z<pcb.get(i+1).getArrival_time()){
						MLFQ.z= pcb.get(i+1).getArrival_time();
						q1.add(MLFQ.z);
					}
				}
			}
			//pcb.removeAll(pcb);
		}else{
			q1.add(MLFQ.z);
			for(int i=0; i<pcb.size(); i++){
				if((MLFQ.z+pcb.get(i).getBurst_time())>=MLFQ.artime){
					pcb.get(i).setBurst_time(pcb.get(i).getBurst_time()-(MLFQ.artime-MLFQ.z));
					enqueue(pcb.get(i));
					MLFQ.z+=(MLFQ.artime-MLFQ.z);
					pcb.get(i).setCompletion_time(MLFQ.z);
					pcb.get(i).setTurnaround_time(pcb.get(i).getCompletion_time() - pcb.get(i).getArrival_time());
					pcb.get(i).setWait_time(pcb.get(i).getTurnaround_time() - pcb.get(i).getorigBurst_time());
					q1.add(MLFQ.z);
					break;
				}else{
					MLFQ.z+=(pcb.get(i)).getBurst_time();
					pcb.get(i).setBurst_time(0);
					enqueue(pcb.get(i));
					
					pcb.get(i).setCompletion_time(MLFQ.z);
					pcb.get(i).setTurnaround_time(pcb.get(i).getCompletion_time() - pcb.get(i).getArrival_time());
					pcb.get(i).setWait_time(pcb.get(i).getTurnaround_time() - pcb.get(i).getorigBurst_time());
					q1.add(MLFQ.z);
				}
			}
		}
	
	}
	public void SJF(boolean preempt){
		
		PCB temp= new PCB(0,0,0,0);
		ArrayList<PCB> imagine= new ArrayList<PCB>();;
		ArrayList<PCB> last= new ArrayList<PCB>();;
		
		sortByArrival(temp);
		
		int m=-1;
		q1.add(MLFQ.z);
		int idle=0;
		
		PCB temp0;
		//int b=0;
		while(true){
			//b++;
			if((MLFQ.totalbursttime+MLFQ.start+idle)<=MLFQ.z&&m==pcb.size()-1){
				break;
			}
			System.out.println("VVVV");
			if(MLFQ.z<=(pcb.get(pcb.size()-1).getArrival_time())){
				for(int i=m+1; i<pcb.size(); i++){
					if(MLFQ.z>=(pcb.get(i)).getArrival_time()&&(!(pcb.get(i)).finish)){
						imagine.add(pcb.get(i));
						//System.out.println("VVVV"+pcb.get(i).getPid());
						m=i;
					}
				}
				
				PCB temp1;
				for(int j=0;j<imagine.size();j++){
					for(int i=j+1;i<imagine.size();i++){
						if((imagine.get(i)).getBurst_time() < (imagine.get(j)).getBurst_time()){
							temp1 = imagine.get(j);
							imagine.set( j, imagine.get(i));
							imagine.set(i, temp1);
						}
					}
				}
				
			}else{
				for(int i=0; i<pcb.size(); i++){
					if(!pcb.get(i).finish){
						last.add(pcb.get(i));
					}
				}
				for(int j=0;j<last.size();j++){
					for(int i=j+1;i<last.size();i++){
						if((last.get(i)).getBurst_time() < (last.get(j)).getBurst_time()){
							temp = last.get(j);
							last.set( j, last.get(i));
							last.set(i, temp);
						}
					}
				}
				for(int i=0; i<last.size(); i++){
					if(!preempt){
						last.get(i).setWait_time(MLFQ.z);
						MLFQ.z+=(last.get(i)).getBurst_time();
						last.get(i).setBurst_time(0);
						enqueue(last.get(i));
					}else{
						if((MLFQ.z+last.get(i).getBurst_time())>=MLFQ.artime){
							last.get(i).setWait_time(MLFQ.z);
							
							last.get(i).setBurst_time(last.get(i).getBurst_time()-(MLFQ.artime-MLFQ.z));
							enqueue(last.get(i));
							MLFQ.z+=(MLFQ.artime-MLFQ.z);
							q1.add(MLFQ.z);
							for(int j=0; j<pcb.size(); j++){
								if(last.get(i)==pcb.get(j)){
									pcb.get(j).setCompletion_time(MLFQ.z);
								}
							}
							break;
						}else{
							last.get(i).setWait_time(MLFQ.z);
							MLFQ.z+=(last.get(i)).getBurst_time();
							last.get(i).setBurst_time(0);
							enqueue(last.get(i));
						}
					}
					
					//last.remove(i);
					for(int j=0; j<pcb.size(); j++){
						if(last.get(i)==pcb.get(j)){
							pcb.get(j).setCompletion_time(MLFQ.z);
						}
					}
					q1.add(MLFQ.z);
				}
				break;
			}
			//if(!preempt){
				//imagine.get(0).setBurst_time(0);
				if(m==pcb.size()-1){
					if((MLFQ.z+imagine.get(0).getBurst_time())>=MLFQ.artime&&MLFQ.st){
						imagine.get(0).setBurst_time(imagine.get(0).getBurst_time()-(MLFQ.artime-MLFQ.z));
						enqueue(imagine.get(0));
						MLFQ.z+=(MLFQ.artime-MLFQ.z);
						
						q1.add(MLFQ.z);
						break;
					}else{
						MLFQ.z+=imagine.get(0).getBurst_time();
						imagine.get(0).setBurst_time(0);
						enqueue(temp0=imagine.get(0));
						imagine.remove(0);
						for(int i=0; i<pcb.size(); i++){
							if(pcb.get(i).getPid()==temp0.getPid()){
								//System.out.println("VVVV"+pcb.get(i).getPid());
								pcb.get(i).finish=true;
								
							}
						}
						
						for(int j=0; j<pcb.size(); j++){
							if(temp0==pcb.get(j)){
								pcb.get(j).setCompletion_time(MLFQ.z);
							}
						}
						q1.add(MLFQ.z);
					}
				}
				boolean br= false;
				if(m+1<pcb.size()){
				while(MLFQ.z<pcb.get(m+1).getArrival_time()){
					if((MLFQ.z+imagine.get(0).getBurst_time())>=MLFQ.artime&&MLFQ.st){
						imagine.get(0).setBurst_time(imagine.get(0).getBurst_time()-(MLFQ.artime-MLFQ.z));
						enqueue(imagine.get(0));
						MLFQ.z+=(MLFQ.artime-MLFQ.z);
						
						q1.add(MLFQ.z);
						br=true;
						break;
					}else{
						MLFQ.z+=imagine.get(0).getBurst_time();
						imagine.get(0).setBurst_time(0);
						enqueue(temp0=imagine.get(0));
						imagine.remove(0);
						for(int i=0; i<pcb.size(); i++){
							if(pcb.get(i).getPid()==temp0.getPid()){
								//System.out.println("VVVV"+pcb.get(i).getPid());
								pcb.get(i).finish=true;
								
							}
						}
						
						for(int j=0; j<pcb.size(); j++){
							if(temp0.getPid()==pcb.get(j).getPid()){
								pcb.get(j).setCompletion_time(MLFQ.z);
							}
						}
						q1.add(MLFQ.z);
						
						if(MLFQ.z<pcb.get(m+1).getArrival_time()&&(imagine.size()<=0)){
							//System.out.println("DDDD"+pcb.get(m+1).getPid());
							//System.out.println("EEE"+imagine.size());
							idle+=(pcb.get(m+1).getArrival_time()-MLFQ.z);
							MLFQ.z= pcb.get(m+1).getArrival_time();
							q1.add(MLFQ.z);
							break;
						}
					}
					//System.out.println("VVC");
					
					
				}
				if(br){
					break;
				}
				}
			//}else{
			//	break;
			//}
		}
		
		for(int i=0; i<pcb.size(); i++){
			pcb.get(i).setTurnaround_time(pcb.get(i).getCompletion_time() - pcb.get(i).getArrival_time());
			pcb.get(i).setWait_time(pcb.get(i).getTurnaround_time() - pcb.get(i).getorigBurst_time());
		}
		
		
	}
	public void NonPPrS(boolean preempt){
		
		
		PCB temp= new PCB(0,0,0,0);
		ArrayList<PCB> imagine= new ArrayList<PCB>();;
		ArrayList<PCB> last= new ArrayList<PCB>();;
		
		sortByArrival(temp);
		
		int m=-1;
		q1.add(MLFQ.z);
		int idle=0;
		
		PCB temp0;
		//int b=0;
		while(true){
			//b++;
			if((MLFQ.totalbursttime+MLFQ.start+idle)<=MLFQ.z&&m==pcb.size()-1){
				break;
			}
			//System.out.println("VVVV");
			if(MLFQ.z<=(pcb.get(pcb.size()-1).getArrival_time())){
				for(int i=m+1; i<pcb.size(); i++){
					if(MLFQ.z>=(pcb.get(i)).getArrival_time()&&(!(pcb.get(i)).finish)){
						imagine.add(pcb.get(i));
						//System.out.println("VVVV"+pcb.get(i).getPid());
						m=i;
					}
				}
				
				PCB temp1;
				for(int j=0;j<imagine.size();j++){
					for(int i=j+1;i<imagine.size();i++){
						if((imagine.get(i)).getPriority() < (imagine.get(j)).getPriority()){
							temp1 = imagine.get(j);
							imagine.set( j, imagine.get(i));
							imagine.set(i, temp1);
						}
					}
				}
				
			}else{
				for(int i=0; i<pcb.size(); i++){
					if(!pcb.get(i).finish){
						last.add(pcb.get(i));
					}
				}
				for(int j=0;j<last.size();j++){
					for(int i=j+1;i<last.size();i++){
						if((last.get(i)).getPriority() < (last.get(j)).getPriority()){
							temp = last.get(j);
							last.set( j, last.get(i));
							last.set(i, temp);
						}
					}
				}
				for(int i=0; i<last.size(); i++){
					if(!preempt){
						last.get(i).setWait_time(MLFQ.z);
						MLFQ.z+=(last.get(i)).getBurst_time();
						last.get(i).setBurst_time(0);
						enqueue(last.get(i));
					}else{
						if((MLFQ.z+last.get(i).getBurst_time())>=MLFQ.artime){
							last.get(i).setWait_time(MLFQ.z);
							
							last.get(i).setBurst_time(last.get(i).getBurst_time()-(MLFQ.artime-MLFQ.z));
							enqueue(last.get(i));
							MLFQ.z+=(MLFQ.artime-MLFQ.z);
							q1.add(MLFQ.z);
							for(int j=0; j<pcb.size(); j++){
								if(last.get(i)==pcb.get(j)){
									pcb.get(j).setCompletion_time(MLFQ.z);
								}
							}
							break;
						}else{
							last.get(i).setWait_time(MLFQ.z);
							MLFQ.z+=(last.get(i)).getBurst_time();
							last.get(i).setBurst_time(0);
							enqueue(last.get(i));
						}
					}
					
					//last.remove(i);
					for(int j=0; j<pcb.size(); j++){
						if(last.get(i)==pcb.get(j)){
							pcb.get(j).setCompletion_time(MLFQ.z);
						}
					}
					q1.add(MLFQ.z);
				}
				break;
			}
			//if(!preempt){
				//imagine.get(0).setBurst_time(0);
				if(m==pcb.size()-1){
					if((MLFQ.z+imagine.get(0).getBurst_time())>=MLFQ.artime&&MLFQ.st){
						imagine.get(0).setBurst_time(imagine.get(0).getBurst_time()-(MLFQ.artime-MLFQ.z));
						enqueue(imagine.get(0));
						MLFQ.z+=(MLFQ.artime-MLFQ.z);
						
						q1.add(MLFQ.z);
						break;
					}else{
						MLFQ.z+=imagine.get(0).getBurst_time();
						imagine.get(0).setBurst_time(0);
						enqueue(temp0=imagine.get(0));
						imagine.remove(0);
						for(int i=0; i<pcb.size(); i++){
							if(pcb.get(i).getPid()==temp0.getPid()){
								//System.out.println("VVVV"+pcb.get(i).getPid());
								pcb.get(i).finish=true;
								
							}
						}
						
						for(int j=0; j<pcb.size(); j++){
							if(temp0==pcb.get(j)){
								pcb.get(j).setCompletion_time(MLFQ.z);
							}
						}
						q1.add(MLFQ.z);
					}
				}
				boolean br= false;
				if(m+1<pcb.size()){
				while(MLFQ.z<pcb.get(m+1).getArrival_time()){
					if((MLFQ.z+imagine.get(0).getBurst_time())>=MLFQ.artime&&MLFQ.st){
						imagine.get(0).setBurst_time(imagine.get(0).getBurst_time()-(MLFQ.artime-MLFQ.z));
						enqueue(imagine.get(0));
						MLFQ.z+=(MLFQ.artime-MLFQ.z);
						
						q1.add(MLFQ.z);
						br=true;
						break;
					}else{
						MLFQ.z+=imagine.get(0).getBurst_time();
						imagine.get(0).setBurst_time(0);
						enqueue(temp0=imagine.get(0));
						imagine.remove(0);
						for(int i=0; i<pcb.size(); i++){
							if(pcb.get(i).getPid()==temp0.getPid()){
								//System.out.println("VVVV"+pcb.get(i).getPid());
								pcb.get(i).finish=true;
								
							}
						}
						
						for(int j=0; j<pcb.size(); j++){
							if(temp0.getPid()==pcb.get(j).getPid()){
								pcb.get(j).setCompletion_time(MLFQ.z);
							}
						}
						q1.add(MLFQ.z);
						
						if(MLFQ.z<pcb.get(m+1).getArrival_time()&&(imagine.size()<=0)){
							//System.out.println("DDDD"+pcb.get(m+1).getPid());
							//System.out.println("EEE"+imagine.size());
							idle+=(pcb.get(m+1).getArrival_time()-MLFQ.z);
							MLFQ.z= pcb.get(m+1).getArrival_time();
							q1.add(MLFQ.z);
							break;
						}
					}
					//System.out.println("VVC");
					
					
				}
					if(br){
						break;
					}
				}
			//}else{
			//	break;
			//}
		}
		
		for(int i=0; i<pcb.size(); i++){
			pcb.get(i).setTurnaround_time(pcb.get(i).getCompletion_time() - pcb.get(i).getArrival_time());
			pcb.get(i).setWait_time(pcb.get(i).getTurnaround_time() - pcb.get(i).getorigBurst_time());
		}
		
	}
	public void RR(int quantum, boolean mlfq, boolean preempt, int index){
		
		PCB temp= new PCB(0,0,0,0);
		ArrayList<PCB> imagine= new ArrayList<PCB>();;
		
		sortByArrival(temp);
		int now=0;
		
		int idle=0;
		if(!mlfq){
		PCB pcb2, pcb3=new PCB(0,0,0,0);
		
		q1.add(MLFQ.z);
		if(pcb.size()!=0){
			imagine.add(pcb.get(0));
		}
		
		while((MLFQ.totalbursttime+MLFQ.start+idle)>MLFQ.z){
			int is=0;
			if(imagine.size()==0){
				for(int i=0; i<pcb.size();i++){
					if(pcb3==pcb.get(i)){
						is=i;
					}
				}
				is++;
				if(MLFQ.z<pcb.get(is).getArrival_time()){
					idle+=(pcb.get(is).getArrival_time()-MLFQ.z);
					MLFQ.z= pcb.get(is).getArrival_time();
					q1.add(MLFQ.z);
				}
				imagine.add(pcb.get(is));
			}
			if(imagine.get(0).getBurst_time()>quantum){
				(imagine.get(0)).setBurst_time((imagine.get(0)).getBurst_time()-quantum);
				enqueue(pcb2=imagine.remove(0));
				MLFQ.z+=quantum;
				q1.add(MLFQ.z);
				boolean there=false;
				for(int j=0; j<pcb.size(); j++){
					if(MLFQ.z>=pcb.get(j).getArrival_time() && pcb2!=pcb.get(j) && pcb.get(j).getBurst_time()!=0){
						there=false;
						
						for(int i=0; i<imagine.size();i++){
							if(pcb.get(j)==imagine.get(i)){
								there=true;
							}
						}
						
						if(!there){
							imagine.add(pcb.get(j));
						}
					}
				}
				
				
				imagine.add(pcb2);
				
			}else{
				
				now= imagine.get(0).getBurst_time();
				(imagine.get(0)).setBurst_time(0);
				for(int j=0; j<pcb.size(); j++){
					if(pcb.get(j)==imagine.get(0)){
						(pcb.get(j)).setBurst_time(0);
					}
				}
				enqueue(pcb3=imagine.remove(0));
				//System.out.println("A"+pcb3.getPid());
				MLFQ.z+=now;
				for(int j=0; j<pcb.size(); j++){
					if(pcb3==pcb.get(j)){
						pcb.get(j).setCompletion_time(MLFQ.z);
						pcb.get(j).setTurnaround_time(pcb.get(j).getCompletion_time() - pcb.get(j).getArrival_time());
						pcb.get(j).setWait_time(pcb.get(j).getTurnaround_time() - pcb.get(j).getorigBurst_time());
					}
				}
				q1.add(MLFQ.z);
				
			}
		}
		}else{
			q1.add(MLFQ.z);
			int befq=quantum;
			for(int i=0; i<pcb.size(); i++){
				
				if(i==0&&((quantum-MLFQ.rTime[index])!=0)){
					quantum-=MLFQ.rTime[index];
				}else{
					quantum= befq;
				}
				if(pcb.get(i).getBurst_time()>=quantum){
					if(!preempt){
					MLFQ.z+=quantum;
					pcb.get(i).setBurst_time(pcb.get(i).getBurst_time()-quantum);
					}else{
						if((MLFQ.z+pcb.get(i).getBurst_time())>=MLFQ.artime){
							
							pcb.get(i).setBurst_time(pcb.get(i).getBurst_time()-(MLFQ.artime-MLFQ.z));
							MLFQ.z+=(MLFQ.artime-MLFQ.z);
							enqueue(pcb.get(i));
							q1.add(MLFQ.z);
							pcb.get(i).setCompletion_time(MLFQ.z);
							break;
						}else{
							MLFQ.z+=quantum;
							pcb.get(i).setBurst_time(pcb.get(i).getBurst_time()-quantum);
							pcb.get(i).setCompletion_time(MLFQ.z);
						}
					}
				}else{
					if(!preempt){
					MLFQ.z+=pcb.get(i).getBurst_time();
					pcb.get(i).setBurst_time(0);
					pcb.get(i).setCompletion_time(MLFQ.z);
					}else{
						if((MLFQ.z+pcb.get(i).getBurst_time())>=MLFQ.artime){
							
							
							pcb.get(i).setBurst_time(pcb.get(i).getBurst_time()-(MLFQ.artime-MLFQ.z));
							MLFQ.z+=(MLFQ.artime-MLFQ.z);
							enqueue(pcb.get(i));
							
							q1.add(MLFQ.z);
							pcb.get(i).setCompletion_time(MLFQ.z);
							break;
						}else{
							MLFQ.z+=pcb.get(i).getBurst_time();
							pcb.get(i).setBurst_time(0);
							pcb.get(i).setCompletion_time(MLFQ.z);
						}
					}
				}
				enqueue(pcb.get(i));
				q1.add(MLFQ.z);
				MLFQ.rTime[index]=0;
			}
		}
		//pcb.removeAll(pcb);
		
	}
	public void PrS(boolean preempt){
		if(pcb.size()!=0){
			if(!preempt||MLFQ.st){
			PCB temp= new PCB(0,0,0,0);
			ArrayList<PCB> imagine= new ArrayList<PCB>();;
			
			sortByArrival(temp);
			for(int i=0; i<pcb.size(); i++){
				if(pcb.get(i).getBurst_time()!=0){
					//imagine.add(pcb.get(i));
					System.out.println("A"+pcb.get(i).getPid());
				}
			}
			int next=0, now=0, prev=0;
			
			q1.add(MLFQ.z);
			pcb.get(0).finish= true;
			imagine.add(pcb.get(0));
			boolean same= false;
			for(int i=0; i<pcb.size()-1; i++){
				
				if((pcb.get(i+1)).getArrival_time()==(pcb.get(i)).getArrival_time()){
						same= true;
						pcb.get(i+1).finish= true;
						imagine.add(pcb.get(i+1));
						PCB temp1;
						for(int j=0;j<imagine.size();j++){
							for(int k=j+1;k<imagine.size();k++){
								if((imagine.get(k)).getPriority() < (imagine.get(j)).getPriority()){
									temp1 = imagine.get(j);
									imagine.set( j, imagine.get(k));
									imagine.set(k, temp1);
								}
							}
						}
						continue;
					}
				if(MLFQ.z<=pcb.get(pcb.size()-1).getArrival_time()){
					
				if((imagine.get(0)).getBurst_time() >= (next=(pcb.get(i+1)).getArrival_time()-MLFQ.z)){
					(imagine.get(0)).setBurst_time(now=(imagine.get(0)).getBurst_time()-next);
						enqueue(imagine.get(0));
						MLFQ.z+=next;
						System.out.println("ZXC"+next);
						for(int j=0; j<pcb.size(); j++){
							if(imagine.get(0)==pcb.get(j)){
								pcb.get(j).setCompletion_time(MLFQ.z);
								pcb.get(j).setBurst_time(now);
								System.out.println("DDD"+pcb.get(j).getPid()+" "+now);
								if(now!=0){
								imagine.add(pcb.get(j));
								}else{
									pcb.get(j).finish=true;
								}
								imagine.remove(0);
								break;
							}
						}
						//if(now==0){
							
						//}
						
						q1.add(MLFQ.z);
						//pcb.get(i+1).finish= true;
						imagine.add(pcb.get(i+1));
						PCB temp1;
						for(int j=0;j<imagine.size();j++){
							for(int k=j+1;k<imagine.size();k++){
								if((imagine.get(k)).getPriority() < (imagine.get(j)).getPriority()){
									temp1 = imagine.get(j);
									imagine.set( j, imagine.get(k));
									imagine.set(k, temp1);
								}
							}
						}
						
					
				}else{
					next=(pcb.get(i+1)).getArrival_time()-MLFQ.z;
					now= (imagine.get(0)).getBurst_time();
					while(next-now>0){
						if(MLFQ.z>=pcb.get(pcb.size()-1).getArrival_time()){
							break;
						}
						MLFQ.z+=imagine.get(0).getBurst_time();
						(imagine.get(0)).setBurst_time(0);
						enqueue(imagine.get(0));
						System.out.println("M"+imagine.get(0).getPid());
							System.out.println("M"+imagine.get(0).getBurst_time());
													System.out.println("M"+now);

						
						for(int j=0; j<pcb.size(); j++){
							if(imagine.get(0)==pcb.get(j)){
								pcb.get(j).setCompletion_time(MLFQ.z);
								pcb.get(j).setBurst_time(0);
								pcb.get(j).finish=true;
							}
						}
						q1.add(MLFQ.z);
						imagine.remove(0);
						int emp= imagine.size();
						imagine.add(pcb.get(i+1));
						//pcb.get(i+1).finish=true;
						boolean fin=false;
						for(int k=0; k<i; k++){
							if(pcb.get(k).finish!=true){
								fin=true;
							}
						}
						if(MLFQ.z<pcb.get(i+1).getArrival_time()&&(emp==0||!fin)&&!same){
							MLFQ.z= pcb.get(i+1).getArrival_time();
							q1.add(MLFQ.z);
							break;
						}
						
						prev=now;
						now += imagine.get(0).getBurst_time();
						int d=2;
						if(next-now<=0){
							System.out.println("E"+imagine.get(0).getBurst_time());
							(imagine.get(0)).setBurst_time(now=(imagine.get(0)).getBurst_time()-(next-prev));
							enqueue(imagine.get(0));
							System.out.println("G"+imagine.get(0).getPid());
							System.out.println("G"+imagine.get(0).getBurst_time());
							MLFQ.z+=(next-prev);
							for(int j=0; j<pcb.size(); j++){
								if(imagine.get(0)==pcb.get(j)){
									pcb.get(j).setCompletion_time(MLFQ.z);
									pcb.get(j).setBurst_time(now);
								}
							}
							q1.add(MLFQ.z);
							if(pcb.size()>(i+d)+1){
								imagine.add(pcb.get(i+d));
							}
							PCB temp1;
						
							for(int j=0;j<imagine.size();j++){
								for(int k=j+1;k<imagine.size();k++){
									if((imagine.get(k)).getPriority() < (imagine.get(j)).getPriority()){
										temp1 = imagine.get(j);
										imagine.set( j, imagine.get(k));
										imagine.set(k, temp1);
									}
								}
							}
							d++;
							break;
		
						}
						
					}
				}
			}
		}
		imagine.removeAll(imagine);
		for(int i=0; i<pcb.size(); i++){
			if(pcb.get(i).getBurst_time()!=0){
				imagine.add(pcb.get(i));
				//System.out.println("H"+pcb.get(i).getPid());
				//System.out.println("R"+pcb.get(i).getBurst_time());
			}
		}
		PCB temp1;
		//System.out.println("D"+imagine.size());
		for(int j=0;j<imagine.size();j++){
			System.out.println("L"+imagine.get(j).getPid());
		}
		for(int j=0;j<imagine.size();j++){
			for(int k=j+1;k<imagine.size();k++){
				if((imagine.get(k)).getPriority() < (imagine.get(j)).getPriority()){
					temp1 = imagine.get(j);
					imagine.set( j, imagine.get(k));
					imagine.set(k, temp1);
				}
			}
		}
		remProcandsetTime(imagine);
		}else{
			NonPPrS(true);
		}
		}
	}
	public void SRTF(boolean preempt){
		
		if(pcb.size()!=0){
			if(!preempt||MLFQ.st){
			PCB temp= new PCB(0,0,0,0);
			ArrayList<PCB> imagine= new ArrayList<PCB>();;
			
			sortByArrival(temp);
			for(int i=0; i<pcb.size(); i++){
				if(pcb.get(i).getBurst_time()!=0){
					//imagine.add(pcb.get(i));
					//System.out.println("A"+pcb.get(i).getPid());
				}
			}
			int next=0, now=0, prev=0;
			
			q1.add(MLFQ.z);
			pcb.get(0).finish= true;
			imagine.add(pcb.get(0));
			boolean same= false;
			for(int i=0; i<pcb.size()-1; i++){
				
				if((pcb.get(i+1)).getArrival_time()==(pcb.get(i)).getArrival_time()){
						//System.out.println("JK");
						same= true;
						pcb.get(i+1).finish= true;
						imagine.add(pcb.get(i+1));
						PCB temp1;
						for(int j=0;j<imagine.size();j++){
							for(int k=j+1;k<imagine.size();k++){
								if((imagine.get(k)).getBurst_time() < (imagine.get(j)).getBurst_time()){
									temp1 = imagine.get(j);
									imagine.set( j, imagine.get(k));
									imagine.set(k, temp1);
								}
							}
						}
						continue;
					}
				if(MLFQ.z<=pcb.get(pcb.size()-1).getArrival_time()){
					
				if((imagine.get(0)).getBurst_time() >= (next=(pcb.get(i+1)).getArrival_time()-MLFQ.z)){
					(imagine.get(0)).setBurst_time(now=(imagine.get(0)).getBurst_time()-next);
						enqueue(imagine.get(0));
						MLFQ.z+=next;
						//System.out.println("ZXC"+next);
						for(int j=0; j<pcb.size(); j++){
							if(imagine.get(0)==pcb.get(j)){
								pcb.get(j).setCompletion_time(MLFQ.z);
								pcb.get(j).setBurst_time(now);
								//System.out.println("DDD"+pcb.get(j).getPid()+" "+now);
								if(now!=0){
								imagine.add(pcb.get(j));
								}else{
									pcb.get(j).finish=true;
								}
								imagine.remove(0);
								break;
							}
						}
						//if(now==0){
							
						//}
						
						q1.add(MLFQ.z);
						//pcb.get(i+1).finish= true;
						imagine.add(pcb.get(i+1));
						PCB temp1;
						for(int j=0;j<imagine.size();j++){
							for(int k=j+1;k<imagine.size();k++){
								if((imagine.get(k)).getBurst_time() < (imagine.get(j)).getBurst_time()){
									temp1 = imagine.get(j);
									imagine.set( j, imagine.get(k));
									imagine.set(k, temp1);
								}
							}
						}
						
					
				}else{
					next=(pcb.get(i+1)).getArrival_time()-MLFQ.z;
					now= (imagine.get(0)).getBurst_time();
					while(next-now>0){
						if(MLFQ.z>=pcb.get(pcb.size()-1).getArrival_time()){
							break;
						}
						MLFQ.z+=imagine.get(0).getBurst_time();
						(imagine.get(0)).setBurst_time(0);
						enqueue(imagine.get(0));
						//System.out.println("M"+imagine.get(0).getPid());
						//System.out.println("M"+imagine.get(0).getBurst_time());
						//System.out.println("M"+now);

						
						for(int j=0; j<pcb.size(); j++){
							if(imagine.get(0)==pcb.get(j)){
								pcb.get(j).setCompletion_time(MLFQ.z);
								pcb.get(j).setBurst_time(0);
								pcb.get(j).finish=true;
							}
						}
						q1.add(MLFQ.z);
						imagine.remove(0);
						int emp= imagine.size();
						imagine.add(pcb.get(i+1));
						//pcb.get(i+1).finish=true;
						boolean fin=false;
						for(int k=0; k<i; k++){
							if(pcb.get(k).finish!=true){
								fin=true;
							}
						}
						if((MLFQ.z<pcb.get(i+1).getArrival_time()&&(emp==0||!fin))&&!same){
							System.out.println("JK"+emp+""+fin+""+same);
							MLFQ.z= pcb.get(i+1).getArrival_time();
							q1.add(MLFQ.z);
							break;
						}
						same=false;
						
						prev=now;
						now += imagine.get(0).getBurst_time();
						//int d=2;
						if(next-now<=0){
							System.out.println("E"+imagine.get(0).getBurst_time());
							(imagine.get(0)).setBurst_time(now=(imagine.get(0)).getBurst_time()-(next-prev));
							enqueue(imagine.get(0));
							System.out.println("G"+imagine.get(0).getPid());
							System.out.println("G"+imagine.get(0).getBurst_time());
							MLFQ.z+=(next-prev);
							for(int j=0; j<pcb.size(); j++){
								if(imagine.get(0)==pcb.get(j)){
									pcb.get(j).setCompletion_time(MLFQ.z);
									pcb.get(j).setBurst_time(now);
								}
							}
							q1.add(MLFQ.z);
							
							imagine.add(imagine.get(0));
							imagine.remove(0);
							PCB temp1;
						
							for(int j=0;j<imagine.size();j++){
								for(int k=j+1;k<imagine.size();k++){
									if((imagine.get(k)).getBurst_time() < (imagine.get(j)).getBurst_time()){
										temp1 = imagine.get(j);
										imagine.set( j, imagine.get(k));
										imagine.set(k, temp1);
									}
								}
							}
							
							//d++;
							break;
		
						}
						
					}
				}
			}
		}
		imagine.removeAll(imagine);
		for(int i=0; i<pcb.size(); i++){
			if(pcb.get(i).getBurst_time()!=0){
				imagine.add(pcb.get(i));
				//System.out.println("H"+pcb.get(i).getPid());
				//System.out.println("R"+pcb.get(i).getBurst_time());
			}
		}
		PCB temp1;
		//System.out.println("D"+imagine.size());
		//for(int j=0;j<imagine.size();j++){
			//System.out.println("L"+imagine.get(j).getPid());
		//}
		for(int j=0;j<imagine.size();j++){
			for(int k=j+1;k<imagine.size();k++){
				if((imagine.get(k)).getBurst_time() < (imagine.get(j)).getBurst_time()){
					temp1 = imagine.get(j);
					imagine.set( j, imagine.get(k));
					imagine.set(k, temp1);
				}
			}
		}
		remProcandsetTime(imagine);
		}else{
			SJF(true);
		}
		}
	}
	public void sortByArrival(PCB temp){
		for(int j=0;j<pcb.size();j++){
            for(int i=j+1;i<pcb.size();i++){
				if((pcb.get(i)).getArrival_time() < (pcb.get(j)).getArrival_time()){
                    temp = pcb.get(j);
                    pcb.set( j, pcb.get(i));
                    pcb.set(i, temp);
                }
            }
		}
	}
	public void remProcandsetTime(ArrayList<PCB> imagine){
		
		while(imagine.size()!=0){
			MLFQ.z+=imagine.get(0).getBurst_time();
			for(int j=0; j<pcb.size(); j++){
				if(imagine.get(0)==pcb.get(j)){
					pcb.get(j).setCompletion_time(MLFQ.z);
				}
			}
			enqueue(imagine.remove(0));
			q1.add(MLFQ.z);
			if(MLFQ.z>=MLFQ.totalbursttime){
				//break;
			}
		}
		//pcb.removeAll(pcb);
		for(int i=0; i<pcb.size(); i++){
			pcb.get(i).setTurnaround_time(pcb.get(i).getCompletion_time() - pcb.get(i).getArrival_time());
			pcb.get(i).setWait_time(pcb.get(i).getTurnaround_time() - pcb.get(i).getorigBurst_time());
		}
	}
	public ArrayList<PCB> getPCB(){
		return pcb;
	}
	public void enqueue(PCB pcb){
		q.add(pcb);
	}
	public void enqueue1(int p){
		q1.add(p);
	}
	public void rm(PCB p){
		for(int i=0; i<pcb.size(); i++){
			if(p.getPid()==pcb.get(i).getPid()){
				pcb.remove(i);
			}
		}
	}
	public void removeA(){
		pcb.removeAll(pcb);
	}
	public void update(PCB p){
		for(int i=0; i<pcb.size(); i++){
			if(p.getPid()==pcb.get(i).getPid()){
				pcb.get(i).setBurst_time(p.getBurst_time());
				pcb.get(i).setCompletion_time(p.getCompletion_time());
				pcb.get(i).setTurnaround_time(p.getTurnaround_time());
			}
		}
	}
	public PCB show(){
		return q.peek();
	}
	public Integer show1(){
		return q1.peek();
	}
	public int getSize(){
		return q.size();
	}
	public int getSize1(){
		return q1.size();
	}
	public PCB dequeue(){
		return q.remove();
	}
	public Integer dequeue1(){
		return q1.remove();
	}
	public void reset(){
		
		while(q1.size()!=0){
			q1.remove();
		}
		while(q.size()!=0){
			q.remove();
		}
	}
}
