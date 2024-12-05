(function () {
    "use strict";
    var closeMenus = function () {
        var openLaunchers = document.querySelectorAll('.dropdown [aria-expanded="true"]'),
            openMenus = document.querySelectorAll('.dropdown [aria-expanded="true"] + .links'),
            len = openLaunchers.length;
        for (i=0; i<len; i++) {
            openLaunchers[i].setAttribute("aria-expanded", "");
            openLaunchers[i].parentElement.focus();
            openLaunchers[i].setAttribute("aria-expanded", "false");
            hideMenu(openMenus[i]);
        }
        document.removeEventListener('click', closeMenus);
    },
    hideMenu = function(thisMenu) {
        setTimeout(function(){thisMenu.setAttribute("hidden", "hidden");}, 300);
    },
    launcherClick = function(e) {
        var launcher  = e.target,
            container = launcher.parentElement,
            menu      = launcher.parentElement.querySelector('.dropdown .links');
        launcher.blur();
        closeMenus();
        menu.removeAttribute("hidden");
        setTimeout(function() {
            launcher.setAttribute("aria-expanded", "true");
        }, 50);
        document.addEventListener('click', closeMenus);
        e.stopPropagation();
        e.preventDefault();
        menu.focus();
    },
    launchers = document.querySelectorAll('.dropdown .launcher'),
    len   = launchers.length,
    i = 0;

    for (i=0; i<len; i++) {
        launchers[i].addEventListener('click', launcherClick);
    }
})();
