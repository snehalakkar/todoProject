app.service('userformService', function($http) {
	
	this.runservice = function(method, url, object) {
		console.log(object);
		
		return $http({

			method : method,
			url : url,
			data : object,
			headers : {
				'accessToken' : localStorage.getItem("accessToken")
			}
		// here we are setting localstorage
		});
	};
	
	this.resetpwd = function(method, url, object) {
		return $http({
			method : method,
			url : url,
			data : object
		});
	};
});
