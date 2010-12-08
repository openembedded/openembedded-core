DESCRIPTION = "X11 toolkit intrinsics library"

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=917881a51be1013bee30d7f3f5ba9f55"


DEPENDS += "libsm virtual/libx11 kbproto libxdmcp"
PROVIDES = "xt"

PR = "r0"
PE = "1"

XORG_PN = "libXt"

BBCLASSEXTEND = "native"

EXTRA_OECONF += "--disable-install-makestrs --disable-xkb"

do_compile() {
	(
		unset CC LD CXX CCLD CFLAGS
		oe_runmake -C util 'XT_CFLAGS=' 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE -I${STAGING_INCDIR_NATIVE} ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makestrs
	)
	if [ "$?" != "0" ]; then
		exit 1
	fi
	oe_runmake
}

SRC_URI[md5sum] = "8a414f8f2327aaa616ca2dcac1f5d8c3"
SRC_URI[sha256sum] = "eab91b594b801f7f07a20f936dda70a629028858cbcf541becf94f2786ae6b01"
