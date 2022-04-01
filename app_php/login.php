<?php
	include 'bdd.php';
	include 'encriptacion.php';

	error_reporting(0);
	$correo = $_REQUEST['correo'];
	$passwd = $_REQUEST['passwd'];
	//Verificacion de usuario existente en la BDD, si existe, $res = 1 else $res = 0
	$cadena = "SELECT * FROM usuarios WHERE correo = '$correo' AND passwd = '$passwd' ";

	$res = $conexion->prepare($cadena);
	$res->execute();
	$datos = array();
	$res = $res->fetchAll();
	foreach ($res as $row) 
	{
	    $datos[]=$row;
	}
	echo json_encode($datos);
?>