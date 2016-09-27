(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('DepartmentDialogController', DepartmentDialogController);

    DepartmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Department', 'Location', 'Employee'];

    function DepartmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Department, Location, Employee) {
        var vm = this;

        vm.department = entity;
        vm.clear = clear;
        vm.save = save;
        vm.locations = Location.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.department.id !== null) {
                Department.update(vm.department, onSaveSuccess, onSaveError);
            } else {
                Department.save(vm.department, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ptotrackerApp:departmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
