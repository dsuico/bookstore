/**
 * 
 */
 
 $(document).ready(function(){
 	$(".cartItemQty").on('change', function(){
 		let id = this.id
 		$('#update-item-'+id).css('display','inline-block')
 	})
 	
 	$("#theSameAsShippingAddress").on('click', function(){
 		$(".billingAddress").prop("disabled", $("#theSameAsShippingAddress").is(":checked") ? true : false)
 	})
 })