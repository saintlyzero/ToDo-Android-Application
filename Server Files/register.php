<?php

include('connect.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		
		$name=$_POST['name'];
		$username=$_POST['username'];
		$password=$_POST['password'];
							
		$sql = "INSERT INTO user (username, password, name)
		VALUES ('$username','$password','$name')";

		if ($conn->query($sql) === TRUE) 
		{
			echo "Registerd Successfully!";
		} 
		else 
		{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}	
	}
?>
