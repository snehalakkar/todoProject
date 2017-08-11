app.controller('loginformCtrl', function($scope, $state, userformService) {

	$scope.login = function() {
		var method = "POST";
		var url = "userlogin";

		var serviceobj = userformService.runservice(method, url, $scope.user);

		serviceobj.then(function(response) {

			if (response.status == 200) {
				
				console.log("accessToken",response.data.accessToken);
				console.log("refreshToken",response.data.refreshToken);
				localStorage.setItem("accessToken", response.data.accessToken);
				localStorage.setItem("refreshToken", response.data.refreshToken);
				
				$state.go("homepage");
			} else {
				$state.go("userLogin");
			}
		})
	}
});