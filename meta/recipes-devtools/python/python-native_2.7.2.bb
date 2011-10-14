require python.inc
DEPENDS = "openssl-native bzip2-full-native zlib-native readline-native sqlite3-native"
PR = "${INC_PR}.0"

SRC_URI += "file://04-default-is-optimized.patch \
           file://05-enable-ctypes-cross-build.patch \
           file://06-ctypes-libffi-fix-configure.patch \
           file://10-distutils-fix-swig-parameter.patch \
           file://11-distutils-never-modify-shebang-line.patch \
           file://12-distutils-prefix-is-inside-staging-area.patch \
           file://debug.patch \
           file://unixccompiler.patch \
           file://nohostlibs.patch \
           file://multilib.patch "
S = "${WORKDIR}/Python-${PV}"

inherit native

EXTRA_OEMAKE = '\
  BUILD_SYS="" \
  HOST_SYS="" \
  LIBC="" \
  STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE} \
  STAGING_INCDIR=${STAGING_INCDIR_NATIVE} \
'

do_configure_prepend() {
	autoreconf --verbose --install --force --exclude=autopoint Modules/_ctypes/libffi || bbnote "_ctypes failed to autoreconf"
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${bindir}/
	install -m 0755 Parser/pgen ${D}${bindir}/

	# Make sure we use /usr/bin/env python
	for PYTHSCRIPT in `grep -rIl ${bindir}/python ${D}${bindir}`; do
		sed -i -e '1s|^#!.*|#!/usr/bin/env python|' $PYTHSCRIPT
	done

	ln -sf python ${D}${bindir}/python2

}
