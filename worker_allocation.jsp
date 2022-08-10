
<%@ page import = "java.sql.*" %>
<%@ page import = "java.io.*" %>
<%@ page import = "java.util.*" %>

<%
ResultSet recentAllocations = (ResultSet)getServletContext().getAttribute("recent_allocations");
%>

<!DOCTYPE html>
<html lang="en">
<jsp:include page="./inc/_assets.jsp" />
<body>
  <div class="container-scroller">
    <!-- partial:partials/_navbar.html -->
    <jsp:include page="./inc/_header.jsp" />
    <!-- partial -->
    <div class="container-fluid page-body-wrapper">
      <!-- partial -->
      <!-- partial:partials/_sidebar.html -->
    
      <jsp:include page="./inc/_sidebar.jsp" />

      <!-- partial -->
      <div class="main-panel">
        <div class="content-wrapper">
          <div class="page-header">
            <h3 class="page-title">
              Worker Allocations
            </h3>
          </div>
          <!-- Content here -->



<div class="row">
    <div class="col-lg-6">
    <div class="card card-primary">

    <div class="card-header">

    <h5>Production Line Worker Allocation<h5>

    </div>



    <div class="card-body">

    <form method="post" action="allocate_workers" class="form">

         <div class="form-group">
        <label>Short Term Product line</label>
        <input type="number" name="short_term" required class="form-control short-term" placeholder="Short Term Product Line..." value="1">
        </div>


        

        <div class="form-group">
        <label>Mid Term Product line (Half of Long Term)</label>
        <input type="number" name="mid_term" required class="form-control mid-term no-pointer-events " placeholder="Mid Term Product Line..."    value="1">
        </div>

        <div class="form-group">
        <label>Long Term Product Line(4 times Short term)</label>
        <input type="number" name="long_term" required class="form-control long-term no-pointer-events " style="pointer-events:none" placeholder="Long Term Product Line..."  value="1" >
        </div>

        <button type="submit" name="allocate_workers" class="btn btn-primary">Allocate Workers Now</button>
    </form>

    </div>

    

    </div>

    </div>
      




    <% if(recentAllocations!=null){ %>
    <div class="col-lg-6">
    <div class="card card-primary">

    <div class="card-header">

    <h5>Recent Allocations<h5>

    </div>

    <div class="card-body">


    <table class="table">
    <tr>
        <th>Production Line</th>
        <th>Number Of workers</th>
        <th>Allocation Date</th>
    <tr>

    <%if(recentAllocations.next()){ do{ %>
    <tr>
        <td><%=recentAllocations.getString("production_line_name")%></td>
        <td><%=recentAllocations.getString("number_of_workers")%></td>
        <td><%=recentAllocations.getString("allocation_date")%></td>
    <tr>

    <% }while(recentAllocations.next());} %>


    </table>

    </div>

    

    </div>

    </div>

<% } %>
    






















  </div>



























          <!-- Content Ends -->
       
       
       
        </div>
        <!-- content-wrapper ends -->
        <!-- partial:partials/_footer.html -->
        <jsp:include page="./inc/_footer.jsp"/>
        <!-- partial -->
      </div>
      <!-- main-panel ends -->
    </div>
    <!-- page-body-wrapper ends -->
  </div>
  <!-- container-scroller -->
  <jsp:include page="./inc/_scripts.jsp"/>

  <script>


  (function(){


    let longTermInput = document.querySelector(".long-term");
    let midTermInput = document.querySelector(".mid-term");
    let shortTermInput = document.querySelector(".short-term");

   
    shortTermInput.addEventListener("input",(ev)=>{
       let value = ev.target.value;


        if(value<0){
            ev.target.value = 0;
            value = 0;
        }

       longTermInput.value = 4*value; 
       midTermInput.value = 2*value; 
    });

  })();


  </script>
  </body>

</html>