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
});