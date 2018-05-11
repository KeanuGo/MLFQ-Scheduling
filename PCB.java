public class PCB{
	protected int arrival_time, burst_time, pid, priority, origburst_time;
	public boolean finish= false;
	public PCB(int pid, int arrival_time, int burst_time, int priority){
		this.pid= pid;
		this.arrival_time= arrival_time;
		this.burst_time= burst_time;
		origburst_time= burst_time;
		this.priority= priority;
	}
	public int getBurst_time(){
		return burst_time;
	}
	public int getorigBurst_time(){
		return origburst_time;
	}
	public void setBurst_time(int burst_time){
		this.burst_time = burst_time;
	}
	public int getArrival_time(){
		return arrival_time;
	}
	public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
	public int getPriority(){
		return priority;
	}
}
