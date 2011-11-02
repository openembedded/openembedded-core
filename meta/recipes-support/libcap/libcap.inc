DESCRIPTION = "support for getting/setting POSIX.1e capabilities"
HOMEPAGE = "http://sites.google.com/site/fullycapable/"

# no specific GPL version required
LICENSE = "BSD | GPLv2"
LIC_FILES_CHKSUM = "file://License;md5=3f84fd6f29d453a56514cb7e4ead25f1"

DEPENDS = "attr perl-native-runtime"
DEPENDS += "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
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

EXTRA_OEMAKE = " \
  LIBATTR=yes \
  PAM_CAP=${@base_contains('DISTRO_FEATURES', 'pam', 'yes', 'no', d)} \
  INDENT= SYSTEM_HEADERS=${STAGING_INCDIR} RAISE_SETFCAP=no \
  lib=${@os.path.basename('${libdir}')} \
"
EXTRA_OEMAKE_virtclass-native = " \
  LIBATTR=no \
  PAM_CAP=no \
  INDENT= \
  lib=${@os.path.basename('${libdir}')} \
"

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake install DESTDIR="${D}" prefix="${prefix}"
}

# pam files
FILES_${PN} += "${libdir}/security/*.so"
FILES_${PN}-dbg += "${libdir}/security/.debug/*.so"

BBCLASSEXTEND = "native"
