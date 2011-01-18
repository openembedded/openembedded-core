require x-load.inc

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/x-load-git/${MACHINE}"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=25;md5=ef08d08cb99057bbb5b9d6d0c5a4396f"

PV = "1.42+${PR}+git${SRCPV}"
PR="r13"

#SRC_URI = "git://www.sakoman.net/git/x-load-omap3.git;branch=master;protocol=git"
#SRC_URI = "git://gitorious.org/x-load-omap3/mainline.git;branch=master;protocol=git"
SRC_URI = "git://gitorious.org/x-loader/x-loader.git;branch=master;protocol=git"

SRC_URI_append_beagleboard = " \
                              file://name.patch;patch=1 \
                             "
S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(beagleboard|omap3evm|overo)"
