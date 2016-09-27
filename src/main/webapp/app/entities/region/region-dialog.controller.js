(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('RegionDialogController', RegionDialogController);

    RegionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Region', 'Country'];

    function RegionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Region, Country) {
        var vm = this;

        vm.region = entity;
        vm.clear = clear;
        vm.save = save;
        vm.countries = Country.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.region.id !== null) {
                Region.update(vm.region, onSaveSuccess, onSaveError);
            } else {
                Region.save(vm.region, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ptotrackerApp:regionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
