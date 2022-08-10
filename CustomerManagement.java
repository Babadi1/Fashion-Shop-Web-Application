import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.security.*;
import java.util.*;
public class CustomerManagement extends  HttpServlet{
    public  void doPost(HttpServletRequest request ,HttpServletResponse response)throws  IOException{
        if(request.getParameter("register")!=null){
            registerCustomer(request,response);
        }

        if(request.getParameter("login")!=null){
            loginCustomer(request,response);
        }   
    }


   public  void doGet(HttpServletRequest request ,HttpServletResponse response)throws  IOException{

        try{

            Connection conn = DB.initializeDatabase();
            Statement stmt = conn.createStatement();
            String query = "SELECT * from worker_allocation join production_lines on worker_allocation.production_line_id=production_lines.production_line_id order by worker_allocation.allocation_date desc limit 3";
            ResultSet recentAllocations = stmt.executeQuery(query);

            //request.getSession().put("results",rs);
            getServletContext().setAttribute(
                "recent_allocations",recentAllocations
            );
    
            request.getRequestDispatcher("all_customers.jsp").include(request,response);

            stmt.close();
        }catch(Exception e){

             try{
                PrintWriter out = response.getWriter();
                out.println(e);
                out.close();
            }catch(Exception ex){
                
            }

        }

   }
  

    private void registerCustomer(HttpServletRequest request,HttpServletResponse response){

        try{
        Connection conn = DB.initializeDatabase();
        Statement insertionStatement = conn.createStatement();

        String fullName =  (request.getParameter("full_name"));
        String gender = (request.getParameter("gender"));
        String location = (request.getParameter("location"));
        String email = (request.getParameter("email"));
        String password = Security.getMd5((request.getParameter("password")));
        
        String query = "INSERT  into customers(full_name,email,gender,location,password) values('"+fullName+"','"+email+"','"+gender+"','"+location+"','"+password+"')";
        
        // call this to execute insert or update queries
        insertionStatement.executeUpdate(query);
        

        insertionStatement.close();
        conn.close();

        Message.alertSuccess(request,"Account Created Successfully");
    

        response.sendRedirect("register");

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



    private void loginCustomer(HttpServletRequest request,HttpServletResponse response){

        try{
        Connection conn = DB.initializeDatabase();
        Statement loginStatement = conn.createStatement();

        String email = (request.getParameter("email"));
        String password = Security.getMd5((request.getParameter("password")));
        
        String query = "SELECT  * from  customers where password='"+password+"' and email='"+email+"' limit 1" ;
        
        // call this to execute insert or update queries
       ResultSet userRs =  loginStatement.executeQuery(query);

       if(userRs.next()){
        String customer_name = userRs.getString("full_name");
        String customer_id = userRs.getString("customer_id");
        String customer_email = userRs.getString("email");
        String customer_gender = userRs.getString("gender");
        String customer_location = userRs.getString("location");
        String customer_date = userRs.getString("created_at");

        HttpSession session = request.getSession();

        HashMap<String,String> loggedIncustomer = new HashMap<>();
        loggedIncustomer.put("customer_name",customer_name);
        loggedIncustomer.put("customer_id",customer_id);
        loggedIncustomer.put("customer_email",customer_email);
        loggedIncustomer.put("customer_gender",customer_gender);
        loggedIncustomer.put("customer_location",customer_location);
        loggedIncustomer.put("customer_date",customer_date);
        session.setAttribute("customer",loggedIncustomer);
        session.setAttribute("customer_id",customer_id);


        Message.alertSuccess(request,"Logged In Successfully");
        response.sendRedirect("get_products");
       }else{
        Message.alertError(request,"Loggin Failed");
        response.sendRedirect("login");
       }
        

        loginStatement.close();
        conn.close();

        
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







}