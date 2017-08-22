SUMMARY = "Machine specific systemd units"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PR = "r19"

inherit systemd
SYSTEMD_SERVICE_${PN} = ""

ALLOW_EMPTY_${PN} = "1"
