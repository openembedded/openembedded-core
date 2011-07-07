require quilt.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
RDEPENDS_${PN} += "patch diffstat bzip2 util-linux"
PR = "r2"
SRC_URI += "file://aclocal.patch \
            file://gnu_patch_test_fix_target.patch \
           "

SRC_URI[md5sum] = "f77adda60039ffa753f3c584a286f12b"
SRC_URI[sha256sum] = "73fd760d3b5cbf06417576591dc37d67380d189392db9000c21b7cbebee49ffc"

inherit autotools gettext 
