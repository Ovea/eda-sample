(function() {

    var me;

    bus.local.topic('/event/me/loaded').subscribe(function(user) {
       me = user;
    });

    bus.local.topic('/event/dom/loaded').subscribe(function() {

        function publish() {
            var msg = $.trim($('#chatroom input').val());
            if (msg) {
                $('#chatroom input').val('');
                bus.remote.topic('/event/chatroom/message').publish({
                    msg: msg
                });
            }
        }

        $('#chatroom').dialog({
            stack: false,
            width: 600,
            height: 400
        });
        $('#chatroom').dialog('widget').find('.ui-dialog-titlebar-close').remove();
        $('#chatroom input').keypress(function(evt) {
            if (evt.which == 13) {
                publish();
            }
        });
        $('#chatroom button').click(function() {
            publish();
        });
    });

    bus.remote.topic('/event/chatroom/message').subscribe(function(evt) {
        var li = $('<li>' + new Date(evt.at).toLocaleTimeString() + ' [<span>' + evt.user + '</span>] ' + evt.msg + '</li>');
        $('.wrapper ul').append(li);
        if (evt.user == me.name) {
            li.find('span').addClass('me');
        }
        $('.wrapper').get(0).scrollTop = $('.wrapper').height();
    });

    bus.remote.topic('/event/user/connected').subscribe(function(user) {
        var li = $('<li>' + new Date().toLocaleTimeString() + ' - <span>' + user.name + '</span> connected !</li>');
        if (user.name == me.name) {
            li.find('span').addClass('me');
        }
        $('.wrapper ul').append(li);
    });

    bus.remote.topic('/event/user/disconnected').subscribe(function(evt) {
        var li = $('<li>' + new Date().toLocaleTimeString() + ' - <span>' + evt.user + '</span> disconnected !</li>');
        if (evt.user == me.name) {
            li.find('span').addClass('me');
        }
        $('.wrapper ul').append(li);
    });

})();
