<?php

include('connect.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
				
		$taskId=$_POST['taskId'];
							
		$sql = "DELETE from task where id = '$taskId'";
		
		if ($conn->query($sql) === TRUE) 
		{
			echo "Task Deleted";
		} 
		else 
		{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}	
	}
?>
