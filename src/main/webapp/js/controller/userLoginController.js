app.controller('loginformCtrl', function($scope, $state, userformService) {

	$scope.login = function() {
		var method = "POST";
		var url = "userlogin";

		var serviceobj = userformService.runservice(method, url, $scope.user);

		serviceobj.then(function(response) {

			if (response.status == 200) {
				alert('success');
				alert(JSON.stringify(response));
				alert(JSON.stringify(response.data.accessToken));
				alert(JSON.stringify(response.data.refreshToken));
				var accessToken=JSON.stringify(response.data.accessToken);
				var refreshToken=JSON.stringify(response.data.refreshToken);
				
				localStorage.setItem("accessToken", accessToken);
				localStorage.setItem("refreshToken", refreshToken);
				
				$state.go("homepage");
			} else {
				alert('not success');
				$state.go("userLogin");
			}
		})
	}
});