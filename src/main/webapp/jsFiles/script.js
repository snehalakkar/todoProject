
var app = angular.module('myApp', []);
app.controller('formCtrl',function($scope,$log) {
	/*console.log("hhh");*/
	alert("hii");
//    $scope.fullName = "John";
    console.log($scope.fullName)
     $log.log('This is log.');
           
            $log.info('This is info.');
            $log.warn('This is warning.');
            $log.debug('This is debugging.');
    
//	var user;
 $scope.submitRegistration=function()
 {
	 
	 $scope.fullName = "John";
	 alert($scope.fullName);
	/*console.log("user details" ,user);*/
	// console.log(user.fullName);
 };
 
});

 
 /*$http({
     method : "POST",
     url : "/saveUser",
     data:user
 }).then(function mySuccess(response) {
     $scope.myWelcome = response.data;
     console.log(response.data);
 }, function myError(response) {
     $scope.myWelcome = response.statusText;
 });*/
 
