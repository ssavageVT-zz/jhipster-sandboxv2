(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoRequestController', PtoRequestController);

    PtoRequestController.$inject = ['$scope', '$state', 'PtoRequest'];

    function PtoRequestController ($scope, $state, PtoRequest) {
        var vm = this;
        
        vm.ptoRequests = [];

        loadAll();

        function loadAll() {
            PtoRequest.query(function(result) {
                vm.ptoRequests = result;
            });
        }
    }
})();
