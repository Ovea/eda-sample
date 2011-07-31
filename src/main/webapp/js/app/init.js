Log.level = Log.DEBUG;

bus = {

    local: new EventBus({
        name: 'EventBus Local'
    }),

    remote: EventBus.cometd({
        name: 'EventBus Remote',
        logLevel: 'warn',
        url: document.location.href.substring(0, document.location.href.length - document.location.pathname.length) + '/async',
        onConnect: function() {
            bus.local.topic('/event/bus/remote/connected').publish();
        },
        onDisconnect: function() {
            bus.local.topic('/event/bus/remote/disconnected').publish();
        }
    })

};

$(function() {
    bus.local.topic('/event/dom/loaded').publish();
});

$(document).ajaxError(function(e, xhr) {
    switch (xhr.status) {
        case 403:
            bus.local.topic('/event/server/session/expired').publish();
            break;
    }
});
