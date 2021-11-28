cordova.define("cordova-plugin-cameracustom.CameraCustom", function(require, exports, module) {
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
    CameraCustom.deleteDevice=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'deleteDevice', [arg0])
    }
    CameraCustom.messagePanel=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'messagePanel', [arg0])
    }
    CameraCustom.renameDevice=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'renameDevice', [arg0])
    }
    CameraCustom.createView=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createView', [arg0])
    }
    CameraCustom.initDevice=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'initDevice', [arg0])
    }
    CameraCustom.playback=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'playback', [arg0])
    }
    CameraCustom.playbackStatus=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'playbackStatus', [arg0])
    }
    CameraCustom.shareDevice=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'shareDevice', [arg0])
    }
    CameraCustom.delShare=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'delShare', [arg0])
    }
    CameraCustom.getShare=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getShare', [arg0])
    }
    CameraCustom.modifyShare=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'modifyShare', [arg0])
    }
    CameraCustom.addTimer=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'addTimer', [arg0])
    }
    CameraCustom.timerStatus=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'timerStatus', [arg0])
    }
    CameraCustom.updateTimer=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'updateTimer', [arg0])
    }
    CameraCustom.getTimerTask=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getTimerTask', [arg0])
    }
    CameraCustom.updateSchedule=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'updateSchedule', [arg0])
    }
    CameraCustom.getAllScene=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getAllScene', [arg0])
    }
    CameraCustom.getConditionList=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getConditionList', [arg0])
    }
    CameraCustom.createTempCondition=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createTempCondition', [arg0])
    }
    CameraCustom.getDeviceTaskOperationList=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getDeviceTaskOperationList', [arg0])
    }
    CameraCustom.createDevCondition=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createDevCondition', [arg0])
    }
    CameraCustom.createTimingCondition=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createTimingCondition', [arg0])
    }
    CameraCustom.getDeviceConditionOperationList=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getDeviceConditionOperationList', [arg0])
    }
    CameraCustom.createSceneTask=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createSceneTask', [arg0])
    }
    
    CameraCustom.createScene=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createScene', [arg0])
    }
    CameraCustom.modifyScene=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'modifyScene', [arg0])
    }
    CameraCustom.deleteScene=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'deleteScene', [arg0])
    }
    CameraCustom.executeScene=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'executeScene', [arg0])
    }
    
    CameraCustom.group=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'group', [arg0])
    }
    CameraCustom.dismiss=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'dismiss', [arg0])
    }
    CameraCustom.wifilock=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'wifilock', [arg0])
    }
    CameraCustom.addmember=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'addmember', [arg0])
    }
    CameraCustom.invitecode=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'invitecode', [arg0])
    }
    CameraCustom.findDeviceType=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'findDeviceType', [arg0])
    }
    CameraCustom.initDevice=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'initDevice', [arg0])
    }
    CameraCustom.pushReg=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'pushReg', [arg0])
    }
    CameraCustom.remoteUnlock=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'remoteUnlock', [arg0])
    }
    CameraCustom.createTempPass=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createTempPass', [arg0])
    }
    CameraCustom.getTempPass=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getTempPass', [arg0])
    }
    CameraCustom.delTempPass=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'delTempPass', [arg0])
    }
    CameraCustom.createDynaPass=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'createDynaPass', [arg0])
    }
    CameraCustom.addLockUser=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'addLockUser', [arg0])
    }
    CameraCustom.updateLockUser=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'updateLockUser', [arg0])
    }
    CameraCustom.getLockUser=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getLockUser', [arg0])
    }
    CameraCustom.updateFamilyLockUser=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'updateFamilyLockUser', [arg0])
    }
    CameraCustom.deleteLockUser=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'deleteLockUser', [arg0])
    }
    CameraCustom.getRecords=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getRecords', [arg0])
    }
    CameraCustom.getUnlockRecords=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getUnlockRecords', [arg0])
    }
    CameraCustom.getHijackRecords=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getHijackRecords', [arg0])
    }
    CameraCustom.setHijackConfig=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'setHijackConfig', [arg0])
    }
    CameraCustom.rmHijackConfig=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'rmHijackConfig', [arg0])
    }
    CameraCustom.devicelist=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'devicelist', [arg0])
    }
    CameraCustom.memberlist=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'memberlist', [arg0])
    }
    CameraCustom.getmessage=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'getmessage', [arg0])
    }
    CameraCustom.deletemessage=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'deletemessage', [arg0])
    }
    CameraCustom.cloudSer=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'cloudSer', [arg0])
    }
    CameraCustom.logout=function(arg0, success, error){
        exec(success, error, 'CameraCustom', 'logout', [arg0])
    }
    module.exports=CameraCustom
    
    
    
    
    
    });
    