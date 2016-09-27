(function() {
    'use strict';

    angular
        .module('ptotrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pto-request', {
            parent: 'entity',
            url: '/pto-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PtoRequests'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pto-request/pto-requests.html',
                    controller: 'PtoRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pto-request-detail', {
            parent: 'entity',
            url: '/pto-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PtoRequest'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pto-request/pto-request-detail.html',
                    controller: 'PtoRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PtoRequest', function($stateParams, PtoRequest) {
                    return PtoRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pto-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pto-request-detail.edit', {
            parent: 'pto-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-request/pto-request-dialog.html',
                    controller: 'PtoRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PtoRequest', function(PtoRequest) {
                            return PtoRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pto-request.new', {
            parent: 'pto-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-request/pto-request-dialog.html',
                    controller: 'PtoRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                requestDate: null,
                                hoursRequested: null,
                                isApproved: null,
                                approvedBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pto-request', null, { reload: 'pto-request' });
                }, function() {
                    $state.go('pto-request');
                });
            }]
        })
        .state('pto-request.edit', {
            parent: 'pto-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-request/pto-request-dialog.html',
                    controller: 'PtoRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PtoRequest', function(PtoRequest) {
                            return PtoRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pto-request', null, { reload: 'pto-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pto-request.delete', {
            parent: 'pto-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pto-request/pto-request-delete-dialog.html',
                    controller: 'PtoRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PtoRequest', function(PtoRequest) {
                            return PtoRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pto-request', null, { reload: 'pto-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
