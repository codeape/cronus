
define([
  'jquery',
  'underscore',
  'backbone',
  'mustache'
  ], function($, _, Backbone, Mustache) {

    var form_template = '' +
      '<div class="container" id="login-div">' +
      '  <div class="jumbotron" id="login-form">' +
      '    <h2>Sign in</h2>' +
      '    <label class="sr-only" for="user">User</label>' +
      '    <input type="text" id="user" class="form-control" placeholder="User" required autofocus>' +
      '    <label class="sr-only" for="password">Password</label>' +
      '    <input type="password" id="password"  class="form-control" placeholder="Password" required>' +
      '    <button class="btn btn-primary btn-block" id="login">Sign in</button>' +
      '  </div>' +
      '</div>"'

    var LoginView = Backbone.View.extend({
      el: '#main',

      initialize: function(options) {
        this.listenTo(this.model, "sync", this.onSyncModel)
        this.render()
      },

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
      },

      onSyncModel: function(evt) {
        console.log(this.model.get("token"))
        $("#token").val(this.model.get("token"))
      },

      template: Mustache.render.bind(null, form_template),

      render: function() {
        console.log(form_template)
        this.$el.html(this.template())
        return this;
      }
    });

    return LoginView
});