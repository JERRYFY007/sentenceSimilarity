package ruc.irm.similarity.sentence.editdistance;

import java.util.ArrayList;
import java.util.List;


/**
 * 夏天提出的新的支持非相邻块交互的编辑距离算法
 * 
 * @author <a href="mailto:iamxiatian@gmail.com">夏天</a>
 * @organization 中国人民大学信息资源管理学院 知识工程实验室
 */
public class XiatianEditDistance extends EditDistance {
    /** 块交换代价 */
	public static double swapCost = 0.5;
    
    private SuperString<? extends EditUnit> S,T;
    private double[][][][] QArray;
    
    public double getEditDistance(SuperString<? extends EditUnit> S, SuperString<? extends EditUnit> T){
    	this.S = S;
    	this.T = T;
    	QArray = new double[S.length()+1][S.length()+1][T.length()+1][T.length()+1];
        for(int i=0;i<=S.length();i++){
            for(int i2=0;i2<=S.length();i2++)
                for(int j=0;j<=T.length();j++)
                    for(int j2=0;j2<=T.length();j2++){
                        QArray[i][i2][j][j2]=Double.MAX_VALUE;
                    }
        }        
        return Q(0,S.length()-1,0,T.length()-1);
    }
    
    private double Q(int i1,int im,int j1,int jn){
    	if(QArray[i1][im][j1][jn]<Double.MAX_VALUE){
    		return QArray[i1][im][j1][jn];
    	}
        double cost = 0;                
        if(im<i1){
        	for(int j = j1; j<=jn; j++){
        		cost += T.elementAt(j).getInsertionCost();
        	}        	
        }else if(jn<j1){
        	for(int i=i1; i<=im; i++){
        		cost += S.elementAt(i).getDeletionCost();
        	}
        }else if(im==i1 && jn==j1){
        	cost = S.elementAt(i1).getSubstitutionCost(T.elementAt(j1));        	
        } else if(i1==im){            
            double minSubValue = S.elementAt(i1).getSubstitutionCost(T.elementAt(j1));
            int minPosJ = j1;
            for(int j=j1+1;j<=jn;j++){
            	double subValue = S.elementAt(i1).getSubstitutionCost(T.elementAt(j));
            	if(minSubValue > subValue){
            		minSubValue = subValue;
            		minPosJ = j;
            	}                	
            }
            for(int j=j1;j<=jn;j++){
            	if(j == minPosJ){
            		cost += minSubValue;             	
            	}else{
            		cost += T.elementAt(j).getInsertionCost();
            	}
            }              
        }else if(j1==jn){    
        	int minPosI = i1;
        	double minSubValue = S.elementAt(i1).getSubstitutionCost(T.elementAt(j1));            
            for(int i=i1+1;i<=im;i++){
            	double subValue = S.elementAt(i).getSubstitutionCost(T.elementAt(j1));
            	if(minSubValue > subValue){
            		minSubValue = subValue;
            		minPosI = i;
            	}                	
            }
            for(int i=i1;i<=im;i++){
            	if(i == minPosI){
            		cost += minSubValue;             	
            	}else{
            		cost += S.elementAt(i).getDeletionCost();
            	}
            }            
        }else{        	
        	cost = QArray[i1][im][j1][jn];
            loop:for(int i=i1;i<im;i++){
                //block X divide to 3 parts.
                for(int LX=0;LX<=im-i;LX++){                    
                    //process Y sentence
                    for(int j=j1;j<jn;j++){
                    	//if(cost<=swapCost)break;
                        for(int LY=0;LY<=jn-j;LY++){                                 	
                        	//不交换的代价
                            double cost1 = Q(i1,i,j1,j)+Q(i+1,i+LX,j+1,j+LY)+Q(i+LX+1,im,j+LY+1,jn);
                            //交互代价
                            double cost2 = Q(i1,i,j+LY+1,jn)+Q(i+1,i+LX,j+1,j+LY)+Q(i+LX+1,im,j1,j)+swapCost;
                            cost = Math.min(Math.min(cost1, cost2),cost);
                            if(cost == 0) break loop;
                        }
                    }                    
                }             
            }            
        }        
        
        QArray[i1][im][j1][jn] = cost;
        return cost;
    }
    
    public static void main(String[] argv) {  
    	long start=System.currentTimeMillis();
        EditDistance ed = new XiatianEditDistance();
        String s1 = "触电了怎么办";
        String s2 = "触电的应对措施";
        //String s2 = "我的密码我忘记了,我该怎样做呢?";
        System.out.println(ed.getEditDistance(SuperString.createCharSuperString(s1), SuperString.createCharSuperString(s2)));
        float getsimilar= (float) ed.getSimilarity(s1, s2);
        System.out.println("primary similarity: "+getsimilar);
        List<String> important=new ArrayList<String> ();
        important.add("变压");
        important.add("什么");
        float common=(float) 0.5;
        for(String imt : important){
        	if(s1.contains(imt)&&s2.contains(imt)){
         	   
      	      common=(float) (common+0.1);
      	   
          }
        }
      
       
        
       if(common>1.0){
    	   common=(float) 1.0;
       }
       
      System.out.println("common: "+common);
      float totalsimilar=(float) (0.8*getsimilar+0.2*common);
      System.out.println(totalsimilar);
      long end=System.currentTimeMillis();
      float total=(float) (1.0*(end-start)/1000);
      System.out.println("total time:"+total);

    }
}
