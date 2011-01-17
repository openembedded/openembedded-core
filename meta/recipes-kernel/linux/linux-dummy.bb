SECTION = "kernel"
DESCRIPTION = "Dummy Linux kernel"
LICENSE = "GPL"

LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PROVIDES += "virtual/kernel"

PACKAGES_DYNAMIC += "kernel-module-*"
PACKAGES_DYNAMIC += "kernel-image-*"

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
