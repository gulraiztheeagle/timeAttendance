<html>
 
Dear ${managerName},<br><br>
 ${name} has requested a ${subject} for you to approve. Following are the details.<br><br> 

<head>
<style>
table, th, td {
  border: 1px solid black;
  border-collapse: collapse;
}
th, td {
  padding: 5px;
  text-align: left;
}
th {
  text-align: left;
}
</style>
</head>
   
 <body>
 <div style="overflow-x:auto;"> 
<table>

   <tr>
      <th>Start Date</th>
      <td>${startDate?date}</td>
   </tr>
   <tr>
      <th>End Date</th>
      <td>${endDate?date}</td>
   </tr>
      <tr>
      <th>Submission Date</th>
      <td>${submissionDate?date}</td>
   </tr>
   
   <tr>
      <th>Reason</th>
      <td>${reason}</td>
   </tr>
   <tr>
      <th>Comment</th>
      <td>${(remarks)!" "}</td> 
   </tr>
</table>
</div>
<br><br><br><br><br>

*** This is an auto-generated email, please do not reply ***

    </body>
</html>