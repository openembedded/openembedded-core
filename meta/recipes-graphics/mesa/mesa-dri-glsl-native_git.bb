require mesa-dri-glsl-native.inc

LIC_FILES_CHKSUM = "file://../../docs/license.html;md5=03ccdc4c379c4289aecfb8892c546f67"

SRCREV = "c1f4867c89adb1a6b19d66ec8ad146115909f0a7"
PV = "8.0.4+git${SRCPV}"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git"
S = "${WORKDIR}/git/src/glsl"

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
