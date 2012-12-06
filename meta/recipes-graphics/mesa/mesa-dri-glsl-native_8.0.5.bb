require mesa-dri-glsl-native.inc

LIC_FILES_CHKSUM = "file://glsl_parser.yy;beginline=2;endline=23;md5=a12a9c0208ee64a07ce314dfed4c81eb"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"
SRC_URI[md5sum] = "01305591073a76b65267f69f27d635a3"
SRC_URI[sha256sum] = "511b8da34f8e69ed24caf422964fb7ae747f3b1d8093f6b8aa2602164a475a5e"

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
