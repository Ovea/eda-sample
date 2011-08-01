(function() {

    var dlg = $("<div id='sessionExpiredDialog' title='Server'>Session timed out !</div>");

    function callback() {
        if (dlg.dialog('isOpen') !== true) {
            dlg.dialog({
                modal: true,
                resizable: false,
                buttons: {
                    Ok: function() {
                        window.location = '/login.html';
                    }
                }
            });
        }
    }

    bus.local.sync(['/event/dom/loaded', '/event/request/refused'], callback);

    bus.remote.topic('/event/server/session/expired').subscribe(function() {
        callback();
        bus.remote.stop();
    });

})();
