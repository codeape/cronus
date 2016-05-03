$(function(){
    var Credentials = Backbone.Model.extend({
        url: '/user/authenticate',
        defaults: {
            uid: null,
            password: null
        }
    });

    var cred = new Credentials();

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

    var loginView = new LoginView({model: new Credentials()})
});

