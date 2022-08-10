import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
public class Employee extends  HttpServlet{
    public  void doPost(HttpServletRequest request ,HttpServletResponse response)throws  IOException{
        if(request.getParameter("allocate_workers")!=null){
            allocateWorkers(request,response);
        }else{

        try{

           PrintWriter out = response.getWriter();
           out.println("receiving...");
           out.close();

        }catch(Exception e){

        }
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
      
            request.getRequestDispatcher("worker_allocation.jsp").include(request,response);

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
  


    private void allocateWorkers(HttpServletRequest request,HttpServletResponse response){

        try{
        Connection conn = DB.initializeDatabase();
        Statement allocationStatement = conn.createStatement();

        int longTermWorkers =  Integer.parseInt(request.getParameter("long_term"));
        int midTermWorkers = Integer.parseInt(request.getParameter("mid_term"));
        int shortTermWorkers = Integer.parseInt(request.getParameter("short_term"));

        // This array is to automate our insertion of workers
        int workersArray[] = {
            longTermWorkers,midTermWorkers,shortTermWorkers
        };

        for(int index=0;index<workersArray.length;index++){
        String query = "INSERT  into worker_allocation(production_line_id,number_of_workers) values("+(index+1)+","+workersArray[index]+")";
        
        // call this to execute insert or update queries
        allocationStatement.executeUpdate(query);
        }

        allocationStatement.close();
        conn.close();

        Message.alertSuccess(request,"Allocated Successfully");

        response.sendRedirect("worker_allocations");

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