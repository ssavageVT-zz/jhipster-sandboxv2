(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoPeriodDeleteController',PtoPeriodDeleteController);

    PtoPeriodDeleteController.$inject = ['$uibModalInstance', 'entity', 'PtoPeriod'];

    function PtoPeriodDeleteController($uibModalInstance, entity, PtoPeriod) {
        var vm = this;

        vm.ptoPeriod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PtoPeriod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
