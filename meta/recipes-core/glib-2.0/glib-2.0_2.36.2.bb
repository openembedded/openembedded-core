require glib.inc

PE = "1"

DEPENDS += "libffi zlib"
DEPENDS_class-native += "libffi-native"
DEPENDS_class-nativesdk += "nativesdk-libffi nativesdk-zlib ${BPN}-native"

SHRT_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://glib-2.0_fix_for_x32.patch \
           file://fix-conflicting-rand.patch \
           file://Makefile-ptest.patch \
           file://run-ptest \
          "

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch"

SRC_URI[md5sum] = "d791774ac49eaf8ba59792427bb74af7"
SRC_URI[sha256sum] = "5ea98451fb57d0ba523a1e836545f0a919b498863056fdd9da69d148c1347f80"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS_${PN}-ptest += "\
            tzdata \
            tzdata-americas \
            tzdata-asia \
            tzdata-europe \
            tzdata-posix \
            python-pygobject \
            python-dbus \
           "

RDEPENDS_${PN}-ptest_append_libc-glibc = "\
            eglibc-gconv-utf-16 \
            eglibc-charmap-utf-8 \
            eglibc-gconv-cp1255 \
            eglibc-charmap-cp1255 \
            eglibc-gconv-utf-32 \
            eglibc-gconv-utf-7 \
            eglibc-charmap-invariant \
            eglibc-localedata-translit-cjk-variants \
           "

do_configure_prepend() {
	sed -i -e '1s,#!.*,#!${USRBINPATH}/env python,' ${S}/gio/gdbus-2.0/codegen/gdbus-codegen.in
}

do_install_append() {
  # remove some unpackaged files
  rm -f ${D}${datadir}/glib-2.0/codegen/*.pyc
  rm -f ${D}${datadir}/glib-2.0/codegen/*.pyo
  # and empty dirs
  rm -rf ${D}${libdir}/gio

  # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
  # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
  if [ -f ${D}${bindir}/glib-mkenums ]; then
    sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/env perl,' ${D}${bindir}/glib-mkenums
  fi
}
