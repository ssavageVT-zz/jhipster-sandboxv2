(function() {
    'use strict';
    angular
        .module('ptotrackerApp')
        .factory('PtoRequest', PtoRequest);

    PtoRequest.$inject = ['$resource', 'DateUtils'];

    function PtoRequest ($resource, DateUtils) {
        var resourceUrl =  'api/pto-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.requestDate = DateUtils.convertDateTimeFromServer(data.requestDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
