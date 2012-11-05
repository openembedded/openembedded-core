require glib.inc

PR = "r0"
PE = "1"

DEPENDS += "libffi python-argparse-native zlib"
DEPENDS_class-native += "libffi-native python-argparse-native"
DEPENDS_class-nativesdk += "nativesdk-libffi python-argparse-native nativesdk-zlib ${BPN}-native"

SHRT_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://glib-2.0_fix_for_x32.patch \
          "
SRC_URI[md5sum] = "ee779493b083be9348a841e0a51f1b27"
SRC_URI[sha256sum] = "6e84dc9d84b104725b34d255421ed7ac3629e49f437d37addde5ce3891c2e2f1"

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch"
BBCLASSEXTEND = "native nativesdk"

do_configure_prepend() {
	sed -i -e '1s,#!.*,#!${USRBINPATH}/env python,' ${S}/gio/gdbus-2.0/codegen/gdbus-codegen.in
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
    sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/env perl,' ${D}${bindir}/glib-mkenums
  fi
}
