
function showError(msg) {
	$("#warningMessage").html(msg);
	$("#warningType").html("Error!");
	$("#warningDiv").css("visiblity:visible; w3-red");
}

function showWarning(msg) {
	$("#warningMessage").html(msg);
	$("#warningType").html("Warning!");
	$("#warningDiv").css("visiblity:visible; w3-yellow");
}

function showSuccess(msg) {
	$("#warningMessage").html(msg);
	$("#warningType").html("Success!");
	$("#warningDiv").css("visiblity:visible; w3-green");
}

function showInfo(msg) {
	$("#warningMessage").html(msg);
	$("#warningType").html("Info");
	$("#warningDiv").css("visiblity:visible; w3-blue");
}

function clearMessage() {
	$("#warningDiv").css("visiblity:hidden; w3-white");	
}