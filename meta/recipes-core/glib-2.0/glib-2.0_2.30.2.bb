require glib.inc

PR = "r2"
PE = "1"

DEPENDS += "libffi python-argparse-native zlib"
DEPENDS_virtclass-native += "libffi-native python-argparse-native"
DEPENDS_virtclass-nativesdk += "libffi-nativesdk python-argparse-native zlib-nativesdk"

SHRT_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.bz2 \
           file://configure-libtool.patch \
           file://60_wait-longer-for-threads-to-die.patch \
           file://g_once_init_enter.patch \
           file://glib-2.0_fix_for_x32.patch \
          "
SRC_URI[md5sum] = "b40f3889e8d24e1b367763673ca6deb5"
SRC_URI[sha256sum] = "94b1f1a1456c67060ca868d299bef3f7268a2c1c5c360aabb7149d4d9b2fdcd3"

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
