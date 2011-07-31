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

