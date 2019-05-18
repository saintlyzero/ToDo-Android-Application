<?php

include('connect.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		$taskId=$_POST['taskId'];
		$taskdescription=$_POST['taskdescription'];
		$taskname=$_POST['taskname'];
		$status=$_POST['status'];
							
		$sql = "UPDATE task SET name = '$taskname', description = '$taskdescription', status = '$status' where id = '$taskId'";
		
		if ($conn->query($sql) === TRUE) 
		{
			echo "Updated Task";
		} 
		else 
		{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}	
	}
?>
