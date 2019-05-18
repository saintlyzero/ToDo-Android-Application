<?php

include('connect.php');

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		
		$username=$_POST['username'];
				
		$return_arr = array();
		
		$sql = "SELECT * FROM task where username = '$username'";
	
		$result = mysqli_query($conn,$sql);
	
		$response = array(); 
		
		while($row = mysqli_fetch_array($result))
		{
			$temp = array(); 
			$temp['TaskId']=$row['id'];
			$temp['TaskName']=$row['name'];
			$temp['TaskDescription']=$row['description'];
			$temp['status']=$row['status'];
			
			array_push($response,$temp);
		}

		echo json_encode($response);	
	}
	else
	{
		echo 'error';
	}
?>
