DESCRIPTION = "A collection of utilities and DSOs to handle compiled objects."
HOMEPAGE = "https://fedorahosted.org/elfutils"
SECTION = "base"
LICENSE = "GPLv2 with exceptions"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3\
                    file://EXCEPTION;md5=570adcb0c1218ab57f2249c67d0ce417"
DEPENDS = "libtool bzip2 zlib"

PR = "r2"

SRC_URI = "https://fedorahosted.org/releases/e/l/elfutils/elfutils-${PV}.tar.bz2"

SRC_URI[md5sum] = "a0bed1130135f17ad27533b0034dba8d"
SRC_URI[sha256sum] = "8aebfa4a745db21cf5429c9541fe482729b62efc7e53e9110151b4169fe887da"

# pick the patch from debian
# http://ftp.de.debian.org/debian/pool/main/e/elfutils/elfutils_0.148-1.debian.tar.gz

SRC_URI += "\
        file://redhat-portability.diff \
        file://redhat-robustify.diff \
        file://hppa_backend.diff \
        file://arm_backend.diff \
        file://mips_backend.diff \
        file://m68k_backend.diff \
        file://do-autoreconf.diff \
        file://testsuite-ignore-elflint.diff \
        file://elf_additions.diff \
	file://elfutils-fsize.patch \
	file://remove-unused.patch \
	file://mempcpy.patch \
"
# The buildsystem wants to generate 2 .h files from source using a binary it just built,
# which can not pass the cross compiling, so let's work around it by adding 2 .h files
# along with the do_configure_prepend()

SRC_URI += "\
        file://i386_dis.h \
        file://x86_64_dis.h \
"
inherit autotools

EXTRA_OECONF = "--program-prefix=eu-"

do_configure_prepend() {
	sed -i 's:./i386_gendis:echo\ \#:g' ${S}/libcpu/Makefile.am

	cp ${WORKDIR}/*dis.h ${S}/libcpu
}

# Only append ldflags for target recipe
TARGET_LDFLAGS_libc-uclibc += "${@['', '-lintl -luargp']['${PN}' == '${BPN}']}"

BBCLASSEXTEND = "native nativesdk"

# Package utilities separately
PACKAGES =+ "${PN}-binutils"
FILES_${PN}-binutils = "\
    ${bindir}/eu-addr2line \
    ${bindir}/eu-ld \
    ${bindir}/eu-nm \
    ${bindir}/eu-readelf \
    ${bindir}/eu-size \
    ${bindir}/eu-strip"
# Fix library issues
FILES_${PN} =+ "${libdir}/*-${PV}.so"

# The elfutils package contains symlinks that trip up insane
INSANE_SKIP_elfutils = "1"
