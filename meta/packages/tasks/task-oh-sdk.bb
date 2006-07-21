DESCRIPTON = "Software Development Tasks for OpenedHand Poky"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r7"

PACKAGES = "\
    task-oh-sdk \
    task-oh-sdk-base \
    task-oh-sdk-apps \
    task-oh-sdk-libs \
    task-oh-sdk-x11-base \
    task-oh-sdk-x11-apps \
    task-oh-sdk-x11-libs \
    task-oh-sdk-x11-xext \
    task-oh-sdk-x11-xlibs"

ALLOW_EMPTY = "1"

RDEPENDS_task-oh-sdk := "\
    task-oh-sdk-base \
    task-oh-sdk-apps \
    task-oh-sdk-libs \
    task-oh-sdk-x11-base \
    task-oh-sdk-x11-apps \
    task-oh-sdk-x11-libs \
    task-oh-sdk-x11-xext \
    task-oh-sdk-x11-xlibs"

RDEPENDS_task-oh-sdk-base := "\
    autoconf \
    automake \
    binutils \
    binutils-symlinks \
    gcc \
    gcc-symlinks \
    glibc-dev \
    make \
    perl-module-re \
    perl-module-text-wrap \
    pkgconfig"

RDEPENDS_task-oh-sdk-apps := "\
    avahi-dev \
    binutils-dev \
    console-tools-dev \
    db-dev \
    gdb-dev \
    udev-dev"

RDEPENDS_task-oh-sdk-libs := "\
    bzip2-dev \
    dbus-dev \
    eds-dbus-dev \
    glib-2.0-dev \
    ncurses-dev \
    zlib-dev"

RDEPENDS_task-oh-sdk-x11-base := "\
    atk-dev \
    cairo-dev \
    gconf-dbus-dev \
    gtk+-dev \
    libmatchbox-dev \
    matchbox-desktop-dev \
    pango-dev \
    startup-notification-dev"
#    gtk-engines-dev \
#    libsvg-cairo-dev \

RDEPENDS_task-oh-sdk-x11-apps := "\
    dates-dev \
"

RDEPENDS_task-oh-sdk-x11-libs := "\
    gconf-dbus-dev \
    gnome-vfs-dbus-dev \
"

RDEPENDS_task-oh-sdk-x11-xext := "\
    compositeext-dev \
    damageext-dev \
    fixesext-dev \
    randrext-dev \
    recordext-dev \
    renderext-dev \
    resourceext-dev \
    xcalibrate-dev \
    xcalibrateext-dev \
    xextensions-dev \
    xproto-dev \
    xtrans-dev"

RDEPENDS_task-oh-sdk-x11-xlibs := "\
    libx11-dev \
    libxau-dev \
    libxcursor-dev \
    libxdmcp-dev \
    libxext-dev \
    libxfixes-dev \
    libxfont-dev \
    libxft-dev \
    libxpm-dev \
    libxrandr-dev \
    libxrender-dev \
    libxsettings-client-dev \
    libxt-dev \
    libxtst-dev"
#    libxdamage-dev \
#    libxcomposite-dev \



RDEPENDS_task-sdk-unsorted := "\
    gnome-mime-data-dev \
    gtk-clearlooks-engine-dev \
    ipkg-dev \
    js-dev \
    kernel-dev \
    libapm-dev \
    libcurl-dev \
    libdaemon-dev \
    libexpat-dev \
    libfakekey-dev \
    libfontconfig-dev \
    libfreetype-dev \
    libgcrypt-dev \
    libglade-2.0-dev \
    libgmp-dev \
    libgnutls-dev \
    libgpg-error-dev \
    libgtkhtml-2-dev \
    libice-dev \
    libiconv-dev \
    libipkg-dev \
    libiw-dev \
    libjpeg-dev \
    libloudmouth-1-dev \
    libpcap-dev \
    libpng-dev \
    libpng12-dev \
    libpopt-dev \
    libreadline-dev \
    libsm-dev \
    libstartup-notification-1-dev \
    libxau-dev \
    libxcalibrate-dev \
    libxml2-dev \
    libxmu-dev \
    libxmuu-dev \
    libxsettings-dev \
    linux-libc-headers-dev \
    lttng-viewer-dev \
    matchbox-desktop-dev \
    ppp-dev \
    shared-mime-info-dev \
    sysfsutils-dev \
    sysvinit-dev \
    others found \
    expat-dev \
    fontconfig-dev \
    freetype-dev \
    gstreamer-dev \
    ice-dev \
    ipkg-dev \
    jpeg-dev \
    libapm-dev \
    libdisplaymigration-dev \
    libetpan-dev \
    libgcrypt-dev \
    libglade-dev \
    libgpg-error-dev \ 
    libidl-dev \
    libiw-dev \
    libmimedir-dev \
    libpcap-dev \
    libpixman-dev \
    libpng-dev \
    libschedule-dev \
    libsm-dev \
    libsoundgen-dev \
    libsvg-dev \
    libtododb-dev \
    libts-dev \
    libxml2-dev \    
    openobex-dev \
    popt-dev \
    readline-dev \
    sqlite-dev \
    xmu-dev"
