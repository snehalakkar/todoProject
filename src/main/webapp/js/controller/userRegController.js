app.controller('regformCtrl', function($scope, $state, userformService) {

	$scope.submitRegistration = function() {
		var method = "POST";
		var url = "registerUser1";

		var serobj = userformService.runservice(method, url, $scope.user);

		serobj.then(function mySuccess(response) {
			$scope.myWelcome = response.data;
			console.log(response.data);
			response.data = JSON.stringify(response.data);
			$state.go("confirmEmail");

		}, function myError(response) {
			$scope.myWelcome = response.statusText;
			alert(response.data.status);
			$state.go("userRegistration");
		});
	};
});