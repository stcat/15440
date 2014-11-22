package util;

import java.util.LinkedList;
import java.util.List;


public abstract class Jobs implements serializable {
	private Integer stat;
	private int id;
	private List<Tasks> allTasks;
	private List<Tasks> queueTasks;
	private List<Tasks> runningTasks;
	private int numFailures;
	private String job;
	private String inputFile;
	private List<String> outFiles;
	
	
	//Status = 3 means it started
	//Status = 2 means it is running
	//Status = 1 means it finished successfully
	//Status = 0 means it finished due to a kill
	
	public Jobs()
	{
		allTasks = new LinkedList<Tasks>();
		queueTasks = new LinkedList<Tasks>();
		runningTasks = new LinkedList<Tasks>();
		numFailures = 0;
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
			if(t.getStatus() < 3)
			{
				t.setStatus(3);
				queueTasks.add(t);
			}
			else{
				allTasks.add(t);
			}
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
					return 3;
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
	public void setString(String j)
	{
		job = j;
	}
	public String toString()
	{
		return job;
	}
	public void setInputFile(String in)
	{
		inputFile = in;
	}
	public String getInputFile()
	{
		return inputFile;
	}
	public void setOutputFiles(List<String> out)
	{
		outFiles = out;
	}
	public List<String> getOutputFiles()
	{
		return outFiles;
	}
	
	
	public abstract int getRecordSize();
	public abstract void map(String key, String value, OutputCollector<String,String> output);
	public abstract void reduce(String key, List<String> values, OutputCollector<String,String> output);
}
