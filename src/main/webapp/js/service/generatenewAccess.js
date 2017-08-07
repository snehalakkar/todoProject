app.service('generateAccessService', function($http) {

	this.runservice = function() {

		return $http({

			method : "POST",
			url :"generateNewaccessToken",
			data : {},
			headers: {'refreshToken': localStorage.getItem("refreshToken")}  //here we are setting local storage
		});
	};
});
