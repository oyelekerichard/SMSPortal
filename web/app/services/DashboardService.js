app.service('userService', function ($http, $rootScope) {
    var user, users, roles, role, cUser;
    this.insertUser = function (user, roleId) {
        var strObj = angular.toJson(user);
        var promise = $http.post('web/user/addUser/' + roleId, strObj).
                success(function (data, status, headers, config) {
                    console.log("Data retn = " + data.retn);
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.updateUser = function (user) {
        var strObj = angular.toJson(user);
        var promise = $http.post('web/user/update', strObj).
                success(function (data, status, headers, config) {
                    user = data;
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return user;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.findUsers = function (page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findUsers/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            users = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.findUser = function (userId) {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findUser/',
            params: {
                userId: userId
            }
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            user = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.findRoles = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findRoles/'
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            roles = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.findPermissions = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findPermissions'
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            roles = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.findNavigations = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findNavigations'
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            roles = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.findRole = function (userId) {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findRole/',
            params: {
                userId: userId
            }
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            role = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.findCurrentUser = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/user/getCurrentUser/'
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            console.log(data);
            cUser = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.changePassword = function (userid, credentials) {
        var strObj = angular.toJson(credentials);
        var promise = $http.post('web/user/changePassword/' + userid, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return user;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.changeUserState = function (userid, userstate) {
        var strObj = angular.toJson({});
        var promise = $http.post('web/user/changeUserState/' + userid, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    user = data;
                    return user;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.resetPassword = function (userid) {
        var strObj = angular.toJson({});
        var promise = $http.post('web/user/resetPassword/' + userid, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    user = data;
                    return user;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.search = function (searchString) {
        var promise = // Simple GET request example :
                $http.get('web/user/search/' + searchString).
                success(function (data, status, headers, config) {
                    if (data.retn !== 0) {
                        warning("Warning", data.desc);
                    }
                    users = data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', status);
                });
        return promise;
    };
    this.createRole = function (role, permissions) {
        var strObj = angular.toJson(permissions);
        var promise = $http.post('web/user/createRole/' + role.name + '/' + role.description, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.createRole2 = function (role, permissions, navigations) {
        var o = {
            permissions: permissions,
            navigations: navigations
        };
        var strObj = angular.toJson(o);
        var promise = $http.post('web/user/createRole2/' + role.name + '/' + role.description, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.updateRole = function (role, permissions) {
        var strObj = angular.toJson(permissions);
        var promise = $http.post('web/user/updateRole/' + role.name + '/' + role.description + '/' + role.id, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.updateRole2 = function (role, permissions, navigations) {
        var o = {
            permissions: permissions,
            navigations: navigations
        };
        var strObj = angular.toJson(o);
        var promise = $http.post('web/user/updateRole2/' + role.name + '/' + role.description + '/' + role.id, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.updateUserRole = function (userId, roleId) {
        var promise = $http({
            method: 'GET',
            url: 'web/user/updateUserRole/' + userId + '/' + roleId
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            users = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.createChannel = function (channel, roles) {
        var strObj = angular.toJson(roles);
        var promise = $http.post('web/user/createChannel/' + channel.name + '/' + channel.description, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.updateChannel = function (channel, roles) {
        var strObj = angular.toJson(roles);
        var promise = $http.post('web/user/updateChannel/' + channel.name + '/' + channel.description + '/' + channel.id, strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.findChannels = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findChannels/'
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            roles = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };

    this.findSettings = function (page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/user/findSettings/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.saveSettings = function (location) {
        var strObj = angular.toJson(location);
        var promise = $http.post('web/user/addSettings', strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.changeSetting = function (id, newValue, settingsSection) {
        var promise = $http({
            method: 'GET',
            url: 'web/user/changeSetting/' + id + '/' + newValue + '/' + settingsSection
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findNetworkProviders = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findNetworkProviders'
        }).success(function (data, status, headers, config) {
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
});
app.service('smsService', function ($http) {

    this.findBroadcastLogs = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findBroadcastLogs'
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.getSMSData = function (start, end, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getSMSData/' + start + '/' + end + '/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getSMSDataByRange = function (rangeType, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getSMSData/' + rangeType + '/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getSMSUnitsCount = function (rangeType, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getSMSUnitsCount'
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.downloadSMSData = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/downloadSMSData/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.reInitiateAction = function (uniqueId) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/reInitiateAction/' + uniqueId
        }).success(function (data, status, headers, config) {
            if (data.retn === 0) {
                successToastr('Success', data.obj);
            } else {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.resendResponse = function (uniqueId) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/resendResponse/' + uniqueId
        }).success(function (data, status, headers, config) {
            if (data.retn === 0) {
                successToastr('Success', data.desc);
            } else {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getSearchData = function (criterion, searchText, start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/searchByCriterion/' + criterion + '/' + searchText + '/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.allSMSBySearchCriterion = function (criterion, searchText, start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/allSMSBySearchCriterion/' + criterion + '/' + searchText + '/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getUSSDSearchData = function (criterion, searchText, start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getUSSDDataByCriterion/' + criterion + '/' + searchText + '/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.downloadSearchByCriterion = function (criterion, searchText, start, end) {
        var promise = $http({
            method: 'POST',
            url: 'web/portal/downloadSearchByCriterion/' + criterion + '/' + searchText + '/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.verifyPhoneNumber = function (phoneNumber) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/verifyPhoneNumber/' + phoneNumber
        }).success(function (data, status, headers, config) {
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.sendCustomSMS = function (msisdn, incoming) {
        var m = {
            msisdn: msisdn,
            incoming: incoming
        };
        var strObj = angular.toJson(m);
        var promise = $http.post('web/portal/sendCustomSMS/', strObj).
                success(function (data, status, headers, config) {
                    if (data.retn === 0) {
                        successToastr('Success', data.desc);
                    } else {
                        warning("Warning", data.desc);
                    }
                    return data;
                }).
                error(function (data, status, headers, config) {
                    errorToastr('Error', 'error ==== ' + status);
                });
        return promise;
    };
    this.getSMSDataByKeyword = function (keyword, start, end, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getSMSDataByKeyword/' + keyword + '/' + start + '/' + end + '/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getSMSDataByKeywordByRange = function (keyword, rangeType, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getSMSDataByKeyword/' + keyword + '/' + rangeType + '/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getDeliveryReportStatsByRange = function (rangeType) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getDeliveryReportStatsByRange/'+ rangeType 
        }).success(function (data, status, headers, config) {
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.getDeliveryReportStatsByDate = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getDeliveryReportStatsByDate/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
    this.searchDeliveryReports = function (searchCriterion, searchText, start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/searchDeliveryReports/' + searchCriterion + '/' + searchText +'/'+ start + '/' + end
        }).success(function (data, status, headers, config) {
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
});
app.service('notificationService', function () {
    this.warning = function (header, message) {
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "1000",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.warning(message, header);
    };
    this.success = function (header, message) {
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "1000",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.success(message, header);

    };
    this.error = function (header, message) {
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "1000",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.error(message, header);
    };
    this.logObjectFieldsArray = function (objArray) {
        for (var i = 0; i < objArray.length; i++) {
            var obj = objArray[i];
            logObjectFields(obj);
        }

    };
    this.logObjectFields = function (obj) {
        for (x in obj) {
            console.log(x + " ====== " + obj[x]);
        }
    };
});
app.service('ussdService', function ($http) {
    var settings;
    this.findDistinctTransaction = function (page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findDistinctTransaction/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findLogsBySessionId = function (sessionid) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findLogsBySessionId/' + sessionid
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };

    this.getDashboardData = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getDashboardData'
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.getDashboardDataByRangeType = function (rangeType) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getDashboardData/' + rangeType
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.getDashboardDataByDateRange = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getDashboardData/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findDistinctTransactionByDate = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findDistinctTransaction/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findDistinctTransactionByRange = function (rangeType) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findDistinctTransaction/' + rangeType
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findDistinctUSSDTemplates = function () {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findDistinctUSSDTemplates'
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findUniqueUSSDCountByRange = function (rangeType) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findUniqueUSSDCountByRange/' + rangeType
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findUniqueUSSDCountByDate = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findUniqueUSSDCountByDate/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findDistinctTransactionByDatePS = function (start, end, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findDistinctTransaction/' + start + '/' + end + '/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.findDistinctTransactionByRangePS = function (rangeType, page, size) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/findDistinctTransaction/' + rangeType + '/' + page + '/' + size
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.searchUSSDTxn = function (search) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/searchUSSDTxn/' + search
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.getUSSDDashboardStats = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getUSSDDashboardStats/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    this.getUSSDDashboardStatsByRange = function (rangeType) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/getUSSDDashboardStatsByRange/' + rangeType
        }).success(function (data, status, headers, config) {
            settings = data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });
        return promise;
    };
    
    this.downloadUSSDData = function (start, end) {
        var promise = $http({
            method: 'GET',
            url: 'web/portal/downloadSMSData/' + start + '/' + end
        }).success(function (data, status, headers, config) {
            if (data.retn !== 0) {
                warning("Warning", data.desc);
            }
            return data;
        }).error(function (data, status, headers, config) {
            errorToastr('Error', status);
        });

        return promise;
    };
});


function warning(header, message) {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.warning(message, header);
}
;
function successToastr(header, message) {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.success(message, header);

}
;
function errorToastr(header, message) {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.error(message, header);
}
;
function logObjectFieldsArray(objArray) {
    for (var i = 0; i < objArray.length; i++) {
        var obj = objArray[i];
        logObjectFields(obj);
    }

}
;
function logObjectFields(obj) {
    for (x in obj) {
        console.log(x + " ====== " + obj[x]);
    }
}
;
