(function() {

    bus.local.topic('/event/dom/loaded').subscribe(function() {
        $.idleTimer(20000);
        $(document).bind('idle.idleTimer', function() {
            bus.local.topic('/event/idle').publish('inactive');
        });
        $(document).bind('active.idleTimer', function() {
            bus.local.topic('/event/idle').publish('active');
        });
    });

})();
