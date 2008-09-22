HOMEPAGE = "http://www.moblin.org/projects/projects_connman.php"
SUMMARY  = "Moblin Glib D-Bus integration"
LICENSE  = "GPL LGPL"
DEPENDS  = "glib-2.0 dbus"
PV       = "0.0+git${SRCREV}"
S        = "${WORKDIR}/git"

SRC_URI  = "git://moblin.org/repos/projects/libgdbus.git;protocol=http"

inherit autotools pkgconfig

do_stage() {
    autotools_stage_all
}
