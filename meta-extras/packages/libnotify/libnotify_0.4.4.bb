DESCRIPTION = "Send desktop notifications to a notification daemon"
HOMEPAGE = "http://www.galago-project.org/"
SECTION = "libs"
LICENSE = "LGPL"
DEPENDS = "dbus gtk+"
PR = "r0"

SRC_URI = "http://www.galago-project.org/files/releases/source/${PN}/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
