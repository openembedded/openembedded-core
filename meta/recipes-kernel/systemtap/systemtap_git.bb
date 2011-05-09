DESCRIPTION = "SystemTap - script-directed dynamic tracing and performance analysis tool for Linux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "elfutils"

SRCREV = "4ab3a1863bf4f472acae7a809bf2b38d91658aa8"
PR = "r1"
PV = "1.4+git${SRCPV}"

SRC_URI = "git://sources.redhat.com/git/systemtap.git;protocol=git \
	file://fix_for_compilation_with_gcc-4.6.0.patch \
          "

EXTRA_OECONF = "--prefix=${D} --with-libelf=${STAGING_DIR_TARGET} --without-rpm \
	     ac_cv_file__usr_include_nss=no \
	     ac_cv_file__usr_include_nss3=no \
	     ac_cv_file__usr_include_nspr=no \
	     ac_cv_file__usr_include_nspr4=no \
	     ac_cv_file__usr_include_avahi_client=no \
	     ac_cv_file__usr_include_avahi_common=no "

SRC_URI[md5sum]    = "cb202866ed704c44a876d041f788bdee"
SRC_URI[sha256sum] = "8ffe35caec0d937bd23fd78a3a8d94b58907cc0de0330b35e38f9f764815c459"

COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuppc|emenlow|crownbay|atom-pc|n450)"

S = "${WORKDIR}/git"

inherit autotools
