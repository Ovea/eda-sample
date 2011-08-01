// REST com.

bus.local.topic('/event/login/requested').subscribe(function(user) {
    $.post('service/security/login', {
        user: user
    }, function(data) {
        if (data.error) {
            bus.local.topic('/event/login/failed').publish(data.error);
        } else {
            bus.local.topic('/event/login/succeed').publish(data.user);
        }
    }, 'json')
});

// UI component

bus.local.topic('/event/dom/loaded').subscribe(function() {
    $('#login').click(function(e) {
        e.preventDefault();
        var u = $.trim($('#username').val());
        if (u.length) {
            bus.local.topic('/event/login/requested').publish(u);
        } else {
            $('.error').text('Missing username !');
        }
    });
});

bus.local.topic('/event/login/failed').subscribe(function(err) {
    $('.error').text(err);
});

bus.local.topic('/event/login/succeed').subscribe(function() {
    window.location = '/home.html';
});
