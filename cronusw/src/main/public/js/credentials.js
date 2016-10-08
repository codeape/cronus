
define([
  'jquery',
  'underscore',
  'backbone'
  ], function($, _, Backbone) {

    var Credentials = Backbone.Model.extend({
        url: '/user/authenticate',
        defaults: {
            uid: null,
            password: null,
            token: null
        }
    });

    return Credentials
});