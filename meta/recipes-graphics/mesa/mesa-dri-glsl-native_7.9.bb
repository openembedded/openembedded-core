DESCRIPTION = "gl shader language specific build from mesa-dri"
HOMEPAGE = "http://mesa3d.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://main.cpp;endline=22;md5=a12a9c0208ee64a07ce314dfed4c81eb"
PR = "r0"

DEPENDS = "makedepend-native talloc-native"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"

S = "${WORKDIR}/Mesa-${PV}/src/glsl"

inherit native

# use default config for native build
do_configure_prepend() {
	ln -sf ${S}/../../configs/default ${S}/../../configs/current
}

do_install() {
	install -d ${D}/${bindir}
	install -m 755 ${S}/glsl_compiler ${D}/${bindir}/
}
