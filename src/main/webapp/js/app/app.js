(function() {

    var dlg = $("<div id='sessionExpiredDialog' title='Server'>Session timed out !</div>");

    bus.local.topic('/event/server/session/expired').subscribe(function() {
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
    });

    bus.remote.start();

})();
