
ClassicEditor
	.create( document.querySelector( '#editor' ), {
		placeholder: '내용을 입력해주세요.',
		fontSize: {
			options: [10, 12, 14, 'default', 18, 20, 22],
			supportAllValues: true
        },
        fontColor: {
			colors: [
				{color: '#FFFFFF', label:'#FFFFFF'},
				{color: '#67594E', label:'#67594E'},
				{color: '#382818', label:'#382818'},
				{color: '#58555C', label:'#58555C'},
				{color: '#000000', label:'#000000'},
				{color: '#B52A2F', label:'#B52A2F'},
				{color: '#E46E30', label:'#E46E30'},
				{color: '#D09316', label:'#D09316'},
				{color: '#C7B72C', label:'#C7B72C'},
				{color: '#4FA170', label:'#4FA170'},
				{color: '#499D9F', label:'#499D9F'},
				{color: '#2C73CF', label:'#2C73CF'},
				{color: '#4742B8', label:'#4742B8'},
				{color: '#9F6CED', label:'#9F6CED'},
				{color: '#9F3FAA', label:'#9F3FAA'}
			],
			supportAllValues: true
		},
		simpleUpload: {
			uploadUrl: 'http://localhost/diary/upload/image',
			withCredentials: true,
		}
        
	})
	.then( editor => {
		window.editor = editor;
	} )
	.catch( error => {
		console.error( 'There was a problem initializing the editor.', error );
} );

$("#edit_button").on("click", function() {
	$("#editor_area").val(editor.getData());
	var title = $(".editor_title").val();
	var contents = $("#editor_area").val();
	if(title.length == 0) {
		$(".editor_title").css('border', '1px solid #D3728E');
	} 
	if(title.length != 0 && contents.length != 0){
		$("#diary_form").submit();
	}
	console.log(title);
	console.log(contents);
})

$(".editor_title").on("propertychange change keyup paste input", function() {
	var title = $(".editor_title").val();
	if(title == "") {
		$(".editor_title").css('border', '1px solid #D3728E');
	} else {
		$(".editor_title").css('border', '1px solid #dddddd');
	}
})