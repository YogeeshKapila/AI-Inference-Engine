import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



class Predicate_object{
	
	String Predicate;
	ArrayList<String> variables;
	
	Predicate_object(Predicate_object o1)
	{
		this.Predicate = o1.Predicate;
		this.variables = new ArrayList<String>();
		this.variables.addAll(o1.variables);
	}
	
	
	Predicate_object(String pred, String[] strings,int i)
	{
		Predicate = pred;
	    variables = new ArrayList<String>();
		for(String s : strings)
		{
			s = s.trim();
			if(s.charAt(0) - 'A' <0 || s.charAt(0) - 'A' >25)
			{
				variables.add(s+i);
			}
			else
			variables.add(s);
		}
	}
	
	Predicate_object(String pred, String[] strings)
	{
		Predicate = pred;
		variables = new ArrayList<String>();
		for(String s : strings)
			variables.add(s.trim());
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Predicate == null) ? 0 : Predicate.hashCode());
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Predicate_object)) {
			return false;
		}
		Predicate_object other = (Predicate_object) obj;
		if (Predicate == null) {
			if (other.Predicate != null) {
				return false;
			}
		} else if (!Predicate.equals(other.Predicate)) {
			return false;
		}
		if (variables == null) {
			if (other.variables != null) {
				return false;
			}
		} else if (!variables.equals(other.variables)) {
			return false;
		}
		
		else if(variables.size() != other.variables.size())
			return false;
		else if(variables.size()==other.variables.size())
		{
			for(int k = 0;k<this.variables.size();k++)
			{
				if(Character.isUpperCase(this.variables.get(k).charAt(0)) && !Character.isUpperCase(other.variables.get(k).charAt(0)))
					return false;
				else if(!Character.isUpperCase(this.variables.get(k).charAt(0)) && Character.isUpperCase(other.variables.get(k).charAt(0)))
					return false;
				else if(Character.isUpperCase(this.variables.get(k).charAt(0)) && Character.isUpperCase(other.variables.get(k).charAt(0)))
				{
					if(!(this.variables.get(k).equals(other.variables.get(k))))
						return false;
				}
			}
		}
		return true;
	}
	
	}