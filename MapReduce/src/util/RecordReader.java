package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RecordReader {
	
	private List<RandomAccessFile> fileReader;
	private int recordLength;
	private boolean ismap;
	
	public RecordReader(List<RandomAccessFile> f, int length, boolean t)
	{
		fileReader = f;
		recordLength = length;
		ismap = t;
	}
	
	public Map<String,List<String>> getKeyValuePairs () {
		System.out.println("Getting Key Value Pairs");
		Map<String,List<String>> pairs = new HashMap<String,List<String>>();
		int numLines = Configuration.mapPartitionSize;
		try {
			for(RandomAccessFile reader: fileReader)
			{
				for(int i = 0; i < numLines; i++)
				{	
					if(ismap)
					{
						String record = reader.readLine();
						List<String> values = pairs.get(record);
						if(values == null)
						{
							values = new LinkedList<String>();
						}
						values.add(record);
						pairs.put(record,values);
					}
					else
					{
						String record = reader.readLine();
						if(record != null){
							int index = record.indexOf("|");
							String key = record.substring(0,index);
							String value = record.substring(index+1,record.length());
							List<String> values = pairs.get(key);
							if(values == null)
							{
								values = new LinkedList<String>();
							}
							values.add(value);
							pairs.put(key,values);
						}
					}
				}
			}
			System.out.println("Got Key Value Pairs");
			return pairs;
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
