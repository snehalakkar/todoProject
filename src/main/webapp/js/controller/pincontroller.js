app.controller('pinCtrl', function($scope, $controller) {
	
	$controller('homeCtrl',{$scope:$scope});
		$scope.homecard=false;
		$scope.archievecard=false;
		$scope.trashcard=false;
		$scope.takenotecard=false;
		$scope.pincard=true;
		
		$scope.setmargintop={
				"margin-top" : "60px"
		}
});