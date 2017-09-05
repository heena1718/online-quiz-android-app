<?php

	$con = mysqli_connect("localhost","root","","playNwin") or die("Connection failed");
	
	$fname = $_REQUEST['f_name'];
	$lname = $_REQUEST['l_name'];
	$email = $_REQUEST['email'];
	$pass = $_REQUEST['pass'];
	$contact = $_REQUEST['contact'];
	
	$query ="insert into register(f_name,l_name,email,pwd,c_no,wallet) values ('$fname','$lname','$email','$pass','$contact','0')";
	
	if(mysqli_query($con,$query))
	{
		$result['status']="true";
	}
	else
	{
		$result['status']="false";
	}
	
	echo json_encode($result);
?>