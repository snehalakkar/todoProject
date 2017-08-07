app.controller('homeCtrl', function($scope, $state, $uibModal, userformService,
		generateAccessService) {
	$scope.myVarheader = false;
	$scope.myVarfooter = false;
	$scope.showimages = false;

	$scope.noteInput = function() {
		$scope.myVarheader = true;
		$scope.myVarfooter = true;

	}

	/* to set type of view as grid */
	$scope.gridview1 = function() {
		$scope.showgrid = true;
		$scope.showlist = false;
		$scope.toggleview = " col-lg-4 col-md-6 col-sm-12 col-xs-12 grid";
		$scope.colspacing = "col-lg-2";
		$scope.newcolspace = "col-lg-8";

		localStorage.setItem("view", "grid");

	}

	/* to set type of view as list */
	$scope.listview1 = function() {
		$scope.showlist = true;
		$scope.showgrid = false;
		$scope.toggleview = "col-lg-12 col-md-10 col-sm-12 col-xs-12 list";
		$scope.colspacing = "col-lg-2";
		$scope.newcolspace = "col-lg-8";

		localStorage.setItem("view", "list");
	}

	/* to check for view in localstorage for each refresh */
	if (localStorage.view == "list") {
		$scope.listview1();
	} else {
		$scope.gridview1();
	}

	/* to save notes in db */
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

			if (response.data.status == 120) {
				alert('check');
				var serviceobj1 = generateAccessService.runservice();
				alert(serviceobj1);
				serviceobj1.then(function(response) {
					alert("**" + response);
					if (response.status == 200) {
						alert('successfully access updated...');
						alert(response);

						alert(JSON.stringify(response.data.accessToken));
						alert(JSON.stringify(response.data.refreshToken));
						var accessToken = JSON
								.stringify(response.data.accessToken);
						var refreshToken = JSON
								.stringify(response.data.refreshToken);

						localStorage.setItem("accessToken", accessToken);
						localStorage.setItem("refreshToken", refreshToken);

						var method = "POST";
						var url = "app/saveTodo";

						var obj = {};
						obj.title = $scope.title;
						obj.description = $scope.description;

						var serviceobj = userformService.runservice(method,
								url, obj);
					} else if (response.status == 404) {
						alert('logout');
						$state.go("userLogin");
					}
				})
				alert('not success');
			}

			if (response.data.status == 1) {
				alert('success');
				alert(JSON.stringify(response));

				$state.reload();

				$scope.title = "";
				$scope.description = "";

			}
		})
	}

	// get all todotask from db.
	$scope.getNotes = function() {
		var method = "POST";
		var url = "app/getAllTodoTask";

		var obj = {};

		var serviceobj = userformService.runservice(method, url, obj);

		serviceobj.then(function(response) {

			if (response.status == 200) {
				$scope.records = response.data.reverse();

				/* setting name and email to use that in profile */
				$scope.username = response.data[0].user.fullName;
				$scope.firstchar = $scope.username[0];
				console.log($scope.firstchar);
				$scope.useremail = response.data[0].user.email;

			} else {
				$state.go('userLogin');
			}
		})
	}

	/* calling getNotes() */
	$scope.getNotes();

	/* open dropdown on img list to delete and make a copy */
	$scope.opendropdown = function(id) {
		var method = "POST";
		var url = "app/deleteTodo/" + id;
		var obj = {};

		var serviceobj = userformService.runservice(method, url, obj);
		serviceobj.then(function(response) {

			if (response.status == 200) {

				/* loading page */
				$state.reload();
			}
		});

	}

	/* open popup to update todo */
	$scope.openModal = function(x) {

		$scope.modalInstance = $uibModal.open({
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'templates/updatepopup.html',
			size : 'md',
			controller : function($scope, $uibModalInstance) {
				var id = x.todoId;
				this.title = x.title;
				this.description = x.description;
				this.user = x.user;
				this.updatecolor = x.color;

				/* update todo in DB */
				this.update = function() {
					var $ctrl = this;
					var method = "POST";
					var url = "app/updateTodo/" + id;
					var obj = {};
					obj.title = $ctrl.title;
					obj.description = $ctrl.description;
					obj.user = this.user;
					obj.color = $ctrl.updatecolor;
					console.log("new data");
					console.log(obj);
					var serviceobj = userformService.runservice(method, url,
							obj);
					serviceobj.then(function(response) {

						if (response.status == 200) {

							$state.reload();
						}
					});
					$uibModalInstance.close();
				}
			},
			controllerAs : '$ctrl',
		});
	}

	/* logout user and send back to login page */
	$scope.logout = function() {
		var method = "POST";
		var url = "app/logout";
		var obj = {};

		var serviceobj = userformService.runservice(method, url, obj);

		serviceobj.then(function(response) {

			if (response.status == 200) {

				localStorage.removeItem("accessToken");
				localStorage.removeItem("refreshToken");
				$state.go("userLogin");

			}
		});
	};

	/* set color of div */
	$scope.colorchange = function(x, colorcode) {
		x.color = colorcode;
		console.log('set color ' + x.color);

		// calling update method to set color in DB
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);

		serviceobj.then(function(response) {
			if (response.status == 200) {
				console.log('color updated successfully...');
				$state.reload();
			} else {
				console.log('color not updated...');
			}
		});
	}
});
