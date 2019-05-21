app.controller('NavbarController', function ($scope, $rootScope, $location, userService) {
    
    init();
    function init() {
        console.log("NavbarController initiated!!!!");
        userService.findCurrentUser().then(function (response) {
            $rootScope.cUser = response.data.obj;
        }); 
    };
    $scope.getClass = function (path) {
        if ($location.path().substr(0, path.length) === path) {
            return true;
        } else {
            return false;
        }
    };
    $rootScope.hasPermission = function (index) {
        var permissions = $rootScope.cUser.role.permission.includes(index);
        return permissions;
    };
    $rootScope.hasNavigation = function (index) {
        var permissions = $rootScope.cUser.role.navigation.includes(index);
        return permissions;
    };
});