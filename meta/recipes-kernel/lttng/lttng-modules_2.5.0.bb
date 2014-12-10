SECTION = "devel"
SUMMARY = "Linux Trace Toolkit KERNEL MODULE"
DESCRIPTION = "The lttng-modules 2.0 package contains the kernel tracer modules"
LICENSE = "LGPLv2.1 & GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1412caf5a1aa90d6a48588a4794c0eac \
                    file://gpl-2.0.txt;md5=751419260aa954499f7abaabaa882bbe \
                    file://lgpl-2.1.txt;md5=243b725d71bb5df4a1e5920b344b86ad"

DEPENDS = "virtual/kernel"

inherit module

SRCREV = "789fd1d06d07aeb9a403bdce1b3318560cfc6eca"

COMPATIBLE_HOST = '(x86_64|i.86|powerpc|aarch64|mips|arm).*-linux'

SRC_URI = "git://git.lttng.org/lttng-modules.git;branch=stable-2.5 \
           file://lttng-modules-replace-KERNELDIR-with-KERNEL_SRC.patch \
           file://Update-compaction-instrumentation-to-3.16-kernel.patch \
           file://Update-vmscan-instrumentation-to-3.16-kernel.patch \
           file://Fix-noargs-probes-should-calculate-alignment-and-eve.patch \
           file://Update-statedump-to-3.17-nsproxy-locking.patch \
           file://Update-kvm-instrumentation-compile-on-3.17-rc1.patch \
           file://fix_build_with_v3.17_kernel.patch \
           file://compaction-fix-mm_compaction_isolate_template-build.patch \
           "

export INSTALL_MOD_DIR="kernel/lttng-modules"
export KERNEL_SRC="${STAGING_KERNEL_DIR}"


S = "${WORKDIR}/git"

do_install_append() {
	# Delete empty directories to avoid QA failures if no modules were built
	find ${D}/lib -depth -type d -empty -exec rmdir {} \;
}

python do_package_prepend() {
    if not os.path.exists(os.path.join(d.getVar('D', True), 'lib/modules')):
        bb.warn("%s: no modules were created; this may be due to CONFIG_TRACEPOINTS not being enabled in your kernel." % d.getVar('PN', True))
}

