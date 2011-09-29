SUMMARY = "Kexec fast reboot tools"
DESCRIPTION = "Kexec is a fast reboot feature that lets you reboot to a new Linux kernel"
AUTHOR = "Eric Biederman"
HOMEPAGE = "http://kernel.org/pub/linux/utils/kernel/kexec/"
SECTION = "kernel/userland"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea5bed2f60d357618ca161ad539f7c0a \
                    file://kexec/kexec.c;beginline=1;endline=20;md5=af10f6ae4a8715965e648aa687ad3e09"
DEPENDS = "zlib"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/kexec/kexec-tools-${PV}.tar.gz"

inherit autotools

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-(linux|freebsd.*)'
