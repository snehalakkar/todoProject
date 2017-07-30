app.controller('homeCtrl',
		function($scope, $rootScope, $state, userformService) {
			$scope.myVarheader = false;
			$scope.myVarfooter = false;
			$scope.showimages = false;

			if ($scope.myVarheader == false && $scope.myVarfooter == false) {
				$scope.getnotesMargin = {
					"margin-top" : "160px"
				};
			}
			$scope.noteInput = function() {

				$scope.myVarheader = true;
				$scope.myVarfooter = true;

				$scope.getnotesMargin = {
					"margin-top" : "250px"
				}
			}

			$scope.savenote = function() {

				$scope.myVarheader = false;
				$scope.myVarfooter = false;

				var method = "POST";
				var url = "app/saveTodo";

				var obj = {};
				obj.title = $scope.title;
				obj.description = $scope.description;

				var serviceobj = userformService.runservice(method, url, obj);

				serviceobj.then(function(response) {

					alert("************" + response);

					if (response.data.status == 200) {
						alert('success');
						alert(JSON.stringify(response));

						$scope.title = "";
						$scope.description = "";

					} else if (response.data.status == 420) {
						$scope.generatenewAccess();
						alert('not success');
					}
				})
			}

			// get all todotask from db.
			$rootScope.getNotes = function() {
				var method = "POST";
				var url = "app/getAllTodoTask";

				var obj = {};

				var serviceobj = userformService.runservice(method, url, obj);

				serviceobj.then(function(response) {

					if (response.status == 200) {
						alert('success');
						alert(JSON.stringify(response.data));
						alert(JSON.stringify(response.data[1].title));
						alert(JSON.stringify(response.data[1].description));
						$scope.records = response.data;

					} else {
						alert('not success');
					}
				})
			};

			/* calling getNotes() */
			$rootScope.getNotes();

			$scope.logout = function() {
				var method = "POST";
				var url = "app/logout";
				var obj = {};

				var serviceobj = userformService.runservice(method, url, obj);

				serviceobj.then(function(response) {

					if (response.status == 200) {
						alert('successfully logout...');
						alert(JSON.stringify(response));

						localStorage.removeItem("accessToken");
						localStorage.removeItem("refreshToken");
						$state.go("userLogin");

					} else {
						alert('not successfully logout');
						$state.go("homepage");
					}
				})
			}
		});

		$scope.generatenewAccess = function() {
			var method = "POST";
			var url = "generateNewaccessToken";
			var obj = {};
			var serviceobj = generateAccessService.runservice(method, url, obj);

		}
