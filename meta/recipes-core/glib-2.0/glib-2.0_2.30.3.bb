require glib.inc

PR = "r2"
PE = "1"

DEPENDS += "libffi python-argparse-native zlib"
DEPENDS_virtclass-native += "libffi-native python-argparse-native"
DEPENDS_virtclass-nativesdk += "libffi-nativesdk python-argparse-native zlib-nativesdk"

SHRT_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://60_wait-longer-for-threads-to-die.patch \
           file://g_once_init_enter.patch \
           file://glib-2.0_fix_for_x32.patch \
           file://nodbus.patch \
          "
SRC_URI[md5sum] = "0f9fa329c6c1012d0fd861ad3d8a4520"
SRC_URI[sha256sum] = "e6cbb27c71c445993346e785e8609cc75cea2941e32312e544872feba572dd27"

# Only apply this patch for target recipe on uclibc
SRC_URI_append_libc-uclibc = " ${@['', 'file://no-iconv.patch']['${PN}' == '${BPN}']}"

SRC_URI_append_virtclass-native = " file://glib-gettextize-dir.patch"
BBCLASSEXTEND = "native nativesdk"

PERLPATH = "${bindir}/env perl"
PERLPATH_virtclass-native = "/usr/bin/env perl"
PERLPATH_virtclass-nativesdk = "/usr/bin/env perl"

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
  # and empty dirs
  rmdir ${D}${libdir}/gio/modules/
  rmdir ${D}${libdir}/gio/

  # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
  # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
  if [ -f ${D}${bindir}/glib-mkenums ]; then
    sed -i -e '1s,#!.*perl,#! ${PERLPATH},' ${D}${bindir}/glib-mkenums
  fi
}

PACKAGES += "${PN}-codegen"
FILES_${PN}-codegen = "${libdir}/gdbus-2.0/codegen/*.py"
FILES_${PN} += "${datadir}/glib-2.0/gettext/mkinstalldirs ${datadir}/glib-2.0/gettext/po/Makefile.in.in"
