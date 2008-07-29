require shared-mime-info.inc

DEPENDS += "shared-mime-info-native"
PR = "r1"

do_install_append() {
    update-mime-database ${D}${datadir}/mime

    # we do not need it on device and it is huge
    rm ${D}${destdir}/mime/packages/freedesktop.org.xml
}
