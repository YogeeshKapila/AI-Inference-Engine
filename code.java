import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



public class code{
	
	static int no_of_query;
	static int no_of_sentences;
	static HashMap<String,ArrayList<ArrayList<Predicate_object>>> kb;
	static HashSet<clause_dnf> visited;

	static void process_sentence(String line,int i)
	{
		ArrayList<Predicate_object> sentence = new ArrayList<Predicate_object>();
		String[] s2;
		HashSet<String> preds_for_hashmap = new HashSet<String>();
		
		for(String s1 : line.split("\\|"))
		{
				s2 = s1.trim().split("\\(");
				String[] s3 = s2[1].trim().substring(0, s2[1].trim().length()-1).split(",");
				
				Predicate_object pr;
				
				pr = new Predicate_object(s2[0].trim(),s3,i);
				preds_for_hashmap.add(s2[0].trim());
				sentence.add(pr);
		}
		
		clause_dnf for_hashset = new clause_dnf(sentence);
		visited.add(for_hashset);
		
		for(String s : preds_for_hashmap)
		{
			if(!kb.containsKey(s))
			{
				ArrayList<ArrayList<Predicate_object>> sentences_in_hashmap = new ArrayList<ArrayList<Predicate_object>>();
				sentences_in_hashmap.add(sentence);
				kb.put(s,sentences_in_hashmap);
			}
			else
			{
			 kb.get(s).add(sentence);
			}
				
		}
		
	}
	
	public static String negation_of_input_query(String query)
	{
		if(query.charAt(0)=='~')
            return query.substring(1);
        else
            return "~"+query;
	}
	
	public static void hashmap_print(String m)
	{
		System.out.println();
		System.out.println(m);
		
		for(Map.Entry<String,ArrayList<ArrayList<Predicate_object>>> entry : kb.entrySet())
        {
        	String key = entry.getKey();
        	System.out.print(key+"------>");
        	for(ArrayList<Predicate_object> ap : entry.getValue())
        	{
        		System.out.print("[");
        		for(Predicate_object pob : ap)
        		{
        			System.out.print(pob.Predicate+" (");
        			for(String s : pob.variables)
        			{
        			System.out.print(s+",");
        			}
        			System.out.print(") ");
        		}
        		System.out.print("]");
        	}
        	System.out.println();
        }
	    
		
		System.out.println();
		System.out.println();
	}
	
	
	public static void hashset_print(HashSet<clause_dnf> ccc)
	{
		System.out.println();
		System.out.println();
		
		for(clause_dnf c : ccc)
        {
        	for(Predicate_object ap : c.clause)
        	{
        			System.out.print(ap.Predicate+" (");
        			for(String s : ap.variables)
        			{
        			System.out.print(s+",");
        			}
        			System.out.print(") ");
        		}
        	System.out.println();
        	}
        }
	
	public static boolean unify(ArrayList<Predicate_object> q_arg1,ArrayList<Predicate_object> arg2,HashSet<clause_dnf> vc)
	{
		
		for(Predicate_object query_objects : q_arg1)
		{	
		int ii = 0;	
		String search_for_resolution = negation_of_input_query(query_objects.Predicate);
		for(Predicate_object ps : arg2)
		{
			ii++;
			if(search_for_resolution.equals(ps.Predicate))
			{
				HashMap<String,String> unification_map = unification_args(query_objects.variables,ps.variables);
				if(unification_map == null)
				{
					if(ii == arg2.size())
					return false;
				}
				else
				{
					ArrayList<ArrayList<Predicate_object>> ans_s_q = new ArrayList<ArrayList<Predicate_object>>();
				    ans_s_q = replace_unify_mapping(unification_map,arg2,q_arg1);
				    
				    ans_s_q = merge_remove_after_unify(ans_s_q);
				    
				    if(ans_s_q.get(0).size() == arg2.size()+q_arg1.size())
				    	return false;
				   
				    if(ans_s_q.get(0).size()==0)
				    	return true;
				      
				    clause_dnf t = new clause_dnf(ans_s_q.get(0));
		
				    if(!vc.contains(t))
				    {
				    	vc.add(t);
				    	
				    	ArrayList<Predicate_object> query = new ArrayList<Predicate_object>();
				    	ArrayList<ArrayList<Predicate_object>> sentences_in_kb = new ArrayList<ArrayList<Predicate_object>>();
						query.addAll(ans_s_q.get(0));
						
						for(Predicate_object p : query)
						{	
							sentences_in_kb = kb.get(negation_of_input_query(p.Predicate));
							if(sentences_in_kb == null)
								continue;
						
						for(ArrayList<Predicate_object> s : sentences_in_kb)
						{
							if(unify(query,s,vc))
								return true;
						}
				    	
						}
				    	
				    	
				    }
				    else
				    {
				    	return false;
				    }
				}
			}
		}
		
		}
		return false;
	}
	
	public static ArrayList<ArrayList<Predicate_object>> merge_remove_after_unify(ArrayList<ArrayList<Predicate_object>> s1_s2)
	{
		ArrayList<Predicate_object> f = new ArrayList<Predicate_object>();
		ArrayList<ArrayList<Predicate_object>> f1 = new ArrayList<ArrayList<Predicate_object>>();
		
		HashSet<Predicate_object> merged_query = new HashSet<Predicate_object>(s1_s2.get(0));
		merged_query.addAll(s1_s2.get(1));
		HashSet<Predicate_object> sentence = new HashSet<Predicate_object>(s1_s2.get(0));
		HashSet<Predicate_object> query = new HashSet<Predicate_object>(s1_s2.get(1));
		Predicate_object temp1;
		
		for(Predicate_object ps : sentence)
		{
			String temp = negation_of_input_query(ps.Predicate);
		    temp1 = new Predicate_object(temp,ps.variables.toArray(new String[ps.variables.size()]));
			if(query.contains(temp1))
			{
				merged_query.remove(temp1);
				merged_query.remove(ps);
			}
			
		}
		
		for(Predicate_object t : merged_query)
		{
			f.add(t);
		}
		f1.add(f);
		
		return f1;
		
		
	}
	
	public static ArrayList<ArrayList<Predicate_object>> replace_unify_mapping(HashMap<String,String> unification_map,ArrayList<Predicate_object> sentence_kb,ArrayList<Predicate_object> query)
	{
		ArrayList<Predicate_object> sen = new ArrayList<Predicate_object>();
		
		for(Predicate_object temp: sentence_kb)
		{
			Predicate_object o = new Predicate_object(temp);
			sen.add(o);
		}
		
		ArrayList<Predicate_object> que = new ArrayList<Predicate_object>();
		
		for(Predicate_object temp: query)
		{
			Predicate_object o = new Predicate_object(temp);
			que.add(o);
		}
		
		
		String replacement;
		
		for(Predicate_object s1 : sen)
		{
			for(int i=0;i<s1.variables.size();i++)
			{
			    replacement = unification_map.get(s1.variables.get(i));
				if(replacement!=null)
				s1.variables.set(i, replacement);
			}
		}
		
		for(Predicate_object s2 : que)
		{
			for(int i=0;i<s2.variables.size();i++)
			{
				replacement = unification_map.get(s2.variables.get(i));
				if(replacement!=null)
				s2.variables.set(i, replacement);
			}
		}
		
		ArrayList<ArrayList<Predicate_object>> s1_s2 = new ArrayList<ArrayList<Predicate_object>>();
		s1_s2.add(sen);
		s1_s2.add(que);
		
		return s1_s2;	
	}
	
	
	public static HashMap<String,String> unification_args(ArrayList<String> arg1, ArrayList<String> arg2)
	{
		HashMap<String,String> unify_mapping = new HashMap<String,String>();
		
		for(int i=0;i<arg1.size();i++)
		{
			String one = arg1.get(i);
			String two = arg2.get(i);
			if(one.equals(two))
			{
				continue;
			}
			else
			{
				if(one.charAt(0)>='a' && one.charAt(0)<='z' && two.charAt(0) >= 'A' && two.charAt(0) <= 'Z')
				{
					unify_mapping.put(one, two);
				}
				else if(one.charAt(0) >= 'A' && one.charAt(0) <= 'Z' && two.charAt(0) >= 'a' && two.charAt(0) <= 'z')
				{
					unify_mapping.put(two, one);
				}
				else if(one.charAt(0) >= 'a' && one.charAt(0) <= 'z' && two.charAt(0) >= 'a' && two.charAt(0) <= 'z')
				{
					unify_mapping.put(one, two);
				}
				else
				{
					unify_mapping = null;
					break;
				}
			}
		}
		
		return unify_mapping;
		
	}
	
	public static String resolve(String line)
	{
		ArrayList<ArrayList<Predicate_object>> sentences_in_kb = new ArrayList<ArrayList<Predicate_object>>();
		
		String[] s1 = line.split("\\(");
		String[] s2 = s1[1].trim().substring(0, s1[1].trim().length()-1).split(",");
		
		Predicate_object q_pr;
		String q_predicate_for_search = s1[0].trim();
		String q_predicate_insert_kb = negation_of_input_query(q_predicate_for_search);
		
		q_pr = new Predicate_object(q_predicate_insert_kb,s2);
		
		HashSet<clause_dnf> visited_clone = new HashSet<clause_dnf>();
		for(clause_dnf cd : visited)
		{
			clause_dnf temp = new clause_dnf(cd);
			visited_clone.add(temp);
		}
		
		//ADD NEGATION TO KB
		
			ArrayList<Predicate_object> temp1 = new ArrayList<Predicate_object>();
			temp1.add(q_pr);
			clause_dnf t = new clause_dnf(temp1);
			visited_clone.add(t);
			
			ArrayList<ArrayList<Predicate_object>> temp;
			boolean single = false;
			
			if(!(kb.containsKey(q_predicate_insert_kb)))
			{
				temp = new ArrayList<ArrayList<Predicate_object>>();
				temp.add(temp1);
				kb.put(q_predicate_insert_kb, temp);
				single = true;
			}
			else
			{
				temp = kb.get(q_predicate_insert_kb);
				temp.add(temp1);
				single = false;
			}
			
		ArrayList<Predicate_object> query = new ArrayList<Predicate_object>();
		query.add(q_pr);
		
		sentences_in_kb = kb.get(q_predicate_for_search);
		boolean found = false;
		
		if(sentences_in_kb == null)
		{
			return "FALSE";
		}
			
		try
		{
		for(ArrayList<Predicate_object> s : sentences_in_kb)
		{
			boolean check = unify(query,s,visited_clone);
			if(check)
			{ 
				found = true;
				if(single)
				{													//REMOVE NEGATION FROM KB
				kb.remove(q_predicate_insert_kb);
				}
				else
				{
				temp.remove(temp.size()-1);
				}
				return("TRUE");
			}
		}
			
			if(!found)
			{
					if(single)
					{
					kb.remove(q_predicate_insert_kb);
					}												//REMOVE NEGATION FROM KB
					else
					{
					temp.remove(temp.size()-1);
					}	
					return("FALSE");	
			}
		}
		catch(StackOverflowError e)
		{
			return "FALSE";
		}
		return "FALSE";
	}
	
	
	public static void main(String[] args) {
	
		String finput = "input.txt";
		String foutput = "output.txt";
		BufferedReader input = null;
		FileReader fr = null;
		FileWriter fw =null;
		PrintWriter po = null;
		homework h = new homework();
		
		try
		{
			fr = new FileReader(finput);
			input = new BufferedReader(fr);
            fw = new FileWriter(foutput);
            po = new PrintWriter(fw);
          
            String line;
            line = input.readLine().trim();
            no_of_query = Integer.parseInt(line);
            String[] queries = new String[no_of_query];
            for(int i=0;i<no_of_query;i++)
            {
            	line = input.readLine().trim();
            	queries[i] = line.trim();
            }
           
            line = input.readLine().trim();
            no_of_sentences = Integer.parseInt(line);
            
            kb = new HashMap<String,ArrayList<ArrayList<Predicate_object>>>();
            visited = new HashSet<clause_dnf>();
            for(int i=0;i<no_of_sentences;i++)
            {
            	line = input.readLine().trim();
            	process_sentence(line.trim(),i);

            }
            
            for(String s : queries)
            {
            	String m = resolve(s);
            	
            	po.printf("%s%n",m);
            	
            	//System.out.println(m);
            }
    		
            po.close();
    		fr.close();
    		fw.close();	
            
            
		}
		
		

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}