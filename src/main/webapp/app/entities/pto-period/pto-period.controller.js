(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoPeriodController', PtoPeriodController);

    PtoPeriodController.$inject = ['$scope', '$state', 'PtoPeriod'];

    function PtoPeriodController ($scope, $state, PtoPeriod) {
        var vm = this;
        
        vm.ptoPeriods = [];

        loadAll();

        function loadAll() {
            PtoPeriod.query(function(result) {
                vm.ptoPeriods = result;
            });
        }
    }
})();
