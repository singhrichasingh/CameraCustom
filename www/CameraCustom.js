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
CameraCustom.home=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'home', [arg0])
}
CameraCustom.createhome=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'createhome', [arg0])
}
CameraCustom.qrGen=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'qrGen', [arg0])
}
CameraCustom.createView=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'createView', [arg0])
}
CameraCustom.initDevice=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'initDevice', [arg0])
}
module.exports=CameraCustom
