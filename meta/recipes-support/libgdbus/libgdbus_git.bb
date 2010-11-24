HOMEPAGE = "http://www.moblin.org/projects/projects_connman.php"
SUMMARY  = "Moblin Glib D-Bus integration"
LICENSE  = "GPLv2&LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
		    file://COPYING.LIB;md5=fb504b67c50331fc78734fed90fb0e09"
DEPENDS  = "glib-2.0 dbus"
PV       = "0.0+git${SRCREV}"
S        = "${WORKDIR}/git"

SRC_URI  = "git://git.kernel.org/pub/scm/bluetooth/libgdbus.git;protocol=git"

inherit autotools pkgconfig
