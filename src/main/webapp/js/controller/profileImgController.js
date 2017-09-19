app.controller('profileImageController', function($scope, $rootScope, $state,
		userformService, $uibModalInstance) {

	$scope.myImage = '';
	$scope.myCroppedImage = '';
	$scope.displayimgbeforecrop = false;
	$scope.displaycropimg=false;
	
	var handleFileSelect = function(evt) {
		var file = evt.currentTarget.files[0];
		var reader = new FileReader();
		reader.onload = function(evt) {
			$scope.$apply(function($scope) {
				$scope.myImage = evt.target.result;
			});
		};
		reader.readAsDataURL(file);
		$scope.displayimgbeforecrop = true;
		$scope.displaycropimg=true;
	};

	$scope.profileImage = function() {
		angular.element(document.querySelector('#fileInput')).on('change',
				handleFileSelect);
	}

	$scope.saveprofilePic = function(myCroppedImage) {
		console.log('in saveprofilePic');
		var method = "POST";
		var url = "app/updateUserProfile";
		var obj = {};
		obj.profile = myCroppedImage;
		obj.userId = $scope.userId;
		console.log(obj.profile);
		console.log($scope.userId);

		var serviceobj = userformService.runservice(method, url, obj);

		serviceobj.then(function(response) {
			console.log('response profile ', response);
			if (response.status == 200) {
				console.log('profile set successfully');
				$state.reload();
			}
		})

	}
});