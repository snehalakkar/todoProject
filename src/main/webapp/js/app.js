//main js file which decides the paths that where to go
//we use sanitize to read data in div
var app = angular.module('todo', [ 'ui.router', 'ngSanitize', 'ui.bootstrap',
		'ui.bootstrap.datetimepicker', '720kb.tooltips', 'ui.sortable',
		'ngCookies', 'ngImgCrop' ]);
app.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state('userLogin', {
		url : '/login',
		templateUrl : 'templates/userLogin.html',
		controller : 'loginformCtrl'
	})

	.state('userRegistration', {
		url : '/registration',
		templateUrl : 'templates/userRegistration.html',
		controller : 'regformCtrl'
	})

	.state('confirmEmail', {
		url : '/confirmEmail',
		templateUrl : 'templates/confirmationEmail.html',
	})

	.state('homepage', {
		url : '/home',
		templateUrl : 'templates/home.html',
		controller : 'homeCtrl'
	})

	.state('archivenote', {
		url : '/archive',
		templateUrl : 'templates/home.html',
		controller : 'archiveCtrl'
	})

	.state('trashnote', {
		url : '/trash',
		templateUrl : 'templates/home.html',
		controller : 'trashCtrl'
	})

	.state('remindernote', {
		url : '/reminder',
		templateUrl : 'templates/home.html',
		controller : 'reminderCtrl'
	})

	.state('resetPassword', {
		url : '/resetPassword',
		templateUrl : 'templates/resetPassword.html',
		controller : 'loginformCtrl'
	});

	/*
	 * .state('validateOtp', { url : '/validateOtp', templateUrl :
	 * 'templates/ValidateOtp.html', controller : 'loginformCtrl' })
	 * 
	 * .state('resetNewPassword', { url : '/resetNewPassword', templateUrl :
	 * 'templates/enterNewPwd.html', controller : 'loginformCtrl' });
	 */

	$urlRouterProvider.otherwise('/login');

});

// this is to use ng-model on div
app.directive('contenteditable', function() {
	return {
		restrict : 'A',
		require : '?ngModel',
		link : function(scope, element, attr, ngModel) {
			var read;
			if (!ngModel) {
				return;
			}
			ngModel.$render = function() {
				return element.html(ngModel.$viewValue);
			};
			element.bind('blur', function() {
				if (ngModel.$viewValue !== $.trim(element.html())) {
					return scope.$apply(read);
				}
			});
			return read = function() {
				console.log("read()");
				return ngModel.$setViewValue($.trim(element.html()));
			};
		}
	};
});

/** ************* IMAGE FILE UPLOAD**************** */
app.directive("ngFileSelect", function(fileReader, $timeout) {
	return {
		scope : {
			ngModel : '='
		},
		link : function($scope, el) {
			function getFile(file) {
				fileReader.readAsDataUrl(file, $scope).then(function(result) {
					$timeout(function() {
						$scope.ngModel = result;
					});
				});
			}

			el.bind("change", function(e) {
				var file = (e.srcElement || e.target).files[0];
				getFile(file);
			});
		}
	};
});

app.factory("fileReader", function($q, $log) {
	var onLoad = function(reader, deferred, scope) {
		return function() {
			scope.$apply(function() {
				deferred.resolve(reader.result);
			});
		};
	};

	var onError = function(reader, deferred, scope) {
		return function() {
			scope.$apply(function() {
				deferred.reject(reader.result);
			});
		};
	};

	var onProgress = function(reader, scope) {
		return function(event) {
			scope.$broadcast("fileProgress", {
				total : event.total,
				loaded : event.loaded
			});
		};
	};

	var getReader = function(deferred, scope) {
		var reader = new FileReader();
		reader.onload = onLoad(reader, deferred, scope);
		reader.onerror = onError(reader, deferred, scope);
		reader.onprogress = onProgress(reader, scope);
		return reader;
	};

	var readAsDataURL = function(file, scope) {
		var deferred = $q.defer();

		var reader = getReader(deferred, scope);
		reader.readAsDataURL(file);

		return deferred.promise;
	};

	return {
		readAsDataUrl : readAsDataURL
	};
});

// sortable drag and drop
/*
 * $scope.sortableOptions = { update: function(e, ui) { var logEntry =
 * tmpList.map(function(i){ return i.value; }).join(', ');
 * $scope.sortingLog.push('Update: ' + logEntry); }, stop: function(e, ui) { //
 * this callback has the changed model var logEntry = tmpList.map(function(i){
 * return i.value; }).join(', '); $scope.sortingLog.push('Stop: ' + logEntry); } };
 */