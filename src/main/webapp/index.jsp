<!DOCTYPE html>
<html ng-app="todo">
<head>

<link rel="stylesheet"	href="bower_components/bootstrap-css-only/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="css/loginRegfile.css">
<link rel="stylesheet" type="text/css" href="css/home.css">
</head>
<body>
	<ui-view>
	</ui-view>
</body>
<script src="bower_components/jquery/dist/jquery.js"></script>
<script src="bower_components/angular/angular.js"></script>
<script	src="bower_components/angular-ui-router/release/angular-ui-router.js"></script>
<script src="bower_components/bootstrap/dist/js/bootstrap.js"></script>
<script src="js/app.js"></script>
<script src="js/controller/userLoginController.js"></script>
<script src="js/controller/userRegController.js"></script>
<script src="js/controller/homeController.js"></script>
<script src="js/service/todoservice.js"></script>
<script src="bower_components/angular-sanitize/angular-sanitize.js"></script>
</html>