var exec = require('cordova/exec');

var CameraCustom={};
CameraCustom.init=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'init', [arg0])
}
CameraCustom.search=function(arg0, success, error){
    exec(success, error, 'CameraCustom', 'search', [arg0])
}
module.exports=CameraCustom
