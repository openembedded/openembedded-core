SUMMARY = "Keytable files and keyboard utilities"
# everything minus console-fonts is GPLv2+
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a5fcc36121d93e1f69d96a313078c8b5"
DEPENDS = "libcheck"

inherit autotools gettext ptest

RREPLACES_${PN} = "console-tools"
RPROVIDES_${PN} = "console-tools"
RCONFLICTS_${PN} = "console-tools"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/${BPN}/${BP}.tar.bz2 \
           file://uclibc-stdarg.patch \
          "

SRC_URI[md5sum] = "f80b93a6abddb6cc2a3652daaf7562ba"
SRC_URI[sha256sum] = "223d60bb6882323cca161aeb5965590768b2f590fd7cddbf27511ad0ba7a429e"

PACKAGECONFIG ?= "${@base_contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}"
PACKAGECONFIG[pam] = "--enable-vlock, --disable-vlock, libpam,"

PACKAGES += "${PN}-consolefonts ${PN}-keymaps ${PN}-unimaps ${PN}-consoletrans"

FILES_${PN}-consolefonts = "${datadir}/consolefonts"
FILES_${PN}-consoletrans = "${datadir}/consoletrans"
FILES_${PN}-keymaps = "${datadir}/keymaps"
FILES_${PN}-unimaps = "${datadir}/unimaps"

inherit update-alternatives

ALTERNATIVE_${PN} = "chvt deallocvt fgconsole openvt"
ALTERNATIVE_PRIORITY = "100"

BBCLASSEXTEND = "native"
