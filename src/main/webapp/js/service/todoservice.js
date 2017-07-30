app.service('userformService', function($http) {

	this.runservice = function(method, url,object) {
		alert('hi snel');
		alert(JSON.stringify(object));

		return $http({

			method : method,
			url : url,
			data : object,
			headers: {'accessToken': localStorage.getItem("accessToken")}  //here we are setting localstorage
		});
	};
});
