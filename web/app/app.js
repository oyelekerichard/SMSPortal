/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var allowedPages = [];

var app = angular.module('hmsApp', ['remoteValidation', 'ui.bootstrap', 'ui.router', 'ngRoute', 'ui.mask', 'ngAnimate', 'ui.date',
    'angularFileUpload', 'ngSanitize', 'ngCsv', 'ng-fusioncharts', 'bw.paging'])
        .directive('dynamic', function ($compile) {
            return {
                restrict: 'A',
                replace: true,
                link: function (scope, ele, attrs) {
                    scope.$watch(attrs.dynamic, function (html) {
                        ele.html(html);
                        $compile(ele.contents())(scope);
                    });
                }
            };
        });

//This configures the routes and associates each route with a view and a controller
app.config(["$routeProvider", function ($routeProvider, $http) {
        $routeProvider
                .when('/users',
                        {
                            controller: 'UserController',
                            templateUrl: 'app/partials/users.html'
                        })
                .when('/roles',
                        {
                            controller: 'RoleController',
                            templateUrl: 'app/partials/role.html'
                        })
                .when('/incoming',
                        {
                            controller: 'SMSController',
                            templateUrl: 'app/partials/incoming.html'
                        })
                .when('/incomingussd',
                        {
                            controller: 'USSDTransactionController',
                            templateUrl: 'app/partials/incomingussd.html'
                        })
                .when('/ussddashboard',
                        {
                            controller: 'USSDTransactionController',
                            templateUrl: 'app/partials/ussddashboard.html'
                        })
                .when('/track',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/track.html'
                        })
                .when('/wfm',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/wfm.html'
                        })
                .when('/payment',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/payment.html'
                        })
                .when('/meter',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/meter.html'
                        })
                .when('/staff',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/staff.html'
                        })
                .when('/issue',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/issue.html'
                        })
                .when('/getinventory',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/getinventory.html'
                        })
                .when('/token',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/token.html'
                        })
                .when('/register',
                        {
                            controller: 'KeywordController',
                            templateUrl: 'app/partials/register.html'
                        })
                .when('/search',
                        {
                            controller: 'SearchController',
                            templateUrl: 'app/partials/search.html'
                        })
                .when('/searchussd',
                        {
                            controller: 'SearchController',
                            templateUrl: 'app/partials/searchussd.html'
                        })
                .when('/deliveryreports',
                        {
                            controller: 'DeliveryReportController',
                            templateUrl: 'app/partials/deliveryreports.html'
                        })
                .when('/searchdeliveryreports',
                        {
                            controller: 'DeliveryReportController',
                            templateUrl: 'app/partials/searchdeliveryreports.html'
                        })
                .when('/dashboard',
                        {
                            controller: 'DashboardController',
                            templateUrl: 'app/partials/dashboard.html'
                        })
                .when('/profile',
                        {
                            controller: 'UserController',
                            templateUrl: 'app/partials/profile.html'
                        })
                .when('/settings',
                        {
                            controller: 'SettingsController',
                            templateUrl: 'app/partials/settings2.html'
                        })
                .when('/error',
                        {
                            controller: 'AdmissionController',
                            templateUrl: 'app/partials/error.html'
                        })
                .when('/welcome',
                        {
                            controller: 'UserController',
                            templateUrl: 'app/partials/welcome.html'
                        })
                .otherwise({redirectTo: '/welcome'});
    }]);

app.run([
    '$rootScope',
    function ($rootScope) {
        $rootScope.$on('$routeChangeStart', function (event, next, current) {
            $rootScope.isRouteLoading = true;
        });
        $rootScope.$on('$routeChangeSuccess', function (event, next, current) {
            $rootScope.isRouteLoading = false;
        });
    }
]);

app.factory('Excel', function ($window) {
    var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
            base64 = function (s) {
                return $window.btoa(unescape(encodeURIComponent(s)));
            },
            format = function (s, c) {
                return s.replace(/{(\w+)}/g, function (m, p) {
                    return c[p];
                })
            };
    return {
        tableToExcel: function (tableId, worksheetName) {
            var table = $(tableId),
                    ctx = {
                        worksheet: worksheetName,
                        table: table.html()
                    },
                    href = uri + base64(format(template, ctx));
            return href;
        }
    };
});

angular.module('app.directives')
        .directive('ngEnter', function () { //a directive to 'enter key press' in elements with the "ng-enter" attribute

            return function (scope, element, attrs) {

                element.bind("keydown keypress", function (event) {
                    if (event.which === 13) {
                        scope.$apply(function () {
                            scope.$eval(attrs.ngEnter);
                        });
                        event.preventDefault();
                    }
                });
            };
        });