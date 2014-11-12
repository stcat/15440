import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class MasterConnection {
	public String slaveAddr;
	public int port;
	public ServerSocket serverSoc;
	public MasterCoordinator coord;
	
	public MasterConnection(String sladdr, int masterListenPort, MasterCoordinator coordinator)
	{
		slaveAddr = sladdr;
		port = masterListenPort;
		coord = coordinator;
	}
	
	public void start()
	{
		serverSoc = null;
		try {
			serverSoc = new ServerSocket(port);
			serverSoc.setSoTimeout(Configuration.timeout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run();
	}
	
	public void run()
	{
		boolean run = true;
		while(run)
		{
			Socket s;
			try {
				s = serverSoc.accept();
				ObjectInputStream in;
				ObjectOutputStream out;
				in = new ObjectInputStream(s.getInputStream());
				out = new ObjectOutputStream(s.getOutputStream());
				Message msg = (Message) in.readObject();
				Message outMsg = processMessage(msg);
				if(outMsg != null)
				{
					out.writeObject(outMsg);
				}
				in.close();
				out.close();
				s.close();
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessage(Message msg)
	{
		try {
			Socket s = new Socket(slaveAddr, Configuration.slaveListenPort);
			ObjectInputStream in;
			ObjectOutputStream out;
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(msg);
			out.flush();
			Message inMsg = (Message)in.readObject();
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Sent when a new Job is received or when looking for a timeout
	public Message sendAck()
	{
		Message resp = new Message();
		resp.setType('a');
		return resp;
	}
	
	public Message processMessage(Message msg)
	{
		if(msg.getType()=='a')
		{
			//Response to a timeout check
			return null;
		}
		else if(msg.getType()=='f')
		{
			//Task Finished
			coord.acknowledgeFinishedTask(msg.getTask());
			return sendAck();
		}
		else if(msg.getType()=='r')
		{
			//Task is Running
			coord.acknowledgeRunningTask(msg.getTask());
			return sendAck();
		}
		else if(msg.getType()=='c')
		{
			return sendAck();
		}
		else if(msg.getType()=='n')
		{
			//New Job issued
			return sendAck();
		}
		else{
			return null;
		}
		
	}
	
	
	
	
}
