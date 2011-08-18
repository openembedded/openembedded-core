SUMMARY = "Tools for zmodem/xmodem/ymodem file transfer"
DESCRIPTION = "Lrzsz is a cosmetically modified zmodem/ymodem/xmodem package built from \
the public-domain version of Chuck Forsberg's rzsz package. \
These programs use error correcting protocols ({z,x,y}modem) to send (sz, sx, sb) and \
receive (rz, rx, rb) files over a dial-in serial port from a variety of programs \
running under various operating systems. "
HOMEPAGE = "http://www.ohse.de/uwe/software/lrzsz.html"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
			file://src/lrz.c;beginline=1;endline=10;md5=5276956373ff7d8758837f6399a1045f"
SECTION = "console/network"
DEPENDS = ""
PR = "r3"

SRC_URI = "http://www.ohse.de/uwe/releases/lrzsz-${PV}.tar.gz \
	   file://autotools.patch \
	   file://makefile.patch \
	   file://gettext.patch"

SRC_URI[md5sum] = "b5ce6a74abc9b9eb2af94dffdfd372a4"
SRC_URI[sha256sum] = "c28b36b14bddb014d9e9c97c52459852f97bd405f89113f30bee45ed92728ff1"

inherit autotools gettext

do_install() {
	install -d ${D}${bindir}/
	install -m 0755 src/lrz src/lsz ${D}${bindir}/
}

pkg_postinst_${PN}() {
	for util in rz rx rb sz sx sb; do
		update-alternatives --install ${bindir}/$util $util lrz 100
	done
}

pkg_postrm_${PN}() {
	for util in rz rx rb sz sx sb; do
		update-alternatives --remove $util ${bindir}/lrz
	done
}
