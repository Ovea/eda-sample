jQuery(document).ajaxError(function(e, xhr) {
    switch (xhr.status) {
        case 403:
            window.location = 'login.html';
            break;
    }
});
