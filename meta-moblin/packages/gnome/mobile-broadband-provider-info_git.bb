
inherit gnome pkgconfig

SRC_URI = "git://git.gnome.org/mobile-broadband-provider-info;protocol=git \
           file://fixpkgconfig.patch;patch=1"

S = "${WORKDIR}/git"
