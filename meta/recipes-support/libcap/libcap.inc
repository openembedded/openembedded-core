SUMMARY = "Library for getting/setting POSIX.1e capabilities"
HOMEPAGE = "http://sites.google.com/site/fullycapable/"

# no specific GPL version required
LICENSE = "BSD | GPLv2"
LIC_FILES_CHKSUM = "file://License;md5=3f84fd6f29d453a56514cb7e4ead25f1"

DEPENDS = "perl-native-runtime"

SRC_URI = "${DEBIAN_MIRROR}/main/libc/libcap2/${BPN}2_${PV}.orig.tar.gz \
           file://fix-CAP_LAST_CAP.patch"

PR = "r1"

inherit lib_package

# do NOT pass target cflags to host compilations
#
do_configure() {
	# libcap uses := for compilers, fortunately, it gives us a hint
	# on what should be replaced with ?=
	sed -e 's,:=,?=,g' -i Make.Rules
	sed -e 's,^BUILD_CFLAGS ?= $(.*CFLAGS),BUILD_CFLAGS := $(BUILD_CFLAGS),' -i Make.Rules

	# disable gperf detection
	sed -e '/shell gperf/cifeq (,yes)' -i libcap/Makefile
}

PACKAGECONFIG ??= "attr ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}"
PACKAGECONFIG_class-native ??= ""

PACKAGECONFIG[attr] = "LIBATTR=yes,LIBATTR=no,attr"
PACKAGECONFIG[pam] = "PAM_CAP=yes,PAM_CAP=no,libpam"

EXTRA_OEMAKE += " \
  INDENT=  \
  lib=${@os.path.basename('${libdir}')} \
  RAISE_SETFCAP=no \
"

EXTRA_OEMAKE_append_class-target = " SYSTEM_HEADERS=${STAGING_INCDIR}"

do_compile() {
	oe_runmake ${EXTRA_OECONF}
}

do_install() {
	oe_runmake install \
		${EXTRA_OECONF} \
		DESTDIR="${D}" \
		prefix="${prefix}" \
		SBINDIR="${D}${sbindir}"
}

do_install_append() {
	# Move the library to base_libdir
	install -d ${D}${base_libdir}
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		mv ${D}${libdir}/* ${D}${base_libdir}
		rmdir ${D}${libdir}
	fi
}

FILES_${PN}-dev += "${base_libdir}/*.so"

# pam files
FILES_${PN} += "${base_libdir}/security/*.so"
FILES_${PN}-dbg += "${base_libdir}/security/.debug/*.so"

BBCLASSEXTEND = "native nativesdk"
