(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .controller('TaskController', TaskController);

    TaskController.$inject = ['$scope', '$state', 'Task'];

    function TaskController ($scope, $state, Task) {
        var vm = this;
        
        vm.tasks = [];

        loadAll();

        function loadAll() {
            Task.query(function(result) {
                vm.tasks = result;
            });
        }
    }
})();
