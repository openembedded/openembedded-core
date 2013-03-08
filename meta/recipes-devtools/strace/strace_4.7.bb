DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=124500c21e856f0912df29295ba104c7"
PR = "r4"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://0003-util-fix-building-when-glibc-has-a-stub-process_vm_r.patch \
           file://0014-x32-update-syscall-table.patch \
           file://0018-x32-update-g-s-etsockopt-syscall-numbers.patch \
           file://0024-x32-add-64bit-annotation-too.patch \
           file://0025-Add-e-trace-memory-option.patch \
           file://0026-linux-add-new-errno-values-for-EPROBE_DEFER-and-EOPE.patch \
           file://0027-Add-AArch64-support-to-strace.patch \
           file://0028-Enhance-quotactl-decoding.patch \
           file://0029-Filter-out-redundant-32-ioctl-entries.patch \
           file://0030-Move-asm-generic-ioctl-definitions-to-linux-ioctlent.patch \
           file://0031-Add-support-for-tracing-32-bit-ARM-EABI-binaries-on-.patch \
           file://0032-Fix-kernel-release-string-parsing.patch \
          "

SRC_URI[md5sum] = "6054c3880a00c6703f83b57f15e04642"
SRC_URI[sha256sum] = "c49cd98873c119c5f201356200a9b9687da1ceea83a05047e2ae0a7ac1e41195"
inherit autotools

export INCLUDES = "-I. -I./linux"

do_install_append() {
	# We don't ship strace-graph here because it needs perl
	rm ${D}${bindir}/strace-graph
}

BBCLASSEXTEND = "native"
