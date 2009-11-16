SECTION = "base"
LICENSE = "OSL"
DESCRIPTION = "A collection of utilities and DSOs to handle compiled objects."
DEPENDS = "libtool"
PR = "r2"

SRC_URI = "http://distro.ibiblio.org/pub/linux/distributions/gentoo/distfiles/elfutils-${PV}.tar.gz \
	   file://warnings.patch;patch=1"

inherit autotools_stage

# Package binaries that overlap with binutils separately
PACKAGES =+ "${PN}-binutils"
FILES_${PN}-binutils = "\
    ${bindir}/addr2line \
    ${bindir}/ld \
    ${bindir}/nm \
    ${bindir}/readelf \
    ${bindir}/size \
    ${bindir}/strip"
# Fix library issues
FILES_${PN} =+ "${libdir}/*-${PV}.so"

# The elfutils package contains symlinks that trip up insane
INSANE_SKIP_elfutils = "1"
