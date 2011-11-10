DESCRIPTION = "This package contains keytable files and keyboard utilities"
# everything minus console-fonts is GPLv2+
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=587ce626d15bd61699a64a6b8a5afefb"

inherit autotools gettext

BBCLASSEXTEND = "native"
RREPLACES_${PN} = "console-tools"
RPROVIDES_${PN} = "console-tools"
RCONFLICTS_${PN} = "console-tools"

PR = "r2"

SRC_URI="${KERNELORG_MIRROR}/linux/utils/kbd/kbd-1.15.2.tar.bz2"
SRC_URI[md5sum] = "e850eb91e4d3b94b194efe8e953204c5"
SRC_URI[sha256sum] = "b3602d191eef7a6a8317fc3cd231efa40a89ac235dce57a77cac825a2a21eba6"

PACKAGES += "${PN}-consolefonts ${PN}-keymaps ${PN}-unimaps ${PN}-consoletrans"

FILES_${PN}-consolefonts = "${datadir}/consolefonts"
FILES_${PN}-consoletrans = "${datadir}/consoletrans"
FILES_${PN}-keymaps = "${datadir}/keymaps"
FILES_${PN}-unimaps = "${datadir}/unimaps"

ALTERNATIVE_NAMES_USRBIN = "chvt deallocvt fgconsole openvt"

do_install_append() {
  usrbinprogs_a="${ALTERNATIVE_NAMES_USRBIN}"
  for p in $usrbinprogs_a; do
    if [ -f "${D}${bindir}/$p" ]; then
      mv "${D}${bindir}/$p" "${D}${bindir}/$p.${PN}"
    fi
  done
}

pkg_postinst_${PN} () {
  usrbinprogs_a="${ALTERNATIVE_NAMES_USRBIN}"
  for p in $usrbinprogs_a; do
    if [ -f "$D${bindir}/$p" ]; then
      update-alternatives --install ${bindir}/$p $p $p.${PN} 100
    fi
  done
}

pkg_postrm_${PN} () {
  usrbinprogs_a="${ALTERNATIVE_NAMES_USRBIN}"
  for p in $usrbinprogs_a; do
    update-alternatives --remove $p $p.${PN}
  done
}
