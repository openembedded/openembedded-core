require quilt.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
RDEPENDS_${PN} += "patch diffstat bzip2 util-linux"
PR = "r0"
SRC_URI += "file://aclocal.patch"

inherit gettext
