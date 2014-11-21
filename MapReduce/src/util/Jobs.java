package util;

import java.util.LinkedList;
import java.util.List;


public abstract class Jobs {
	private Integer stat;
	private int id;
	private List<Tasks> allTasks;
	private List<Tasks> queueTasks;
	private List<Tasks> runningTasks;
	private int numFailures;
	private String job;
	
	//Status = 3 means it started
	//Status = 2 means it is running
	//Status = 1 means it finished successfully
	//Status = 0 means it finished due to a kill
	
	public Jobs(String j)
	{
		allTasks = new LinkedList<Tasks>();
		queueTasks = new LinkedList<Tasks>();
		runningTasks = new LinkedList<Tasks>();
		numFailures = 0;
		job = j;
	}
	public int getStatus()
	{
		return stat;
	}
	public void setStatus(int status)
	{
		stat = status;
	}
	public List<Tasks> getTasks()
	{
		return allTasks;
	}
	public void setTasks(List<Tasks> taskList)
	{
		for(Tasks t: taskList)
		{
			//If queue
			queueTasks.add(t);
			//else
			allTasks.add(t);
		}
	}
	public List<Tasks> runningTasks()
	{
		return runningTasks;
	}
	public List<Tasks> getQueueTasks()
	{
		return queueTasks;
	}
	public int updateTasks(Tasks task)
	{
		int taskStatus = task.getStatus();
		if(taskStatus == 2)
		{
			queueTasks.remove(task);
			runningTasks.add(task);
			if(stat == null)
			{
				stat = 3;
				return 2;
			}
			else if(stat == 3)
			{
				stat = 2;
			}
			return 1;
		}
		else
		{	
			runningTasks.remove(task);
			if(taskStatus == 1)
			{
				if(queueTasks.isEmpty()&&runningTasks.isEmpty()){
					if(allTasks.isEmpty())
					{
						stat = 1;
						return 0;
					}
					queueTasks = allTasks;
				}
				return 1;
			}
			else if(taskStatus == -1)
			{
				queueTasks.add(task);
				if(numFailures < 1)
					return -1;
				return -2;
			}
			else if(taskStatus == 0){
				if(runningTasks.isEmpty())
				{
					stat = 0;
					return 0;
				}
				queueTasks = null;
				allTasks = null;
				return 1;
			}
			return -2;
		}
	}
	public Integer getID(){
		return id;
	}
	public void setID(int ID){
		id = ID;
	}
	
	public String toString()
	{
		return job;
	}
}
