require grub2.inc

RDEPENDS_${PN} = "diffutils freetype grub-editenv"

PACKAGES =+ "grub-editenv"

FILES_grub-editenv = "${bindir}/grub-editenv"

do_install_append () {
    install -d ${D}${sysconfdir}/grub.d
}

INSANE_SKIP_${PN} = "arch"
INSANE_SKIP_${PN}-dbg = "arch"
