app.controller('trashCtrl', function($scope, $controller) {
	
	$controller('homeCtrl',{$scope:$scope});
		$scope.homecard=false;
		$scope.archievecard=false;
		$scope.takenotecard=false;
		$scope.trashcard=true;
		
		$scope.setmargintop={
				"margin-top" : "60px"
		}
});