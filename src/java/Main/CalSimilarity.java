package Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import ruc.irm.similarity.sentence.editdistance.EditDistance;
import ruc.irm.similarity.sentence.editdistance.XiatianEditDistance;

public class CalSimilarity {
	
	public double getPoint(String a,String b,ArrayList<String> c){
		 EditDistance ed = new XiatianEditDistance();
		 double primarySimilar= (double) ed.getSimilarity(a, b);
		 
		 double common=(double) 0.5;
	        for(String imt : c){
	        	if(a.contains(imt)&&b.contains(imt)){
	      	      common=(double) (common+0.1);
	             }
	        }
	       if(common>1.0){
	    	   common=(double) 1.0;
	       }
	       
	      double totalsimilar=(double) (0.8*primarySimilar+0.2*common);
	      
		  return totalsimilar;
	}
	public static void main(String[] args) throws Exception {
		long start=System.currentTimeMillis();
		String s1="CSG是什么意思";
		BufferedReader br1=null;
		BufferedReader br2=null;
		
		br1=new BufferedReader(new FileReader("./File/ext.dic"));
		String line1=null;
		ArrayList<String> important=new ArrayList<String> ();
		while((line1=br1.readLine())!=null){
		
		        important.add(line1);
			
		}
		
		br2=new BufferedReader(new FileReader("./File/1.txt"));
		String line2=null;
		CalSimilarity  cs =new CalSimilarity();
		double re=0.0;
		double max=0.0;
		while((line2=br2.readLine())!=null){
		//	System.out.println(line2);
			 re=cs.getPoint(s1,line2, important);
			System.out.println(re);
			if(re>max){
				max=re;
			}
			
		}	
		br1.close();
		br2.close();
		System.out.println("max:"+max);
	    long end=System.currentTimeMillis();
	    float totaltime=(float) ((end-start)*1.0/1000);
	    System.out.println(totaltime);
	}

}
