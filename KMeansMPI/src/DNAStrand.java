import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


public class DNAStrand implements Datapoint {
	private char[] strand;
	public DNAStrand(String DNA)
	{
		 strand = DNA.toCharArray();
	}
	@Override
	public List<String> getValue() {
		
		List<String> res = new ArrayList<String>();
		for(int i = 0; i<strand.length;i++)
		{
			res.add(String.valueOf(strand[i]));
		}
		return res;
	}
	
	
}
