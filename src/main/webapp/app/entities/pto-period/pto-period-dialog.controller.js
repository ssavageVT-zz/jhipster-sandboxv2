(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoPeriodDialogController', PtoPeriodDialogController);

    PtoPeriodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PtoPeriod', 'Employee'];

    function PtoPeriodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PtoPeriod, Employee) {
        var vm = this;

        vm.ptoPeriod = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ptoPeriod.id !== null) {
                PtoPeriod.update(vm.ptoPeriod, onSaveSuccess, onSaveError);
            } else {
                PtoPeriod.save(vm.ptoPeriod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ptotrackerApp:ptoPeriodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
