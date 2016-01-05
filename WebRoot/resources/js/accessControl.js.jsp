<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

var permission=${sessionScope.SESSION_PERMISSION_URI};
function getPermission(url){
	var strs=permission.split('#');
	var bool=false;
	for(var i=0;i < strs.length;i++){
		if(url==strs[i]){
			bool=true;
			break;
		}
	}
	return bool;
}
