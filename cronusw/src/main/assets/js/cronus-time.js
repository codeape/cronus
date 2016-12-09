
define([
  'jquery',
  'underscore',
  'backbone',
  './credentials',
  './loginview'
  ], function(
  $,
  _,
  Backbone,
  Credentials,
  LoginView) {
    console.log("cronus time is on!")

    var Workspace = Backbone.Router.extend({
        routes: {
            "login":    "login",
            "denied":   "denied",
            "register": "register"
        },
        login: function() {
            console.log("Login view loaded")
            new LoginView({model: window.credentials})
        },
        denied: function() {

        },
        register: function() {

        },
    })

    $.ajaxSetup({
        statusCode: {
            401: function(){
                // Redirec the to the login page.
                window.location.replace('/time/#login');

            },
            403: function() { // Maybe we should handle the rest here and not only 403
                // 403 -- Access denied
                window.location.replace('/time/#denied');
            }
        }
    });



    window.credentials = new Credentials()

    var workspace = new Workspace();
    Backbone.history.start()
    workspace.navigate('login', {trigger: true});

});