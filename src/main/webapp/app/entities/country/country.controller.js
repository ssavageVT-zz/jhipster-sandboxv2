(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('CountryController', CountryController);

    CountryController.$inject = ['$scope', '$state', 'Country'];

    function CountryController ($scope, $state, Country) {
        var vm = this;
        
        vm.countries = [];

        loadAll();

        function loadAll() {
            Country.query(function(result) {
                vm.countries = result;
            });
        }
    }
})();
