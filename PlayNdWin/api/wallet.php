<?php

	$con = mysqli_connect("localhost","root","","playNwin") or die("Connection failed");
	
	$id = $_REQUEST['id'];
	
	$q="select * from register where id = $id";
	
	$table = mysqli_query($con,$q);
	
	if($row = mysqli_fetch_assoc($table))
	{
		$result['wallet']=$row['wallet'];
	}
	echo json_encode($result);
?>