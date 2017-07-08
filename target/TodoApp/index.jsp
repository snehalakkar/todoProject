<html ng-app="myApp">
<head>
<script src="bower_components/angular/angular.js"></script>
<script src="bower_components/jquery/dist/jquery.js"></script>
<script src="jsFiles/script.js"></script>

</head>
<body>

	<h2>Register here......</h2>

	<div ng-controller="formCtrl">
	{{fullName}}
		<form action="">
			FirstName :<input type="text" id="fullName" ng-model="user.fullName"><br>
			
			Email :<input type="text" id="email" ng-model="user.email"><br>
			Mobile :<input type="number" id="mobile" ng-model="user.mobile">
			<br> Password :<input type="password" id="password"
				ng-model="user.password"> <br> <br>
			<!-- <button type="button" ng-click="submitRegistration(user)">submit</button> -->
			<input type="button" value="Register" ng-click="submitRegistration()" />
		</form>
	</div>
</body>
<!-- <script>
//self executing function
 (function(){
	angular.module('myApp',[])
	.controller('formCtrl',function(){
		console.log("hello");
	});
	function formCtrl()
	{
		console.log("hello");
	}
})();
</script> -->
</html>
