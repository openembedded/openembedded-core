DESCRIPTION = "gl shader language specific build from mesa-dri"
HOMEPAGE = "http://mesa3d.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://apps/compile.c;endline=26;md5=27c2833286ce9566b162bcbe21d5b267"
PR = "r0"

DEPENDS = "makedepend-native"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"
SRC_URI[md5sum] = "6be2d343a0089bfd395ce02aaf8adb57"
SRC_URI[sha256sum] = "505bf418dceba05837f4ea1b1972b9620c35f8cb94bc4d1e6d573c15f562576d"

S = "${WORKDIR}/Mesa-7.8.2/src/glsl/"

inherit native

# use default config for native build
do_configure_prepend() {
	ln -s ${S}/../../configs/default ${S}/../../configs/current
}

do_install() {
	install -d ${D}/${bindir}
	install -m 755 ${S}/apps/compile ${D}/${bindir}/glsl-compile
}
