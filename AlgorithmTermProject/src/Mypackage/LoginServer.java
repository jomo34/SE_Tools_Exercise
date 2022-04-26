package Mypackage;
import java.io.IOException;
import Mypackage.Timer;
import java.util.Iterator;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import java.util.TimerTask;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginServer
{
   private static Set<String> userlist = new HashSet<>(); //媛��엯�옄�뱾�쓽 �젙蹂� ���옣.
   private static Set<PrintWriter> writers = new HashSet<>(); //for broadcast
   private static Set<String> chatList = new HashSet<>(); //chat�궗�슜�옄�뱾 由ъ뒪�듃愿�由�
   private static String [][] time1 = new String [255][3];
   private static String [][] table1 = new String [255][3];
   private static int countT1 = 0;
   private static int N=0;
   
 
   public static void main(String[] args) throws Exception //multi thread
   {
      System.out.println("The Gachon SW server is running...");
      for(int i = 0; i < time1.length; i++) 
			Arrays.fill(time1[i], "99999");
      ExecutorService pool = Executors.newFixedThreadPool(500);
      try (ServerSocket listener = new ServerSocket(59001)) 
      {
         while (true)
         {
            pool.execute(new Handler(listener.accept()));
         }
      }
   }
   public static void activity(String time1[][],int N ) {
		// �걹�굹�뒗 �떆媛꾩쓣 湲곗��쑝濡� �젙�젹
	   countT1=0;
	 
	   Arrays.sort(time1, new Comparator<String[]>() {      
           @Override  
          public int compare(String[] o1, String[] o2) {
                  if( Integer.parseInt(o1[1])>Integer.parseInt(o2[1]))
                        return 1;
                    else
                        return -1;
          }
           
           
	   	});
	  
		int prev_end_time = 0;
	
		for(int i = 0; i < N; i++) {
			
			// 吏곸쟾 醫낅즺�떆媛꾩씠 �떎�쓬 �쉶�쓽 �떆�옉 �떆媛꾨낫�떎 �옉嫄곕굹 媛숇떎硫� 媛깆떊 
			if(prev_end_time <= Integer.parseInt(time1[i][0])) {
				prev_end_time =  Integer.parseInt(time1[i][1]);
				table1[countT1][0] = time1[i][0];
				table1[countT1][1] = time1[i][1];
				table1[countT1][2] = time1[i][2];
				countT1++;
			}
		}
          	
		
 }
   public static String Converter(String id)
   {
      String DB_URL = "jdbc:mysql://localhost:3306/algorithm?serverTimezone=UTC"; //�젒�냽�븷 DB �꽌踰�
       Connection conn=null;//mysql �젒�냽�슜
       Statement state=null;//mysql �뿰寃�
       String uname=null;
       try {
          conn=DriverManager.getConnection(DB_URL,"root","990302"); //db二쇱냼��, �궗�슜�옄, 鍮꾨�踰덊샇瑜� �넻�빐�꽌 �젒洹�.
          state=conn.createStatement(); //mysql �뿰寃�
          
          try {
             String sqlname="select name from user where userid like"+"'"+id+"'";
             ResultSet rsf=state.executeQuery(sqlname);
                while(rsf.next())
                {
                uname=rsf.getString("name");
                }
             }
          catch(Exception e)
          {
             e.printStackTrace();
          }
          }
       catch(Exception e)
       {
          e.printStackTrace();
       }
       return uname;
   }
   private static class Handler implements Runnable 
   {
	 
	  private String id;
      private Socket socket;
      private Scanner in;
      private PrintWriter out;
      private String pw;
      int schedulecount=0;
      private String logid=""; //濡쒓렇�븘�씠�뵒 ���옣 蹂��닔
       String DB_URL = "jdbc:mysql://localhost:3306/algorithm"; //�젒�냽�븷 DB �꽌踰�
         Connection conn=null;//mysql �젒�냽�슜
         Statement state=null;//mysql �뿰寃�
      public Handler(Socket socket)
      {
         this.socket = socket;
      }
      public void run() 
      {
    	
         try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
         Timer curtime=new Timer();
         String time=curtime.getCurrentTime();
         String [] scheduler=time.split(":");
         Class.forName("com.mysql.cj.jdbc.Driver"); //mysql driver
         conn=DriverManager.getConnection(DB_URL,"root","990302"); //db二쇱냼��, �궗�슜�옄, 鍮꾨�踰덊샇瑜� �넻�빐�꽌 �젒洹�.
         state=conn.createStatement(); //mysql �뿰寃�
         boolean realuser=false; //�떎�젣 �궗�슜�옄媛� �엳�뒗 吏� �솗�씤�븯湲� �쐞�븳 蹂��닔
         //Statement state9=conn.createStatement();
         //String list="select userid from info"; 
         //ResultSet ulist=state9.executeQuery(list);//�뜲�씠�꽣踰좎씠�뒪�쓽 userid瑜� �떎 媛��졇�샂.
         //while(ulist.next())
         //{
           // userlist.add(ulist.getString("userid"));//�뜲�씠�꽣踰좎씠�뒪�뿉�꽌 媛��졇�삩 userid瑜�  server�쓽 hashlist�뿉���옣.
         //}
         while(true)
         {
         String input=in.nextLine();//�냼耳볦뿉�꽌 �삩 �젙蹂대�� �씫�뼱�샂
       
         System.out.println(input);
            if(input.startsWith("logid")) //client�뿉�꽌 logid媛� �솕�쓣 �븣, 濡쒓렇�씤
            {
               String[]info=input.split(" "); //id�� password媛� 媛숈씠���꽌 �굹�닠以�.
               id=info[0].substring(5); //id
               pw=info[1]; //password
               
               String sqlid="select userid from user where userid like"+"'"+id+"'";
               ResultSet rsi=state.executeQuery(sqlid);
               if ((rsi.next() == false || (id.isEmpty()) == true))
               {
                  out.println("id invalid"); //濡쒓렇�씤 �떎�뙣�씪怨� �겢�씪�씠�뼵�듃�뿉寃� 蹂대궡以�
               }
               rsi.close();
               Statement state2=conn.createStatement();
               String sql="select * from user where userid like"+"'"+id+"'"; //id瑜� 諛뷀깢�쑝濡� 鍮꾨�踰덊샇瑜� 李얠쓬
               ResultSet rs=state2.executeQuery(sql); //寃곌낵 媛� ���옣.
               Statement state3=conn.createStatement();
           
               while(rs.next()) //�뜲�씠�꽣踰좎씠�뒪 �뒪罹�
               {
                  String dbpw=rs.getString("password"); //議곌굔臾몄뿉 留욌뒗 �뙣�뒪�썙�뱶 ���옣
                  
                  if(pw.equalsIgnoreCase(dbpw)) //�엯�젰�븳 鍮꾨�踰덊샇�� �븘�씠�뵒媛� 留욎쓣 �븣, 濡쒓렇�씤�꽦怨�
                  {
                     out.println("access:"+id+":"+rs.getInt("stnumber")+":"+rs.getString("email")+":"+rs.getString("name")+":"+rs.getInt("grade"));//�겢�씪�씠�뼵�듃�뿉寃� �꽦怨듯뻽�떎怨� 蹂대궡以�.
                  }
                  else
                  {
                  out.println("pw invalid"); //濡쒓렇�씤 �떎�뙣   
                  }   
                  }
               rs.close();
               state3.close();
               state2.close();
               writers.add(out);
               }
            else if(input.startsWith("Resister")) //�쉶�썝媛��엯
            {
               String [] nw=input.split(" "); //�쉶�썝媛��엯�릺�꽌 �삩 �젙蹂대�� �굹�닠以�.
               String addname=nw[0].substring(8);
               int addstnum=Integer.parseInt(nw[1]);
               String addemail=nw[2];
               String addid=nw[3];
               String addpw=nw[4];
               int addgrade = Integer.parseInt(nw[5]);
               Statement state4=conn.createStatement();
               String mysql="insert into user values("+"'"+addid+"'"+","+"'"+addpw+"'"+","+addstnum+","+"'"+addemail+"'"+","+"'"+addname+"'"+","+addgrade+")";
               //String mysql="insert into info values("+"'"+addname+"'"+","+addbday+","+"'"+addemail+"'"+","+"'"+addid+"'"+","+"'"+addpw+"'"+","+0+","+"'"+""+"'"+","+"'"+"hello world"+"'"+")";
                  
               //�뜲�씠�꽣踰좎씠�뒪�뿉 ���옣�떆耳쒖＜�뒗 援щЦ
               state4.executeUpdate(mysql); //�뜲�씠�꽣踰좎씠�뒪 �뾽�뜲�씠�듃
               userlist.add(nw[3]); //媛��엯�븯硫�, hashset�뿉 userid 異붽�
               out.println("Welcome"); //�쉶�썝媛��엯 �릺�뿀�떎�뒗 寃껋쓣 蹂대궡以�.
               state4.close();
            }
            else if(input.startsWith("Update")) {  // �궡�젙蹂� 蹂�寃� �븷 �븣 
               String[] ch_info = input.split(":");
               try {
	               String ch_sql = "update user set email = " + "'"+ch_info[2]+"',"+"grade = "+ch_info[3]+" where userid = " +"'"+ch_info[1]+"'";// �땳�꽕�엫�씠�옉 �긽�깭硫붿떆吏� 蹂�寃쏀븿
	               Statement state10 = conn.createStatement();
	               
	               state10.executeUpdate(ch_sql);
	
	               state10.close();
	               out.println("UC"); // �뾽�뜲�씠�듃 �꽦怨듯뻽�떎怨� 蹂대궡以�. 
               }
               // update �떎�뙣�떆,
               catch(Exception e)
               {
            	   out.println("UF");
            	   e.printStackTrace();
               }
               
            }
            else if(input.startsWith("Book"))// �삁�빟諛쏆븘�꽌 ���옣�븯寃뚮걫 �븳�떎.
            {
            	 Calendar now = Calendar.getInstance();
                 int h= now.get(Calendar.HOUR_OF_DAY);
            	String [] bookingList=input.split(" ");
            	 // �삁�빟�옄 �븘�씠�뵒
            	if(h<18) // 6�떆 �씠�쟾�뿉留� �삁�빟�쓣 諛쏅뒗�떎. 
            	{
               	int a = Integer.parseInt(bookingList[1]);
               	int b = Integer.parseInt(bookingList[2]);
               	int c= a/100*60 + a%100;
               	int d= b/100*60 + b%100;
            	
            	
            	        	
				time1[N][0]=String.valueOf(c);//�떆�옉�떆媛� 
    			time1[N][1]=String.valueOf(d);//醫낅즺�떆媛� 
    			time1[N][2]=bookingList[3];//ID
    			System.out.println("start: " + time1[N][0] + " end: "+ time1[N][1]+ " id: "+ time1[N][2]);
    			N++;
            	}
            	else
            	{
            		out.println("Late");
            	}
           
            }
            String result="result ";
			
			
			for(int i=0;i<N;i++) {
				System.out.println("start: " + time1[i][0] + " end: "+ time1[i][1]+ " id: "+ time1[i][2]);
			}
			
			int a=0;

			if(Integer.parseInt(scheduler[0])==15 && schedulecount==0 && Integer.parseInt(scheduler[1])==37) // 6�떆 �릺硫� �븳踰덈쭔 吏쒖��떎. 
			{
				activity(time1,N);
					for(int i=0;i<countT1;i++) {
						int starth=Integer.parseInt(table1[i][0])/60;
						int startm=Integer.parseInt(table1[i][0])%60;
						int endh=Integer.parseInt(table1[i][1])/60;
						int endm=Integer.parseInt(table1[i][1])%60;
						result=result+ starth + ":" +String.format("%02d", startm)+"~"+endh + ":" + String.format("%02d", endm)+ " " + Converter(table1[i][2]) + " ";
					}			 	
              	System.out.println(result);
              	for (PrintWriter writer : writers) 
				{
              		writer.println(result);
              		writer.println(countT1);
              		schedulecount++;
				}
			}
			else if(input.startsWith("Time")) //�뒪耳�以꾩쓣 吏쒖＜�뒗 寃쎌슦, 諛붽퓭�빞�븳�떎. 
			{
				
				
				
					
			}
			
            out.flush();
         }
       
         }   
         catch (Exception e) 
         {
            System.out.println(e);
         } 
         finally {
            if (out != null)
            {
              
            }
            try { socket.close(); }
            catch (IOException e){}
         }
            	}
         }
      
   }
