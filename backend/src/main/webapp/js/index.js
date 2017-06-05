
// viewModel init

// much quicker reation than waiting for google api to load.
// may meed to create viewModel object in the future.
$(function() {
    viewModel = {
        recordList: ko.observable([]),
        recordListCursor: ko.observable(null),
    }
    ko.applyBindings(viewModel)
})



// This is called initially
function init() {
    var apiName = 'myApi';
    var apiVersion = 'v1';
    var apiRoot = 'https://' + window.location.host + '/_ah/api';
    if (window.location.hostname == 'localhost'
      || window.location.hostname == '127.0.0.1'
      || ((window.location.port != "") && (window.location.port > 1023))) {
        // We're probably running against the DevAppServer
      apiRoot = 'http://' + window.location.host + '/_ah/api';
    }
    //apiRoot = "https://antoshkaplus-words.appspot.com/_ah/api"

    var callback = function() {
        loadMoreRecords()
    }

    gapi.client.load(apiName, apiVersion, callback, apiRoot);

}

function placeData() {
    gapi.client.myApi.placeData().execute()
}

function loadMoreRecords() {
    $("#loadMoreRecords").prop('disabled', true);
    gapi.client.myApi
        .getData({pageSize: 10, cursor: viewModel.recordListCursor()})
        .execute(function(resp) {
            if (resp.error != null) {
                console.log(resp.error)
                return
            }
            if (resp) {
                if (resp.list) {
                    viewModel.recordList(viewModel.recordList().concat(resp.list))
                }
                viewModel.recordListCursor(resp.nextCursor)
            }
            console.log(resp)
            $("#loadMoreRecords").prop('disabled', false);
        })
}

function clearData() {
    gapi.client.myApi.clearData().execute(function(resp) {
        if (resp.error != null) {
            console.log(resp.error)
            return
        }
        viewModel.recordList([])
        viewModel.recordListCursor(null)
    })
}

function addRecord() {
    data = $("#inputData").val()
    gapi.client.myApi
        .addRecord({data: data, creationDate: new Date()})
        .execute(function(resp) {
            if (resp.error != null) {
                console.log(resp.error)
                return
            }
        })
}

function updateRecordList() {
    viewModel.recordList([])
    viewModel.recordListCursor(null)
    loadMoreRecords()
}