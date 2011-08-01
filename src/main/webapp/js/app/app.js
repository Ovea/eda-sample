(function() {

    function bootstrap() {
        $.getJSON('/service/users', function(users) {
            bus.local.topic('/event/users/loaded').publish(users);
        });

        $.getJSON('/service/users/me', function(me) {
            bus.local.topic('/event/me/loaded').publish(me);
        });

        bus.remote.start();
    }

    /*bus.remote.topic('/event/server/remote/disconnected').subscribe(function() {
        bootstrap();
    });*/

    bootstrap();

})();
