DESCRIPTION = "OpenSSH SSH task for Poky"
LICENSE = "MIT"
PR = "r0"

inherit packagegroup

RDEPENDS_${PN} = "openssh"
