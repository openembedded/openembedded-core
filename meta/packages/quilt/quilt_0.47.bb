require quilt_${PV}.inc

RDEPENDS_${PN} += "patch diffstat bzip2 util-linux"
SRC_URI += "file://aclocal.patch;patch=1"
PR = "r1"

inherit gettext
