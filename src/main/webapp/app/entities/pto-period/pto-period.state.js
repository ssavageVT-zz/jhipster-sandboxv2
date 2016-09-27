(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pto-period', {
            parent: 'entity',
            url: '/pto-period',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PtoPeriods'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pto-period/pto-periods.html',
                    controller: 'PtoPeriodController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pto-period-detail', {
            parent: 'entity',
            url: '/pto-period/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PtoPeriod'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pto-period/pto-period-detail.html',
                    controller: 'PtoPeriodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PtoPeriod', function($stateParams, PtoPeriod) {
                    return PtoPeriod.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pto-period',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pto-period-detail.edit', {
            parent: 'pto-period-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-period/pto-period-dialog.html',
                    controller: 'PtoPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PtoPeriod', function(PtoPeriod) {
                            return PtoPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pto-period.new', {
            parent: 'pto-period',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-period/pto-period-dialog.html',
                    controller: 'PtoPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                hoursAllowed: null,
                                daysInPeriod: null,
                                hoursAccrued: null,
                                hoursRemaining: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pto-period', null, { reload: 'pto-period' });
                }, function() {
                    $state.go('pto-period');
                });
            }]
        })
        .state('pto-period.edit', {
            parent: 'pto-period',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-period/pto-period-dialog.html',
                    controller: 'PtoPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PtoPeriod', function(PtoPeriod) {
                            return PtoPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pto-period', null, { reload: 'pto-period' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pto-period.delete', {
            parent: 'pto-period',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-period/pto-period-delete-dialog.html',
                    controller: 'PtoPeriodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PtoPeriod', function(PtoPeriod) {
                            return PtoPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pto-period', null, { reload: 'pto-period' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
