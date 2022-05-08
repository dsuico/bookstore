/**
 * 
 */
 
$(document).ready(function(){
	$('.delete-book').on('click', function(){
		/*<![CDATA[*/
		let path = /*[[@{/}]]*/'books/remove'
		/*]]>*/
		let id = $(this).attr('id');
		
		bootbox.confirm({
			message: "are you sure to remove this book? It can't be undone.",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				},
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.post(path, {id: id}, function(res){
						location.reload()
					})
				}
			}
		})
	})
	
	
	$('#deleteSelected').click(function() {
		let idList = $('.checkboxBook')
		let bookIdList = [];
		for(let i = 0; i < idList.length; i++) {
			if($(idList[i]).is(":checked"))
				bookIdList.push(idList[i]['id'])
		}
		
		console.log(bookIdList)
		/*<![CDATA[*/
		let path = /*[[@{/}]]*/'removeList'
		/*]]>*/
		
		bootbox.confirm({
			message: "are you sure to remove all selected book? It can't be undone.",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				},
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.ajax({
						type: 'POST',
						url: path,
						data: JSON.stringify(bookIdList),
						contentType: 'application/json',
						success: function(res){
							console.log(res)
							location.reload()
						},
						error: function(res){
							console.log(res)
							location.reload()
						}
					})
				}
			}
		})
	})
	
	$("#selectAllBooks").click(function(){
		isChecked = $(this).prop("checked")
		
		$(".checkboxBook").each(function() {
		    $(this).prop('checked', isChecked);
		});
	})
})