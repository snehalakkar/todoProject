app.controller('loginformCtrl', function($scope, $state, userformService) {

	$scope.login = function() {
		var method = "POST";
		var url = "userlogin";

		var serviceobj = userformService.runservice(method, url, $scope.user);

		serviceobj.then(function(response) {

			if (response.status == 200) {
				var accessToken=JSON.stringify(response.data.accessToken);
				var refreshToken=JSON.stringify(response.data.refreshToken);
				
				localStorage.setItem("accessToken", accessToken);
				localStorage.setItem("refreshToken", refreshToken);
				
				$state.go("homepage");
			} else {
				$state.go("userLogin");
			}
		})
	}
});