(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoRequestDetailController', PtoRequestDetailController);

    PtoRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PtoRequest', 'Employee'];

    function PtoRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, PtoRequest, Employee) {
        var vm = this;

        vm.ptoRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ptotrackerApp:ptoRequestUpdate', function(event, result) {
            vm.ptoRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
