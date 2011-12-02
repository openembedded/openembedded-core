DEPENDS += "mesa-dri-glsl-native"

SRCREV = "983fa4ad523535debf2e94cf6ac1fd4c5630c0d2"
PV = "7.11+gitr${SRCPV}"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=03ccdc4c379c4289aecfb8892c546f67"
FILESEXTRAPATHS_prepend := "${THISDIR}/mesa-git:"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://uclibc.patch \
           file://crossfix.patch \
           "
S = "${WORKDIR}/git"

do_configure_prepend() {
  #check for python not python2, because python-native does not stage python2 binary/link
  sed -i 's/AC_CHECK_PROGS(\[PYTHON2\], \[python2 python\])/AC_CHECK_PROGS(\[PYTHON2\], \[python python\])/g' ${S}/configure.ac
  # We need builtin_compiler built for buildhost arch instead of target (is provided by mesa-dri-glsl-native)"
  sed -i "s#\./builtin_compiler#${STAGING_BINDIR_NATIVE}/glsl/builtin_compiler#g" ${S}/src/glsl/Makefile
}
