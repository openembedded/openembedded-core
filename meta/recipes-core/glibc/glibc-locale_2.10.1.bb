#INHIBIT_DEFAULT_DEPS = "1"
LICENSE = "LGPL"

BPN = "glibc"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Binary locales are generated at build time if ENABLE_BINARY_LOCALE_GENERATION
# is set. The idea is to avoid running localedef on the target (at first boot)
# to decrease initial boot time and avoid localedef being killed by the OOM
# killer which used to effectively break i18n on machines with < 128MB RAM.

# default to disabled 
ENABLE_BINARY_LOCALE_GENERATION ?= "0"
ENABLE_BINARY_LOCALE_GENERATION_pn-glibc-locale-nativesdk = "0"

#enable locale generation on these arches
# BINARY_LOCALE_ARCHES is a space separated list of regular expressions
BINARY_LOCALE_ARCHES ?= "arm.* i[3-6]86 x86_64 powerpc mips"

# set "1" to use cross-localedef for locale generation
# set "0" for qemu emulation of native localedef for locale generation
LOCALE_GENERATION_WITH_CROSS-LOCALEDEF = "1"

PR = "r0"

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

PROVIDES = "virtual/libc-locale"

PACKAGES = "glibc-locale localedef${PKGSUFFIX}"

PACKAGES_DYNAMIC = "locale-base-* \
                    glibc-gconv-*${PKGSUFFIX}  glibc-charmap-*  glibc-localedata-*  glibc-binary-localedata-*"

FILES_localedef${PKGSUFFIX} = "${bindir}/localedef"

DESCRIPTION_localedef = "glibc: compile locale definition files"

do_install () {
	cp -fpPR ${STAGING_INCDIR}/glibc-locale-internal-${MULTIMACH_TARGET_SYS}/* ${D}
	cp -fpPR ${D}/SUPPORTED ${WORKDIR}
}

do_install[depends] += "virtual/libc${PKGSUFFIX}:do_populate_sysroot"

BBCLASSEXTEND = "nativesdk"
