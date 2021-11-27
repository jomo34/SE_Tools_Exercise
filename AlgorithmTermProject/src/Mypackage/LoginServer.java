package Mypackage;
import java.io.IOException;
import java.util.Iterator;
import java.sql.*;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginServer 
{
   private static Set<String> userlist = new HashSet<>(); //가입자들의 정보 저장.
	private static Set<PrintWriter> writers = new HashSet<>(); //for broadcast
   private static Set<String> chatList = new HashSet<>(); //chat사용자들 리스트관리
   public static void main(String[] args) throws Exception //multi thread
   {
      
      System.out.println("The Gachon SW server is running...");
      ExecutorService pool = Executors.newFixedThreadPool(500);
      try (ServerSocket listener = new ServerSocket(59001)) 
      {
         while (true)
         {
            pool.execute(new Handler(listener.accept()));
         }
      }
   }
   private static class Handler implements Runnable 
   {

      private String id;
      private Socket socket;
      private Scanner in;
      private PrintWriter out;
      private String pw;
      private String logid=""; //로그아이디 저장 변수
       String DB_URL = "jdbc:mysql://localhost:3306/userlist?serverTimezone=UTC"; //접속할 DB 서버
         Connection conn=null;//mysql 접속용
         Statement state=null;//mysql 연결
      public Handler(Socket socket)
      {
         this.socket = socket;
      }
      public void run() 
      {

         try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
         Class.forName("com.mysql.cj.jdbc.Driver"); //mysql driver
         conn=DriverManager.getConnection(DB_URL,"root","jang14"); //db주소와, 사용자, 비밀번호를 통해서 접근.
         state=conn.createStatement(); //mysql 연결
         boolean realuser=false; //실제 사용자가 있는 지 확인하기 위한 변수
         //Statement state9=conn.createStatement();
         //String list="select userid from info"; 
         //ResultSet ulist=state9.executeQuery(list);//데이터베이스의 userid를 다 가져옴.
         //while(ulist.next())
         //{
           // userlist.add(ulist.getString("userid"));//데이터베이스에서 가져온 userid를  server의 hashlist에저장.
         //}
         while(true)
         {
         String input=in.nextLine();//소켓에서 온 정보를 읽어옴
         System.out.println(input);
            if(input.startsWith("logid")) //client에서 logid가 왔을 때, 로그인
            {
               String[]info=input.split(" "); //id와 password가 같이와서 나눠줌.
               id=info[0].substring(5); //id
               pw=info[1]; //password
               
               String sqlid="select userid from user where userid like"+"'"+id+"'";
               ResultSet rsi=state.executeQuery(sqlid);
               if ((rsi.next() == false || (id.isEmpty()) == true))
               {
                  out.println("id invalid"); //로그인 실패라고 클라이언트에게 보내줌
               }
               rsi.close();
               Statement state2=conn.createStatement();
               String sql="select * from user where userid like"+"'"+id+"'"; //id를 바탕으로 비밀번호를 찾음
               ResultSet rs=state2.executeQuery(sql); //결과 값 저장.
               Statement state3=conn.createStatement();
           
               while(rs.next()) //데이터베이스 스캔
               {
                  String dbpw=rs.getString("password"); //조건문에 맞는 패스워드 저장
                  if(pw.equalsIgnoreCase(dbpw)) //입력한 비밀번호와 아이디가 맞을 때, 로그인성공
                  {
                     out.println("access:"+id+":"+rs.getInt("stnumber")+":"+rs.getString("email")+":"+rs.getString("name")+":"+rs.getInt("grade"));//클라이언트에게 성공했다고 보내줌.
                  }
                  else
                  {
                  out.println("pw invalid"); //로그인 실패   
                  }   
                  }
               rs.close();
               state3.close();
               state2.close();
               writers.add(out);
               }
            else if(input.startsWith("Resister")) //회원가입
            {
               String [] nw=input.split(" "); //회원가입되서 온 정보를 나눠줌.
               String addname=nw[0].substring(8);
               int addstnum=Integer.parseInt(nw[1]);
               String addemail=nw[2];
               String addid=nw[3];
               String addpw=nw[4];
               int addgrade = Integer.parseInt(nw[5]);
               Statement state4=conn.createStatement();
               String mysql="insert into user values("+"'"+addid+"'"+","+"'"+addpw+"'"+","+addstnum+","+"'"+addemail+"'"+","+"'"+addname+"'"+","+addgrade+")";
               //String mysql="insert into info values("+"'"+addname+"'"+","+addbday+","+"'"+addemail+"'"+","+"'"+addid+"'"+","+"'"+addpw+"'"+","+0+","+"'"+""+"'"+","+"'"+"hello world"+"'"+")";
                  
               //데이터베이스에 저장시켜주는 구문
               state4.executeUpdate(mysql); //데이터베이스 업데이트
               userlist.add(nw[3]); //가입하면, hashset에 userid 추가
               out.println("Welcome"); //회원가입 되었다는 것을 보내줌.
               state4.close();
            }
            else if(input.startsWith("Update")) {  // 내정보 변경 할 때 
               String[] ch_info = input.split(":");
               try {
	               String ch_sql = "update user set email = " + "'"+ch_info[2]+"',"+"grade = "+ch_info[3]+" where userid = " +"'"+ch_info[1]+"'";// 닉네임이랑 상태메시지 변경함
	               Statement state10 = conn.createStatement();
	               
	               state10.executeUpdate(ch_sql);
	
	               state10.close();
	               out.println("UC"); // 업데이트 성공했다고 보내줌. 
               }
               // update 실패시,
               catch(Exception e)
               {
            	   out.println("UF");
            	   e.printStackTrace();
               }
               
            }
            else if(input.startsWith("Book"))// 예약받아서 저장하게끔 한다.
            {
            	String [] bookingList=input.split(" ");
            	System.out.println(bookingList[1]); // 시작 시간
            	System.out.println(bookingList[2]); // 종료 시간
            	System.out.println(bookingList[3]); // 예약자 아이디 
            	
            	
            		
            }
			else if(input.startsWith("Start")) //스케줄을 짜주는 경우, 바꿔야한다. 
			{
				// 스케줄 표 짜지면, 자동으로 broadcasting 
				for (PrintWriter writer : writers) 
				{
					writer.println("Request:");//client에게 채팅 요청사항을 브로드캐스트
				}
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
