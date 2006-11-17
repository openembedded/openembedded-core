DESCRIPTION = "Kexec is a fast reboot feature that lets you reboot to a new Linux kernel"
AUTHOR = "Eric Biederman"
HOMEPAGE = "http://www.xmission.com/~ebiederm/files/kexec/"
SECTION = "kernel/userland"
DEPENDS = "virtual/kernel zlib"
LICENSE = "GPL"
PR = "r0"

inherit autotools

EXTRA_OEMAKE = "LDFLAGS=-L${STAGING_LIBDIR} -Wl,--rpath-link,${STAGING_LIBDIR} "
EXTRA_OECONF = " --with-zlib=${STAGING_LIBDIR}/.."

SRC_URI = "http://www.xmission.com/~ebiederm/files/kexec/kexec-tools-${PV}.tar.gz \
           file://kexec-tools-arm.patch;patch=1 "
