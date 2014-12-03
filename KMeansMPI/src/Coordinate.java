import java.util.LinkedList;
import java.util.List;


public class Coordinate implements Datapoint {
	public double x;
	public double y;
	
	public Coordinate(double x,double y)
	{
		this.x =x;
		this.y = y;
	}
	
	@Override
	public List<String> getValue() {
		List<String> result = new LinkedList<String>();
		result.add(Double.toString(x));
		result.add(Double.toString(y));
		return result;
	}
}
