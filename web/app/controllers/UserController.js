app.controller('UserController', function ($modal, $log, $scope, $rootScope, userService, $location) {
    var roles;
    init();

    function init() {
        $scope.page = 1;
        $scope.size = 10;
        $scope.selectedRoutes = [];

        userService.findUsers($scope.page, $scope.size).then(function (response) {
            $scope.u = response.data.obj;
            console.log($scope.u.userdata);
        });
        userService.findCurrentUser().then(function (response) {
            $rootScope.cUser = response.data.obj;
        });
    }
    $scope.findUsers = function () {
        $scope.page = 1;
        var page = $scope.page;
        var size = $scope.size;
        userService.findUsers($scope.page, $scope.size).then(function (response) {
            $scope.users = response.data.obj;
        });
    };
    $scope.findUserRole = function (roleId) {
        var roleName;
        userService.findRole(roleId).then(function (response) {
            roleName = response.data.category;
        });
        return roleName;
    };
    $scope.next = function () {
        if ($scope.users.end === $scope.users.totalCount) {
        } else {
            $scope.page = $scope.page + 1;
            userService.findUsers($scope.page, $scope.size).then(function (response) {
                $scope.users = response.data.obj;
            });
        }

    };
    $scope.prev = function () {

        if ($scope.page === 1) {
        } else {
            $scope.page = $scope.page - 1;
            userService.findUsers($scope.page, $scope.size).then(function (response) {
                $scope.users = response.data.obj;
            });
        }
    };
    $scope.navigateToCreate = function () {
        $location.url("users/create");
    };
    $scope.insertUser = function () {
        $scope.user.eligibleRoutes = $scope.selectedRoutes;
        userService.insertUser($scope.user).then(function (response) {
            $location.url("users");
        });
    };
    $scope.updateUser = function () {
        userService.updateUser($scope.cUser).then(function (response) {
            $location.url("users");
        });
    };
    $scope.changePassword = function () {
        userService.changePassword($scope.cUser.id, $scope.cred).then(function (response) {

        });
    };
    $scope.changeUserState = function (userid) {
        userService.changeUserState(userid, "Swap").then(function (response) {
            $scope.user = response.data.obj;
            userService.findUsers($scope.page, $scope.size).then(function (response) {
                $scope.users = response.data.obj;
            });
        });
    };
    $scope.resetPassword = function (userid) {
        userService.resetPassword(userid).then(function (response) {
            userService.findUsers($scope.page, $scope.size).then(function (response) {
                $scope.users = response.data.obj;
            });
        });
    };
    $scope.search = function () {
        if ($scope.searchString === "" || $scope.searchString === undefined) {
            userService.findUsers(1, $scope.size).then(function (response) {
                $scope.users = response.data.obj;
            });
        } else {
            userService.search($scope.searchString).then(function (response) {
                $scope.users = response.data.obj;
            });
        }

    };
    $scope.manageUser = function (user) {
        $scope.modalUser = user;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'usermanage.html',
            controller: 'UserModalController',
            resolve: {
                user: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
            userService.findUsers($scope.page, $scope.size).then(function (response) {
                $scope.u = response.data.obj;
            });
        });
    };
    $scope.viewUser = function (user) {
        $scope.modalUser = user;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'viewUser.html',
            controller: 'UserModalController',
            resolve: {
                user: function () {
                    return $scope.modalUser;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
            userService.findUsers($scope.page, $scope.size).then(function (response) {
                $scope.users = response.data.obj;
            });
        });
    };

    $scope.createUser = function () {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'adduser.html',
            controller: 'UserModalController',
            resolve: {
                user: function () {
                    return {};
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            userService.findUsers($scope.page, $scope.size).then(function (response) {
            $scope.u = response.data.obj;
        });
        }, function () {
            userService.findUsers($scope.page, $scope.size).then(function (response) {
            $scope.u = response.data.obj;
        });
        });
    };
});
app.controller('UserModalController', function ($scope, $modalInstance, $rootScope, user, userService) {
    init();

    function init() {
        $scope.user = user;
        $scope.p = {};
        userService.findRoles().then(function (response) {
            $scope.rroles = response.data.obj;
        });
    }
    ;

    $scope.updateUser = function () {
        $rootScope.isRouteLoading = true;
        userService.updateUserRole($scope.user.id, $scope.p.id).then(function (response) {
            $rootScope.isRouteLoading = false;
            $modalInstance.close();
        });
    };

    $scope.insertUser = function () {
        $rootScope.isRouteLoading = true;
        userService.insertUser($scope.user, $scope.p.id).then(function (response) {
            $rootScope.isRouteLoading = false;
            $modalInstance.close();
        });
    };
});

app.controller('RoleController', function ($modal, $log, $scope, $rootScope, userService) {
    init();

    function init() {
        userService.findRoles().then(function (response) {
            $scope.rroles = response.data.obj;
            console.log($scope.rroles);
        });
    }
    ;
    $scope.findRoles = function () {
        userService.findRoles().then(function (response) {
            $scope.rroles = response.data.obj;
        });
    };
    $scope.addRole = function () {
        $scope.modalrole = {};
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'addrole.html',
            controller: 'RoleModalController',
            resolve: {
                role: function () {
                    return $scope.modalrole;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        }, function () {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        });
    };
    $scope.viewRole = function (role) {
        $scope.modalrole = role;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'viewrole.html',
            controller: 'RoleModalController',
            resolve: {
                role: function () {
                    return $scope.modalrole;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        });
    };
    $scope.updateRole = function (role) {
        $scope.modalrole = role;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'updateRole.html',
            controller: 'RoleModalController',
            resolve: {
                role: function () {
                    return $scope.modalrole;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        }, function () {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        });
    };
});
app.controller('RoleModalController', function ($scope, $modalInstance, role, userService) {
    init();
    function init() {
        $scope.role = role;
        console.log(role);
        userService.findPermissions().then(function (response) {
            $scope.permissions = response.data.obj;
            if ($scope.role.id !== undefined) {
                for (var i = 0; i < $scope.permissions.length; i++) {
                    var x = $scope.permissions[i];
                    if (role.permission.includes(x.id + "")) {
                        x.description = true;
                        $scope.permissions[i] = x;
                    }
                }
            }
        });


        userService.findNavigations().then(function (response) {
            $scope.navigations = response.data.obj;
            console.log($scope.navigations);
            if ($scope.role.id !== undefined) {
                for (var i = 0; i < $scope.navigations.length; i++) {
                    var x = $scope.navigations[i];
                    if (role.navigation.includes(x.id + "")) {
                        console.log($scope.navigations[i]);
                        x.description = true;
                        $scope.navigations[i] = x;
                        console.log($scope.navigations[i]);
                    }
                }
            }
        });
    }
    ;

    $scope.createRole = function () {
        userService.createRole2($scope.role, $scope.permissions, $scope.navigations).then(function (response) {
            console.log(response);
            $modalInstance.close();
        });
    };
    $scope.updateRole = function () {
        userService.updateRole2($scope.role, $scope.permissions, $scope.navigations).then(function (response) {
            $modalInstance.close();
        });
    };
});

app.controller('ChannelController', function ($modal, $scope, userService) {
    init();

    function init() {
        userService.findChannels().then(function (response) {
            $scope.channels = response.data.obj;
        });
    }
    ;
    $scope.findChannels = function () {
        userService.findRoles().then(function (response) {
            $scope.rroles = response.data.obj;
        });
    };
    $scope.addChannel = function () {
        $scope.modalrole = {};
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'addchannel.html',
            controller: 'ChannelModalController',
            resolve: {
                role: function () {
                    return $scope.modalrole;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        }, function () {
            userService.findChannels().then(function (response) {
                $scope.channels = response.data.obj;
            });
        });
    };
    $scope.viewChannel = function (channel) {
        $scope.modalrole = channel;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'viewchannel.html',
            controller: 'ChannelModalController',
            resolve: {
                role: function () {
                    return $scope.modalrole;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            //$scope.selected = selectedItem;
        }, function () {
            userService.findChannels().then(function (response) {
                $scope.channels = response.data.obj;
            });
        });
    };
    $scope.updateChannel = function (channel) {
        $scope.modalrole = channel;
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'updateChannel.html',
            controller: 'ChannelModalController',
            resolve: {
                role: function () {
                    return $scope.modalrole;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
            userService.findRoles().then(function (response) {
                $scope.rroles = response.data.obj;
            });
        }, function () {
            userService.findChannels().then(function (response) {
                $scope.channels = response.data.obj;
            });
        });
    };
});
app.controller('ChannelModalController', function ($scope, $rootScope, $modalInstance, role, userService) {
    init();
    function init() {
        $scope.p = {};
        $scope.channel = role;
        userService.findRoles().then(function (response) {
            $scope.roles = response.data.obj;
            if ($scope.channel.id !== undefined) {
                for (var i = 0; i < $scope.roles.length; i++) {
                    var x = $scope.roles[i];
                    if (role.permission.includes(x.id + "")) {
                        x.description = true;
                        $scope.roles[i] = x;
                    }
                }
            }
        });
    }
    ;

    $scope.createChannel = function () {
        console.log($scope.roles);
        $scope.p.roles = [];
        for (var i = 0; i < $scope.roles.length; i++) {
            var x = $scope.roles[i];
            x.createdBy = $rootScope.cUser.id;
            x.modifiedBy = $rootScope.cUser.id;
            $scope.p.roles.push(x);
        }
        userService.createChannel($scope.channel, $scope.p.roles).then(function (response) {
            $modalInstance.close();
        });
    };
    $scope.updateChannel = function () {
        for (var i = 0; i < $scope.roles.length; i++) {
            var x = $scope.roles[i];
            x.createdBy = $rootScope.cUser.id;
            x.modifiedBy = $rootScope.cUser.id;
            $scope.p.roles.push(x);
        }
        userService.updateChannel($scope.channel, $scope.p.roles).then(function (response) {
            $modalInstance.close();
        });
    };
});

app.controller('SettingsController', function ($scope, $modal, $log, userService) {
    init();
    function init() {
        console.log("SettingsController initiated!!!!");
        $scope.p = {};
        $scope.p.size = 10;
        $scope.p.page = 0;
        userService.findSettings($scope.p.page, $scope.p.size).then(function (response) {
            $scope.p.settings = response.data.settings;
        });
    }
    ;

    $scope.open = function () {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'settings.html',
            controller: 'SettingsMiniController',
            resolve: {
                details: function () {
                    return {};
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
            userService.findSettings($scope.p.page, $scope.p.size).then(function (response) {
                $scope.p.settings = response.data.settings;
            });
        });
    };
    $scope.edit = function (s) {
        var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'settings.html',
            controller: 'SettingsEditMiniController',
            resolve: {
                details: function () {
                    return s;
                }
            }
        });
        modalInstance.result.then(function (selectedItem) {
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
            userService.findSettings($scope.p.page, $scope.p.size).then(function (response) {
                $scope.p.settings = response.data.settings;
            });
        });
    };
});
app.controller('SettingsMiniController', function ($scope, $modalInstance, userService, details) {

    init();
    function init() {
        console.log("Angular JS loaded");
        console.log(details);
        $scope.p = {};
        $scope.w = {};
        $scope.w.showCreate = true;
        $scope.w.showEdit = false;
    }
    ;


    $scope.createSetting = function () {
        userService.saveSettings($scope.p).then(function (response) {
            $modalInstance.dismiss('cancel');
        });
    };
    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
});
app.controller('SettingsEditMiniController', function ($scope, $modalInstance, userService, details) {

    init();
    function init() {
        console.log("Angular JS loaded");
        console.log(details);
        $scope.p = details;
        $scope.w = {};
        $scope.w.showCreate = false;
        $scope.w.showEdit = true;
    }
    ;


    $scope.editSetting = function () {
        userService.changeSetting($scope.p.id, $scope.p.currentValue).then(function (response) {
            $modalInstance.dismiss('cancel');
        });
    };
    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
});