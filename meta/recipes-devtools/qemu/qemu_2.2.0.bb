require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
            file://37ed3bf1ee07bb1a26adca0df8718f601f231c0b.patch \
            file://qemu-CVE-2015-3456.patch \
            file://01-xen-properly-gate-host-writes-of-modified-PCI-CFG-contents-CVE-2015-4103.patch \
            file://02-xen-dont-allow-guest-to-control-MSI-mask-register-CVE-2015-4104.patch \
            file://03-xen-MSI-X-limit-error-messages-CVE-2015-4105.patch \
            file://04-xen-MSI-dont-open-code-pass-through-of-enable-bit-mod-CVE-2015-4106.patch \
            file://05-xen-pt-consolidate-PM-capability-emu_mask-CVE-2015-4106.patch \
            file://06-xen-pt-correctly-handle-PM-status-bit-CVE-2015-4106.patch \
            file://07-xen-pt-split-out-calculation-of-throughable-mask-CVE-2015-4106.patch \
            file://08-xen-pt-mark-all-PCIe-capability-bits-read-only-CVE-2015-4106.patch \
            file://09-xen-pt-mark-reserved-bits-in-PCI-config-space-fields-CVE-2015-4106.patch \
            file://10-xen-pt-add-a-few-PCI-config-space-field-descriptions-CVE-2015-4106.patch \
            file://11-xen-pt-unknown-PCI-config-space-fields-should-be-readonly-CVE-2015-4106.patch \
            file://qemu-fix-CVE-2015-3209.patch \
           "
SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BP}.tar.bz2"
SRC_URI[md5sum] = "f7a5e2da22d057eb838a91da7aff43c8"
SRC_URI[sha256sum] = "b68c9b6c7c694f5489b5a6bffe993cd976ffbb78e7d178eb3bc016caf460039c"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
