<?php 

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "tododb";

$conn = new mysqli($servername, $username, $password, $dbname);

if (mysqli_connect_errno())
{
    echo "Failed to connect to database: " . mysqli_connect_error();
}

/*else
{
	echo "Connection Established";
}*/

?> 