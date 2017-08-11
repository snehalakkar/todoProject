app.controller('archiveCtrl', function($scope, $controller) {
	
	$controller('homeCtrl',{$scope:$scope});
		$scope.homecard=false;
		$scope.archievecard=true;
		$scope.trashcard=false;
		$scope.takenotecard=false;
		
		$scope.setmargintop={
				"margin-top" : "60px"
		}
});