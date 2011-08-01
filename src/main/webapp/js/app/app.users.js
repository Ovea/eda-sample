(function() {

    var me;

    function row(user) {
        var li = $('<li>' + user.name + '</li>');
        li.data('user', user);
        li.addClass(user.name == me.name ? 'me' : user.status);
        return li;
    }

    bus.local.sync(['/event/users/loaded', '/event/me/loaded', '/event/dom/loaded'], function(call1, call2) {
        me = call2.arg[0];
        var users = call1.arg[0];
        var ul = $('#users ul');
        ul.empty();
        for (var i in users) {
            ul.append(row(users[i]));
        }
        $('#users').dialog({
            stack: false,
            closeOnEscape: false,
            position: ['right','top']
        });
        $('#users').dialog('widget').find('.ui-dialog-titlebar-close').remove();
    });

    bus.local.topic('/event/idle').subscribe(function(status) {
        bus.remote.topic('/event/user/status/changed').publish({
            status: status == 'active' ? 'online' : 'away'
        });
    });

    bus.remote.topic('/event/user/connected').subscribe(function(user) {
        $('#users ul').append(row(user));
    });

    bus.remote.topic('/event/user/disconnected').subscribe(function(evt) {
        $('#users li').filter(function() {
            return evt.user == $(this).data('user').name;
        }).remove();
    });

    bus.remote.topic('/event/user/status/changed').subscribe(function(evt) {
        if(evt.user != me.name) {
            $('#users li').filter(function() {
                return evt.user == $(this).data('user').name;
            }).removeClass('online').removeClass('away').addClass(evt.status);
        }
    });

})();
