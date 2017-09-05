<?php
	$connection = mysqli_connect('localhost', 'root', '', 'playNwin');
	$c_id = $_REQUEST['c_id'];
	$query = "SELECT * FROM question where c_id = $c_id order by RAND() LIMIT 1";
	$res = mysqli_query($connection, $query);
	
	while($row = mysqli_fetch_array($res)){
		$result['ques'] = $row['ques'];
		$result['o1'] = $row['optiona'];
		$result['o2'] = $row['optionb'];
		$result['o3'] = $row['optionc'];
		$result['o4'] = $row['optiond'];
		$result['ans'] = $row['c_ans'];
	}
	echo json_encode($result);
?>
