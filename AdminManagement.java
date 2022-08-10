import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.security.*;
import java.util.*;
public class AdminManagement extends  HttpServlet{

     Connection conn;
    public void init(){
        try{
            this.conn = DB.initializeDatabase();
        }catch(Exception e){

        }
    }
    public  void doPost(HttpServletRequest request ,HttpServletResponse response)throws  IOException{
        if(request.getParameter("register")!=null){
            registerAdmin(request,response);
        }

        if(request.getParameter("login")!=null){
            loginAdmin(request,response);
        }
        
    }

   public  void doGet(HttpServletRequest request ,HttpServletResponse response)throws  IOException{

        try{

            if(request.getParameter("delete")!=null){
                    deleteAdmin(request,response);
            }

                HttpSession session = request.getSession();
                if(session.getAttribute("admin")==null){

                
                    response.sendRedirect("admin_login");
                    return;
                }
            
            getAdmins(request,response);

        }catch(Exception e){

             try{
                PrintWriter out = response.getWriter();
                out.println(e);
                out.close();
            }catch(Exception ex){
                
            }

        }

   }






   private void getAdmins(HttpServletRequest request,HttpServletResponse response) throws Exception{
     Statement stmt = this.conn.createStatement();
            String query = "SELECT * from  users";
            ResultSet recentAdmins= stmt.executeQuery(query);

            getServletContext().setAttribute(
                "recent_admins",recentAdmins
            );
    
            request.getRequestDispatcher("admin_register.jsp").include(request,response);

            stmt.close();

   }



     private void deleteAdmin(HttpServletRequest request,HttpServletResponse response) throws Exception{
        PrintWriter out = response.getWriter();
        String adminId = request.getParameter("admin_id");
        try{
        Statement deleteStatement = this.conn.createStatement();
        String query = "DELETE from users where user_id="+adminId+"";
        deleteStatement.executeUpdate(query);
        deleteStatement.close();

        Message.alertSuccess(request,"Deleted Successfully");
    
        }catch(Exception e){
            out.println(e);
        }

    }
  

    private void registerAdmin(HttpServletRequest request,HttpServletResponse response){

        try{
          Statement insertionStatement = this.conn.createStatement();

        String fullName =  (request.getParameter("full_name"));
        String gender = (request.getParameter("gender"));
        String location = (request.getParameter("location"));
        String email = (request.getParameter("email"));
        String password = Security.getMd5((request.getParameter("password")));
        
        String query = "INSERT  into users(user_full_name,email,password) values('"+fullName+"','"+email+"','"+password+"')";
        
        // call this to execute insert or update queries
        insertionStatement.executeUpdate(query);
    
        insertionStatement.close();
        conn.close();
        Message.alertSuccess(request,"Registered Successfully");
    
        response.sendRedirect("admin_register");

        }catch(Exception e){

            System.out.println(e);

            try{
                PrintWriter out = response.getWriter();
                out.println(e);
                out.close();
            }catch(Exception ex){
                
            }

        }
    }

    private void loginAdmin(HttpServletRequest request,HttpServletResponse response){

        try{
         Statement loginStatement = this.conn.createStatement();

        String email = (request.getParameter("email"));
        String password = Security.getMd5((request.getParameter("password")));
        
        String query = "SELECT  * from  users where password='"+password+"' and email='"+email+"' limit 1" ;
        
        // call this to execute insert or update queries
       ResultSet userRs =  loginStatement.executeQuery(query);

       if(userRs.next()){
        String customer_name = userRs.getString("user_full_name");
        String customer_id = userRs.getString("user_id");
        String customer_email = userRs.getString("email");
        String customer_date = userRs.getString("created_at");

        HttpSession session = request.getSession();

        HashMap<String,String> loggedInAdmin = new HashMap<>();
        loggedInAdmin.put("name",customer_name);
        loggedInAdmin.put("admin_id",customer_id);
        loggedInAdmin.put("email",customer_email);
        loggedInAdmin.put("date",customer_date);
        session.setAttribute("admin",loggedInAdmin);
        session.setAttribute("name",customer_name);
        response.sendRedirect("dashboard");
        


                

       }else{
        Message.alertError(request,"Loggin Failed!!");
        response.sendRedirect("admin_login");
       }
        

        loginStatement.close();
        

        }catch(Exception e){

            System.out.println(e);

            try{
                PrintWriter out = response.getWriter();
                out.println(e);
                out.close();
            }catch(Exception ex){
                
            }

        }

    }

    public void destroy(){
        try{
            this.conn.close();
        }catch(Exception e){

        }
    }







}