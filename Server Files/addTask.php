<?php

include('connect.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		$taskdescription=$_POST['taskdescription'];
		$taskname=$_POST['taskname'];
		$username=$_POST['username'];
		$status=$_POST['status'];
							
		$sql = "INSERT INTO task (name, description, status, username)
		VALUES ('$taskname','$taskdescription','$status','$username')";

		if ($conn->query($sql) === TRUE) 
		{
			echo mysqli_insert_id($conn);
			// Returns the Auto-increment ID generated of the new record
		} 
		else 
		{
			//echo "Error: " . $sql . "<br>" . $conn->error;
		}	
	}
?>
