import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



class clause_dnf
 {
	 ArrayList<Predicate_object> clause;
	 
	static void print(ArrayList<Predicate_object> c)
	{
		for(Predicate_object p: c)
		{
			System.out.print(p.Predicate+"  ");
			for(String vars : p.variables)
				System.out.print(vars+",");
		}
	}
	 
	 
	 clause_dnf(ArrayList<Predicate_object> a)
	 {
		 clause = new ArrayList<Predicate_object>();
		 clause.addAll(a);
	 }
	 
	 clause_dnf(clause_dnf c1)
	 {
		 clause = new ArrayList<Predicate_object>();
		 for(Predicate_object p : c1.clause)
		 {
			 Predicate_object temp = new Predicate_object(p);
			 clause.add(temp);
		 }
	 }
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clause == null) ? 0 : clause.hashCode());
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
		if (!(obj instanceof clause_dnf)) {
			return false;
		}
		clause_dnf other = (clause_dnf) obj;
		if (clause == null) {
			if (other.clause != null) {
				return false;
			}
		} else if (!clause.equals(other.clause)) {
			return false;
		}
		else 
		{
			if(clause.size()!= other.clause.size())
			{
				return false;
			}
			else if(clause.size()==other.clause.size())
			{
				for(int j=0;j<clause.size();j++)
				{
					Predicate_object t = clause.get(j);
					Predicate_object t1 = other.clause.get(j);
					if(!(t.equals(t1)))
						return false;
				}	
			}	
		}
			
		return true;
	}
 }