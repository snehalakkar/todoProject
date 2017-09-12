app.controller('loginformCtrl', function($scope, $state, userformService) {

	$scope.login = function() {
		var method = "POST";
		var url = "userlogin";

		var serviceobj = userformService.runservice(method, url, $scope.user);

		serviceobj.then(function(response) {
			console.log("response.status ", response.status);
			if (response.status == 200) {

				localStorage.setItem("accessToken", response.data.accessToken);
				localStorage
						.setItem("refreshToken", response.data.refreshToken);

				$state.go("homepage");
			} else {
				console.log('login unsucessfull');
				$state.go("userLogin");
			}
		})
	}

	$scope.sendMail = function() {
		var method = "POST";
		var url = "forgotpwdApi";

		var serviceobj = userformService.resetpwd(method, url, $scope.user);
		serviceobj.then(function(response) {
			
			if (response.data.status  == 5) {
				console.log("otp send to mail ");
				$state.go('validateOtp');
			} 
			$scope.valEmailError = "Email does not exist";
		});
	};

	$scope.validateOtp = function() {
		var method = "POST";
		var url = "validateOtp";
		var obj={};
		obj.otp=$scope.otp;
		var serviceobj = userformService.resetpwd(method, url,obj);

		serviceobj.then(function(response) {
			console.log("response33 ", response);

			if (response.data.status == 6) {
				console.log("otp correct ");
				$state.go('resetNewPassword');
			}
			else{
				console.log("otp incorrect ");
				$scope.valOtpError = "Otp is not correct or timeout";
			}
		});
	}
	
	$scope.resetNewPassword=function(){
		var method = "POST";
		var url = "resetNewPassword";
		console.log($scope.user);
		var serviceobj = userformService.resetpwd(method, url,$scope.user);
		serviceobj.then(function(response) {
			
		});
	}
});