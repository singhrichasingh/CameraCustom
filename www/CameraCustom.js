var exec = require('cordova/exec');

var CameraCustom={};
CameraCustom.init=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'init', [arg0])
}
CameraCustom.getOtp=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'getOtp', [arg0])
}
CameraCustom.reg=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'reg', [arg0])
}
CameraCustom.login=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'login', [arg0])
}
CameraCustom.search=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'search', [arg0])
}
module.exports=CameraCustom
