require glib.inc

PR = "r2"
PE = "1"

DEPENDS += "libffi python-argparse-native"
DEPENDS_virtclass-native += "libffi-native python-argparse-native"
DEPENDS_virtclass-nativesdk += "libffi-nativesdk python-argparse-native zlib-nativesdk"

SHRT_VER = "${@bb.data.getVar('PV',d,1).split('.')[0]}.${@bb.data.getVar('PV',d,1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.bz2 \
           file://configure-libtool.patch \
           file://60_wait-longer-for-threads-to-die.patch \
           file://g_once_init_enter.patch \
           file://remove.test.for.qsort_r.patch \
          "
SRC_URI[md5sum] = "fee101d9d7daa8ddfbae00325f307f3b"
SRC_URI[sha256sum] = "ca9c731017ab370859e847f8b70079bc6dcf389dc0ccb0d0419925aff81b9687"

# Only apply this patch for target recipe on uclibc
SRC_URI_append_libc-uclibc = " ${@['', 'file://no-iconv.patch']['${PN}' == '${BPN}']}"

SRC_URI_append_virtclass-native = " file://glib-gettextize-dir.patch"
BBCLASSEXTEND = "native nativesdk"

do_configure_prepend() {
  # missing ${topdir}/gtk-doc.make and --disable-gtk-doc* is not enough, because it calls gtkdocize (not provided by gtk-doc-native)
  sed -i '/^docs/d' ${S}/configure.ac
  sed -i 's/SUBDIRS = . m4macros glib gmodule gthread gobject gio tests po docs/SUBDIRS = . m4macros glib gmodule gthread gobject gio tests po/g' ${S}/Makefile.am
  sed -i -e "s:TEST_PROGS += gdbus-serialization::g"  ${S}/gio/tests/Makefile.am
}

do_install_append() {
  # remove some unpackaged files
  rm -f ${D}${libdir}/gdbus-2.0/codegen/*.pyc
  rm -f ${D}${libdir}/gdbus-2.0/codegen/*.pyo
}

PACKAGES += "${PN}-codegen"
FILES_${PN}-codegen = "${libdir}/gdbus-2.0/codegen/*.py"
FILES_${PN} += "${datadir}/glib-2.0/gettext/mkinstalldirs ${datadir}/glib-2.0/gettext/po/Makefile.in.in"
