<?php

	$con = mysqli_connect("localhost","root","","playNwin") or die("Connection failed");
	
	$email = $_REQUEST['email'];
	$pwd = $_REQUEST['pass'];
	
	$q = "select * from register where email = '$email'";
	
	$table = mysqli_query($con,$q);
	if($row = mysqli_fetch_assoc($table))
	{
		if($pwd == $row['pwd'])
		{
			$result['status']="true";
			$result['id']=$row['id'];
			$result['email']=$row['email'];
			$result['wallet']=$row['wallet'];
		}
		else
		{
			$result['status']="false";
			$result['msg'] ="Password donot match";
		}
	}
	else
	{
		$result['status']="false";
		$result['msg'] ="User does not exist";
	}
	echo json_encode($result);
?>