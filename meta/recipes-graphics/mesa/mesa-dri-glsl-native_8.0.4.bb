DESCRIPTION = "gl shader language specific build from mesa-dri"
HOMEPAGE = "http://mesa3d.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://glsl_parser.cpp;beginline=3;endline=33;md5=d078f1cddc2fc355719c090482254bd9"

DEPENDS = "makedepend-native"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"
SRC_URI[md5sum] = "d546f988adfdf986cff45b1efa2d8a46"
SRC_URI[sha256sum] = "02ed19f4f5f6535dda03a9932a81438aa78ea723ebba1f39a3d49a70a4e1d07e"

S = "${WORKDIR}/Mesa-${PV}/src/glsl/"

inherit native

# use default config for native build
do_configure_prepend() {
	ln -sf ${S}/../../configs/default ${S}/../../configs/current
}

do_install() {
	install -d ${D}/${bindir}/glsl
	install -m 755 ${S}/builtin_compiler ${D}/${bindir}/glsl/builtin_compiler
	install -m 755 ${S}/glsl_compiler ${D}/${bindir}/glsl/glsl_compiler
}
