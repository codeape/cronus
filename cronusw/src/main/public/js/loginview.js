
define([
  'jquery',
  'underscore',
  'backbone'
  ], function($, _, Backbone) {

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

    return LoginView
});