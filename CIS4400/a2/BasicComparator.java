import java.util.*;

public class BasicComparator implements Comparator 
{ 
	public int compare (Object a1, Object a2)
	{
		double d1 = ((Double) a1).doubleValue();
		double d2 = ((Double) a2).doubleValue();

		if (d1 < d2)
		{
			return -1;
		}
		else 
		{
			return 1;
		}
	}
}

