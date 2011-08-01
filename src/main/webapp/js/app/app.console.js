(function() {

    function log(from, topic, data) {
        $('#console').append(new Date().toTimeString() + ' - ' + from + ' [' + topic.name + '] ' + (data || '') + '<br/>');
    }

    bus.local.topics(
        '/event/dom/loaded',
        '/event/request/refused',
        '/event/bus/remote/connected',
        '/event/bus/remote/disconnected',
        '/event/idle',
        '/event/me/loaded',
        '/event/users/loaded').subscribe(function() {
            log('local ', this, arguments.length ? $.toJSON(arguments[0]) : '');
        });

    bus.remote.topics(
        '/event/user/status/changed',
        '/event/user/connected',
        '/event/user/disconnected',
        '/event/server/session/expired').subscribe(function() {
            log('remote', this, arguments.length ? $.toJSON(arguments[0]) : '');
        });

})();
