<!DOCTYPE html>
<html ng-app="todo">
<head>

 
<link rel="icon" href="images/google_keep1600.png">

<link rel="stylesheet"	href="bower_components/bootstrap-css-only/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="bower_components/angular-bootstrap-datetimepicker/src/css/datetimepicker.css">
<link rel="stylesheet" type="text/css" href="css/loginRegfile.css">
<link rel="stylesheet" type="text/css" href="css/home.css">

<!-- for uib modal work -->
<link rel="stylesheet" type="text/css" href="bower_components/angular-bootstrap/ui-bootstrap-csp.css">
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
<script src="js/controller/archivecontroller.js"></script>
<script src="js/controller/trashcontroller.js"></script>
<script src="js/controller/reminderController.js"></script>
<script src="js/service/generatenewAccess.js"></script>
<script src="bower_components/angular-sanitize/angular-sanitize.js"></script>
<script src="bower_components/moment/moment.js"></script>
<!-- fb share  -->
<script type="text/javascript" src="http://connect.facebook.net/en_US/sdk.js"></script>

<!-- for datetimepicker -->
<script src="bower_components/angular-bootstrap-datetimepicker/src/js/datetimepicker.js"></script>
<script src="bower_components/angular-bootstrap-datetimepicker/src/js/datetimepicker.templates.js"></script>

<script src="bower_components/angular-tooltips/dist/angular-tooltips.js"></script>

<!-- for uib modal work -->
<script src="bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>

<!-- for drag and drop using sortable -->
<script src="bower_components/angular-ui-sortable/sortable.js"></script>

</html>