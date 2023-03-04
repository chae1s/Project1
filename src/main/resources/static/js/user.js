/**
 * 
 */
 
 /* 메일 중복 확인 */
$("#email").on("blur", function() {
	alert("hello");
//	emailCheck;
})

function emailCheck() {
	const email = $("#email").val();
	
	const userDTO = {
		result : email
	};
	
	$.ajax({
		url : "/users/emailCheck",
		type : "GET",
		data : userDTO,
		success : function(data) {
			if(data) {
				$(".email_warn").css('color', '#7286D3');
				$(".email_warn").text("사용 가능한 이메일 입니다.");
				$("#email").css('border', '2px solid #7286D3');
			} else {
				$(".email_warn").css('color', '#D3728E');
				$(".email_warn").text("이미 가입되어 있는 이메일 입니다.");
				$("#email").css('border', '2px solid #D3728E');
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("ERROR : " + textStatus + " : " + errorThrown);
		}
		
	});
}