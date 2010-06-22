require quilt.inc

RDEPENDS_${PN} += "patch diffstat bzip2 util-linux"
PR = "r0"
SRC_URI += "file://aclocal.patch"

inherit gettext
