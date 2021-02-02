var seckill = {

    checkIdentification: {
        function(username, password) {
            return
        }
    },

    checkPhone: {
        function(phone) {
            return (phone != NaN && phone.length === 11 && !isNaN(phone));
        }
    },

    URL: {
        currentTime : function() {
            return "/seckill/getCurrentTime";
        },
        stateExposer: function(seckillInfoId) {
            return "/seckill/" + seckillInfoId + "/stateExposer";
        },
        startExecution(seckillInfoId, encodedValue) {
            return "/seckill/" + seckillInfoId + "/" + encodedValue + "/startExecution";
        }
    },

    detail: {
        init: function(params) {
            console.log("得到手机号码");
            var username = $.cookie('username');
            var password = $.cookie('password');

        }
    }

}