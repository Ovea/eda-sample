(function() {

    bus.local.topic('/event/dom/loaded').subscribe(function() {
        $('#logout').click(function() {
            $.post('/service/security/logout', function() {
                window.location = '/';
            });
        });
    });

})();
