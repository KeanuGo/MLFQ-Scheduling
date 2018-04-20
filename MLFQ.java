import java.util.*;
import javax.swing.*;    
import javax.swing.table.*;    
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
public class MLFQ extends Thread{
	static ArrayList<PCB> pcb1= new ArrayList<PCB>();
	Scanner input= new Scanner(System.in);
	static String[] schedAssign;
	static int[] rrAssign;
	static int[] priority;
	static int[] rTime;
	static boolean st= false;
	static int z=0;
	boolean finish=false;
	static boolean isFixedTimeSlots= false;
	static PCBQueue pcq[];
	PCBQueue pcqinit;
	static JPanel qs= new JPanel();;
	static JPanel qp= new JPanel();;
	static int artime=0;
	int ditime[];
	static int time[];
	String scheduler[];
	static int totalbursttime=0;
	static int value=0;
	static int start=0;
	static int noOfqueues;
	static int befre=500;
	static JFrame f;
	static boolean fin=false;
	static boolean responded[];
	static JLabel process[];
	static boolean there[][];
	static JPanel q[];
	public MLFQ(){
		
		
	}
	public void run() {
		qp.removeAll();
		qp.setLayout(new FlowLayout());
		scheduler= new String[noOfqueues];
		rTime= new int[noOfqueues];
		ditime= new int[noOfqueues];
		
		pcq= new PCBQueue[noOfqueues];
		
		for(int i=0; i<noOfqueues; i++){
			pcq[i]= new PCBQueue(new ArrayList<PCB>());
		}
		
		for(int i=0; i<pcb1.size(); i++){
			totalbursttime+=pcb1.get(i).getorigBurst_time();
		}
		
		pcqinit= new PCBQueue(pcb1);
		pcqinit.FCFS(false);
		
		int emp=1;
		z=0;
		
		int point=0;
		
		z=pcqinit.show().getArrival_time();
		start=pcqinit.show().getArrival_time();
		if(!isFixedTimeSlots){
			while(pcqinit.getSize()>0){
				
				artime=pcqinit.show().getArrival_time();
					
				if(pcqinit.show().getArrival_time()<=z){
					
					pcq[point].insert(pcqinit.dequeue());
					if(pcqinit.getSize()>0&&(!schedAssign.equals("RR")&&noOfqueues==1)){
						while(pcqinit.getSize()>0){
							System.out.println("FFF"+pcqinit.getSize());
							pcq[point].insert(pcqinit.dequeue());
							System.out.println("FFF"+pcqinit.getSize());
							if(pcqinit.getSize()<=0)
								break;
						}
					}	
					
					exec(schedAssign[point], point, rrAssign[point], false);
					if(pcq[point].getSize()!=0){
						emp=1;
					}
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
						
					}else{
						pcq[point].dequeue1();
					}
					int siz= pcq[point].getSize();
					
					for(int j=0; j<siz; j++){
						PCB pr;
						JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
						lad.setBorder(new LineBorder(Color.BLACK, 2));
						if(there[point][pr.getPid()-1]==false){
							q[point].add(process[pr.getPid()-1]);
							refresh1();
							there[point][pr.getPid()-1]=true;
						}
						qp.add(lad);
						refresh();
						try {
						  sleep(1000);
						} catch (InterruptedException e) {
						  throw new RuntimeException(e);
						}
						pcq[point].rm(pr);
						if(pr.getBurst_time()>0){
							if(schedAssign[point].equals("RR")&&noOfqueues!=1){
								System.out.println("Hey");	
								pcq[point+1].insert(pr);
								there[point][pr.getPid()-1]=false;
							}
							pcq[point].rm(pr);
						}
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						if(pcq[point].getSize()>0){
							if(pcq[point].show().getArrival_time()==pcq[point].show1()){
								int c=0;
								if(befre!=pcq[point].show1()){
									befre=pcq[point].show1();
									qp.add(new JLabel(Integer.toString(c=pcq[point].dequeue1())));
									System.out.println("SS"+c);	
								}else{
									pcq[point].dequeue1();
								}
							}
						}
					}
				}else{
					
					point= emp%(noOfqueues-1);
					
					exec(schedAssign[point], point, rrAssign[point], true);
					if(pcq[point].getSize()==0){
						emp++;
					}
					int bef;
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						qp.add(new JLabel(Integer.toString(bef=pcq[point].dequeue1())));
						
					}else{
						bef= pcq[point].dequeue1();
					}
					int siz= pcq[point].getSize();
					for(int j=0; j<siz; j++){
						PCB pr;
						
						JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
						lad.setBorder(new LineBorder(Color.BLACK, 2));
						if(there[point][pr.getPid()-1]==false){
							q[point].add(process[pr.getPid()-1]);
							refresh1();
							there[point][pr.getPid()-1]=true;
						}
						qp.add(lad);
						refresh();
						try {
						  sleep(1000);
						} catch (InterruptedException e) {
						  throw new RuntimeException(e);
						}
						pcq[point].rm(pr);
						if(pr.getBurst_time()>0){
							pcq[point].rm(pr);
							if(schedAssign[point].equals("RR")&& rrAssign[point]==(pcq[point].show1()-bef)){
								pcq[(point+1)%noOfqueues].insert(pr);
								there[point][pr.getPid()-1]=false;
							}else{
								pcq[point].insert(pr);
							}
							rTime[point]= pcq[point].show1()-bef;
							
						}
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
					}
					
					point=0;
					
				}
			}
			for(int i=1; i<noOfqueues-1; i++){
				point++;
				exec(schedAssign[point], point, rrAssign[point], false);
				
				if(befre!=pcq[point].show1()){
					befre=pcq[point].show1();
					qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
					
				}else{
					pcq[point].dequeue1();
				}
				
				int siz= pcq[point].getSize();
				for(int j=0; j<siz; j++){
					PCB pr;
					
					JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
					lad.setBorder(new LineBorder(Color.BLACK, 2));
					if(there[point][pr.getPid()-1]==false){
						q[point].add(process[pr.getPid()-1]);
						refresh1();
						there[point][pr.getPid()-1]=true;
					}
					qp.add(lad);
					refresh();
					try {
					  sleep(1000);
					} catch (InterruptedException e) {
					  throw new RuntimeException(e);
					}
					pcq[point].rm(pr);
					if(pr.getBurst_time()>0){
						if(schedAssign[point].equals("RR")&&noOfqueues!=1){
							pcq[point+1].insert(pr);
							there[point][pr.getPid()-1]=false;
						}
						pcq[point].rm(pr);
					}
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
						
					}else{
						pcq[point].dequeue1();
					}
				}
			}
			point=(point+1)%noOfqueues;
			if(noOfqueues==1){
			
			}else{
				exec(schedAssign[point], point, rrAssign[point], false);
			}
			System.out.println("Yo"+pcq[point].getSize());
			if(pcq[point].getSize1()>0){
				if(befre!=pcq[point].show1()){
					if(schedAssign[point].equals("RR")){
						befre=pcq[point].show1();
						qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
					}
				}else{
					pcq[point].dequeue1();
				}
			}
			int siz=pcq[point].getSize();
			
			for(int j=0; j<siz; j++){
				PCB pr;
				JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
				lad.setBorder(new LineBorder(Color.BLACK, 2));
				if(there[point][pr.getPid()-1]==false){
					q[point].add(process[pr.getPid()-1]);
					refresh1();
					there[point][pr.getPid()-1]=true;
				}
				qp.add(lad);
				refresh();
				try {
				  sleep(1000);
				} catch (InterruptedException e) {
				  throw new RuntimeException(e);
				}
				if(befre!=pcq[point].show1()){
					befre=pcq[point].show1();
					qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
					
				}else{
					pcq[point].dequeue1();
				}
				if(pcq[point].getSize()>0){
					if(pcq[point].show().getArrival_time()==pcq[point].show1()){
						int c=0;
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(c=pcq[point].dequeue1())));
							System.out.println("SS"+c);	
						}else{
							pcq[point].dequeue1();
						}
					}
				}
			}
		}else{
			start=0;
			int tm= time[0];
			if(!schedAssign[0].equals("RR")){
				st= true;
				while(true){
					boolean idle= true;
					for(int i=0; i< noOfqueues; i++){
						if(pcq[i].getPCB().size()!=0){
							idle= false;
						}
					}
					int x=0;
					if(idle&&(pcqinit.show().getArrival_time()>z)){
						x=(pcqinit.show().getArrival_time()-z);
						z= z+(pcqinit.show().getArrival_time()-z);
						start=start+x;
					}
					idle=true;
					if(pcqinit.show().getArrival_time()<=z){
						if(pcqinit.getSize()>0){
							pcq[point].insert(pcqinit.dequeue());
						}
						artime=tm;
						exec(schedAssign[point], point, rrAssign[point], true);
						tm= tm+(time[point]);
						
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						int siz= pcq[point].getSize();
						for(int j=0; j<siz; j++){
							PCB pr;
							
							JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
							lad.setBorder(new LineBorder(Color.BLACK, 2));
							if(there[point][pr.getPid()-1]==false){
								q[point].add(process[pr.getPid()-1]);
								refresh1();
								there[point][pr.getPid()-1]=true;
							}
							qp.add(lad);
							refresh();
							try {
							  sleep(1000);
							} catch (InterruptedException e) {
							  throw new RuntimeException(e);
							}
							pcq[point].rm(pr);
							if(pr.getBurst_time()>0){
								if(schedAssign[point].equals("SJF")||schedAssign[point].equals("NonPPrS")||schedAssign[point].equals("FCFS")){
									pcq[point].insert(pr);
								}
							}
							if(befre!=pcq[point].show1()){
								befre=pcq[point].show1();
								qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
								
							}else{
								pcq[point].dequeue1();
							}
							
						}
						if(z==totalbursttime+start){
							break;
						}
					}
				}
			}else{
				start=0;
				while(pcqinit.getSize()>0){
					boolean idle= true;
					for(int i=0; i< noOfqueues; i++){
						if(pcq[i].getPCB().size()!=0){
							idle= false;
						}
					}
					int x=0;
					if(idle&&(pcqinit.show().getArrival_time()>z)){
						x=(pcqinit.show().getArrival_time()-z);
						z= z+(pcqinit.show().getArrival_time()-z);
						start=start+x;
					}
					idle=true;
					artime=pcqinit.show().getArrival_time();
					System.out.println("Ho"+ artime);
					if(pcqinit.show().getArrival_time()<=z){
						pcq[point].insert(pcqinit.dequeue());
						int sbt= rrAssign[point]+z;
						int bef= tm;
						if(sbt<=tm){
							exec(schedAssign[point], point, rrAssign[point], false);
						}else{
							artime=tm;
							exec(schedAssign[point], point, rrAssign[point], true);
							tm= tm+(time[point+1]);
						}
						if(pcq[point].getSize()!=0){
							emp=1;
						}
						int ber=0;
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(ber=pcq[point].dequeue1())));
							
						}else{
							ber= pcq[point].dequeue1();
						}
						int siz= pcq[point].getSize();
						for(int j=0; j<siz; j++){
							PCB pr;
							
							JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
							lad.setBorder(new LineBorder(Color.BLACK, 2));
							if(there[point][pr.getPid()-1]==false){
								q[point].add(process[pr.getPid()-1]);
								refresh1();
								there[point][pr.getPid()-1]=true;
							}
							qp.add(lad);
							refresh();
							try {
							  sleep(1000);
							} catch (InterruptedException e) {
							  throw new RuntimeException(e);
							}
							pcq[point].rm(pr);
							if(pr.getBurst_time()>0){
								if(schedAssign[point].equals("RR")){
									if((pcq[point].show1()-ber)==rrAssign[point]){
										pcq[point].rm(pr);
										pcq[point+1].insert(pr);
										there[point][pr.getPid()-1]=false;
									}else{
										pcq[point].rm(pr);
										pcq[point].insert(pr);
										rTime[point]= pcq[point].show1()-ber;
									}
								}else{
									pcq[point].rm(pr);
									pcq[point].insert(pr);
								}
							}
							
							if(befre!=pcq[point].show1()){
								befre=pcq[point].show1();
								qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
								
							}else{
								pcq[point].dequeue1();
							}
						}
						if(sbt<=bef){
							
						}else{
							for(int i=1; i<noOfqueues; i++){
								point++;
								tm-=ditime[point];
								artime=tm;
								exec(schedAssign[point], point, rrAssign[point], true);
								ditime[point]=0;
								tm+= time[(point+1)%(noOfqueues)];
								
								
								ber=0;
								if(befre!=pcq[point].show1()){
									befre=pcq[point].show1();
									qp.add(new JLabel(Integer.toString(ber=pcq[point].dequeue1())));
									
								}else{
									ber= pcq[point].dequeue1();
								}
								
								siz= pcq[point].getSize();
								for(int j=0; j<siz; j++){
									PCB pr;
									
									JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
									lad.setBorder(new LineBorder(Color.BLACK, 2));
									if(there[point][pr.getPid()-1]==false){
										q[point].add(process[pr.getPid()-1]);
										refresh1();
										there[point][pr.getPid()-1]=true;
									}
									qp.add(lad);
									refresh();
									try {
									  sleep(1000);
									} catch (InterruptedException e) {
									  throw new RuntimeException(e);
									}
									if(pr.getBurst_time()>0){
										
										if(schedAssign[point].equals("RR")){
											if((pcq[point].show1()-ber)==rrAssign[point]){
												pcq[point].rm(pr);
												pcq[(point+1)%(noOfqueues)].insert(pr);
												there[point][pr.getPid()-1]=false;
											}else{
												pcq[point].rm(pr);
												pcq[point].insert(pr);
												rTime[point]= pcq[point].show1()-ber;
											}
										
										}else{
											pcq[point].rm(pr);
											if(point!=0 && j==(siz-1)){
												pcq[point-1].insert(pr);
												there[point][pr.getPid()-1]=false;
											}else{
												pcq[point].insert(pr);
											}
										}
									}
									if(befre!=pcq[point].show1()){
										befre=pcq[point].show1();
										qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
										
									}else{
										pcq[point].dequeue1();
									}
								}
							}
							point=0;
						}
					}else{
						
						point= emp%(noOfqueues-1);
						if((artime-z)>time[point]){
							artime= time[point]+z;
						}
						ditime[point]= artime-z;
						tm+=ditime[point];
						
						
						exec(schedAssign[point], point, rrAssign[point], true);
						
						
						if(pcq[point].getSize()==0){
							emp++;
						}
						int bef=0;
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(bef=pcq[point].dequeue1())));
							
						}else{
							bef= pcq[point].dequeue1();
						}
						int siz= pcq[point].getSize();
						
						for(int j=0; j<siz; j++){
							PCB pr;
							
							JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
							lad.setBorder(new LineBorder(Color.BLACK, 2));
							if(there[point][pr.getPid()-1]==false){
								q[point].add(process[pr.getPid()-1]);
								refresh1();
								there[point][pr.getPid()-1]=true;
							}
							qp.add(lad);
							refresh();
							try {
							  sleep(1000);
							} catch (InterruptedException e) {
							  throw new RuntimeException(e);
							}
							pcq[point].rm(pr);
							if(pr.getBurst_time()>0){
								pcq[point].rm(pr);
								if(schedAssign[point].equals("RR")&& rrAssign[point]==(pcq[point].show1()-bef)){
									System.out.println("GG");
									pcq[(point+1)%noOfqueues].insert(pr);
								}else{
									if(schedAssign[point].equals("RR")){
										pcq[point].insert(pr);
									}else{	
										if(point!=0 && j==(siz-1)){
											System.out.println("HH");
											pcq[point-1].insert(pr);
											there[point][pr.getPid()-1]=false;
										}else{
											pcq[point].insert(pr);
										}
									}
								}
								rTime[point]= pcq[point].show1()-bef;
								
							}
							if(befre!=pcq[point].show1()){
								befre=pcq[point].show1();
								qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
								
							}else{
								pcq[point].dequeue1();
							}
						}
						point=0;
					}
				}
				
				while(true){
					point=(point+1)%noOfqueues;
					tm+=time[point];
					
					artime=tm;
					if((rrAssign[point]+z)<artime && schedAssign[point].equals("RR"))
						artime=rrAssign[point]+z;
					exec(schedAssign[point], point, rrAssign[point], true);
					
					int ber;
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						qp.add(new JLabel(Integer.toString(ber=pcq[point].dequeue1())));
						
					}else{
						ber= pcq[point].dequeue1();
					}
					System.out.println("Hej"+point);
					int siz= pcq[point].getSize();
					for(int j=0; j<siz; j++){
						PCB pr;
						
						JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
						lad.setBorder(new LineBorder(Color.BLACK, 2));
						if(there[point][pr.getPid()-1]==false){
							q[point].add(process[pr.getPid()-1]);
							refresh1();
							there[point][pr.getPid()-1]=true;
						}
						qp.add(lad);
						refresh();
						try {
						  sleep(1000);
						} catch (InterruptedException e) {
						  throw new RuntimeException(e);
						}
						pcq[point].rm(pr);
						if(pr.getBurst_time()>0){
							
							if(schedAssign[point].equals("RR")){
								if((pcq[point].show1()-ber)==rrAssign[point]){
									pcq[point].rm(pr);
									pcq[(point+1)%(noOfqueues)].insert(pr);
									there[point][pr.getPid()-1]=false;
								}else{
									pcq[point].rm(pr);
									pcq[point].insert(pr);
									rTime[point]= pcq[point].show1()-ber;
								}
							
							}else{
								pcq[point].rm(pr);
								if(point!=0 && j==(siz-1)){
									pcq[point-1].insert(pr);
									there[point][pr.getPid()-1]=false;
								}else{
									pcq[point].insert(pr);
								}
							}
						}
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						if(z==totalbursttime+start){
							break;
						}
					}
					if(z==totalbursttime+start){
						break;
					}
				}
			}
		}
		refresh();
		 fin= true;
	}
	public void exec(String scheduler, int index, int quantum, boolean preempt){
		if(scheduler.equals("FCFS")){
			pcq[index].FCFS(preempt);
		}else if(scheduler.equals("SJF")){
			pcq[index].SJF(preempt);
		}else if(scheduler.equals("NonPPrS")){
			pcq[index].NonPPrS(preempt);
		}else if(scheduler.equals("RR")){
			if(noOfqueues==1){
				pcq[index].RR(quantum, false, preempt, index);
			}else{
				pcq[index].RR(quantum, true, preempt, index);
			}
		}else if(scheduler.equals("PrS")){
			pcq[index].PrS(preempt);
		}else if(scheduler.equals("SRTF")){
			pcq[index].SRTF(preempt);
		}
	}
	public void refresh(){
		f.add(qp,BorderLayout.SOUTH);
		f.repaint();
		f.revalidate();
	}
	public void refresh1(){
		f.add(qs,BorderLayout.CENTER);
		f.repaint();
		f.revalidate();
	}
	
	public static void main(String[] args){
		f = new JFrame("MLFQ");  
		JPanel p = new JPanel(new BorderLayout());  
		DefaultTableModel model = new DefaultTableModel();
		final JTable jt=new JTable(model);
		DefaultTableModel model1 = new DefaultTableModel();
		final JTable jt1=new JTable(model1);
		jt1.setEnabled(false);
		jt.setEnabled(false);
		model.addColumn("Process ID");
		model.addColumn("Arrival Time");
		model.addColumn("Burst Time");
		model.addColumn("Priority");
		
		model1.addColumn("Process ID");
		model1.addColumn("Remaining Burst Time");
		model1.addColumn("Turnaround Time");
		model1.addColumn("Waiting Time");
		
		JButton add= new JButton("Add Process");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowct= jt.getRowCount()+1;
				JTextField xField = new JTextField(5);
				JTextField yField = new JTextField(5);
				JTextField zField = new JTextField(5);
				

				JPanel myPanel = new JPanel();
				myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
				myPanel.add(new JLabel("Arrival Time:"));
				myPanel.add(xField);
				myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				myPanel.add(new JLabel("Burst Time:"));
				myPanel.add(yField);
				myPanel.add(new JLabel("Priority:"));
				myPanel.add(zField);
				int result = JOptionPane.showConfirmDialog(null, myPanel,
					"P"+rowct, JOptionPane.OK_CANCEL_OPTION);

				
				if (result == JOptionPane.OK_OPTION) {
					if(xField.getText().equals(null)||xField.getText().equals("")||yField.getText().equals(null)||yField.getText().equals("")||zField.getText().equals(null)||zField.getText().equals("")){
						
					}else{
						model.addRow(new Object[] {Integer.toString(rowct), xField.getText(), yField.getText(), zField.getText() });
					}
				} else {
					//System.out.println("Login canceled");
				}
			}
		});
		
		JButton clear= new JButton("Empty Table");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setRowCount(0);
			}
		});
		
		JButton addRandomProcess= new JButton("Add a Random Process");
		addRandomProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowct= jt.getRowCount()+1;
				int burst=1;
				int prio=1;
				do{
				burst= new Random().nextInt(11);
				prio= new Random().nextInt(11);
				}while(burst==0||prio==0);
				model.addRow(new Object[] {Integer.toString(rowct), Integer.toString(new Random().nextInt(11)), Integer.toString(burst),Integer.toString(prio) });
					
			}
		});
		
		
		JButton simulate= new JButton("Simulate");
		
		simulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(jt.getRowCount()!=0){
				JPanel myPanel = new JPanel();
				
				SpinnerModel spmodel = new SpinnerNumberModel(1, 1, 20, 1);		
				JSpinner js = new JSpinner(spmodel);
				myPanel.add(new JLabel("Enter no of queues: "), BorderLayout.WEST);
				myPanel.add(js, BorderLayout.CENTER);
				
				int result = JOptionPane.showConfirmDialog(null, myPanel,
					"Simulate", JOptionPane.OK_CANCEL_OPTION);
				
				if (result == JOptionPane.OK_OPTION) {
					myPanel = new JPanel();
					JScrollPane scrollPane = new JScrollPane(myPanel);
					scrollPane.setPreferredSize(new Dimension(300, 200));
					myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
					value = (Integer) js.getValue();
					schedAssign= new String[value];
					rrAssign= new int[value];
					priority= new int[value];
					responded= new boolean[value];
					time= new int[value];
					JLabel label[]= new JLabel[value];
					
					JSpinner jspin[] = new JSpinner[value];
					for(int i=0; i<value; i++){
						label[i]= new JLabel();
						myPanel.add(new JLabel("Q"+Integer.toString(i+1)));
						String schedAlgo[]={"FCFS", "SJF", "SRTF", "NonPPrS", "RR", "PrS"};        
						JComboBox<String> cb=new JComboBox<String>(schedAlgo);    
						cb.setBounds(50, 50, 90, 20);  
						SpinnerModel spmode = new SpinnerNumberModel(1, 1, 20, 1);		
						jspin[i] = new JSpinner(spmode);
						JPanel schedpan= new JPanel();
						schedpan.add(new JLabel("Sched:"), BorderLayout.WEST);
						schedpan.add(cb, BorderLayout.CENTER);
						myPanel.add(schedpan);
						JPanel prioritypan= new JPanel();
						prioritypan.add(new JLabel("Prio:"), BorderLayout.WEST);
						prioritypan.add(jspin[i], BorderLayout.CENTER);
						myPanel.add(label[i]);
						myPanel.add(prioritypan);
						
						
						schedAssign[i]="FCFS";
						rrAssign[i]=0;
						int index=i;
						cb.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent event) {
								@SuppressWarnings("unchecked")
								JComboBox<String> combo = (JComboBox<String>) event.getSource();
								String selected = (String) combo.getSelectedItem();
								schedAssign[index]=selected;
								if (selected.equals("RR")) {
									JPanel pan= new JPanel();
									SpinnerModel spmodel = new SpinnerNumberModel(0, 0, 20, 1);		
									JSpinner js = new JSpinner(spmodel);
									pan.add(js);
									
									int result = JOptionPane.showConfirmDialog(null, pan,
									"Enter quantum", JOptionPane.OK_CANCEL_OPTION);
									if (result == JOptionPane.OK_OPTION) {
										rrAssign[index] = (Integer) js.getValue();
										label[index].setText("Time Quantum: "+Integer.toString(rrAssign[index]));
									} else {
										//System.out.println("Login canceled");
									}
								}else{
									rrAssign[index] = 0;
									label[index].setText("");
								}
							}
						});
					}
					JOptionPane.showMessageDialog(f, scrollPane);
					
					for(int i=0; i<value; i++){
						priority[i]= (Integer) jspin[i].getValue();
					}
					int temp = 0;
					int rrtemp= 0;
					String schtemp="";
					for (int i = 0; i < value; i++) {
						for (int j = 1; j < (value - i); j++) {

							if (priority[j - 1] < priority[j]) {
								temp = priority[j - 1];
								rrtemp = rrAssign[j - 1];
								schtemp = schedAssign[j - 1];
								priority[j - 1] = priority[j];
								rrAssign[j - 1] = rrAssign[j];
								schedAssign[j - 1] = schedAssign[j];
								priority[j] = temp;
								rrAssign[j] = rrtemp;
								schedAssign[j] = schtemp;
							}

						}
					}
					JPanel prppan= new JPanel();
					prppan.setLayout(new FlowLayout());
					JRadioButton r1=new JRadioButton("Higher before lower");    
					JRadioButton r2=new JRadioButton("Fixed time slots");    
					r1.setBounds(75,50,100,30);    
					r2.setBounds(75,100,100,30);    
					ButtonGroup bg=new ButtonGroup();    
					bg.add(r1);bg.add(r2);    
					prppan.add(r1);
					prppan.add(r2);
					result = JOptionPane.showConfirmDialog(null, prppan,
					"PriorityPolicy", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						if(r1.isSelected()){    
							isFixedTimeSlots= false;  
						}else{
							isFixedTimeSlots= true;
							for(int i=0; i<value; i++){
								time[i]= priority[i]*8;
							}
						}
						pcb1= new ArrayList<PCB>();
						model1.setRowCount(0);
						process= new JLabel[jt.getRowCount()];
						there= new boolean[value][jt.getRowCount()];
						for (int i = 0; i < jt.getRowCount(); i++) {
							int info[]= new int[4];
							for (int j = 0; j < 4; j++) {
								info[j] = Integer.parseInt((String) jt.getValueAt(i, j));
							}
							
							 pcb1.add(new PCB(info[0],info[1],info[2],info[3]));
							 process[i]= new JLabel("P"+info[0]);
							 process[i].setBorder(new LineBorder(Color.BLACK, 2));
							 for(int m=0; m<value; m++){
								there[m][i]= false;
							 }
							 model1.addRow(new Object[] {Integer.toString(i+1), "", "", "" });
						}
						totalbursttime=0;
						z=0;						
						
						qs.removeAll();
						//f.remove(qs);
						qs.setLayout(new BoxLayout(qs, BoxLayout.Y_AXIS));
						//f.remove(qs);
						//JScrollPane spane=new JScrollPane(qs);
						//spane.removeAll();
						q= new JPanel[value];
						for(int j=0; j<value; j++){
							q[j]= new JPanel();
							q[j].setLayout(new FlowLayout());
							
							qs.add(q[j]);
						}
						noOfqueues= value;
						fin= false;
						
						new MLFQ().start();
						//f.repaint();
						//f.validate();
						for(int i=0; i< value; i++){
							int ind=i;
							new Thread() {
								public void run() {
									try{
									while(true){
										
										
										q[ind].removeAll();
										try {
										  sleep(100);
										} catch (InterruptedException e) {
										  throw new RuntimeException(e);
										}
										if(fin)
											break;
										//q[ind].add(new JLabel("Q"+(ind+1)));
										
										int l=pcq[ind].getPCB().size();
										ArrayList<PCB> pc= pcq[ind].getPCB();
										for(int i=0; i<l; i++){
											//JLabel lad= new JLabel("P"+Integer.toString(pc.get(i).getPid()));
											model1.setValueAt(pc.get(i).getBurst_time(), pc.get(i).getPid()-1, 1);
											
											model1.setValueAt(pc.get(i).getCompletion_time()-pc.get(i).getArrival_time(), pc.get(i).getPid()-1, 2);
											model1.setValueAt(pc.get(i).getCompletion_time()-pc.get(i).getArrival_time()-pc.get(i).getorigBurst_time(), pc.get(i).getPid()-1, 3);
											//lad.setBorder(new LineBorder(Color.BLACK, 2));
											//q[ind].add(lad);
											
										}
										/*
										for(int i=0; i<jt.getRowCount()-l; i++){
											JLabel lad= new JLabel("  ");
											lad.setBorder(new LineBorder(Color.BLACK, 2));
											q[ind].add(lad);
											
										}
										if(isFixedTimeSlots){
											q[ind].add(new JLabel(Integer.toString(time[ind])));
										}
										f.add(qs,BorderLayout.CENTER);
										*/
										f.repaint();
										f.revalidate();
										try {
										  sleep(100);
										} catch (InterruptedException e) {
										  throw new RuntimeException(e);
										}
										
									}
									}catch(Exception e){}
									
								}

							}.start();
						}
						
						
						
					} else {
						
					}
				} else {
				}
				
			}
			}
		});
		
        JScrollPane sp=new JScrollPane(jt);   
		//JScrollPane sp1=new JScrollPane(jt1);   
         
		p.add(sp, BorderLayout.CENTER);	
		JPanel b= new JPanel();
		b.add(add);
		b.add(simulate);
		b.add(clear);
		b.add(addRandomProcess);
		
		p.add(b, BorderLayout.SOUTH);		
		f.add(p, BorderLayout.WEST); 
		//f.add(sp1, BorderLayout.EAST); 	
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.setSize(new Dimension(1366, 600));
		f.setLocationRelativeTo(null);
        f.setVisible(true);  
		
	}
}