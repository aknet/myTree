import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.*;
import org.gedcom4j.parser.*;

import org.neo4j.driver.*;

class Indv {
	private String id;
	private String sex;
	private String name;
	private String surname;
	private String birthday;
	private String isDead;
	private String deathday;
	
	public Indv(){
		id="";
		sex="";
		name="";
		surname="";
		birthday="";
		isDead="";
		deathday="";
	};
	
	public void setId(String str){
		id=str;
	}
	public void setSex(String str){
		sex=str;
	}
	public void setName(String str){
		name=str;
	}
	public void setSurname(String str){
		surname=str;
	}
	public void setBirthday(String str){
		birthday=str;
	}
	public void setDeath(String str){
		isDead="true";
		deathday=str;
	}

	public String getId(){
		return id;
	}
	public String getSex(){
		return sex;
	}
	public String getName(){
		return name;
	}
	public String getSurname(){
		return surname;
	}
	public String getBirthday(){
		return birthday;
	}
	public String getDeath(){
		if (isDead=="true")
			return deathday;
		else
			return "";
	}
	public String getFullInfo(){
		String result="";
		result=getId()+" "+
				getSex()+" "+
				getName()+" "+
				getSurname()+" "+
				getBirthday()+" "+
				getDeath();
		return result;
	}
	
}//Indv class

public class LoadGEDCOM 
{
	static int getIndexofIndividual(List<Indv> indvs, String id)
	{
		int result = 0;
		
		for (int i = 0; i < indvs.size(); i++)
		{
		    if(indvs.get(i).getId().endsWith(id))
		    	result=i;
		}
		
		return result;
	}
	
	public static void main(String args[])
	{
		GedcomParser gp = new GedcomParser();
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		try {
			gp.load("src/Sample.ged");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GedcomParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gedcom g = gp.getGedcom();
		List<Indv> indvs = new Vector<Indv>();
		int it=1;
		System.out.println(g.getIndividuals().size());
		for (Individual i : g.getIndividuals().values()) {
			Indv indv = new Indv(); 
//			System.out.println("---"+it+"---");
//			System.out.println(i.getXref().toString());
//			System.out.println(i.toString());
//			System.out.println(i.getSex());
			
			indv.setId(i.getXref().toString());
			indv.setSex(i.getSex().toString());
			String surname, firstname;
			if (i.getNames()!= null)
			{
				
				surname = (i.getNames().get(0).getSurname()==null) ? "Unknown":i.getNames().get(0).getSurname().toString();
				firstname = (i.getNames().get(0).getGivenName()==null) ? "Unknown":i.getNames().get(0).getGivenName().toString();
//				System.out.println(surname+" "+firstname);
			}
			else
			{
//				System.out.println("Unknown"+" "+"Unknown");
				surname = "Unknown";
				firstname = "Unknown";
			}
			indv.setName(firstname);
			indv.setSurname(surname);
//			System.out.println(i.getFormattedName());
			//System.out.println(i.getEvents().toString());
			String birth="", death="";
			//birth = (i.getEvents().get(0).getDate()==null) ? "":i.getEvents().get(0).getDate().toString();
			indv.setBirthday(birth);
			
//Commented out because of exception			
//			if (i.getEvents().size()>1)
//			{
//				death = (i.getEvents().get(1).getDate()==null) ? "-":i.getEvents().get(1).getDate().toString();
////				System.out.println(death);
//				indv.setDeath(death);
//			}
//			System.out.println(birth+" - "+death);
			
			indvs.add(indv);
			//System.out.println(indv.getFullInfo());
			//System.out.println(q);
			/*
			String xx = (q) ? "Unknown":i.getNames().get(0).getGivenName().toString();
			System.out.println(xx);
			*/
			it++;
			//if (it>30) break;
		}//for Individuals
		/*
		for (Indv person : indvs)
		{
		    System.out.println(person.getFullInfo());
		}
		*/
		
		System.out.println("******FAMILIES******");
		it=0;
		System.out.println(g.getFamilies().size());
		for (Family f : g.getFamilies().values()) {
			System.out.println("---"+it+"---");
			System.out.println(f.getXref().toString());
			if (f.getHusband()!=null)
			{
				//System.out.println(f.getHusband().getIndividual().getXref());
				Individual husband = f.getHusband().getIndividual();
				if(husband.getNames()!=null)
				{
					String surname, firstname;
					surname = (husband.getNames().get(0).getSurname()==null) ? "Unknown":husband.getNames().get(0).getSurname().toString();
					firstname = (husband.getNames().get(0).getGivenName()==null) ? "Unknown":husband.getNames().get(0).getGivenName().toString();
					//System.out.println(surname+" "+firstname);
					System.out.println(indvs.get(getIndexofIndividual(indvs, f.getHusband().getIndividual().getXref())).getFullInfo());
				}//if husband has name
			}//if husband
			if (f.getWife()!=null)
			{
				//System.out.println(f.getWife().getIndividual().getXref());
				Individual wife = f.getWife().getIndividual();
				if(wife.getNames()!=null)
				{
					String surname, firstname;
					surname = (wife.getNames().get(0).getSurname()==null) ? "Unknown":wife.getNames().get(0).getSurname().toString();
					firstname = (wife.getNames().get(0).getGivenName()==null) ? "Unknown":wife.getNames().get(0).getGivenName().toString();
					//System.out.println(surname+" "+firstname);
					System.out.println(indvs.get(getIndexofIndividual(indvs, f.getWife().getIndividual().getXref())).getFullInfo());
				}//if wife has name
			}//if wife
			if (f.getChildren()!=null)
				System.out.println(f.getChildren().toString());
		    it++;
			//if (it>30) break;
		}//for Families
		
		System.out.println(indvs.get(getIndexofIndividual(indvs, "test")).getFullInfo());
		System.out.println(indvs.get(0).getFullInfo());
		
//		GraphDatabaseService graphDb = new GraphDatabaseFactory()
//        .newEmbeddedDatabaseBuilder( testDirectory.databaseDir() )
//        .loadPropertiesFromFile( pathToConfig + "neo4j.conf" )
//        .newGraphDatabase();
		
		
	}//main
	
	
	
	
	
	
	
	
}//LoadGEDCOM



