$(function(){
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
            403: function() {
                // 403 -- Access denied
                window.location.replace('/time/#denied');
            }
        }
    });

    var Credentials = Backbone.Model.extend({
        url: '/user/authenticate',
        defaults: {
            uid: null,
            password: null,
            token: null
        }
    });

    window.credentials = new Credentials()

    var LoginView = Backbone.View.extend({
        el: '#login-form',

        events: {
            "change #user": 'onChangeUser',
            "change #password": 'onChangePassword',
            "click #login": 'onClickLogin'
        },

        onChangeUser: function(evt) {
            this.model.set('uid', evt.currentTarget.value);
        },

        onChangePassword: function(evt) {
            this.model.set('password', evt.currentTarget.value);
        },

        onClickLogin: function(evt) {
            this.model.save();
        }
    });

    Backbone.history.start()
    var workspace = new Workspace();
    window.location.replace('/time/#login');
});