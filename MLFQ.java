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
	
	static int[] startTime;
	static int[] completionTime;
	static int[] burstTime;
	static int[] arrivalTime;
	
	static boolean[] started;
	
	static boolean st= false;
	static int z=0;
	boolean finish=false;
	static boolean isFixedTimeSlots= false;
	static PCBQueue pcq[];
	PCBQueue pcqinit;
	static JPanel qs= new JPanel();;
	static JPanel qp= new JPanel();;
	static JPanel cp= new JPanel();;
	static int artime=0;
	int ditime[];
	static int time[];
	static int clock= 0;
	String scheduler[];
	static int totalbursttime=0;
	static int value=0;
	static int start=0;
	static int add=0;
	static int noOfqueues;
	static int befre=500;
	static int qbefre=500;
	static JFrame f;
	static boolean fin=false;
	static boolean responded[];
	static JLabel process[];
	static DefaultTableModel model1;
	static int rct;
	public MLFQ(){
		
		
	}
	public void run() {
		clock= 0;
		st= false;
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
					if(qbefre!=point)
						qp.add(new JLabel(" Q"+(point+1)+" "));
					qbefre=point;
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
						qp.add(lad);
						
						refresh();
						if(!started[pr.getPid()-1])
							startTime[pr.getPid()-1]= befre;
						started[pr.getPid()-1]=true;
						
						pcq[point].rm(pr);
						if(pr.getBurst_time()>0){
							if(schedAssign[point].equals("RR")&&noOfqueues!=1){
								System.out.println("Hey");	
								pcq[point+1].insert(pr);
							}
							pcq[point].rm(pr);
						}
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							timer(befre);
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						completionTime[pr.getPid()-1]= befre;
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
						completionTime[pr.getPid()-1]= befre;
					}
				}else{
					
					point= emp%(noOfqueues-1);
					
					exec(schedAssign[point], point, rrAssign[point], true);
					if(pcq[point].getSize()==0){
						emp++;
					}
					if(qbefre!=point)
						qp.add(new JLabel(" Q"+(point+1)+" "));
					qbefre=point;
					int bef;
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						timer(befre);
						qp.add(new JLabel(Integer.toString(bef=pcq[point].dequeue1())));
						
					}else{
						bef= pcq[point].dequeue1();
					}
					int siz= pcq[point].getSize();
					for(int j=0; j<siz; j++){
						PCB pr;
						
						JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
						lad.setBorder(new LineBorder(Color.BLACK, 2));
						qp.add(lad);
						refresh();
						if(!started[pr.getPid()-1])
							startTime[pr.getPid()-1]= befre;
						started[pr.getPid()-1]=true;
						pcq[point].rm(pr);
						if(pr.getBurst_time()>0){
							pcq[point].rm(pr);
							if(schedAssign[point].equals("RR")&& rrAssign[point]==(pcq[point].show1()-bef)){
								pcq[(point+1)%noOfqueues].insert(pr);
							}else{
								pcq[point].insert(pr);
							}
							rTime[point]= pcq[point].show1()-bef;
							
						}
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							timer(befre);
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						completionTime[pr.getPid()-1]= befre;
					}
					
					point=0;
					
				}
			}
			for(int i=1; i<noOfqueues-1; i++){
				point++;
				exec(schedAssign[point], point, rrAssign[point], false);
				if(qbefre!=point)
					qp.add(new JLabel(" Q"+(point+1)+" "));
				qbefre=point;
				if(befre!=pcq[point].show1()){
					befre=pcq[point].show1();
					timer(befre);
					qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
					
				}else{
					pcq[point].dequeue1();
				}
				
				int siz= pcq[point].getSize();
				for(int j=0; j<siz; j++){
					PCB pr;
					JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
					lad.setBorder(new LineBorder(Color.BLACK, 2));
					qp.add(lad);
					refresh();
					if(!started[pr.getPid()-1])
						startTime[pr.getPid()-1]= befre;
					started[pr.getPid()-1]=true;
					pcq[point].rm(pr);
					if(pr.getBurst_time()>0){
						if(schedAssign[point].equals("RR")&&noOfqueues!=1){
							pcq[point+1].insert(pr);
						}
						pcq[point].rm(pr);
					}
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						timer(befre);
						qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
						
					}else{
						pcq[point].dequeue1();
					}
					completionTime[pr.getPid()-1]= befre;
				}
			}
			point=(point+1)%noOfqueues;
			if(noOfqueues==1){
			
			}else{
				exec(schedAssign[point], point, rrAssign[point], false);
			}
			if(qbefre!=point)
				qp.add(new JLabel(" Q"+(point+1)+" "));
			qbefre=point;
			System.out.println("Yo"+pcq[point].getSize());
			if(pcq[point].getSize1()>0){
				if(befre!=pcq[point].show1()){
					if(schedAssign[point].equals("RR")){
						befre=pcq[point].show1();
						timer(befre);
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
				qp.add(lad);
				refresh();
				if(!started[pr.getPid()-1])
					startTime[pr.getPid()-1]= befre;
				started[pr.getPid()-1]=true;
				
				if(befre!=pcq[point].show1()){
					befre=pcq[point].show1();
					timer(befre);
					qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
					
				}else{
					pcq[point].dequeue1();
				}
				completionTime[pr.getPid()-1]= befre;
				if(pcq[point].getSize()>0){
					if(pcq[point].show().getArrival_time()==pcq[point].show1()){
						int c=0;
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							timer(befre);
							qp.add(new JLabel(Integer.toString(c=pcq[point].dequeue1())));
							System.out.println("SS"+c);	
						}else{
							pcq[point].dequeue1();
						}
					}
				}
				completionTime[pr.getPid()-1]= befre;
			}
		}else{
			ArrayList<PCB> rem= new ArrayList<PCB>();
			int now=0;
			start=0;
			int tm= time[0];
			if(!schedAssign[0].equals("RR")){
				//z=now;
				//pcqinit= new PCBQueue(pcb1);
				st= true;
				while(true){
					//pcqinit= new PCBQueue(pcb1);
					
					
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
						while(pcqinit.getSize()>0){
							pcq[point].insert(pcqinit.dequeue());
						}
						if(schedAssign[point].equals("SJF")||schedAssign[point].equals("NonPPrS")||schedAssign[point].equals("FCFS")){
							artime=tm;
							exec(schedAssign[point], point, rrAssign[point], true);
							now=z;
							pcqinit= new PCBQueue(pcb1);
							//if(z>=artime)
						tm= tm+(time[point]);
						}else{
							exec(schedAssign[point], point, rrAssign[point], false);
						}
						
						if(qbefre!=point)
							qp.add(new JLabel(" Q"+(point+1)+" "));
						qbefre=point;
						
						
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							timer(befre);
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						int siz= pcq[point].getSize();
						for(int j=0; j<siz; j++){
							if(schedAssign[point].equals("SJF")||schedAssign[point].equals("NonPPrS")||schedAssign[point].equals("FCFS")){
							}else{
								artime=tm;
							}
							PCB pr;
							
							JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
							lad.setBorder(new LineBorder(Color.BLACK, 2));
							qp.add(lad);
							refresh();
							if(!started[pr.getPid()-1])
								startTime[pr.getPid()-1]= befre;
							started[pr.getPid()-1]=true;
							
							pcq[point].rm(pr);
							if(pr.getBurst_time()<=0){
								pcqinit.rm(pr);
								rem.add(pr);
							}
								
							if(pr.getBurst_time()>0){
								if(schedAssign[point].equals("SJF")||schedAssign[point].equals("NonPPrS")||schedAssign[point].equals("FCFS")){
									pcq[point].insert(pr);
									pcqinit.update(pr);
								}else{
									while(pcq[point].show1()>artime){
										timer(artime);
										qp.add(new JLabel(Integer.toString(artime)));
										for(int i=1; i<noOfqueues; i++){
											if(qbefre!=point)
												qp.add(new JLabel(" Q"+(i+1)+" "));
											qbefre=i;
											refresh();
										}
										if(qbefre!=point)
											qp.add(new JLabel(" Q"+(1)+" "));
										qbefre=1;
										refresh();
										lad= new JLabel("P"+ pr.getPid());
										lad.setBorder(new LineBorder(Color.BLACK, 2));
										qp.add(lad);
										refresh();
										if(!started[pr.getPid()-1])
											startTime[pr.getPid()-1]= befre;
										started[pr.getPid()-1]=true;
										tm= tm+(time[point]);
										artime+= (time[point]);
									}
								}
							}
							if(befre!=pcq[point].show1()){
								befre=pcq[point].show1();
								timer(befre);
								qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
								
							}else{
								pcq[point].dequeue1();
							}
							completionTime[pr.getPid()-1]= befre;
						}
						if(schedAssign[point].equals("SJF")||schedAssign[point].equals("NonPPrS")||schedAssign[point].equals("FCFS")){
						}else{
							break;
						}
						if(z==totalbursttime+start){
							break;
						}
					}
					for(int m=0; m<rem.size(); m++){
						pcqinit.rm(rem.get(m));
					}
					if(schedAssign[point].equals("SJF")||schedAssign[point].equals("NonPPrS")||schedAssign[point].equals("FCFS")){
					pcq[point].reset();
					pcq[point].removeA();
					pcqinit.FCFS(false);
					z=artime;
					}
				}
			}else{
				tm=pcqinit.show().getArrival_time();
				while(true){
					int prv= tm;
					tm+=time[point];
					if(point==0 && pcqinit.getSize()>0){
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
						
							while(pcqinit.show().getArrival_time()<=tm){
								System.out.println("K"+tm);
								pcq[point].insert(pcqinit.dequeue());
								if(pcqinit.getSize()<1){
									break;
								}
							}
						
					}
					artime=tm;
					
					exec(schedAssign[point], point, rrAssign[point], true);
					if(qbefre!=point)
						qp.add(new JLabel(" Q"+(point+1)+" "));
					qbefre=point;
					int siz= pcq[point].getSize();
					if(siz==0){
						tm= prv;
					}
					int ber;
					if(befre!=pcq[point].show1()){
						befre=pcq[point].show1();
						qp.add(new JLabel(Integer.toString(ber=pcq[point].dequeue1())));
						
					}else{
						ber= pcq[point].dequeue1();
					}
					System.out.println("Hej"+point);
					
					for(int j=0; j<siz; j++){
						PCB pr;
						
						JLabel lad= new JLabel("P"+ (pr=pcq[point].dequeue()).getPid());
						lad.setBorder(new LineBorder(Color.BLACK, 2));
						qp.add(lad);
						refresh();
						if(!started[pr.getPid()-1])
							startTime[pr.getPid()-1]= befre;
						started[pr.getPid()-1]=true;
						pcq[point].rm(pr);
						if(pr.getBurst_time()>0){
							
							if(schedAssign[point].equals("RR")){
								//if((pcq[point].show1()-ber)==rrAssign[point]){
									pcq[point].rm(pr);
									pcq[(point+1)%(noOfqueues)].insert(pr);
									System.out.println("Me");
								
							
							}else{
								pcq[point].rm(pr);
								if(point!=0 && j==(siz-1)){
									pcq[point-1].insert(pr);
								}else{
									pcq[point].insert(pr);
								}
							}
						}
						if(befre!=pcq[point].show1()){
							befre=pcq[point].show1();
							timer(befre);
							qp.add(new JLabel(Integer.toString(pcq[point].dequeue1())));
							
						}else{
							pcq[point].dequeue1();
						}
						if(pcq[point].getSize()>0){
							if(pcq[point].show().getArrival_time()==pcq[point].show1()){
								int c=0;
								if(befre!=pcq[point].show1()){
									befre=pcq[point].show1();
									timer(befre);
									qp.add(new JLabel(Integer.toString(c=pcq[point].dequeue1())));
									System.out.println("SS"+c);	
								}else{
									pcq[point].dequeue1();
								}
							}
						}
						completionTime[pr.getPid()-1]= befre;
						if(z==totalbursttime+start+add){
							break;
						}
					}
					if(z==totalbursttime+start+add){
						break;
					}
					tm=z;
					point=(point+1)%noOfqueues;
				}
			}
		}
		refresh();
		int sumresponse= 0, sumwaiting= 0;
		for(int i=0; i<rct; i++){
			model1.setValueAt(startTime[i]-arrivalTime[i], i, 1);
			sumresponse+= (startTime[i]-arrivalTime[i]);
			model1.setValueAt(completionTime[i]-arrivalTime[i], i, 2);
			model1.setValueAt(completionTime[i]-arrivalTime[i]-burstTime[i], i, 3);
			sumwaiting+= (completionTime[i]-arrivalTime[i]-burstTime[i]);
		}
		model1.addRow(new Object[] {"Ave. Response Time", ":", Float.toString((float)sumresponse/rct), "" });
		model1.addRow(new Object[] {"Ave. Waiting Time", ":", Float.toString((float)sumwaiting/rct), "" });
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
		//f.add(qp,BorderLayout.SOUTH);
		f.repaint();
		f.revalidate();
	}
	public void timer(int befre){
		while(clock<befre){
			try {
			  sleep(1000);
			} catch (InterruptedException e) {
			  throw new RuntimeException(e);
			}
			clock++;
			cp.removeAll();
			cp.add(new JLabel(Integer.toString(clock)));
			refresh();
		}
	}
	
	public static void main(String[] args){
		f = new JFrame("MLFQ");  
		JPanel p = new JPanel(new BorderLayout());  
		DefaultTableModel model = new DefaultTableModel();
		final JTable jt=new JTable(model);
		model1 = new DefaultTableModel();
		final JTable jt1=new JTable(model1);
		jt1.setEnabled(false);
		jt.setEnabled(false);
		model.addColumn("Process ID");
		model.addColumn("Arrival Time");
		model.addColumn("Burst Time");
		model.addColumn("Priority");
		
		model1.addColumn("Process ID");
		model1.addColumn("Response Time");
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
				burst= new Random().nextInt(51);
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
						startTime= new int[jt.getRowCount()];
						completionTime= new int[jt.getRowCount()];
						started= new boolean[jt.getRowCount()];
						burstTime= new int[jt.getRowCount()];
						arrivalTime= new int[jt.getRowCount()];
						rct= jt.getRowCount();
						for (int i = 0; i < jt.getRowCount(); i++) {
							int info[]= new int[4];
							for (int j = 0; j < 4; j++) {
								info[j] = Integer.parseInt((String) jt.getValueAt(i, j));
							}
							
							 pcb1.add(new PCB(info[0],info[1],info[2],info[3]));
							 process[i]= new JLabel("P"+info[0]);
							 process[i].setBorder(new LineBorder(Color.BLACK, 2));
							 burstTime[i]= info[2];
							 arrivalTime[i]= info[1];
							 model1.addRow(new Object[] {Integer.toString(i+1), "", "", "" });
						}
						totalbursttime=0;
						z=0;						
						noOfqueues= value;
						fin= false;

						new MLFQ().start();
					} else {
						
					}
				} else {
				}
				
			}
			}
		});
		
        JScrollPane sp=new JScrollPane(jt);   
		JScrollPane sp1=new JScrollPane(jt1);
		JScrollPane sp2=new JScrollPane(qp);   
         
		p.add(sp, BorderLayout.CENTER);	
		JPanel b= new JPanel();
		b.add(add);
		b.add(simulate);
		b.add(clear);
		b.add(addRandomProcess);
		
		p.add(b, BorderLayout.SOUTH);
		cp.setLayout(new BorderLayout());
		
		cp.add(new JLabel(Integer.toString(clock)), BorderLayout.CENTER);
		f.add(cp, BorderLayout.WEST); 
		f.add(p, BorderLayout.CENTER); 
		sp2.setPreferredSize(new Dimension(50,50));
		f.add(sp2,BorderLayout.SOUTH);
		f.add(sp1, BorderLayout.EAST); 	
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.setSize(new Dimension(1366, 600));
		f.setLocationRelativeTo(null);
        f.setVisible(true);  
		
	}
}
