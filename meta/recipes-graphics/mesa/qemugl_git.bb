DESCRIPTION = "QEMU i386 OpenGL passtrough"
HOMEPAGE = "http://savannah.nongnu.org/projects/qemugl"
SECTION = "x11/drivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://opengl_client.c;beginline=4;endline=23;md5=a7dbe915be5fb5df8fd496f348ed9a05 \
                    file://parse_mesa_get_c.c;befinline=4;endline=23;md5=a55f258f32720c9565a425a3956bcb5e"

DEPENDS = "virtual/libx11 xproto glproto libxfixes"

COMPATIBLE_HOST = '(x86_64.*|i.86.*)-(linux|freebsd.*)'

SRC_URI = "git://git.yoctoproject.org/qemugl;protocol=git \
           file://versionfix.patch \
           file://remove-x11r6-lib-dir.patch \
           file://call_opengl_fix.patch \
           file://extensions_emulation.patch"
S = "${WORKDIR}/git"

SRCREV = "d888bbc723c00d197d34a39b5b7448660ec1b1c0"

PV = "0.0+git${SRCPV}"
PR = "r11"

DEFAULT_PREFERENCE = "-1"

do_install () {
	install -d ${D}${libdir}/
    if [ "${PN}" != "qemugl-nativesdk" ]; then
        install -m 0755 ${S}/libGL.so.1.2 ${D}${libdir}/libGL-qemu.so.1.2
    else
	    install -m 0755 ${S}/libGL.so.1.2 ${D}${libdir}/libGL.so.1.2
	    ln -s libGL.so.1.2 ${D}${libdir}/libGL.so.1
	    ln -s libGL.so.1 ${D}${libdir}/libGL.so
    fi
}

# This cannot be converted to run at pacakge install time, because
# it depends on being run after the libgl1 package is installed,
# and RPM cannot guarantee the order of pacakge insallation.
pkg_postinst_${PN} () {
#!/bin/sh -e
if [ x"$D" = "x" ]; then
	rm -f ${libdir}/libGL.so.1.2
	ln -s libGL-qemu.so.1.2 ${libdir}/libGL.so.1.2
else
	exit 1
fi
}

BBCLASSEXTEND = "nativesdk"
