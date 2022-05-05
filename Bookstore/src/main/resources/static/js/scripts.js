/**
 * 
 */
function checkPasswordMatch() {
 	let password = $("#txtNewPassword").val()
 	let confirmPassword = $("#txtConfirmPassword").val()
 	console.log(password)
 	if(password == "" && confirmPassword == "") {
 		$("#checkPasswordMatch").html("")
 		$("updateUserInfoButton").prop('disabled', false)
 	} else {
 		if(password != confirmPassword) {
 			$("#checkPasswordMatch").html("Password does not match")
 			$("#updateUserInfoButton").prop("disabled", true)
 		} else {
 		$("#checkPasswordMatch").html("Password match")
 			$("#updateUserInfoButton").prop("disabled", false)
 		}
 	}
 }
 $(document).ready(function(){
 	$(".cartItemQty").on('change', function(){
 		let id = this.id
 		$('#update-item-'+id).css('display','inline-block')
 	})
 	
 	$("#theSameAsShippingAddress").on('click', function(){
 		$(".billingAddress").prop("disabled", $("#theSameAsShippingAddress").is(":checked") ? true : false)
 	})
 	

	$("#txtConfirmPassword").keyup(checkPasswordMatch);
	$("#txtNewPassword").keyup(checkPasswordMatch);
 })
 
 
 
