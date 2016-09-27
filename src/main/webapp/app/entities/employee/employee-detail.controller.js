(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'Department', 'Job', 'PtoPeriod', 'PtoRequest'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, Department, Job, PtoPeriod, PtoRequest) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ptotrackerApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
