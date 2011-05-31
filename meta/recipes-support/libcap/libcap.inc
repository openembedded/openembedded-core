DESCRIPTION = "support for getting/setting POSIX.1e capabilities"
HOMEPAGE = "http://sites.google.com/site/fullycapable/"

# no specific GPL version required
LICENSE = "BSD | GPL"
LIC_FILES_CHKSUM = "file://License;md5=731de803c1ccbcb05a9b3523279c8d7f"

DEPENDS = "libpam attr perl-native-runtime"
# attr and pam are disabled by EXTRA_OEMAKE_virtclass-native
DEPENDS_virtclass-native = "perl-native-runtime"

SRC_URI = "${KERNELORG_MIRROR}/linux/libs/security/linux-privs/libcap2/${BPN}-${PV}.tar.bz2"

inherit lib_package

do_configure() {
	# libcap uses := for compilers, fortunately, it gives us a hint
	# on what should be replaced with ?=
	sed -e 's,:=,?=,g' -i Make.Rules
	sed -e 's,BUILD_CFLAGS ?=,BUILD_CFLAGS := $(BUILD_CFLAGS),' -i Make.Rules
}

EXTRA_OEMAKE = "LIBATTR=yes PAM_CAP=yes INDENT= lib=lib SYSTEM_HEADERS=${STAGING_INCDIR} RAISE_SETFCAP=no"
EXTRA_OEMAKE_virtclass-native = "LIBATTR=no PAM_CAP=no INDENT= lib=lib"

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake install DESTDIR="${D}"
}

# pam files
FILES_${PN} += "${libdir}/security/*.so"
FILES_${PN}-dbg += "${libdir}/security/.debug/*.so"

BBCLASSEXTEND = "native"
