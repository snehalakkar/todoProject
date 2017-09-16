app.controller('homeCtrl', function($scope, $state, $uibModal, $interval,$cookies,
		userformService, generateAccessService) {
	
	console.log("ch acc token is there or not ",localStorage.getItem("accessToken"));
	if(localStorage.getItem("accessToken")== undefined || localStorage.getItem("accessToken") == null){
		// Retrieving a cookie
		  var googleaccessToken = $cookies.get('googleaccessToken');
		  console.log("googleaccessToken ", googleaccessToken);
		  var googlerefreshToken = $cookies.get('googlerefreshToken');
		  console.log("googlerefreshToken ", googlerefreshToken);
		  localStorage.setItem("accessToken",googleaccessToken);
		  localStorage.setItem("refreshToken",googlerefreshToken);
		  $cookies.remove("googleaccessToken");
		  $cookies.remove("googlerefreshToken");
	}
	
	$scope.myVarheader = false;
	$scope.myVarfooter = false;
	$scope.showimages = false;
	$scope.displayremainder = false;
	$scope.pincard = true;

	$scope.noteInput = function() {
		$scope.myVarheader = true;
		$scope.myVarfooter = true;
	}

	// set archieve and homecard logic
	$scope.homecard = true;
	$scope.archievecard = false;
	$scope.takenotecard = true;
	$scope.trashcard = false;
	$scope.remindercard = false;

	$scope.records = new Array();

	// to display greater than todays reminder
	$scope.checkreminder = new Date();

	// just to display next week day in html
	var nextweek = new Date();
	nextweek.setDate(nextweek.getDate() + 7);
	var weekday = new Array(7);
	weekday[0] = "Sun";
	weekday[1] = "Mon";
	weekday[2] = "Tue";
	weekday[3] = "Wed";
	weekday[4] = "Thu";
	weekday[5] = "Fri";
	weekday[6] = "Sat";
	$scope.nextweekday = weekday[nextweek.getDay()];

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
		obj.image = $scope.image;
		console.log('img ', $scope.image);
		var serviceobj = userformService.runservice(method, url, obj);

		serviceobj.then(function(response) {

			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {
						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/saveTodo";

						var obj = {};
						obj.title = $scope.title;
						obj.description = $scope.description;

						var serviceobj1 = userformService.runservice(method,
								url, obj);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.data.status == 1) {
								$state.reload();
								$scope.title = "";
								$scope.description = "";
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}

			if (response.data.status == 1) {
				$state.reload();
				$scope.title = "";
				$scope.description = "";
			}
		})
	}

	// this timeout function will call notify after every 10sec
	// 10000 means delay in milisec (i.e.,10 sec)

	/*
	 * $interval(function() { console.log('in notify'); var abc = new
	 * Date().getTime();
	 * 
	 * for (var i = 0; i < $scope.records.length; i++) {
	 * 
	 * var rem1 = $scope.records[i].reminder;
	 * 
	 * console.log("diffreence:", rem1 - abc); console.log(rem1 - abc < 300000);
	 * 
	 * if (rem1 - abc > 0 && rem1 - abc < 300000) { console.log("reminder set",
	 * $scope.records[i].title); var alerted = localStorage.getItem('alerted') ||
	 * ''; var remcount = new Array();
	 * 
	 * alert("reminder set", JSON .stringify($scope.records[i].title)); remcount =
	 * $scope.records[i].title; console.log("remcount ", remcount); } } count =
	 * count - 1; }, [ 12000 ]);
	 */

	/*
	 * $interval(function() { console.log('in notify'); var abc = new
	 * Date().getTime();
	 * 
	 * for (var i = 0; i < $scope.records.length; i++) {
	 * 
	 * var rem1 = $scope.records[i].reminder;
	 * 
	 * console.log("diffreence:", rem1 - abc); console.log(rem1 - abc < 300000);
	 * 
	 * if (rem1 - abc > 0 && rem1 - abc < 300000) { console.log("reminder set",
	 * $scope.records[i].title);
	 * 
	 * var alerted = localStorage.getItem('alerted') || ''; if (alerted !=
	 * 'yes') { alert("My alert."); localStorage.setItem('alerted', 'yes'); } } } }, [
	 * 5000 ]);
	 */

	// get all todotask from db.
	/*
	 * $scope.getNotes = function() { var method = "POST"; var url =
	 * "app/getAllTodoTask";
	 * 
	 * var obj = {};
	 * 
	 * var serviceobj = userformService.runservice(method, url, obj);
	 * 
	 * serviceobj.then(function(response) { if (response.status == 200) {
	 * $scope.records = response.data.reverse(); console.log("records",
	 * $scope.records); setting name and email to use that in profile
	 * $scope.username = response.data[0].user.fullName; $scope.firstchar =
	 * $scope.username[0]; console.log($scope.firstchar); $scope.useremail =
	 * response.data[0].user.email; console.log(response); } else {
	 * $state.go('userLogin'); } }) }
	 */
	
	// get all todotask from db.
	$scope.getNotes = function() {
		console.log('in11 ');
		var method = "POST";
		var url = "app/getAllTodoTask";

		var obj = {};

		var serviceobj = userformService.runservice(method, url, obj);

		serviceobj.then(function(response) {
			console.log('response11 ', response);
			if (response.status == 200) {
				$scope.records = response.data.reverse();
				console.log("records", $scope.records);

				/* calling displayPin() */
				$scope.displayPin($scope.records);

				/* setting name and email to use that in profile */
				$scope.username = response.data[0].user.fullName;
				$scope.firstchar = $scope.username[0];
				console.log($scope.firstchar);
				$scope.useremail = response.data[0].user.email;
				$scope.userId = response.data[0].user.userId;
				$scope.profile = response.data[0].user.profile;
				console.log($scope.userId);

			} else if (response.status == 404 || response.status == 500) {
				$state.go('userLogin');
			}
		})
	}

	/* calling getNotes() */
	$scope.getNotes();

	/* to display pin name */
	$scope.displayPin = function(allNotes) {
		console.log("allNotes ", allNotes);
		$scope.counter = 0;
		for (var i = 0; i < allNotes.length; i++) {
			if (allNotes[i].pin == true) {
				$scope.counter++;
			}
		}
		console.log("$scope.counter ", $scope.counter);
		if ($scope.counter > 0 && $scope.remindercard == false
				&& $scope.trashcard == false && $scope.archievecard == false) {
			$scope.pincard = true;
			$scope.othersName = true;
			$scope.displayPinName = true;
		} else {
			$scope.pincard = false;
			$scope.othersName = false;
			$scope.displayPinName = false;
		}
	}

	/* open dropdown on img list to delete and make a copy */
	$scope.deletenote = function(id) {
		var method = "POST";
		var url = "app/deleteTodo/" + id;
		var obj = {};

		var serviceobj = userformService.runservice(method, url, obj);
		serviceobj.then(function(response) {

			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {
						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/deleteTodo/" + id;
						var obj = {};

						var serviceobj1 = userformService.runservice(method,
								url, obj);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('delete todo successfully');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}

			if (response.status == 200) {
				console.log('delete todo successfully');
				$state.reload();
			}
		});
	}

	/* setting reminder */
	$scope.createReminder = function(x, reminderTime) {

		if (reminderTime == "today") {
			var today = new Date();
			today.setHours(20, 00, 00);
			x.reminder = today;
			console.log("today" + today);
		} else if (reminderTime == "tomarrow") {
			var tomarrow = new Date();
			tomarrow.setDate(tomarrow.getDate() + 1);
			tomarrow.setHours(8, 00, 00);
			x.reminder = tomarrow;
			console.log("tomarrow" + tomarrow);
		} else if (reminderTime == "nextweek") {
			var nextweek = new Date();
			nextweek.setDate(nextweek.getDate() + 7);
			nextweek.setHours(8, 00, 00);
			x.reminder = nextweek;
			console.log("nextweek" + nextweek);
		} else {
			console.log(x.reminder);
		}
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {

			if (response.status == 200) {
				console.log("reminder updated successfully...");
				$scope.getNotes();
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
				this.image = x.image;
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
					obj.image = $ctrl.image;
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
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {
						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/logout";
						var obj = {};

						var serviceobj1 = userformService.runservice(method,
								url, obj);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.data.status == 555
									|| response.data.status == -555) {
								localStorage.removeItem("accessToken");
								localStorage.removeItem("refreshToken");
								$state.go("userLogin");
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			} else if (response.data.status == 555
					|| response.data.status == -555) {
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
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {

						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/updateTodo/" + x.todoId;

						var serviceobj1 = userformService.runservice(method,
								url, x);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('color updated successfully...');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}
			if (response.status == 200) {
				console.log('color updated successfully...');
				$state.reload();
			}
		})
	};

	/* to delete the set reminder */
	$scope.deleteReminder = function(x) {
		console.log("inside deletereminder");
		console.log(x.reminder);
		x.reminder = null;
		console.log(x.reminder);
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {
						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/updateTodo/" + x.todoId;

						var serviceobj1 = userformService.runservice(method,
								url, x);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('reminder delete successfully...');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}

			if (response.status == 200) {
				console.log('reminder delete successfully...');
				$state.reload();
			}
		})
	};

	/* to set archieve in todoTask */
	$scope.archievenotes = function(x) {
		console.log('inside archieve');
		x.archieve = true;
		x.pin = false;
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {
						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/updateTodo/" + x.todoId;

						var serviceobj1 = userformService.runservice(method,
								url, x);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('archieve set successfully...');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}

			if (response.status == 200) {
				console.log('archieve set successfully...');
				$state.reload();
			}
		})
	};

	/* to unarchieve in todoTask */
	$scope.unarchievenotes = function(x) {
		console.log('inside unarchieve');
		x.archieve = false;
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {
						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/updateTodo/" + x.todoId;

						var serviceobj1 = userformService.runservice(method,
								url, x);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('unarchieve set successfully...');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}

			if (response.status == 200) {
				console.log('unarchieve set successfully...');
				$state.reload();
			}
		})
	};

	/* to trash todo */
	$scope.trashnote = function(x) {
		console.log('inside trash');
		x.trash = true;
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {

						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/updateTodo/" + x.todoId;

						var serviceobj1 = userformService.runservice(method,
								url, x);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('todo trash successfully...');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}

			if (response.status == 200) {
				console.log('todo trash successfully...');
				$state.reload();
			}
		});
	}

	/* to restore todo */
	$scope.restore = function(x) {
		console.log('inside restore');
		x.trash = false;
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.data.status == 120) {
				console.log('status 120 acc exp');
				var serviceobj1 = generateAccessService.runservice();
				serviceobj1.then(function(response) {
					console.log("response ", response);
					if (response.data.status == 11) {

						localStorage.setItem("accessToken",
								response.data.tokens.accessToken);
						localStorage.setItem("refreshToken",
								response.data.tokens.refreshToken);

						var method = "POST";
						var url = "app/updateTodo/" + x.todoId;

						var serviceobj1 = userformService.runservice(method,
								url, x);
						serviceobj1.then(function(response) {
							console.log("response with new acc ", response);
							if (response.status == 200) {
								console.log('todo restore successfully...');
								$state.reload();
							}
						})
					}
					if (response.data.status == -11) {
						console.log('logout bcoz refresh also expired');
						$state.go("userLogin");
					}
				})
			}
			if (response.status == 200) {
				console.log('todo restore successfully...');
				$state.reload();
			}
		});
	}

	$scope.pinNote = function(x) {
		console.log('inside pin');
		x.pin = true;
		x.archieve = false;
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.status == 200) {
				console.log('todo pin successfully...');
				$state.reload();
			}
		})
	}

	$scope.unpinNote = function(x) {
		console.log('inside unpin');
		x.pin = false;
		var method = "POST";
		var url = "app/updateTodo/" + x.todoId;

		var serviceobj = userformService.runservice(method, url, x);
		serviceobj.then(function(response) {
			if (response.status == 200) {
				console.log('todo unpin successfully...');
				$state.reload();
			}
		})
	}

	$scope.refresh = function() {
		console.log('refresh...');
		window.location.reload();
	}

	/* share todoNotes with fb */
	$scope.shareWithFB = function(todo) {
		console.log("facebook share")
		FB.init({
			appId : '114643112594248',
			status : true,
			xfbml : true,
			version : 'v2.7',
		});

		FB.ui({
			method : 'share_open_graph',
			action_type : 'og.shares',
			action_properties : JSON.stringify({
				object : {
					'og:title' : todo.title.replace(/<[^>]+>/gm, ' ').replace(
							/\&nbsp;/g, ''),
					'og:description' : todo.description.replace(/<[^>]+>/gm,
							' ').replace(/\&nbsp;/g, ''),
				}
			})
		},
		// callback
		function(response) {
			if (response && !response.error_message) {
				console.log('successfully shared...');
			} else {
				console.log('not successfully shared...');
			}
		});
	}

	// for add image logic
	$scope.addImage = function() {
		alert('in add img');
		document.getElementById("addimginput").click();
	}

	/*
	 * $scope.setprofile = function() {
	 * document.getElementById("profileinput").click(); }
	 * 
	 * $scope.setprofilePic = function(profile) { console.log('profile ',
	 * profile); var method = "POST"; var url = "app/updateUserProfile"; var obj =
	 * {}; obj.profile = profile; obj.userId = $scope.userId;
	 * 
	 * var serviceobj = userformService.runservice(method, url, obj);
	 * 
	 * serviceobj.then(function(response) { console.log('response profile ',
	 * response); if (response.status == 200) { console.log('profile set
	 * successfully'); } }) }
	 */

	$scope.setprofilePic = function() {
		console.log('in setprofilePic');

		$scope.modalInstance = $uibModal.open({
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'templates/setprofilePic.html',
			scope : $scope,
			size : 'md',
			controller : function($scope, $uibModalInstance) {
				this.selectFile =function(){
				document.getElementById("profileinput").click();
				}
				
				this.saveprofilePic = function() {
					var $ctrl = this;
					var method = "POST";
					var url = "app/updateUserProfile";
					var obj = {};
					obj.profile = $ctrl.profile;
					obj.userId = $scope.userId;
					console.log($ctrl.profile);
					console.log($scope.userId);
					
					var serviceobj = userformService.runservice(method, url,
							obj);

					serviceobj.then(function(response) {
						console.log('response profile ', response);
						if (response.status == 200) {
							console.log('profile set successfully');
							$state.reload();
						}
					})
					$uibModalInstance.close();
				}
			},
			controllerAs : '$ctrl',
		});
	}

	/*
	 * $scope.collaborateNotes = function(x) { console.log('in collaborator');
	 * $scope.modalInstance = $uibModal.open({ ariaLabelledBy : 'modal-title',
	 * ariaDescribedBy : 'modal-body', templateUrl :
	 * 'templates/collaboratepopup.html', size : 'md', controller :
	 * function($scope, $uibModalInstance) { this.todoId = x.todoId; this.user =
	 * x.user; this.userId = x.user.userId; this.email = x.user.email;
	 * this.fullName = x.user.fullName; this.profile = x.user.profile; //
	 * collabrate todo with other user this.collabrate = function() { var $ctrl =
	 * this; var method = "POST"; var url = "app/colabrateNote"; var obj = {};
	 * obj.todoId = this.todoId; obj.colaboratorEmail=$ctrl.colaboratorEmail;
	 * var serviceobj = userformService.runservice(method, url, obj);
	 * serviceobj.then(function(response) { if (response.status == 200) {
	 * console.log('note collaborate successfully'); } });
	 * $uibModalInstance.close(); } }, controllerAs : '$ctrl', }); }
	 */

	$scope.collaborateNotes = function(x) {
		console.log('in collaborator');
		$scope.modalInstance = $uibModal.open({
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'templates/collaboratepopup.html',
			size : 'md',
			controller : function($scope, $uibModalInstance) {
				this.todoId = x.todoId;
				/*
				 * this.user = x.user; this.userId = x.user.userId;
				 */
				this.email = x.user.email;
				this.fullName = x.user.fullName;
				this.profile = x.user.profile;
				// collabrate todo with other user
				this.collabrate = function() {
					var $ctrl = this;
					var method = "POST";
					var url = "app/colabrateNote";
					var obj = {};
					obj.todoId = this.todoId;
					obj.colaboratorEmail = $ctrl.colaboratorEmail;
					var serviceobj = userformService.runservice(method, url,
							obj);
					serviceobj.then(function(response) {
						console.log("response of sharedcoll ", response);
						$scope.collrecords = response.data.reverse();
						this.collrecords = $scope.collrecords;
						var $ctrl = this;
						console.log("$ctrl ", $ctrl);
						if (response.status == 200) {
							console.log('note collaborate successfully');
						}
					});
					$uibModalInstance.close();
				}
			},
			controllerAs : '$ctrl',
		});
	}
});
