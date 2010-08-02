DESCRIPTION = "gl shader language specific build from mesa-dri"
HOMEPAGE = "http://mesa3d.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11"
LICENSE = "MIT"
PR = "r0"

DEPENDS = "makedepend-native"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"

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
