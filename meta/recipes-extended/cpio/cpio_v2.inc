SUMMARY = "GNU cpio is a program to manage archives of files"
DESCRIPTION = "GNU cpio is a tool for creating and extracting archives, or copying files from one place to \
another. It handles a number of cpio formats as well as reading and writing tar files."
HOMEPAGE = "http://www.gnu.org/software/cpio/"
SECTION = "base"

DEPENDS = "texinfo-native"

SRC_URI = "${GNU_MIRROR}/cpio/cpio-${PV}.tar.gz \
    file://statdef.patch \
    "

inherit autotools gettext

S = "${WORKDIR}/cpio-${PV}"

do_install () {
    autotools_do_install
    install -d ${D}${base_bindir}/
    mv "${D}${bindir}/cpio" "${D}${base_bindir}/cpio.${PN}"
    rmdir ${D}${bindir}/
    case ${TARGET_OS} in
        *-uclibc*) ;;
        *) mv "${D}${libexecdir}/rmt" "${D}${libexecdir}/rmt.${PN}" ;;
    esac
}

pkg_postinst_${PN} () {
    update-alternatives --install ${base_bindir}/cpio cpio cpio.${PN} 100
    if [ -f ${libexecdir}/rmt.${PN} ]
    then
        update-alternatives --install ${libexecdir}/rmt rmt rmt.${PN} 50
    fi
}

pkg_prerm_${PN} () {
    update-alternatives --remove cpio cpio.${PN}
    if [ -f ${libexecdir}/rmt.${PN} ]
    then
        update-alternatives --remove rmt rmt.${PN}
    fi
}
