#connman config to ignore wired interfaces on qemu machines

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI_append_qemuall = " file://main.conf"

PR = "r0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    #Blacklist ethn network interface in case of qemu* machines
    if test -e ${WORKDIR}/main.conf; then
        install -d ${D}${sysconfdir}/connman
        install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman
    fi
}
