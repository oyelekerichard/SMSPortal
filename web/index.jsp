<%-- 
    Document   : index
    Created on : Nov 9, 2017, 12:38:00 PM
    Author     : adekanmbi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js"  class="no-js" data-ng-app="hmsApp" data-ng-cloak="">
    <!--<![endif]-->
    <!-- BEGIN HEAD -->
    <head>
        <meta charset="utf-8"/>
        <title>EKEDP SMS PORTAL</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <meta content="Index Page for SMS Portal" name="description"/>
        <meta content="Adekanmbi Oluremi" name="author"/>
        <!-- BEGIN GLOBAL MANDATORY STYLES -->
        <!--        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css"/>-->
        <link href="assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
        <!-- END GLOBAL MANDATORY STYLES -->
        <!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
        <link href="assets/global/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/fullcalendar/fullcalendar.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/morris/morris.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="assets/global/plugins/bootstrap-toastr/toastr.min.css"/>
        <!-- END PAGE LEVEL PLUGIN STYLES -->
        <!-- BEGIN PAGE STYLES -->
        <link href="assets/admin/pages/css/tasks.css" rel="stylesheet" type="text/css"/>
        <link href="assets/admin/pages/css/profile.css" rel="stylesheet" type="text/css"/>
        <link href="assets/admin/pages/css/error.css" rel="stylesheet" type="text/css"/>

        <!-- END PAGE LEVEL STYLES -->

        <!-- BEGIN THEME STYLES -->
        <!-- DOC: To use 'rounded corners' style just load 'components-rounded.css' stylesheet instead of 'components.css' in the below style tag -->
        <link href="assets/global/css/components-md.css" id="style_components" rel="stylesheet" type="text/css"/>
        <link href="assets/global/css/plugins-md.css" rel="stylesheet" type="text/css"/>
        <link href="assets/admin/layout4/css/layout.css" rel="stylesheet" type="text/css"/>
        <link href="assets/admin/layout4/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color"/>
        <link href="assets/global/css/custom.css" rel="stylesheet" type="text/css"/>
        <!-- END THEME STYLES -->
        <!--ANGULAR JS STUFF -->
        <script src="assets/global/plugins/angular/angular.min.js"></script>
        <script src="assets/global/plugins/angular/angular-route.min.js"></script>
        <script src="assets/global/plugins/angular/ngRemoteValidate.js"></script>
        <script src="assets/global/plugins/angular/angular-animate.min.js"></script>
        <script src="assets/global/plugins/angular/angular-ui-router.min.js"></script>
        <script src="assets/global/plugins/angular/angular-sanitize.min.js"></script>
        <script src="assets/global/plugins/angular/ui-bootstrap-tpls-0.12.1.min.js"></script>  
        <script src="assets/global/plugins/angular/angular-file-upload-all.min.js"></script>  
        <script src="assets/global/plugins/angular/angular-file-upload-shim.min.js"></script>  
        <script src="assets/global/plugins/export/ng-csv.min.js"></script> 
        <script src="assets/global/plugins/export/FileSaver.js"></script>         
        <script src="assets/global/plugins/angular/match.js"></script> 
        <script src="assets/global/plugins/angular/date.js"></script> 
        <script src="assets/global/plugins/angular/ui-utils.min.js"></script> 
                <!-- BEGIN PAGE LEVEL PLUGINS -->
        <script src="assets/global/plugins/amcharts/amcharts/amcharts.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/serial.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/pie.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/radar.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/themes/light.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/themes/patterns.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/themes/chalk.js" type="text/javascript"></script>
        <script src="assets/global/plugins/amcharts/amcharts/plugins/dataloader/dataloader.min.js"></script>
        <script src="assets/global/plugins/amcharts/amcharts/amChartsDirective.js" type="text/javascript"></script>
        <!-- END PAGE LEVEL PLUGINS -->

        <script type="text/javascript" src="assets/global/plugins/angular/fusioncharts.js"></script>
        <script type="text/javascript" src="assets/global/plugins/angular/fusioncharts.theme.ocean.js"></script>
        <script type="text/javascript" src="assets/global/plugins/angular/angular-fusioncharts.js"></script>
        <script type="text/javascript" src="assets/global/plugins/angular/paging.min.js"></script>

        <script src="app/app.js"></script>

        <script src="app/controllers/NavbarController.js"></script>
        <script src="app/controllers/DashboardController.js"></script>
        <script src="app/controllers/UserController.js"></script>
        <script src="app/services/DashboardService.js"></script>
        <!--ANGULAR JS STUFF END -->
        <!--        <link rel="shortcut icon" href="assets/global/img/favicon2.png" />-->
    </head>
    <!-- END HEAD -->
    <!-- BEGIN BODY -->
    <!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
    <!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
    <!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
    <!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
    <!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
    <!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
    <!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
    <!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
    <!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
    <body class="page-md page-header-fixed page-sidebar-closed-hide-logo page-sidebar-closed-hide-logo"
          data-ng-controller="NavbarController">
        <!-- BEGIN HEADER -->
        <div class="page-header md-shadow-z-1-i navbar navbar-fixed-top" style="z-index: 1000">
            <!-- BEGIN HEADER INNER -->
            <div class="page-header-inner">
                <!-- BEGIN LOGO -->
                <div class="page-logo">
                    <a href="">
                        <img src="assets/global/img/smsportal.png" alt="logo" class="img-responsive" style="margin-top: 5px"/>
                    </a>
                    <div class="menu-toggler sidebar-toggler">
                        <!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
                    </div>
                </div>
                <!-- END LOGO -->
                <!-- BEGIN RESPONSIVE MENU TOGGLER -->
                <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
                </a>
                <!-- END RESPONSIVE MENU TOGGLER -->
                <div class="page-top">
                    <!-- BEGIN TOP NAVIGATION MENU -->
                    <div class="top-menu">
                        <ul class="nav navbar-nav pull-right">
                            <!-- BEGIN USER LOGIN DROPDOWN -->
                            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                            <li class="dropdown dropdown-user dropdown-dark">
                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                    <span class="username username-hide-on-mobile">
                                        {{cUser.lastName + ' ' + cUser.firstName}} </span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-default">
                                    <li>
                                        <a href="#/profile">
                                            <i class="icon-user"></i> My Profile </a>
                                    </li>
                                    <li class="divider">
                                    </li>
                                    <li>
                                        <a href="web/user/logout">
                                            <i class="icon-key"></i> Log Out </a>
                                    </li>
                                </ul>
                            </li>
                            <!-- END USER LOGIN DROPDOWN -->
                            <!-- BEGIN USER LOGIN DROPDOWN -->
                            <!--              <li class="dropdown dropdown-extended quick-sidebar-toggler">
                                            <span class="sr-only">Toggle Quick Sidebar</span>
                                            <i class="icon-logout"></i>
                                          </li>-->
                            <!-- END USER LOGIN DROPDOWN -->
                        </ul>
                    </div>
                    <!-- END TOP NAVIGATION MENU -->
                </div>
                <!-- END PAGE TOP -->
            </div>
            <!-- END HEADER INNER -->
        </div>
        <!-- END HEADER -->
        <div class="clearfix">
        </div>
        <!-- BEGIN CONTAINER -->
        <div class="page-container">
            <!-- BEGIN SIDEBAR -->
            <div class="page-sidebar-wrapper">
                <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
                <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
                <div class="page-sidebar md-shadow-z-2-i  navbar-collapse collapse">
                    <!-- BEGIN SIDEBAR MENU -->
                    <!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
                    <!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
                    <!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
                    <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
                    <!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
                    <!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
                    <ul class="page-sidebar-menu " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
                        <li>
                            <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                            <div class="sidebar-toggler hidden-phone"></div>
                            <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                        </li>
                        <li class="start " data-ng-show="hasNavigation('1')">
                            <a href="javascript:;">
                                <i class="fa fa-user"></i>
                                <span class="title">Account Management</span>
                                <span class="arrow "></span>
                            </a>
                            <ul class="sub-menu" style="display: none;">
                                <li class="start" data-ng-class="{'active':getClass('/users')}">
                                    <a href="#/users">
                                        <i class="fa fa-users"></i>
                                        <span class="title">System Users</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/roles')}">
                                    <a href="#/roles">
                                        <i class="fa fa-certificate"></i>
                                        <span class="title">Roles</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/settings')}">
                                    <a href="#/settings">
                                        <i class="fa fa-gears"></i>
                                        <span class="title">System Settings</span>
                                    </a>
                                </li> 
                            </ul>
                        </li>
                        <li data-ng-show="hasNavigation('3')">
                            <a href="javascript:;">
                                <i class="fa fa-mobile-phone"></i>
                                <span class="title">USSD Management</span>
                                <span class="arrow "></span>
                            </a>
                            <ul class="sub-menu" style="display: none;">
                                <li class="start" data-ng-class="{'active':getClass('/incomingussd')}">
                                    <a href="#/incomingussd">
                                        <i class="fa fa-mobile"></i>
                                        <span class="title">Incoming USSD</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/searchussd')}">
                                    <a href="#/searchussd">
                                        <i class="fa fa-search"></i>
                                        <span class="title">Search USSD</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li data-ng-show="hasNavigation('2')">
                            <a href="javascript:;">
                                <i class="fa fa-mobile-phone"></i>
                                <span class="title">SMS Management</span>
                                <span class="arrow "></span>
                            </a>
                            <ul class="sub-menu" style="display: none;">
                                <li class="start" data-ng-class="{'active':getClass('/incoming')}">
                                    <a href="#/incoming">
                                        <i class="fa fa-mobile"></i>
                                        <span class="title">Incoming SMS</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/search')}">
                                    <a href="#/search">
                                        <i class="fa fa-search"></i>
                                        <span class="title">Search SMS</span>
                                    </a>
                                </li>
                            </ul>
                        </li> 
                        <li data-ng-show="hasNavigation('2')">
                            <a href="javascript:;">
                                <i class="fa fa-mobile-phone"></i>
                                <span class="title">SMS By Keyword</span>
                                <span class="arrow "></span>
                            </a>
                            <ul class="sub-menu" style="display: none;">
                                <li class="start" data-ng-class="{'active':getClass('/track')}">
                                    <a href="#/track">
                                        <i class="fa fa-map-marker"></i>
                                        <span class="title">Track</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/wfm')}">
                                    <a href="#/wfm">
                                        <i class="fa fa-bolt"></i>
                                        <span class="title">WFM</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/payment')}">
                                    <a href="#/payment">
                                        <i class="fa fa-credit-card"></i>
                                        <span class="title">Payment</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/meter')}">
                                    <a href="#/meter">
                                        <i class="fa fa-tachometer"></i>
                                        <span class="title">Meter</span>
                                    </a>
                                </li>

                                <li class="start" data-ng-class="{'active':getClass('/staff')}">
                                    <a href="#/staff">
                                        <i class="fa fa-users"></i>
                                        <span class="title">Staff</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/issue')}">
                                    <a href="#/issue">
                                        <i class="fa fa-warning"></i>
                                        <span class="title">Issue</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/getinventory')}">
                                    <a href="#/getinventory">
                                        <i class="fa fa-database"></i>
                                        <span class="title">Get Inventory</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/token')}">
                                    <a href="#/token">
                                        <i class="fa fa-code"></i>
                                        <span class="title">Token</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/register')}">
                                    <a href="#/register">
                                        <i class="fa fa-mortar-board"></i>
                                        <span class="title">Register</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li data-ng-show="hasNavigation('4')">
                            <a href="javascript:;">
                                <i class="fa fa-bullhorn"></i>
                                <span class="title">Broadcast & Delivery</span>
                                <span class="arrow "></span>
                            </a>
                            <ul class="sub-menu" style="display: none;">
                                <li class="start" data-ng-class="{'active':getClass('/deliveryreports')}">
                                    <a href="#/deliveryreports">
                                        <i class="fa fa-bus"></i>
                                        <span class="title">Delivery Report Stats</span>
                                    </a>
                                </li>
                                <li class="start" data-ng-class="{'active':getClass('/searchdeliveryreports')}">
                                    <a href="#/searchdeliveryreports">
                                        <i class="fa fa-map-marker"></i>
                                        <span class="title">Search Delivery Reports</span>
                                    </a>
                                </li>
                            </ul>
                        </li>

                        <!--                    <li class="start" data-ng-repeat="nav in userNavs" data-ng-class="{'active':getClass('{{nav.navIndicator}}')}">
                                                <a href="{{nav.href}}">
                                                    <i class="{{nav.icon}}"></i> 
                                                    <span class="title">{{nav.text}}</span>
                                                    <span class="selected"></span>
                                                </a>
                                            </li>-->
                    </ul>
                    <!-- END SIDEBAR MENU -->
                </div>
            </div>
            <!-- END SIDEBAR -->
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <div class="page-content">
                    <div ng-if='isRouteLoading'>
                        <div id="loading">
                            <img id="loading-image" src="assets/global/img/loading-spinner-default.gif" alt="Loading..." />
                        </div>
                    </div>
                    <div ng-view>

                    </div>
                </div>

                <!-- BEGIN QUICK SIDEBAR -->
                <a href="javascript:;" class="page-quick-sidebar-toggler"><i class="icon-login"></i></a>
                <div class="page-quick-sidebar-wrapper">
                    <div class="page-quick-sidebar">
                        <div class="nav-justified">
                            <ul class="nav nav-tabs nav-justified">
                                <li>
                                    <a data-toggle="tab">
                                        Alerts 
                                    </a>
                                </li>
                            </ul>
                            <div class="tab-content">
                            </div>
                        </div>
                    </div>
                </div>
                <!-- END QUICK SIDEBAR -->
            </div>
            <!-- END CONTENT -->
        </div>
        <!-- END CONTAINER -->
        <!-- BEGIN FOOTER -->
        <div class="page-footer">
            <div class="page-footer-inner">
                2018 &copy; Powered By Crown Interactive
            </div>
            <div class="scroll-to-top">
                <i class="icon-arrow-up"></i>
            </div>
        </div>
        <!-- END FOOTER -->
        <!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
        <!-- BEGIN CORE PLUGINS -->
        <!--[if lt IE 9]>
        <script src="assets/global/plugins/respond.min.js"></script>
        <script src="assets/global/plugins/excanvas.min.js"></script> 
        <![endif]-->
        <script src="assets/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
        <!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
        <script src="assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery-idle-timeout/jquery.idletimeout.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery-idle-timeout/jquery.idletimer.js" type="text/javascript"></script>  
        <script src="assets/global/plugins/bootstrap-toastr/toastr.min.js"></script>
        <script src="assets/admin/pages/scripts/ui-toastr.js"></script>
        <!-- END CORE PLUGINS -->
        <!-- BEGIN PAGE LEVEL SCRIPTS -->
        <script type="text/javascript" src="assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
        <script type="text/javascript" src="assets/global/plugins/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
        <script type="text/javascript" src="assets/global/plugins/clockface/js/clockface.js"></script>
        <script type="text/javascript" src="assets/global/plugins/bootstrap-daterangepicker/moment.min.js"></script>
        <script type="text/javascript" src="assets/global/plugins/bootstrap-daterangepicker/daterangepicker.js"></script>
        <script type="text/javascript" src="assets/global/plugins/bootstrap-colorpicker/js/bootstrap-colorpicker.js"></script>
        <script type="text/javascript" src="assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
        <script src="assets/global/scripts/metronic.js" type="text/javascript"></script>
        <script src="assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
        <script src="assets/admin/layout2/scripts/quick-sidebar.js" type="text/javascript"></script>
        <script src="assets/admin/layout4/scripts/demo.js" type="text/javascript"></script>
        <script src="assets/admin/pages/scripts/tasks.js" type="text/javascript"></script>
        <script src="assets/admin/pages/scripts/ui-idletimeout.js" type="text/javascript"></script>
        <script src="assets/admin/pages/scripts/components-pickers.js"></script>
        <!-- END PAGE LEVEL SCRIPTS -->
        <script>
                                jQuery(document).ready(function () {
                                    Metronic.init(); // init metronic core componets
                                    Layout.init(); // init layout
                                    Demo.init(); // init demo features
                                    QuickSidebar.init(); // init quick sidebar
                                    Tasks.initDashboardWidget(); // init tash dashboard widget  
                                    UIToastr.init();
                                    ComponentsPickers.init();
                                    //UIIdleTimeout.init();
                                });
        </script>
        <!-- END JAVASCRIPTS -->
    </body>
    <!-- END BODY -->
</html>
