app.controller('reminderCtrl', function($scope, $controller) {

	$controller('homeCtrl',{$scope:$scope});
	$scope.homecard = false;
	$scope.archievecard = false;
	$scope.trashcard = false;
	$scope.takenotecard = true;
	$scope.remindercard = true;
	$scope.pincard = false;

	$scope.setmargintop = {
		"margin-top" : "60px"
	}
});