DEPENDS += "mesa-dri-glsl-native"

SRCREV = "c1f4867c89adb1a6b19d66ec8ad146115909f0a7"
PV = "8.0.4+git${SRCPV}"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=03ccdc4c379c4289aecfb8892c546f67"
FILESEXTRAPATHS_prepend := "${THISDIR}/mesa-git:"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://0001-Compile-with-uclibc.patch \
           file://0002-cross-compile.patch \
           file://0003-fix-for-x32.patch \
           "
SRC_URI += "${@base_contains('DISTRO_FEATURES', 'x11', '', 'file://0004-gross-hack-to-prevent-from-install-libgl.patch', d)}"

S = "${WORKDIR}/git"

do_configure_prepend() {
  #check for python not python2, because python-native does not stage python2 binary/link
  sed -i 's/AC_CHECK_PROGS(\[PYTHON2\], \[python2 python\])/AC_CHECK_PROGS(\[PYTHON2\], \[python python\])/g' ${S}/configure.ac
  # We need builtin_compiler built for buildhost arch instead of target (is provided by mesa-dri-glsl-native)"
  sed -i "s#\./builtin_compiler#${STAGING_BINDIR_NATIVE}/glsl/builtin_compiler#g" ${S}/src/glsl/Makefile
}
