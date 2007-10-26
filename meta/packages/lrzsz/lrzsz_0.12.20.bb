LICENSE = "GPL"
SECTION = "console/network"
PRIORITY = "standard"
DESCRIPTION = "Tools for zmodem/xmodem/ymodem file transfer"
DEPENDS = ""
PR = "r3"

SRC_URI = "http://www.ohse.de/uwe/releases/lrzsz-${PV}.tar.gz \
	   file://autotools.patch;patch=1 \
	   file://makefile.patch;patch=1 \
	   file://gettext.patch;patch=1"

inherit autotools gettext

do_install() {
	install -d ${D}${bindir}/
	install -m 0755 src/lrz src/lsz ${D}${bindir}/
}

pkg_postinst() {
	for util in rz rx rb sz sx sb; do
		update-alternatives --install ${bindir}/$util $util lrz 100
	done
}

pkg_postrm() {
	for util in rz rx rb sz sx sb; do
		update-alternatives --remove $util ${bindir}/lrz
	done
}