(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('PtoRequestDeleteController',PtoRequestDeleteController);

    PtoRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'PtoRequest'];

    function PtoRequestDeleteController($uibModalInstance, entity, PtoRequest) {
        var vm = this;

        vm.ptoRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PtoRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
