app.controller('DashboardController', function ($scope, $rootScope, userService, $modal, $log, $interval) {

    init();
    function init() {
        $scope.comment = {};
        $scope.newComment = {};
        $scope.response = "";
        $scope.show = false;
        userService.findCurrentUser().then(function (response) {
            $scope.cUser = response.data.obj;
            $rootScope.cUser = response.data.obj;
        });
    }
    ;

});

app.controller('SMSController', function ($scope, $rootScope, smsService, notificationService, $modal, $log, $interval) {

    init();
    function init() {
        $scope.p = {};
        $scope.p.range = 'TODAY';
        $scope.p.page = 0;
        $scope.p.size = 10;
        $scope.p.autoRefresh = false;
        $scope.currentPage = 0;
        $scope.pageSize = 10;
        $scope.total = 1000;
        $scope.p.interval = 30000;
        $rootScope.isRouteLoading = true;
        smsService.getSMSDataByRange($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            $scope.total = $scope.smsdata.total;
            $scope.pageSize = $scope.smsdata.nosOfPages;
            console.log(response.data);
            $rootScope.isRouteLoading = false;
        });
        smsService.getSMSUnitsCount($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.p.smscount = response.data.obj;
            $rootScope.isRouteLoading = false;
        });
        $scope.p.tcolor = 'btn-warning';
        $scope.p.wcolor = 'btn-primary';
        $scope.p.mcolor = 'btn-primary';
        $scope.p.ycolor = 'btn-primary';
        $interval(function () {
            if ($scope.p.autoRefresh) {
                $rootScope.isRouteLoading = true;
                smsService.getSMSDataByRange($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
                    $scope.smsdata = response.data.obj;
                    $scope.total = $scope.smsdata.total;
                    $scope.pageSize = $scope.smsdata.nosOfPages;
                    console.log(response.data);
                    $rootScope.isRouteLoading = false;
                });
            }

        }, $scope.p.interval);
    }
    ;
    $scope.getColor = function (rangeType) {
        if ('TODAY' === rangeType) {
            $scope.p.tcolor = 'btn-warning';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_WEEK' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-warning';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_MONTH' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-warning';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_YEAR' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-warning';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('YESTERDAY' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-warning';
        }
    };
    $scope.getSMSByRangeType = function (rangeType) {
        $rootScope.isRouteLoading = true;
        $scope.p.range = rangeType;
        $scope.getColor(rangeType);
        smsService.getSMSDataByRange(rangeType, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            console.log(response.data);
            $rootScope.isRouteLoading = false;
        });
    };
    $scope.getSMSByPageSize = function () {
        $rootScope.isRouteLoading = true;
        smsService.getSMSDataByRange($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            console.log(response.data);
            $rootScope.isRouteLoading = false;
        });
    };
    $scope.getSMSData = function () {
        $rootScope.searchType = "DATE";
        if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            $rootScope.from = from;
            $rootScope.to = to;
            $rootScope.fromS = $scope.p.from;
            $rootScope.toS = $scope.p.to;
            smsService.getSMSData(from, to, $scope.p.page, $scope.p.size).then(function (response) {
                $scope.smsdata = response.data.obj;
                $rootScope.isRouteLoading = false;
            });
        }
    };
    $scope.nextPage = function () {
        $scope.p.page = $scope.p.page++;
        smsService.getSMSDataByRange($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            //$scope.p.page = $scope.smsdata.page + 1;
        });
    };
    $scope.previousPage = function () {
        $scope.p.page = $scope.p.page - 1 < 0 ? 0 : $scope.p.page - 1;
        smsService.getSMSDataByRange($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            //$scope.p.page = $scope.smsdata.page - 1;
        });
    };
    $scope.reinitiateSMS = function (sms) {
        $scope.modalUser = sms;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'reinitiateSMS.html',
            controller: 'SMSModalController',
            resolve: {
                sms: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
        });
    };
    $scope.resendSMS = function (sms) {
        $scope.modalUser = sms;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'resendSMS.html',
            controller: 'SMSModalController',
            resolve: {
                sms: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
        });
    };
    $scope.downloadData = function () {
        $scope.modalUser = {};
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'download.html',
            controller: 'SMSModalController',
            resolve: {
                sms: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
        }, function () {
        });
    };
    $scope.DoCtrlPagingAct = function (text, page, pageSize, total) {
        $rootScope.isRouteLoading = true;
        smsService.getSMSDataByRange($scope.p.range, page, pageSize).then(function (response) {
            $scope.p.page = page;
            $scope.smsdata = response.data.obj;
            $scope.total = $scope.smsdata.total;
            $scope.pageSize = $scope.smsdata.nosOfPages;
            $rootScope.isRouteLoading = false;
        });
        $log.info({
            text: text,
            page: page,
            pageSize: pageSize,
            total: total
        });
    };
    $scope.stopRefresh = function () {
        $scope.p.autoRefresh = false;
    };
    $scope.restartRefresh = function () {
        $scope.p.autoRefresh = true;
    };
    $scope.viewStaffDetails = function (sms) {
        $scope.modalUser = sms;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'viewStaffDetails.html',
            controller: 'ViewUserModalController',
            resolve: {
                sms: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
        }, function () {
        });
    };
});
app.controller('SMSModalController', function ($scope, $modalInstance, $rootScope, sms, smsService, notificationService) {
    init();
    function init() {
        $scope.p = sms;
        $scope.w = {};


    }
    ;

    $scope.reintiateCommand = function () {
        smsService.reInitiateAction($scope.p.uniqueId).then(function (response) {
            $modalInstance.dismiss();
            notificationService.warning("Message", "Staff not found!");
        });
    };
    $scope.resendResponse = function () {
        smsService.resendResponse($scope.p.uniqueId).then(function (response) {
            $modalInstance.dismiss();
        });
    };
    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
    $scope.downloadSMSData = function () {
        if ($scope.w.from === undefined || $scope.w.from === 'NaN' || $scope.w.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else if ($scope.w.to === undefined || $scope.w.to === 'NaN' || $scope.w.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.w.from).getTime();
            var to = new Date($scope.w.to).getTime();
            $rootScope.from = from;
            $rootScope.to = to;
            $rootScope.fromS = $scope.w.from;
            $rootScope.toS = $scope.w.to;
            smsService.downloadSMSData(from, to).then(function (response) {
                $modalInstance.dismiss();
            });
        }
    };

});
app.controller('ViewUserModalController', function ($scope, $modalInstance, $rootScope, sms, smsService, notificationService) {
    init();
    function init() {
        $scope.p = sms;
        $scope.w = {};

        smsService.verifyPhoneNumber($scope.p.msisdn).then(function (response) {
            $scope.w = response.data;
            if (!$scope.w.success) {
                $modalInstance.dismiss();
            }
        });
    }
    ;

    $scope.reintiateCommand = function () {
        smsService.reInitiateAction($scope.p.uniqueId).then(function (response) {
            $modalInstance.dismiss();
        });
    };
    $scope.resendResponse = function () {
        smsService.resendResponse($scope.p.uniqueId).then(function (response) {
            $modalInstance.dismiss();
        });
    };
    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
    $scope.downloadSMSData = function () {
        if ($scope.w.from === undefined || $scope.w.from === 'NaN' || $scope.w.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else if ($scope.w.to === undefined || $scope.w.to === 'NaN' || $scope.w.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.w.from).getTime();
            var to = new Date($scope.w.to).getTime();
            $rootScope.from = from;
            $rootScope.to = to;
            $rootScope.fromS = $scope.w.from;
            $rootScope.toS = $scope.w.to;
            smsService.downloadSMSData(from, to).then(function (response) {
                $modalInstance.dismiss();
            });
        }
    };

});

app.controller('SearchController', function ($scope, $rootScope, smsService, notificationService, $modal, $log, $interval) {

    init();
    function init() {
        $scope.p = {};
        $scope.p.showData = false;
        $scope.p.data = [];
    }
    ;

    $scope.findData = function () {
        if ($scope.p.criterion === undefined || $scope.p.criterion === 'NaN' || $scope.p.criterion === null) {
            notificationService.warning("Message", "Please select a criterion");
        } else if ($scope.p.searchText === undefined || $scope.p.searchText === 'NaN' || $scope.p.searchText === null) {
            notificationService.warning("Message", "Please enter your search string.");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            smsService.getSearchData($scope.p.criterion, $scope.p.searchText, from, to).then(function (response) {
                $rootScope.isRouteLoading = false;
                $scope.p.data = response.data.obj;
                $scope.p.showData = true;
            });

        }
    };
    $scope.findUSSDData = function () {
        if ($scope.p.criterion === undefined || $scope.p.criterion === 'NaN' || $scope.p.criterion === null) {
            notificationService.warning("Message", "Please select a criterion");
        } else if ($scope.p.searchText === undefined || $scope.p.searchText === 'NaN' || $scope.p.searchText === null) {
            notificationService.warning("Message", "Please enter your search string.");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            smsService.getUSSDSearchData($scope.p.criterion, $scope.p.searchText, from, to).then(function (response) {
                $rootScope.isRouteLoading = false;
                $scope.p.data = response.data.obj;
                $scope.p.showData = true;
            });

        }
    };
    $scope.downloadSearchByCriterion = function () {
        if ($scope.p.criterion === undefined || $scope.p.criterion === 'NaN' || $scope.p.criterion === null) {
            notificationService.warning("Message", "Please select a criterion");
        } else if ($scope.p.searchText === undefined || $scope.p.searchText === 'NaN' || $scope.p.searchText === null) {
            notificationService.warning("Message", "Please enter your search string.");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else {
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            smsService.downloadSearchByCriterion($scope.p.criterion, $scope.p.searchText, from, to).then(function (response) {
            });
        }
    };
    $scope.sendCustomSMS = function () {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'sendSMS.html',
            controller: 'SearchModalController',
            resolve: {
                sms: function () {
                    return {};
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
        });
    };
    $scope.reinitiateSMS = function (sms) {
        $scope.modalUser = sms;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'reinitiateSMS.html',
            controller: 'SMSModalController',
            resolve: {
                sms: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
        });
    };
    $scope.resendSMS = function (sms) {
        $scope.modalUser = sms;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'resendSMS.html',
            controller: 'SMSModalController',
            resolve: {
                sms: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
        });
    };
});
app.controller('SearchModalController', function ($scope, $modalInstance, $rootScope, sms, smsService, notificationService) {
    init();
    function init() {
        $scope.p = {};
    }
    ;
    $scope.sendCustomSMS = function () {
        smsService.sendCustomSMS($scope.p.msisdn, $scope.p.incoming).then(function (response) {
            $modalInstance.dismiss();
        });
    };
    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

});


app.controller('KeywordController', function ($scope, $rootScope, smsService, notificationService, $modal, $log, $interval, $location) {

    init();
    function init() {
        $scope.p = {};
        $scope.p.range = 'TODAY';
        $scope.p.page = 0;
        $scope.p.size = 10;
        $scope.p.autoRefresh = false;
        $scope.currentPage = 0;
        $scope.pageSize = 10;
        $scope.total = 1000;
        $scope.p.interval = 30000;
        $rootScope.isRouteLoading = true;

        var path = $location.path();
        ;
        if (path === '/track') {
            $scope.p.keyword = 'TRACK';
        } else if (path === '/wfm') {
            $scope.p.keyword = 'WFM';
        } else if (path === '/payment') {
            $scope.p.keyword = 'PAYMENT';
        } else if (path === '/meter') {
            $scope.p.keyword = 'METER';
        } else if (path === '/staff') {
            $scope.p.keyword = 'STAFF';
        } else if (path === '/issue') {
            $scope.p.keyword = 'ISSUE';
        } else if (path === '/getinventory') {
            $scope.p.keyword = 'GETINVENTORY';
        } else if (path === '/token') {
            $scope.p.keyword = 'TOKEN';
        } else if (path === '/register') {
            $scope.p.keyword = 'REG';
        }

        smsService.getSMSDataByKeywordByRange($scope.p.keyword, $scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            $scope.total = $scope.smsdata.total;
            $scope.pageSize = $scope.smsdata.nosOfPages;
            console.log(response.data);
            $rootScope.isRouteLoading = false;
        });

        $scope.p.tcolor = 'btn-warning';
        $scope.p.wcolor = 'btn-primary';
        $scope.p.mcolor = 'btn-primary';
        $scope.p.ycolor = 'btn-primary';
    }
    ;
    $scope.getColor = function (rangeType) {
        if ('TODAY' === rangeType) {
            $scope.p.tcolor = 'btn-warning';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_WEEK' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-warning';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_MONTH' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-warning';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_YEAR' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-warning';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('YESTERDAY' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-warning';
        }
    };
    $scope.DoCtrlPagingAct = function (text, page, pageSize, total) {
        $rootScope.isRouteLoading = true;
        smsService.getSMSDataByKeywordByRange($scope.p.keyword, $scope.p.range, page, pageSize).then(function (response) {
            $scope.p.page = page;
            $scope.smsdata = response.data.obj;
            $scope.total = $scope.smsdata.total;
            $scope.pageSize = $scope.smsdata.nosOfPages;
            $rootScope.isRouteLoading = false;
        });
        $log.info({
            text: text,
            page: page,
            pageSize: pageSize,
            total: total
        });
    };
    $scope.getSMSByRangeType = function (rangeType) {
        $rootScope.isRouteLoading = true;
        $scope.p.range = rangeType;
        $scope.getColor(rangeType);
        smsService.getSMSDataByKeywordByRange($scope.p.keyword, $scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            $scope.total = $scope.smsdata.total;
            //$scope.pageSize = $scope.smsdata.nosOfPages;
            $rootScope.isRouteLoading = false;
        });
    };
    $scope.getSMSByPageSize = function () {
        $rootScope.isRouteLoading = true;
        smsService.getSMSDataByKeywordByRange($scope.p.keyword, $scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.smsdata = response.data.obj;
            $scope.total = $scope.smsdata.total;
            $rootScope.isRouteLoading = false;
        });
    };
});
app.controller('KeywordModalController', function ($scope, $modalInstance, $rootScope, sms, smsService, notificationService) {
    init();
    function init() {
        $scope.p = {};
    }
    ;

});

app.controller('USSDTransactionController', function ($scope, $location, $rootScope, $modal, ussdService, $interval, notificationService, $http) {

    init();
    function init() {
        console.log("USSDTransactionController loaded");
        $scope.p = {};
        $scope.p.page = 0;
        $scope.p.size = 50;
        $scope.p.dAutoRefresh = false;
        $scope.p.autoRefresh = true;
        $scope.p.range = 'TODAY';
        ussdService.findDistinctTransactionByRangePS($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.p.txns = response.data;
        });
        ussdService.getUSSDDashboardStatsByRange($scope.p.range).then(function (response) {
            $scope.p.dashdata = response.data;
        });
        $scope.p.tcolor = 'btn-warning';
        $scope.p.wcolor = 'btn-primary';
        $scope.p.mcolor = 'btn-primary';
        $scope.p.ycolor = 'btn-primary';
        $scope.p.yestcolor = 'btn-primary';

        $interval(function () {
            if ($scope.p.autoRefresh) {
                ussdService.findDistinctTransactionByRangePS($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
                    $scope.p.txns = response.data;
                });
                ussdService.getUSSDDashboardStatsByRange($scope.p.range).then(function (response) {
                    $scope.p.dashdata = response.data;
                });
            }
        }, 30000);

    }
    ;
    $scope.stopRefresh = function () {
        $scope.p.autoRefresh = false;
    };
    $scope.restartRefresh = function () {
        $scope.p.autoRefresh = true;
    };
    $scope.viewTxnDetails = function (txn) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'viewTxnDetails.html',
            controller: 'USSDTransactionMiniController',
            resolve: {
                details: function () {
                    return txn;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
        }, function () {

        });
    };
    $scope.userStatusStyle = function (i) {
        var val;
        if (i.userStatus === 'SUSPENDED') {
            val = 'label label-sm label-danger';
        } else if (i.userStatus === 'ACTIVE') {
            val = 'label label-sm label-success';
        } else {
            val = 'label label-sm label-warning';
        }
        return val;
    };
    $scope.getClass = function (path) {
        if ($location.path().substr(0, path.length) === path) {
            return true;
        } else {
            return false;
        }
    };
    $scope.findDistinctTransaction = function () {
        ussdService.findDistinctTransactionByRangePS($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.p.txns = response.data;
            console.log($scope.p.txns);
        });
    };
    $scope.getColor = function (rangeType) {
        if ('TODAY' === rangeType) {
            $scope.p.tcolor = 'btn-warning';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_WEEK' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-warning';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_MONTH' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-warning';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_YEAR' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-warning';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('YESTERDAY' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-warning';
        }
    };
    $scope.getDashboardDataByDate = function () {
        $rootScope.searchType = "DATE";
        if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            $rootScope.from = from;
            $rootScope.to = to;
            $rootScope.fromS = $scope.p.from;
            $rootScope.toS = $scope.p.to;
            ussdService.findDistinctTransactionByDatePS(from, to, $scope.p.size, $scope.p.size).then(function (response) {
                $scope.p.txns = response.data;
                $rootScope.isRouteLoading = false;
            });
        }
    };
    $scope.getDashboardDataByRangeType = function (rangeType) {
        $rootScope.searchType = "RANGE";
        console.log(rangeType);
        $rootScope.isRouteLoading = true;
        $scope.p.range = rangeType;
        $rootScope.range = $scope.p.range;
        $scope.getColor(rangeType);
        ussdService.findDistinctTransactionByRangePS($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
            $scope.p.txns = response.data;
            $rootScope.isRouteLoading = false;
        });
    };
    $scope.searchUSSDTxn = function () {
        $rootScope.isRouteLoading = true;
        if ($scope.p.searchString === '' || $scope.p.searchString === undefined) {
            ussdService.findDistinctTransactionByRangePS($scope.p.range, $scope.p.page, $scope.p.size).then(function (response) {
                $scope.p.txns = response.data;
                $rootScope.isRouteLoading = false;
            });
        } else {
            ussdService.searchUSSDTxn($scope.p.searchString).then(function (response) {
                $scope.p.txns = response.data;
                $rootScope.isRouteLoading = false;
            });
        }
    };
    $scope.downloadData = function () {
        $scope.modalUser = {};
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'download.html',
            controller: 'USSDTransactionMiniController',
            resolve: {
                details: function () {
                    return {};
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
        }, function () {
        });
    };


});
app.controller('USSDTransactionMiniController', function ($scope, $modalInstance, ussdService, details, notificationService, $rootScope, smsService) {
    init();
    function init() {
        console.log(details);
        $scope.p = {};
        $scope.p.page = 0;
        $scope.p.size = 10;
        $scope.p.d = details;
        ussdService.findLogsBySessionId(details.sessionid).then(function (response) {
            $scope.p.txns = response.data;
            console.log(response.data);
        });
    }
    ;

    $scope.downloadSMSData = function () {
        if ($scope.w.from === undefined || $scope.w.from === 'NaN' || $scope.w.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else if ($scope.w.to === undefined || $scope.w.to === 'NaN' || $scope.w.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.w.from).getTime();
            var to = new Date($scope.w.to).getTime();
            $rootScope.from = from;
            $rootScope.to = to;
            $rootScope.fromS = $scope.w.from;
            $rootScope.toS = $scope.w.to;
            smsService.downloadUSSDData(from, to).then(function (response) {
                $modalInstance.dismiss();
            });
        }
    };
});

app.controller('DeliveryReportController', function ($scope, $rootScope, userService, notificationService, smsService, $modal, $log, $interval, $timeout) {

    init();
    function init() {
        $scope.p = {};

        $scope.p.range = 'THIS_WEEK';
        $scope.p.tcolor = 'btn-warning';
        $scope.p.wcolor = 'btn-primary';
        $scope.p.mcolor = 'btn-primary';
        $scope.p.ycolor = 'btn-primary';
        $scope.p.yestcolor = 'btn-primary';
        $scope.amChartOptions = {
            "type": "serial",
            "theme": "light",
            "categoryField": "date",
            fontFamily: 'Open Sans',
            pathToImages: 'https://cdnjs.cloudflare.com/ajax/libs/amcharts/3.13.0/images/',
            categoryAxis: {
                parseDates: false
            },
            "graphs": [
                {
                    "balloonText": "Delivered : [[value]]",
                    "fillAlphas": 1,
                    "title": "Delivered",
                    "type": "column",
                    "valueField": "delivrd"
                }, {
                    "balloonText": "Undelivered : [[value]]",
                    "fillAlphas": 1,
                    "title": "Undelivered",
                    "type": "column",
                    "valueField": "undeliv"
                }, {
                    "balloonText": "Expired : [[value]]",
                    "fillAlphas": 1,
                    "title": "Expired",
                    "type": "column",
                    "valueField": "expired"
                }, {
                    "balloonText": "Unknown : [[value]]",
                    "fillAlphas": 1,
                    "title": "Unknown",
                    "type": "column",
                    "valueField": "unknown"
                }
            ],
            "guides": [],
            "valueAxes": [
                {
                    title: "Number of SMS : "
                }
            ],
            "chartCursor": {
                "categoryBalloonEnabled": false,
                "cursorAlpha": 0,
                "zoomable": false
            },
            "allLabels": [],
            "balloon": {},
            "depth3D": 10,
            "angle": 20,
            legend: {
                enabled: true
            },
            "titles": [],
            "data": [],
            "export": {
                "enabled": true
            }

        };

        smsService.getDeliveryReportStatsByRange($scope.p.range).then(function (response) {
            $scope.$broadcast("amCharts.updateData", response, "deliverychart");
            $timeout(function () {
                $scope.$broadcast("amCharts.triggerChartAnimate", "deliverychart");
            }, 120);
            $scope.deliveries = response.data;
        });




    }
    ;
    $scope.getColor = function (rangeType) {
        if ('TODAY' === rangeType) {
            $scope.p.tcolor = 'btn-warning';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_WEEK' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-warning';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_MONTH' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-warning';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('THIS_YEAR' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-warning';
            $scope.p.yestcolor = 'btn-primary';
        } else if ('YESTERDAY' === rangeType) {
            $scope.p.tcolor = 'btn-primary';
            $scope.p.wcolor = 'btn-primary';
            $scope.p.mcolor = 'btn-primary';
            $scope.p.ycolor = 'btn-primary';
            $scope.p.yestcolor = 'btn-warning';
        }
    };
    $scope.getSMSByRangeType = function (rangeType) {
        $rootScope.isRouteLoading = true;
        $scope.p.range = rangeType;
        $scope.getColor(rangeType);
        smsService.getDeliveryReportStatsByRange(rangeType).then(function (response) {
            $scope.deliveries = response.data;
            console.log(response.data);
            $rootScope.isRouteLoading = false;
        });
    };

    $scope.sumData = function (data) {
        var total = 0;
        var x = parseInt(data.delivrd, 10);
        if (isNaN(x)) {
            x = 0;
        }
        total += parseInt(x, 10);
        x = parseInt(data.expired, 10);
        if (isNaN(x)) {
            x = 0;
        }
        total += parseInt(x, 10);
        x = parseInt(data.undeliv, 10);
        if (isNaN(x)) {
            x = 0;
        }
        total += parseInt(x, 10);
        x = parseInt(data.unknown, 10);
        if (isNaN(x)) {
            x = 0;
        }
        total += parseInt(x, 10);
        return total;
    };
    $scope.getDeliveryReportStatsByDate = function () {
        $rootScope.searchType = "DATE";
        if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            $rootScope.from = from;
            $rootScope.to = to;
            $rootScope.fromS = $scope.p.from;
            $rootScope.toS = $scope.p.to;
            smsService.getDeliveryReportStatsByDate(from, to, $scope.p.size, $scope.p.size).then(function (response) {
                $scope.$broadcast("amCharts.updateData", response, "deliverychart");
                $timeout(function () {
                    $scope.$broadcast("amCharts.triggerChartAnimate", "deliverychart");
                }, 120);
                $scope.deliveries = response.data;
                $rootScope.isRouteLoading = false;
            });
        }
    };
    $scope.findDeliveryData = function () {
        if ($scope.p.criterion === undefined || $scope.p.criterion === 'NaN' || $scope.p.criterion === null) {
            notificationService.warning("Message", "Please select a criterion");
        } else if ($scope.p.searchText === undefined || $scope.p.searchText === 'NaN' || $scope.p.searchText === null) {
            notificationService.warning("Message", "Please enter your search string.");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else {
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            smsService.searchDeliveryReports($scope.p.criterion, $scope.p.searchText, from, to).then(function (response) {
                $scope.deliveries = response.data.obj;
            });
        }
    };
});

app.controller('AllSMSController', function ($scope, $rootScope, smsService, notificationService, $modal, $log, $interval) {

    init();
    function init() {
        $scope.p = {};
        $scope.p.showData = false;
        $scope.p.data = [];
        $scope.p.logs = [];
        $rootScope.isRouteLoading = true;
        $scope.p.autoRefresh = true;
        smsService.findBroadcastLogs().then(function (response) {
            $scope.p.logs = response.data.obj;
            $rootScope.isRouteLoading = false;
        });

        $interval(function () {
            if ($scope.p.autoRefresh) {
                smsService.findBroadcastLogs().then(function (response) {
                    $scope.p.logs = response.data.obj;
                });
            }
        }, 5000);


    }
    ;

    $scope.findData = function () {
        if ($scope.p.criterion === undefined || $scope.p.criterion === 'NaN' || $scope.p.criterion === null) {
            notificationService.warning("Message", "Please select a criterion");
        } else if ($scope.p.searchText === undefined || $scope.p.searchText === 'NaN' || $scope.p.searchText === null) {
            notificationService.warning("Message", "Please enter your search string.");
        } else if ($scope.p.to === undefined || $scope.p.to === 'NaN' || $scope.p.to === null) {
            notificationService.warning("Message", "Please select an end date");
        } else if ($scope.p.from === undefined || $scope.p.from === 'NaN' || $scope.p.from === null) {
            notificationService.warning("Message", "Please select a start date");
        } else {
            $rootScope.isRouteLoading = true;
            var from = new Date($scope.p.from).getTime();
            var to = new Date($scope.p.to).getTime();
            smsService.allSMSBySearchCriterion($scope.p.criterion, $scope.p.searchText, from, to).then(function (response) {
                $rootScope.isRouteLoading = false;
                $scope.p.data = response.data.obj;
                $scope.p.showData = true;
            });
        }
    };
});