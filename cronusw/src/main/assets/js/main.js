//requirejs.config({
//    removeCombined: true//,
//    //mainConfigFile: './main.js',
//    //include: ['main']
//});
requirejs.config({
  removeCombined: true,
  "paths" : {
    "backbone" : "../lib/backbonejs/backbone",
    "backbonejs" : "../lib/backbonejs/backbone",
    "underscore" : "../lib/underscorejs/underscore",
    "underscorejs" : "../lib/underscorejs/underscore",
    "jquery" : "../lib/jquery/jquery"
  },
  "shim" : {
    "backbone" : {
      "deps" : [ "underscore" ],
      "exports" : "Backbone"
    },
    "backbonejs" : {
      "deps" : [ "underscorejs" ],
      "exports" : "Backbone"
    },
    "underscore" : {
      "exports" : "_"
    },
    "underscorejs" : {
      "exports" : "_"
    },
    "jquery" : {
      "exports" : "$"
    }
  },
  "packages" : [ ]
});

//requirejs.config({baseUrl: '/'});

require( ['./cronus-time'], function ( cronus ) {});