//main js file which decides the paths that where to go
//we use sanitize to read data in div
var app = angular.module('todo', [ 'ui.router' ,'ngSanitize','ui.bootstrap','ui.bootstrap.datetimepicker','720kb.tooltips','ui.sortable']);
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
	});

	$urlRouterProvider.otherwise('/login');

});


//this is to use ng-model on div
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

// sortable drag and drop
/*$scope.sortableOptions = {
	    update: function(e, ui) {
	      var logEntry = tmpList.map(function(i){
	        return i.value;
	      }).join(', ');
	      $scope.sortingLog.push('Update: ' + logEntry);
	    },
	    stop: function(e, ui) {
	      // this callback has the changed model
	      var logEntry = tmpList.map(function(i){
	        return i.value;
	      }).join(', ');
	      $scope.sortingLog.push('Stop: ' + logEntry);
	    }
	  };
*/