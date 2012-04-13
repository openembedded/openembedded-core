DESCRIPTION = "SystemTap - script-directed dynamic tracing and performance analysis tool for Linux"

require systemtap_git.inc

DEPENDS = "elfutils sqlite3 systemtap-native"
DEPENDS_virtclass-native = "elfutils-native sqlite3-native gettext-native"
DEPENDS_virtclass-nativesdk = "elfutils-nativesdk sqlite3-nativesdk gettext-nativesdk"

PR = "r3"

export CC_FOR_BUILD = "${BUILD_CC}"
export CFLAGS_FOR_BUILD = "${BUILD_CFLAGS}"
export LDFLAGS_FOR_BUILD = "${BUILD_LDFLAGS}"

EXTRA_OECONF += "--with-libelf=${STAGING_DIR_TARGET} --without-rpm \
	     ac_cv_file__usr_include_nss=no \
	     ac_cv_file__usr_include_nss3=no \
	     ac_cv_file__usr_include_nspr=no \
	     ac_cv_file__usr_include_nspr4=no \
	     ac_cv_file__usr_include_avahi_client=no \
	     ac_cv_file__usr_include_avahi_common=no "

STAP_DOCS ?= "--disable-docs --disable-publican --disable-refdocs"

EXTRA_OECONF += "${STAP_DOCS} "

inherit autotools gettext

BBCLASSEXTEND = "native nativesdk"

FILES_${PN}-dbg += "${libexecdir}/systemtap/.debug"
