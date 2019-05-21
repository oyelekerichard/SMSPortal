app.directive('myDomDirective', function () {
    return {
        link: function ($scope, element, attrs) {
          
//          $( "div.demo-container" ).html(function() {
//  var emphasis = "<em>" + $( "p" ).length + " paragraphs!</em>";
//  return "<p>All new content for " + emphasis + "</p>";
//});
  
          
          element.bind('render', function () {
                $( 'div.timeline-body' ).html('You clicked me!');
            });
//          var htmlString = $( this ).html();
//  $( this ).text( htmlString );
//});
            element.bind('click', function () {
                element.html('You clicked me!');
            });
            element.bind('mouseenter', function () {
                element.css('background-color', 'yellow');
            });
            element.bind('mouseleave', function () {
                element.css('background-color', 'white');
            });
        }
    };
});