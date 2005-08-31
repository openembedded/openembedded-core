LICENSE = "GPL"
SECTION = "console/network"
PRIORITY = "standard"
DESCRIPTION = "Tools for zmodem/xmodem/ymodem file transfer"
DEPENDS = ""
PR = "r2"

SRC_URI = "http://www.ohse.de/uwe/releases/lrzsz-${PV}.tar.gz \
	   file://autotools.patch;patch=1 \
	   file://makefile.patch;patch=1 \
	   file://gettext.patch;patch=1"

inherit autotools gettext

do_install() {
	install -d ${D}${bindir}/
	install -m 0755 src/lrz src/lsz ${D}${bindir}/
	ln -sf ./lrz ${D}${bindir}/rz
	ln -sf ./lrz ${D}${bindir}/rx
	ln -sf ./lrz ${D}${bindir}/rb
	ln -sf ./lsz ${D}${bindir}/sz
	ln -sf ./lsz ${D}${bindir}/sx
	ln -sf ./lsz ${D}${bindir}/sb
}
