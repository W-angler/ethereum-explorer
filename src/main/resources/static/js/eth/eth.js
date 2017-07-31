var ethURLs = {
    WSConnection: "/eth",
    blockSubscription: "/topic/blocks",
    txSubscription: "/topic/transactions",
    latestBlocks: "/eth/block/latest",
    latestTx: "/eth/transaction/latest",
    getBlock: "/eth/block/{hash}",
    getTx: "/eth/transaction/{hash}",
    searchBlock: "/eth/block/search",
    searchTx: "/eth/transaction/search"
};
//alert a modal dialog
function alertModal(msg) {
    $("#alert-message").text(msg);
    $('#alert-modal').modal();
}
//format datetime
Vue.filter('moment', function (value, formatString) {
    formatString = formatString || 'YYYY-MM-DD HH:mm:ss';
    return moment(value).format(formatString);
});
var socket = new SockJS(ethURLs.WSConnection);
var stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log("Connected:" + frame);
    //blocks
    var blocks = new Vue({
        el: "#blocks",
        data: {
            data: ""
        },
        created: function () {
            var _self = this;
            var result = get(ethURLs.latestBlocks);
            _self.data = result.data;
            stompClient.subscribe(ethURLs.blockSubscription, function (response) {
                _self.showBlocks(JSON.parse(response.body));
            });
        },
        methods: {
            showBlocks: function (block) {
                var _self = this;
                var newBlocks = [];
                newBlocks.push(block);
                for (var i = 0; i < 9; i++) {
                    newBlocks.push(_self.data[i]);
                }
                _self.data = newBlocks;
            }
        }
    });
    //search blocks
    $("#searchBlock").keydown(function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            var block = $("#searchBlock").val();
            var result = post(ethURLs.searchBlock, {
                keyword: block
            });
            if (result.code !== 200) {
                alertModal(result.message);
            }
            else{
                window.open(ethURLs.getBlock.replace("{hash}",result.data.hash));
            }
        }
    });
    //transactions
    var txs = new Vue({
        el: "#transactions",
        data: {
            data: ""
        },
        created: function () {
            var _self = this;
            var result = get(ethURLs.latestTx);
            _self.data = result.data;
            stompClient.subscribe(ethURLs.txSubscription, function (response) {
                _self.showTransactions(JSON.parse(response.body));
            });
        },
        methods: {
            showTransactions: function (transaction) {
                var _self = this;
                var newTxs = [];
                newTxs.push(transaction);
                for (var i = 0; i < 9; i++) {
                    newTxs.push(_self.data[i]);
                }
                _self.data = newTxs;
            }
        }
    });
    //search transaction
    $("#searchTx").keydown(function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            var tx = $("#searchTx").val();
            var result = post(ethURLs.searchTx, {
                keyword: tx
            });
            if (result.code !== 200) {
                alertModal(result.message);
            }
            else{
                window.open(ethURLs.getTx.replace("{hash}",tx));
            }
        }
    });
});