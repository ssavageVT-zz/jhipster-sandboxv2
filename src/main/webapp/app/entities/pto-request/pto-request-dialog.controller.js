(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoRequestDialogController', PtoRequestDialogController);

    PtoRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PtoRequest', 'Employee'];

    function PtoRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PtoRequest, Employee) {
        var vm = this;

        vm.ptoRequest = entity;
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
            if (vm.ptoRequest.id !== null) {
                PtoRequest.update(vm.ptoRequest, onSaveSuccess, onSaveError);
            } else {
                PtoRequest.save(vm.ptoRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ptotrackerApp:ptoRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.requestDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
