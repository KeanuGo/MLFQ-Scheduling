public class PCB{
	protected int arrival_time, burst_time, pid, wait_time, turnarround_time, priority, completion_time, origburst_time;
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
	public void setCompletion_time(int completion_time){
		this.completion_time = completion_time;
	}
	public int getCompletion_time(){
		return completion_time;
	}
	public void setTurnaround_time(int turnarround_time){
		this.turnarround_time = turnarround_time;
	}
	public int getTurnaround_time(){
		return turnarround_time;
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
	public int getWait_time(){
		return wait_time;
	}
	public void setWait_time(int wait_time) {
        this.wait_time = wait_time;
    }
	public int getPriority(){
		return priority;
	}
}