SECTION = "kernel"
SUMMARY = "Dummy Linux kernel"
DESCRIPTION = "Dummy Linux kernel, to be selected as the preferred \
provider for virtual/kernel to satisfy dependencies for situations \
where you wish to build the kernel externally from the build system."
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PROVIDES += "virtual/kernel"

PACKAGES_DYNAMIC += "^kernel-module-.*"
PACKAGES_DYNAMIC += "^kernel-image-.*"

#COMPATIBLE_MACHINE = "your_machine"

PR = "r1"

SRC_URI = "file://COPYING.GPL"
S = "${WORKDIR}"

do_configure() {
        :
}

do_compile () {
        :
}

do_install() {
        :
}

do_bundle_initramfs() {
        :
}

do_deploy() {
	:
}

do_bundle_initramfs[nostamp] = "1"
addtask bundle_initramfs after do_compile
addtask deploy after do_install

