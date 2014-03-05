SUMMARY = "Keyboard maps"
DESCRIPTION = "Keymaps and initscript to set the keymap on bootup."
SECTION = "base"

# Distro can override initscripts provider
VIRTUAL-RUNTIME_initscripts ?= "initscripts"

RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_initscripts} kbd"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r31"

INHIBIT_DEFAULT_DEPS = "1"

inherit update-rc.d

SRC_URI = "file://keymap.sh \
	   file://GPLv2.patch"

INITSCRIPT_NAME = "keymap.sh"
INITSCRIPT_PARAMS = "start 01 S ."

do_install () {
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/keymap.sh ${D}${sysconfdir}/init.d/
}
