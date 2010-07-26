DESCRIPTION = "Kexec is a fast reboot feature that lets you reboot to a new Linux kernel"
AUTHOR = "Eric Biederman"
HOMEPAGE = "http://www.kernel.org/pub/linux/kernel/people/horms/kexec-tools/"
SECTION = "kernel/userland"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea5bed2f60d357618ca161ad539f7c0a \
                    file://kexec/kexec.c;beginline=1;endline=20;md5=af10f6ae4a8715965e648aa687ad3e09"
DEPENDS = "virtual/kernel zlib"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/people/horms/kexec-tools//kexec-tools-${PV}.tar.gz \
           file://configurefix.patch;patch=1"

inherit autotools

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*)-(linux|freebsd.*)'
