DESCRIPTION = "Kexec is a fast reboot feature that lets you reboot to a new Linux kernel"
AUTHOR = "Eric Biederman"
HOMEPAGE = "http://www.xmission.com/~ebiederm/files/kexec/"
SECTION = "kernel/userland"
LICENSE = "GPL"
DEPENDS = "virtual/kernel zlib"

inherit autotools

COMPATIBLE_HOST = '(x86_64|i.86.*|arm.*)-(linux|freebsd.*)'

SRC_URI = "http://www.xmission.com/~ebiederm/files/kexec/kexec-tools-${PV}.tar.gz \
           file://kexec-tools-arm.patch;patch=1 \
           file://kexec-arm-atags.patch;patch=1 \
           file://recentheader.patch;patch=1 \
           file://configurefix.patch;patch=1"
