require python.inc
DEPENDS = "openssl-native bzip2-full-native zlib-native readline-native sqlite3-native"
PR = "${INC_PR}.3"

SRC_URI = "\
  http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.bz2 \
  file://00-fix-bindir-libdir-for-cross.patch;patch=1 \
  file://04-default-is-optimized.patch;patch=1 \
  file://05-enable-ctypes-cross-build.patch;patch=1 \
  file://10-distutils-fix-swig-parameter.patch;patch=1 \
  file://11-distutils-never-modify-shebang-line.patch;patch=1 \
  file://12-distutils-prefix-is-inside-staging-area.patch;patch=1 \
  file://debug.patch;patch=1 \
  file://nohostlibs.patch;patch=1 \
"
S = "${WORKDIR}/Python-${PV}"

inherit native

EXTRA_OEMAKE = '\
  BUILD_SYS="" \
  HOST_SYS="" \
  LIBC="" \
  STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE} \
  STAGING_INCDIR=${STAGING_INCDIR_NATIVE} \
'

NATIVE_INSTALL_WORKS = "1"
do_install() {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${bindir}/
	install -m 0755 Parser/pgen ${D}${bindir}/
}
