/**
 * 
 */
var emailPass = false;
var pwPass = false;
var pwCheckPass = false;
var namePass = false;
var nicknamePass = false; 
var check = false;


 /* 메일 중복, 유효성 검사 체크 */
$("#email").on("propertychange change keyup paste input", function() {
	var email = $("#email").val();
	if(email == "") {	
		inputData('email');
		emailPass = false;
	} else {
		emailCheck(email);
	}
})

/* 닉네임 중복, 유효성 검사 체크 */
$("#nickname").on("propertychange change keyup paste input", function() {
	var nickname = $("#nickname").val();
	if(nickname == "") {
		inputData('nickname');
		nicknamePass = false;
	} else {		
		nicknameCheck(nickname);
	}
})

 /* 비밀번호 유효성 검사, 비밀번호 확인과 일치 체크 */
$("#password").on("propertychange change keyup paste input", function() {	
	var password = $("#password").val();
	var passwordCheck = $("#passwordCheck").val();
	
	if(password.length == 0) {	
		inputData('password');	
		pwPass = false;
	} else {		
		passwordReg(password);
		if(passwordCheck.length != 0) {
			passwordMatch(password, passwordCheck);
		}
	}
	
})

/* 비밀번호와 비밀번호 확인 일치 체크 */
$("#passwordCheck").on("propertychange change keyup paste input", function() {
	var password = $("#password").val();
	var passwordCheck = $("#passwordCheck").val();
	
	if(passwordCheck.length == 0) {
		inputData('passwordCheck');
		pwCheckPass = false;
	} else {
		passwordMatch(password, passwordCheck);
	}
	
})

/* 이름 유효성검사 체크 */
$("#name").on("propertychange change keyup paste input", function() {
	var name = $("#name").val();
	if(name.length == 0) {
		inputData('name');
		namePass = false;
	} else {
		namePass = true;
		$(".name_warn").text("");
		$("#name").css('border', '2px solid #7286D3');
	}
})

/* 약관 전체 동의 버튼 선택 시 */
$("#agreeAll").on("change", function() {
	
	if($("#agreeAll").prop('checked')) {
		$(".agree_num").prop('checked', true);
		check = true;
	} else {
		$(".agree_num").prop('checked', false);
		check = false;
	}
})

/* 약관 개별로 선택했을 때의 체크 박스 */
$(".agree").on("change", function() {
	if($("#agree1").prop('checked') && $("#agree2").prop('checked') && $("#agree3").prop('checked') && $("#agree4").prop('checked')) {
		$("#agreeAll").prop('checked', true);
	} else if($("#agree1").prop('checked') && $("#agree2").prop('checked') && $("#agree3").prop('checked')){
		check = true;
	} else {
		$("#agreeAll").prop('checked', false);
		check = false;
	}
	
	console.log(check);
})

/* input 요소들 모두 true가 되면 버튼 disabled 해제 */
$(".input").on("input change", function() {
	if(emailPass && pwPass && pwCheckPass && namePass && nicknamePass && check) {
		$(".join_btn").prop('disabled', false);
	} else {
		$(".join_btn").prop('disabled', true);
	}
	console.log(emailPass && pwPass && pwCheckPass && namePass && nicknamePass && check);
})

/* 닉네임 중복, 유효성 검사 체크 */
$("#nickname_edit").on("propertychange change keyup paste input", function() {
	var nickname = $("#nickname_edit").val();
	if(nickname == "") {
	} else {		
		nicknameCheck(nickname);
	}
})

/* input의 value가 빈 칸일 때 */
function inputData(data) {
	$('.' + data + '_warn').css('color', '#D3728E');
	$('.' + data + '_warn').text("필수 입력 사항입니다.");
	$('#' + data).css('border', '2px solid #D3728E');
}

/* 이메일 중복 확인 함수 */
function emailCheck(email) {
	
	$.ajax({
		url : "/users/emailCheck",
		type : "POST",
		data : {email : email},
		success : function(check) {
			if(check == 0) {
				$(".email_warn").css('color', '#7286D3');
				$(".email_warn").text("사용 가능한 이메일입니다.");
				$("#email").css('border', '2px solid #7286D3');
				emailPass = true;
			} else if(check == 1){
				$(".email_warn").css('color', '#D3728E');
				$(".email_warn").text("이미 가입되어 있는 이메일입니다.");
				$("#email").css('border', '2px solid #D3728E');
				emailPass = false;
			} else {
				$(".email_warn").css('color', '#D3728E');
				$(".email_warn").text("유효하지 않은 이메일입니다.");
				$("#email").css('border', '2px solid #D3728E');
				emailPass = false;
			}
			abc = check;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("ERROR : " + textStatus + " : " + errorThrown);
		}
		
	});
}



/* 닉네임 중복 확인 함수 */
function nicknameCheck(nickname) {
	
	$.ajax({
		url : "/users/nicknameCheck",
		type : "POST",
		data : {nickname : nickname},
		success : function(check) {
			if(check == 0) {
				$(".nickname_warn").css('color', '#7286D3');
				$(".nickname_warn").text("사용 가능한 닉네임입니다.");
				$("#nickname").css('border', '2px solid #7286D3');
				nicknamePass = true;
			} else if(check == 1) {
				$(".nickname_warn").css('color', '#D3728E');
				$(".nickname_warn").text("이미 사용중인 닉네임입니다.");
				$("#nickname").css('border', '2px solid #D3728E');
				nicknamePass = false;
			} else {
				$(".nickname_warn").css('color', '#D3728E');
				$(".nickname_warn").text("2자 이상 10자 이하로 입력해주세요.");
				$("#nickname").css('border', '2px solid #D3728E');
				nicknamePass = false;
			}
			
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("ERROR : " + textStatus + " : " + errorThrown);
		}
		
	});
}

/* 비밀번호와 비밀번호 확인 일치 여부 */
function passwordMatch(password, passwordCheck) {
	
	if(password == passwordCheck) {
		$(".passwordCheck_warn").css('color', '#7286D3');
		$(".passwordCheck_warn").text("비밀번호가 일치합니다.");
		$("#passwordCheck").css('border', '2px solid #7286D3');
		pwCheckPass = true;
	} else {
		$(".passwordCheck_warn").css('color', '#D3728E');
		$(".passwordCheck_warn").text("비밀번호가 일치하지 않습니다.");
		$("#passwordCheck").css('border', '2px solid #D3728E');
		pwCheckPass = false;
	}
}

/* 비밀번호 유효성 검사 */
function passwordReg(password) {
	var pwReg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!-@#$%^&*+|=])[A-Za-z\d~!-@#$%^&*+|=]{6,20}$/;
	
	if(pwReg.test(password)){
		$(".password_warn").css('color', '#7286D3');
		$(".password_warn").text("사용 가능한 비밀번호입니다.");
		$("#password").css('border', '2px solid #7286D3');
		pwPass = true;
	} else {
		$(".password_warn").css('color', '#D3728E');
		$(".password_warn").text("영문, 숫자, 특수문자를 포함한 8 ~ 20자의 비밀번호를 입력해주세요.");
		$("#password").css('border', '2px solid #D3728E');
		pwPass = false;
	}
}


