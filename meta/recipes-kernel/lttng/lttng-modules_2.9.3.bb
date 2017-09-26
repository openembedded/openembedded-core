SECTION = "devel"
SUMMARY = "Linux Trace Toolkit KERNEL MODULE"
DESCRIPTION = "The lttng-modules 2.0 package contains the kernel tracer modules"
LICENSE = "LGPLv2.1 & GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=362844633a08753bd96ab322a6c7f9f6 \
                    file://gpl-2.0.txt;md5=751419260aa954499f7abaabaa882bbe \
                    file://lgpl-2.1.txt;md5=243b725d71bb5df4a1e5920b344b86ad"

inherit module

COMPATIBLE_HOST = '(x86_64|i.86|powerpc|aarch64|mips|nios2|arm).*-linux'

SRC_URI = "https://lttng.org/files/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://Makefile-Do-not-fail-if-CONFIG_TRACEPOINTS-is-not-en.patch \
           file://BUILD_RUNTIME_BUG_ON-vs-gcc7.patch \
           file://0001-Fix-handle-missing-ftrace-header-on-v4.12.patch \
           file://0002-Fix-sched-for-v4.11.5-rt1.patch \
           file://0003-Fix-Sleeping-function-called-from-invalid-context.patch \
           file://0004-Fix-update-ext4-instrumentation-for-kernel-4.13.patch \
           file://0005-Fix-mmap-caches-aliased-on-virtual-addresses.patch \
           file://0006-Add-kmalloc-failover-to-vmalloc.patch \
           file://0007-Fix-vmalloc-wrapper-on-kernel-4.12.patch \
           file://0008-Fix-vmalloc-wrapper-on-kernel-2.6.38.patch"

SRC_URI[md5sum] = "9abf694dddcc197988189ef65b496f4c"
SRC_URI[sha256sum] = "f911bca81b02a787474f3d100390dad7447f952525e6d041f50991940246bafe"

export INSTALL_MOD_DIR="kernel/lttng-modules"

EXTRA_OEMAKE += "KERNELDIR='${STAGING_KERNEL_DIR}'"

do_install_append() {
	# Delete empty directories to avoid QA failures if no modules were built
	find ${D}/${nonarch_base_libdir} -depth -type d -empty -exec rmdir {} \;
}

python do_package_prepend() {
    if not os.path.exists(os.path.join(d.getVar('D'), d.getVar('nonarch_base_libdir')[1:], 'modules')):
        bb.warn("%s: no modules were created; this may be due to CONFIG_TRACEPOINTS not being enabled in your kernel." % d.getVar('PN'))
}

