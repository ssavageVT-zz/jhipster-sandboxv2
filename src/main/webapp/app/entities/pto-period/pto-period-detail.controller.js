(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoPeriodDetailController', PtoPeriodDetailController);

    PtoPeriodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PtoPeriod', 'Employee'];

    function PtoPeriodDetailController($scope, $rootScope, $stateParams, previousState, entity, PtoPeriod, Employee) {
        var vm = this;

        vm.ptoPeriod = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ptotrackerApp:ptoPeriodUpdate', function(event, result) {
            vm.ptoPeriod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
