DESCRIPTION = "SystemTap - script-directed dynamic tracing and performance analysis tool for Linux"

require systemtap_git.inc

DEPENDS = "elfutils sqlite3 systemtap-native"
DEPENDS_class-native = "elfutils-native sqlite3-native gettext-native"
DEPENDS_class-nativesdk = "nativesdk-elfutils nativesdk-sqlite3 nativesdk-gettext"

RDEPENDS_${PN} += "python bash"
RDEPENDS_${PN}_class-native += "python-native"
RDEPENDS_${PN}_class-nativesdk += "python-native"

PR = "r1"

EXTRA_OECONF += "--with-libelf=${STAGING_DIR_TARGET} --without-rpm \
            --without-nss --without-avahi \
            --disable-server --disable-grapher "

STAP_DOCS ?= "--disable-docs --disable-publican --disable-refdocs"

EXTRA_OECONF += "${STAP_DOCS} "

inherit autotools gettext pkgconfig

BBCLASSEXTEND = "native nativesdk"

FILES_${PN}-dbg += "${libexecdir}/systemtap/.debug"
