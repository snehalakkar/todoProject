		app.controller('loginformCtrl',function($scope, $state, userformService) {

					$scope.isEmailEnter = true;
					$scope.continueBtn=true;

					$scope.login = function() {
						var method = "POST";
						var url = "userlogin";
						console.log($scope.user);
						var serviceobj = userformService.runservice(method,
								url, $scope.user);

						serviceobj.then(function(response) {
							
							if (response.data.status == 8) {

								localStorage.setItem("accessToken",
										response.data.tokens.accessToken);
								localStorage.setItem("refreshToken",
										response.data.tokens.refreshToken);

								$state.go("homepage");
							} else {
								console.log('login unsucessfull');
								$scope.loginpwdincorrect="Password does not exist! ";
								$state.go("userLogin");
							}
						})
					}

					$scope.sendMail = function(user) {

						var method = "POST";
						var url = "forgotpwdApi";

						/* to use this emailId in resetNewPassword() */
						console.log(user);
						$scope.emailId = user.email;

						var serviceobj = userformService.resetpwd(method, url,
								user);
						serviceobj.then(function(response) {

							if (response.data.status == 5) {
								console.log("otp send to mail ");
								$scope.isEmailEnter = false;
								$scope.isNewPwdEnter = false;
								$scope.resendOtp=false;
							} else {
								$scope.valEmailError = "Email does not exist";
							}
						});
					};

					$scope.validateOtp = function(user) {
						
						var method = "POST";
						var url = "validateOtp";

						var serviceobj = userformService.resetpwd(method, url,
								user);

						serviceobj
								.then(function(response) {

									if (response.data.status == 6) {
										console.log("otp correct ");
										$scope.isNewPwdEnter = true;
										
									} else {
										console.log("otp incorrect ");
										$scope.valOtpError = "Otp is incorrect or timeout";
										$scope.resendOtp=true;
										$scope.continueBtn=false;
									}
								});
					}

					$scope.resetNewPassword = function(user) {
						var method = "POST";
						var url = "resetNewPassword";

						var obj = {};
						obj.password1 = user.password1;
						obj.password2 = user.password2;
						obj.emailId = $scope.emailId;

						var serviceobj = userformService.resetpwd(method, url,
								obj);
						serviceobj
								.then(function(response) {

									if (response.data.status == 7) {
										console.log("pwd reset succ ");
										$state.go("userLogin");
									} else {
										console.log("pwd not reset ");
										$scope.pwdResetError = "password not reset!, Re-enter password";
									}
								});
					}
					
					$scope.resendOtp1 = function() {
						var method = "POST";
						var url = "forgotpwdApi";

						var obj={};
						obj.email=$scope.emailId;
						console.log(" $scope.emailId11  ",$scope.emailId);

						var serviceobj = userformService.resetpwd(method, url,
								obj);
						serviceobj.then(function(response) {

							if (response.data.status == 5) {
								console.log("otp send to mail ");
								$scope.isEmailEnter = false;
								$scope.isNewPwdEnter = false;
								$scope.valOtpError ="otp resend to your email";
								$scope.continueBtn=true;
								$scope.resendOtp=false;
							} else {
								$scope.valEmailError = "Email does not exist";
							}
						});
					}
				});