app.service('generateAccessService', function($http) {

	this.runservice = function(method, url,object) {
		alert('hi snel');
		alert(JSON.stringify(object));

		return $http({

			method : method,
			url : url,
			data : object,
			headers: {'refreshToken': localStorage.getItem("refreshToken")}  //here we are setting localstorage
		});
	};
});
